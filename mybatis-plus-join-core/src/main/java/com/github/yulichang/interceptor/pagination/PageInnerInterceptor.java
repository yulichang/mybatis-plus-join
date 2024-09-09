package com.github.yulichang.interceptor.pagination;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.parser.JsqlParserGlobal;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.IDialect;
import com.github.yulichang.wrapper.interfaces.SelectWrapper;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yulichang
 * @since 1.5.0
 */
public class PageInnerInterceptor extends PaginationInnerInterceptor {


    private static final Log log = LogFactory.getLog(PageInnerInterceptor.class);

    public PageInnerInterceptor(PaginationInnerInterceptor pagination) {
        super.setOptimizeJoin(true);
        super.setDbType(pagination.getDbType());
        super.setDialect(pagination.getDialect());
        super.setOverflow(pagination.isOverflow());
        super.setMaxLimit(pagination.getMaxLimit());
    }

    /**
     * 执行count
     */
    @Override
    public boolean willDoQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        //没有wrapper 或者 不是对多查询 不做处理
        SelectWrapper<?, ?> wrapper = findMPJWrapper(parameter).orElse(null);
        if (wrapper == null || !wrapper.isPageByMain()) {
            return true;
        }
        // copy super
        IPage<?> page = wrapper.getPageInfo().getInnerPage();
        if (page == null || page.getSize() < 0 || !page.searchCount() || resultHandler != Executor.NO_RESULT_HANDLER) {
            return true;
        }

        BoundSql countSql;
        MappedStatement countMs = buildCountMappedStatement(ms, page.countId());
        if (countMs != null) {
            countSql = countMs.getBoundSql(parameter);
        } else {
            countMs = buildAutoCountMappedStatement(ms);
            ArrayList<ParameterMapping> mappingArrayList = new ArrayList<>(boundSql.getParameterMappings());
            String countSqlStr = autoCountSql(boundSql.getSql(), mappingArrayList, ms, parameter, wrapper);
            PluginUtils.MPBoundSql mpBoundSql = PluginUtils.mpBoundSql(boundSql);
            countSql = new BoundSql(countMs.getConfiguration(), countSqlStr, mappingArrayList, parameter);
            PluginUtils.setAdditionalParameter(countSql, mpBoundSql.additionalParameters());
        }

