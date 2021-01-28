package com.github.mybatisplus.wrapper;

import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.mybatisplus.base.MyBaseEntity;
import com.github.mybatisplus.toolkit.Constant;
import com.github.mybatisplus.toolkit.MyLambdaUtils;
import com.github.mybatisplus.wrapper.interfaces.MyJoin;
import com.github.mybatisplus.func.MySFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 链接查询子Query
 *
 * @author yulichang
 * @see com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
 * @since 2021/01/19
 */
public class MyJoinLambdaQueryWrapper<T extends MyBaseEntity> extends MyAbstractLambdaWrapper<T, MyJoinLambdaQueryWrapper<T>>
        implements Query<MyJoinLambdaQueryWrapper<T>, T, SFunction<T, ?>>, MyJoin<MyJoinLambdaQueryWrapper<T>, T> {

    protected SharedString sqlSelect = new SharedString();
    /**
     * select字段
     */
    protected List<SelectColumn> selectColumnList = new ArrayList<>();
    /**
     * 表名
     */
    protected SharedString from = new SharedString();
    /**
     * 子表列表
     */
    protected List<SubTable> classList = new ArrayList<>();
    /**
     * 表序号
     */
    protected int rUid = 1;


    public MyJoinLambdaQueryWrapper() {
        super.setEntity(null);
        super.initNeed();
    }

    public MyJoinLambdaQueryWrapper(int rUid) {
        this.rUid = rUid;
        super.setEntity(null);
        super.initNeed();
    }

    public MyJoinLambdaQueryWrapper(T entity, Class<T> entityClass, SharedString sqlSelect, AtomicInteger paramNameSeq,
                                    Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments,
                                    SharedString lastSql, SharedString sqlComment) {
        super.setEntity(entity);
        this.setEntityClass(entityClass);
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
        this.sqlSelect = sqlSelect;
        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
    }

    @Override
    protected MyJoinLambdaQueryWrapper<T> instance() {
        return new MyJoinLambdaQueryWrapper<>(getEntity(), getEntityClass(), null, paramNameSeq, paramNameValuePairs,
                new MergeSegments(), SharedString.emptyString(), SharedString.emptyString());
    }

    @Override
    public void clear() {
        super.clear();
        this.sqlSelect.toNull();
        this.selectColumnList.clear();
        this.from.toNull();
        this.classList.clear();
        this.rUid = 1;
    }

    @Override
    public MyJoinLambdaQueryWrapper<T> select(SFunction<T, ?>... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            for (SFunction<T, ?> str : columns) {
                selectColumnList.add(new SelectColumn(rUid, columnToString(str), null, null));
            }
        }
        return typedThis;
    }

    /**
     * 查询所有字段 (后置处理)
     */
    public MyJoinLambdaQueryWrapper<T> selectAll(Class<T> clazz) {
        setEntityClass(clazz);
        TableInfo info = TableInfoHelper.getTableInfo(getEntityClass());
        info.getFieldList().forEach(s ->
                selectColumnList.add(new SelectColumn(this.rUid, s.getColumn(), null, null)));
        if (StringUtils.isNotBlank(info.getKeyColumn())) {
            selectColumnList.add(new SelectColumn(this.rUid, info.getKeyColumn(), null, null));
        }
        return typedThis;
    }

    /**
     * 字段去重(同时使用select和as的情况下去除select中重复查询的列,优先使用as列作为查询字段)
     * 注意:
     * 只对selectDistinct前面的 select selectAll as(asCount ...)有效
     * 只对当前的表去重,对连接查询的其他表不生效,如需要在对应的表下使用此方法
     */
    public MyJoinLambdaQueryWrapper<T> selectDistinct() {
        List<SelectColumn> collect = selectColumnList.stream().filter(i -> i.getUid() == rUid).collect(Collectors.toList());
        List<SelectColumn> distinctList = new ArrayList<>();

        List<SelectColumn> collect1 = collect.stream().distinct().collect(Collectors.toList());
        if (collect.size() != collect1.size()) {
            collect.forEach(i -> {
                if (!collect1.contains(i)) {
                    distinctList.add(i);
                }
            });
        }
        for (SelectColumn x : collect1) {
            if (x.getAs() != null) {
                for (SelectColumn y : collect1) {
                    if (x.getUid() == y.getUid() && x.getColumn().equals(y.getColumn()) && y.getAs() == null) {
                        distinctList.add(y);
                    }
                }
            }
        }

        selectColumnList.removeIf(distinctList::contains);
        return this;
    }

    @Override
    public MyJoinLambdaQueryWrapper<T> select(Predicate<TableFieldInfo> predicate) {
        return select(getEntityClass(), predicate);
    }


    @Override
    public MyJoinLambdaQueryWrapper<T> select(Class<T> entityClass, Predicate<TableFieldInfo> predicate) {
        this.setEntityClass(entityClass);
        TableInfo info = TableInfoHelper.getTableInfo(getEntityClass());
        info.getFieldList().stream().filter(predicate).forEach(s ->
                selectColumnList.add(new SelectColumn(this.rUid, s.getColumn(), null, null)));
        return typedThis;
    }


    public <DTO> MyJoinLambdaQueryWrapper<T> as(SFunction<T, ?> entityColumn, SFunction<DTO, ?> DTOColumn) {
        selectColumnList.add(new SelectColumn(rUid, columnToString(entityColumn), MyLambdaUtils.getName(DTOColumn), null));
        return this;
    }

    public <DTO> MyJoinLambdaQueryWrapper<T> asCount(MySFunction<T, ?> entityColumn, SFunction<DTO, ?> DTOColumn) {
        selectColumnList.add(new SelectColumn(rUid, columnToString(entityColumn), MyLambdaUtils.getName(DTOColumn), SelectFunc.COUNT));
        return this;
    }

    public <DTO> MyJoinLambdaQueryWrapper<T> asSum(MySFunction<T, ?> entityColumn, SFunction<DTO, ?> DTOColumn) {
        selectColumnList.add(new SelectColumn(rUid, columnToString(entityColumn), MyLambdaUtils.getName(DTOColumn), SelectFunc.SUM));
        return this;
    }

    public <DTO> MyJoinLambdaQueryWrapper<T> asAvg(MySFunction<T, ?> entityColumn, SFunction<DTO, ?> DTOColumn) {
        selectColumnList.add(new SelectColumn(rUid, columnToString(entityColumn), MyLambdaUtils.getName(DTOColumn), SelectFunc.AVG));
        return this;
    }

    public <DTO> MyJoinLambdaQueryWrapper<T> asMax(MySFunction<T, ?> entityColumn, SFunction<DTO, ?> DTOColumn) {
        selectColumnList.add(new SelectColumn(rUid, columnToString(entityColumn), MyLambdaUtils.getName(DTOColumn), SelectFunc.MAX));
        return this;
    }

    public <DTO> MyJoinLambdaQueryWrapper<T> asMin(MySFunction<T, ?> entityColumn, SFunction<DTO, ?> DTOColumn) {
        selectColumnList.add(new SelectColumn(rUid, columnToString(entityColumn), MyLambdaUtils.getName(DTOColumn), SelectFunc.MIN));
        return this;
    }

    public <DTO> MyJoinLambdaQueryWrapper<T> asDateFormat(MySFunction<T, ?> entityColumn, SFunction<DTO, ?> DTOColumn) {
        selectColumnList.add(new SelectColumn(rUid, columnToString(entityColumn), MyLambdaUtils.getName(DTOColumn), SelectFunc.DATE_FORMAT));
        return this;
    }

    /**
     * 左连接查询
     *
     * @param leftCondition  主表参与比较的字段 (on)
     * @param rightCondition 子表参与比较的字段 (on)
     * @param rightWrapper   子表的wrapper
     */
    @Override
    public <R extends MyBaseEntity, TE, RE> MyJoinLambdaQueryWrapper<T> leftJoin(boolean condition, String alias, MySFunction<T, TE> leftCondition, MySFunction<R, RE> rightCondition, Function<MyJoinLambdaQueryWrapper<R>, MyJoinLambdaQueryWrapper<R>> rightWrapper) {
        return join(condition, alias, Constant.LEFT_JOIN, leftCondition, rightCondition, rightWrapper);
    }

    /**
     * 右连接查询(参考左连接)
     */
    @Override
    public <R extends MyBaseEntity, TE, RE> MyJoinLambdaQueryWrapper<T> rightJoin(boolean condition, String alias, MySFunction<T, TE> leftCondition, MySFunction<R, RE> rightCondition, Function<MyJoinLambdaQueryWrapper<R>, MyJoinLambdaQueryWrapper<R>> rightWrapper) {
        return join(condition, alias, Constant.RIGHT_JOIN, leftCondition, rightCondition, rightWrapper);
    }

    /**
     * 内连接查询(参考左连接)
     */
    @Override
    public <R extends MyBaseEntity, TE, RE> MyJoinLambdaQueryWrapper<T> innerJoin(boolean condition, String alias, MySFunction<T, TE> leftCondition, MySFunction<R, RE> rightCondition, Function<MyJoinLambdaQueryWrapper<R>, MyJoinLambdaQueryWrapper<R>> rightWrapper) {
        return join(condition, alias, Constant.INNER_JOIN, leftCondition, rightCondition, rightWrapper);
    }

    private <R extends MyBaseEntity, TE, RE> MyJoinLambdaQueryWrapper<T> join(boolean condition, String alias, String keyWord, MySFunction<T, TE> leftCondition, MySFunction<R, RE> rightCondition, Function<MyJoinLambdaQueryWrapper<R>, MyJoinLambdaQueryWrapper<R>> rightWrapper) {
        if (condition) {
            setEntityClass(MyLambdaUtils.getEntityClass(leftCondition));
            int childrenId = rUid + 1;
            classList.add(new SubTable(alias, keyWord, rUid, MyLambdaUtils.getColumn(leftCondition), childrenId, MyLambdaUtils.getColumn(rightCondition), TableInfoHelper.getTableInfo(MyLambdaUtils.getEntityClass(rightCondition)).getTableName()));
            MyJoinLambdaQueryWrapper<R> apply = rightWrapper.apply(new MyJoinLambdaQueryWrapper<>(childrenId));
            classList.addAll(apply.classList);
            this.selectColumnList.addAll(apply.selectColumnList);
        }
        return this;
    }

    /**
     * 连表条件
     */
    public static class SubTable {

        private String alias;

        private String keyWord;

        private int leftUid;

        private String leftColumn;

        private int rightUid;

        private String rightColumn;

        private String rightTableName;

        public SubTable(String alias, String keyWord, int leftUid, String leftColumn, int rightUid, String rightColumn, String rightTableName) {
            this.alias = alias;
            this.keyWord = keyWord;
            this.leftUid = leftUid;
            this.leftColumn = leftColumn;
            this.rightUid = rightUid;
            this.rightColumn = rightColumn;
            this.rightTableName = rightTableName;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public String getKeyWord() {
            return keyWord;
        }

        public void setKeyWord(String keyWord) {
            this.keyWord = keyWord;
        }

        public int getLeftUid() {
            return leftUid;
        }

        public void setLeftUid(int leftUid) {
            this.leftUid = leftUid;
        }

        public String getLeftColumn() {
            return leftColumn;
        }

        public void setLeftColumn(String leftColumn) {
            this.leftColumn = leftColumn;
        }

        public int getRightUid() {
            return rightUid;
        }

        public void setRightUid(int rightUid) {
            this.rightUid = rightUid;
        }

        public String getRightColumn() {
            return rightColumn;
        }

        public void setRightColumn(String rightColumn) {
            this.rightColumn = rightColumn;
        }

        public String getRightTableName() {
            return rightTableName;
        }

        public void setRightTableName(String rightTableName) {
            this.rightTableName = rightTableName;
        }
    }

    /**
     * select查询字段
     */
    public static class SelectColumn {

        private int uid;

        private String column;

        private String as;

        private SelectFunc function;

        public SelectColumn() {
        }

        public SelectColumn(int uid, String column, String as, SelectFunc function) {
            this.uid = uid;
            this.column = column;
            this.as = as;
            this.function = function;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getColumn() {
            return column;
        }

        public void setColumn(String column) {
            this.column = column;
        }

        public String getAs() {
            return as;
        }

        public void setAs(String as) {
            this.as = as;
        }

        public SelectFunc getFunction() {
            return function;
        }

        public void setFunction(SelectFunc function) {
            this.function = function;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof SelectColumn)) {
                return false;
            }
            SelectColumn v = (SelectColumn) o;
            return v.getUid() == this.uid
                    && eq(v.getColumn(), this.column)
                    && eq(v.getAs(), this.as)
                    && eq(v.getFunction(), this.function);
        }

        private boolean eq(Object o1, Object o2) {
            if (o1 == null && o2 == null) {
                return true;
            }
            if (o1 == null || o2 == null) {
                return false;
            }
            return o1.equals(o2);
        }
    }
}
