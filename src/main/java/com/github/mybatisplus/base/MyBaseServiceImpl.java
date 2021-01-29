package com.github.mybatisplus.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.mybatisplus.wrapper.MyLambdaQueryWrapper;

import java.util.List;
import java.util.Map;

/**
 * @author yulichang
 * @see ServiceImpl
 */
public class MyBaseServiceImpl<M extends MyBaseMapper<T>, T> extends ServiceImpl<M, T> implements MyBaseService<T> {


    @Override
    public <DTO> DTO selectJoinOne(MyLambdaQueryWrapper<T> wrapper, Class<DTO> clazz) {
        return baseMapper.selectJoinOne(wrapper, clazz);

    }

    @Override
    public <DTO> List<DTO> selectJoinList(MyLambdaQueryWrapper<T> wrapper, Class<DTO> clazz) {
        return baseMapper.selectJoinList(wrapper, clazz);
    }

    @Override
    public <DTO, P extends IPage<?>> IPage<DTO> selectJoinListPage(P page, MyLambdaQueryWrapper<T> wrapper, Class<DTO> clazz) {
        return baseMapper.selectJoinPage(page, wrapper, clazz);
    }

    @Override
    public Map<String, Object> selectJoinMap(MyLambdaQueryWrapper<T> wrapper) {
        return baseMapper.selectJoinMap(wrapper);
    }

    @Override
    public List<Map<String, Object>> selectJoinMaps(MyLambdaQueryWrapper<T> wrapper) {
        return baseMapper.selectJoinMaps(wrapper);
    }

    @Override
    public IPage<Map<String, Object>> selectJoinMapsPage(IPage<Map<String, Object>> page, MyLambdaQueryWrapper<T> wrapper) {
        return baseMapper.selectJoinMapsPage(page, wrapper);
    }


}
