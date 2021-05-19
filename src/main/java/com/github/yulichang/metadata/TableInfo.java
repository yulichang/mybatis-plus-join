package com.github.yulichang.metadata;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.session.Configuration;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 为自定义resultType提供resultMap
 * <p>
 * copy {@link com.baomidou.mybatisplus.core.metadata.TableInfo}
 */
@Data
@Setter(AccessLevel.PACKAGE)
@Accessors(chain = true)
@SuppressWarnings("serial")
public class TableInfo implements Constants {

    /**
     * 实体类型
     */
    private Class<?> entityType;
    /**
     * 表主键ID 类型
     */
    private IdType idType = IdType.NONE;
    /**
     * 表名称
     */
    private String tableName;
    /**
     * 表映射结果集
     */
    private String resultMap;
    /**
     * 是否是需要自动生成的 resultMap
     */
    private boolean autoInitResultMap;
    /**
     * 主键是否有存在字段名与属性名关联
     * <p>true: 表示要进行 as</p>
     */
    private boolean keyRelated;
    /**
     * 表主键ID 字段名
     */
    private String keyColumn;
    /**
     * 表主键ID 属性名
     */
    private String keyProperty;
    /**
     * 表主键ID 属性类型
     */
    private Class<?> keyType;
    /**
     * 表主键ID Sequence
     */
    private KeySequence keySequence;
    /**
     * 表字段信息列表
     */
    private List<TableFieldInfo> fieldList;
    /**
     * 命名空间 (对应的 mapper 接口的全类名)
     */
    private String currentNamespace;
    /**
     * MybatisConfiguration 标记 (Configuration内存地址值)
     */
    @Getter
    private Configuration configuration;
    /**
     * 是否开启下划线转驼峰
     * <p>
     * 未注解指定字段名的情况下,用于自动从 property 推算 column 的命名
     */
    private boolean underCamel;
    /**
     * 缓存包含主键及字段的 sql select
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private String allSqlSelect;
    /**
     * 缓存主键字段的 sql select
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private String sqlSelect;
    /**
     * 表字段是否启用了插入填充
     *
     * @since 3.3.0
     */
    @Getter
    @Setter(AccessLevel.NONE)
    private boolean withInsertFill;
    /**
     * 表字段是否启用了更新填充
     *
     * @since 3.3.0
     */
    @Getter
    @Setter(AccessLevel.NONE)
    private boolean withUpdateFill;
    /**
     * 表字段是否启用了逻辑删除
     *
     * @since 3.4.0
     */
    @Getter
    @Setter(AccessLevel.NONE)
    private boolean withLogicDelete;
    /**
     * 逻辑删除字段
     *
     * @since 3.4.0
     */
    @Getter
    @Setter(AccessLevel.NONE)
    private TableFieldInfo logicDeleteFieldInfo;
    /**
     * 表字段是否启用了乐观锁
     *
     * @since 3.3.1
     */
    @Getter
    @Setter(AccessLevel.NONE)
    private boolean withVersion;
    /**
     * 乐观锁字段
     *
     * @since 3.3.1
     */
    @Getter
    @Setter(AccessLevel.NONE)
    private TableFieldInfo versionFieldInfo;

    public TableInfo(Class<?> entityType) {
        this.entityType = entityType;
    }

    /**
     * 获得注入的 SQL Statement
     *
     * @param sqlMethod MybatisPlus 支持 SQL 方法
     * @return SQL Statement
     * @deprecated 3.4.0 如果存在的多mapper共用一个实体的情况，这里可能会出现获取命名空间错误的情况
     */
    @Deprecated
    public String getSqlStatement(String sqlMethod) {
        return currentNamespace + DOT + sqlMethod;
    }

    /**
     * 设置 Configuration
     */
    void setConfiguration(Configuration configuration) {
        Assert.notNull(configuration, "Error: You need Initialize MybatisConfiguration !");
        this.configuration = configuration;
        this.underCamel = configuration.isMapUnderscoreToCamelCase();
    }

    /**
     * 是否有主键
     *
     * @return 是否有
     */
    public boolean havePK() {
        return StringUtils.isNotBlank(keyColumn);
    }

    void setFieldList(List<TableFieldInfo> fieldList) {
        this.fieldList = fieldList;
        AtomicInteger logicDeleted = new AtomicInteger();
        AtomicInteger version = new AtomicInteger();
        fieldList.forEach(i -> {
            if (i.isLogicDelete()) {
                this.withLogicDelete = true;
                this.logicDeleteFieldInfo = i;
                logicDeleted.getAndAdd(1);
            }
            if (i.isWithInsertFill()) {
                this.withInsertFill = true;
            }
            if (i.isWithUpdateFill()) {
                this.withUpdateFill = true;
            }
            if (i.isVersion()) {
                this.withVersion = true;
                this.versionFieldInfo = i;
                version.getAndAdd(1);
            }
        });
        /* 校验字段合法性 */
        Assert.isTrue(logicDeleted.get() <= 1, "@TableLogic not support more than one in Class: \"%s\"", entityType.getName());
        Assert.isTrue(version.get() <= 1, "@Version not support more than one in Class: \"%s\"", entityType.getName());
    }

    public List<TableFieldInfo> getFieldList() {
        return Collections.unmodifiableList(fieldList);
    }

    @Deprecated
    public boolean isLogicDelete() {
        return withLogicDelete;
    }
}
