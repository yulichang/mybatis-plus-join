package com.github.yulichang.common;

import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义连表sql
 * <p>
 * 不使用表别名:
 * <pre>
 *     //注解
 *     @Select("select user.*,user_address.tel from user left join user_address on user.id = user_address.user_id ${ew.customSqlSegment}")
 *
 *     //或者xml
 *     <select id="userLeftJoin" resultType="UserDTO">
 *         select user.*, user_address.tel
 *         from user left join user_address on user.id = user_address.user_id
 *         ${ew.customSqlSegment}
 *     </select>
 *
 *     //mapper
 *     UserDTO userLeftJoin(@Param(Constants.WRAPPER) Wrapper<UserDO> queryWrapper);
 *
 *     wrapper使用方法:
 *     UserDTO userDTO = userMapper.userLeftJoin(new JoinLambdaWrapper<UserDO>()
 *                 .eq(UserDO::getId, "1")
 *                 .eq(UserAddressDO::getUserId, "1"));
 *
 *     对应生成sql:
 *     select user.*, user_address.tel
 *     from user left join user_address on user.id = user_address.user_id
 *     WHERE (
 *         user.id = ?
 *         AND user_address.user_id = ?)
 * </pre>
 * <p>
 * 使用别名:
 *
 * <pre>
 *     //注解
 *     @Select("select u.*,ua.tel from user u left join user_address ua on u.id = ua.user_id ${ew.customSqlSegment}")
 *
 *     //或者xml
 *     <select id="userLeftJoin" resultType="UserDTO">
 *         select u.*, ua.tel
 *         from user u left join user_address ua on u.id = ua.user_id
 *         ${ew.customSqlSegment}
 *     </select>
 *
 *     //mapper
 *     UserDTO userLeftJoin(@Param(Constants.WRAPPER) Wrapper<UserDO> queryWrapper);
 *
 *     wrapper使用方法:
 *     UserDTO userDTO = userMapper.userLeftJoin(new JoinLambdaWrapper<UserDO>()
 *                 .alias(UserDO.class, "u")           //如果sql使用别名,需要再此定义别名
 *                 .alias(UserAddressDO.class, "ua")   //如果sql使用别名,需要再此定义别名
 *                 .eq(UserDO::getId, "1")
 *                 .eq(UserAddressDO::getUserId, "1"));
 *
 *     对应生成sql:
 *     select u.*, ua.tel
 *     from user u left join user_address ua on u.id = ua.user_id
 *     WHERE (
 *         u.id = ?
 *         AND ua.user_id = ?)
 * </pre>
 * <p>
 * 如需单独使用只需拷贝以下类
 * {@link com.github.yulichang.common.JoinLambdaWrapper}
 * {@link com.github.yulichang.common.JoinAbstractWrapper}
 * {@link com.github.yulichang.common.JoinAbstractLambdaWrapper}
 * {@link com.github.yulichang.wrapper.interfaces.Compare}
 * {@link com.github.yulichang.wrapper.interfaces.Func}
 * {@link com.github.yulichang.toolkit.LambdaUtils}
 * <p>
 *
 * @author yulichang
 * @since 1.0.9
 */
public class JoinLambdaWrapper<T> extends JoinAbstractLambdaWrapper<T, JoinLambdaWrapper<T>> {

    /**
     * 实体与别名对应关系
     */
    public JoinLambdaWrapper<T> alias(Class<?> clazz, String alisa) {
        subTable.put(clazz, alisa);
        return typedThis;
    }


    public JoinLambdaWrapper() {
        super.initNeed();
    }

    /**
     * 不建议直接 new 该实例，使用 Wrappers.lambdaQuery(...)
     */
    JoinLambdaWrapper(T entity, Class<T> entityClass, SharedString sqlSelect, AtomicInteger paramNameSeq,
                      Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments,
                      SharedString lastSql, SharedString sqlComment, SharedString sqlFirst) {
        super.setEntity(entity);
        super.setEntityClass(entityClass);
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;

        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
        this.sqlFirst = sqlFirst;
    }


    /**
     * 用于生成嵌套 sql
     * <p>故 sqlSelect 不向下传递</p>
     */
    @Override
    protected JoinLambdaWrapper<T> instance() {
        return new JoinLambdaWrapper<>(getEntity(), getEntityClass(), null, paramNameSeq, paramNameValuePairs,
                new MergeSegments(), SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString());
    }

    @Override
    public void clear() {
        super.clear();
    }
}

