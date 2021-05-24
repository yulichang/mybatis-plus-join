package com.github.yulichang.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.toolkit.ReflectionKit;

import java.util.List;
import java.util.Map;

/**
 * @author yulichang
 * @see ServiceImpl
 */
@SuppressWarnings("unchecked")
public class MPJBaseServiceImpl<M extends MPJBaseMapper<T>, T> extends ServiceImpl<M, T> implements MPJBaseService<T> {

    /**
     * mybatis plus 3.4.3 bug
     * <p>
     * https://gitee.com/baomidou/mybatis-plus/issues/I3SE8R
     * <p>
     * https://gitee.com/baomidou/mybatis-plus/commit/7210b461b23211e6b95ca6de2d846aa392bdc28c
     */
    @Override
    protected Class<T> currentMapperClass() {
        return (Class<T>) ReflectionKit.getSuperClassGenericType(this.getClass(), ServiceImpl.class, 0);
    }

    /**
     * mybatis plus 3.4.3 bug
     * <p>
     * https://gitee.com/baomidou/mybatis-plus/issues/I3SE8R
     * <p>
     * https://gitee.com/baomidou/mybatis-plus/commit/7210b461b23211e6b95ca6de2d846aa392bdc28c
     */
    @Override
    protected Class<T> currentModelClass() {
        return (Class<T>) ReflectionKit.getSuperClassGenericType(this.getClass(), ServiceImpl.class, 1);
    }

    @Override
    public <DTO> DTO selectJoinOne(Class<DTO> clazz, MPJBaseJoin wrapper) {
        return baseMapper.selectJoinOne(clazz, wrapper);
    }

    @Override
    public <DTO> List<DTO> selectJoinList(Class<DTO> clazz, MPJBaseJoin wrapper) {
        return baseMapper.selectJoinList(clazz, wrapper);
    }

    @Override
    public <DTO, P extends IPage<?>> IPage<DTO> selectJoinListPage(P page, Class<DTO> clazz, MPJBaseJoin wrapper) {
        return baseMapper.selectJoinPage(page, clazz, wrapper);
    }

    @Override
    public Map<String, Object> selectJoinMap(MPJBaseJoin wrapper) {
        return baseMapper.selectJoinMap(wrapper);
    }

    @Override
    public List<Map<String, Object>> selectJoinMaps(MPJBaseJoin wrapper) {
        return baseMapper.selectJoinMaps(wrapper);
    }

    @Override
    public <P extends IPage<Map<String, Object>>> IPage<Map<String, Object>> selectJoinMapsPage(P page, MPJBaseJoin wrapper) {
        return baseMapper.selectJoinMapsPage(page, wrapper);
    }
}