        CacheKey cacheKey = executor.createCacheKey(countMs, parameter, rowBounds, countSql);
        List<Object> result = executor.query(countMs, parameter, rowBounds, resultHandler, cacheKey, countSql);
        long total = 0;
        if (CollectionUtils.isNotEmpty(result)) {
            // 个别数据库 count 没数据不会返回 0
            Object o = result.get(0);
            if (o != null) {
                total = Long.parseLong(o.toString());
            }
        }
        page.setTotal(total);
        return continuePage(page);
    }

    /**
     * 添加分页方言
     */
    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        SelectWrapper<?, ?> wrapper = findMPJWrapper(parameter).orElse(null);
        if (wrapper == null || !wrapper.isPageByMain()) {
            return;
        }
        // copy super
        IPage<?> page = wrapper.getPageInfo().getInnerPage();
        if (null == page) {
            return;
        }

        // 处理 orderBy 拼接
        boolean addOrdered = false;
        String buildSql = boundSql.getSql();
        List<OrderItem> orders = page.orders();
        if (CollectionUtils.isNotEmpty(orders)) {
            addOrdered = true;
            buildSql = this.concatOrderBy(buildSql, orders);
        }

        // size 小于 0 且不限制返回值则不构造分页sql
        Long _limit = page.maxLimit() != null ? page.maxLimit() : maxLimit;
        if (page.getSize() < 0 && null == _limit) {
            if (addOrdered) {
                PluginUtils.mpBoundSql(boundSql).sql(buildSql);
            }
            return;
        }

        handlerLimit(page, _limit);
        DialectWrapper dialect = findMPJDialect(executor);

        final Configuration configuration = ms.getConfiguration();
        PluginUtils.MPBoundSql mpBoundSql = PluginUtils.mpBoundSql(boundSql);
        Map<String, Object> additionalParameter = mpBoundSql.additionalParameters();
        dialect.buildPaginationSql(buildSql, boundSql.getParameterMappings(), page.offset(), page.getSize(),
                c -> c.consumers(dialect.getPageMappings(), configuration, additionalParameter),
                ms, parameter);
        mpBoundSql.sql(dialect.getFinallySql());
        mpBoundSql.parameterMappings(dialect.getFullMappings());
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
    }

    public String autoCountSql(String sql, List<ParameterMapping> mappings, MappedStatement ms, Object parameter, SelectWrapper<?, ?> wrapper) {
        try {
            Select select = (Select) JsqlParserGlobal.parse(sql);
            if (select instanceof SetOperationList) {
                throw ExceptionUtils.mpe("不支持 union 对多分页");
            }

            //获取 ? 出现的次数
            String sourceSql = select.toString();
            int count = ParseHelper.countChar(sourceSql);

            String formatSql;
            if (count == mappings.size()) {
                formatSql = ParseHelper.decode(sourceSql);
            } else {
                SqlSource sqlSource = ms.getSqlSource();
                if (sqlSource instanceof DynamicSqlSource) {
                    formatSql = ParseHelper.getOriginalSql(parameter, (DynamicSqlSource) sqlSource);
                } else {
                    log.error("unknown type: " + sqlSource.getClass().getName());
                    throw ExceptionUtils.mpe("not support this sql, please use xml. error sql: " + sql);
                }
            }

            PlainSelect ps = (PlainSelect) JsqlParserGlobal.parse(formatSql);

            // 优化 order by 在非分组情况下
            List<OrderByElement> orderBy = ps.getOrderByElements();
            if (CollectionUtils.isNotEmpty(orderBy)) {
                boolean canClean = true;
                for (OrderByElement order : orderBy) {
                    // order by 里带参数,不去除order by
                    Expression expression = order.getExpression();
                    if (!(expression instanceof Column) && expression.toString().contains(StringPool.QUESTION_MARK)) {
                        canClean = false;
                        break;
                    }
                }
                if (canClean) {
                    ps.setOrderByElements(null);
                }
            }

            List<Join> joins = ps.getJoins();
            if (CollectionUtils.isNotEmpty(joins)) {
                ps.setJoins(null);
            }

            String repSql;
            Distinct distinct = ps.getDistinct();
            GroupByElement groupBy = ps.getGroupBy();
            String mainAlias = Optional.of(ps.getFromItem().getAlias().getName()).orElse("");
            // 包含 distinct、groupBy 不优化
            if (null != distinct || null != groupBy) {
                if (wrapper.getPageInfo().getCountSelectSql() != null) {
                    ps.setSelectItems(Collections.singletonList(new SelectItem<>(new Column().withColumnName(wrapper.getPageInfo().getCountSelectSql()))));
                } else {
                    ps.getSelectItems().removeIf(s -> {
                        if (s.getExpression() instanceof Column) {
                            Column column = (Column) s.getExpression();
                            return !mainAlias.equals(column.getTable().toString());
                        }
                        return false;
                    });
                }
                repSql = lowLevelCountSql(ps.toString());
            } else {
                // 优化 SQL
                ps.setSelectItems(COUNT_SELECT_ITEM);
                repSql = ps.toString();
            }

            // 替换回 ? 同步mappings顺序
            Map<Integer, ParameterMapping> sortMap = new HashMap<>();
            repSql = ParseHelper.encode(mappings, count, repSql, sortMap);
            mappings.clear();
            mappings.addAll(sortMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(Map.Entry::getValue).collect(Collectors.toList()));
            return repSql;
        } catch (JSQLParserException e) {
            logger.error("optimize this sql to a count sql has exception, sql:\"" + sql + "\", exception:\n" + e.getCause());
        } catch (Exception e) {
            logger.error("optimize this sql to a count sql has error, sql:\"" + sql + "\", exception:\n" + e);
        }
        throw ExceptionUtils.mpe("not support this sql, please use xml. error sql: " + sql);
    }

    protected DialectWrapper findMPJDialect(Executor executor) {
        IDialect dialect = super.findIDialect(executor);
        return new DialectWrapper(dialect);
    }

    public static Optional<SelectWrapper<?, ?>> findMPJWrapper(Object parameterObject) {
        if (parameterObject != null) {
            if (parameterObject instanceof Map) {
                Map<?, ?> parameterMap = (Map<?, ?>) parameterObject;
                for (Map.Entry<?, ?> entry : parameterMap.entrySet()) {
                    if (entry.getValue() != null && entry.getValue() instanceof SelectWrapper) {
                        return Optional.of((SelectWrapper<?, ?>) entry.getValue());
                    }
                }
            } else if (parameterObject instanceof SelectWrapper) {
                return Optional.of((SelectWrapper<?, ?>) parameterObject);
            }
        }
        return Optional.empty();
    }

}
