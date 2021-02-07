package com.github.yulichang.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.interfaces.MPJBaseJoin;

import java.util.List;
import java.util.Map;

/**
 * @author yulichang
 * @see ServiceImpl
 */
public class MPJBaseServiceImpl<M extends MPJBaseMapper<T>, T> extends ServiceImpl<M, T> implements MPJBaseService<T> {


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
    public IPage<Map<String, Object>> selectJoinMapsPage(IPage<Map<String, Object>> page, MPJBaseJoin wrapper) {
        return baseMapper.selectJoinMapsPage(page, wrapper);
    }


}
