package com.github.mybatisplus.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.mybatisplus.wrapper.MyWrapper;

import java.util.List;
import java.util.Map;

/**
 * @author yulichang
 * @see ServiceImpl
 */
public class MyBaseServiceImpl<M extends MyBaseMapper<T>, T> extends ServiceImpl<M, T> implements MyBaseService<T> {


    @Override
    public <DTO> DTO selectJoinOne(Class<DTO> clazz, MyWrapper<T> wrapper) {
        return baseMapper.selectJoinOne(clazz, wrapper);

    }

    @Override
    public <DTO> List<DTO> selectJoinList(Class<DTO> clazz, MyWrapper<T> wrapper) {
        return baseMapper.selectJoinList(clazz, wrapper);
    }

    @Override
    public <DTO, P extends IPage<?>> IPage<DTO> selectJoinListPage(P page, Class<DTO> clazz, MyWrapper<T> wrapper) {
        return baseMapper.selectJoinPage(page, clazz, wrapper);
    }

    @Override
    public Map<String, Object> selectJoinMap(MyWrapper<T> wrapper) {
        return baseMapper.selectJoinMap(wrapper);
    }

    @Override
    public List<Map<String, Object>> selectJoinMaps(MyWrapper<T> wrapper) {
        return baseMapper.selectJoinMaps(wrapper);
    }

    @Override
    public IPage<Map<String, Object>> selectJoinMapsPage(IPage<Map<String, Object>> page, MyWrapper<T> wrapper) {
        return baseMapper.selectJoinMapsPage(page, wrapper);
    }


}
