package com.github.mybatisplus.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.mybatisplus.func.MyWrapperFunc;
import com.github.mybatisplus.wrapper.MyLambdaQueryWrapper;

import java.util.List;
import java.util.Map;

/**
 * @author yulichang
 * @see IService
 */
public interface MyBaseService<T> extends IService<T> {

    /**
     * ignore
     */
    default <DTO> DTO selectJoinOne(Class<DTO> clazz, MyWrapperFunc<T> wrapperFunc) {
        return selectJoinOne(clazz, wrapperFunc.apply(new MyLambdaQueryWrapper<>()));
    }

    /**
     * 连接查询返回一条记录
     */
    <DTO> DTO selectJoinOne(Class<DTO> clazz, MyLambdaQueryWrapper<T> wrapper);

    /**
     * ignore
     */
    default <DTO> List<DTO> selectJoinList(Class<DTO> clazz, MyWrapperFunc<T> wrapperFunc) {
        return selectJoinList(clazz, wrapperFunc.apply(new MyLambdaQueryWrapper<>()));
    }

    /**
     * 连接查询返回集合
     */
    <DTO> List<DTO> selectJoinList(Class<DTO> clazz, MyLambdaQueryWrapper<T> wrapper);


    /**
     * ignore
     */
    default <DTO, P extends IPage<?>> IPage<DTO> selectJoinListPage(P page, Class<DTO> clazz, MyWrapperFunc<T> wrapperFunc) {
        return selectJoinListPage(page, clazz, wrapperFunc.apply(new MyLambdaQueryWrapper<>()));
    }

    /**
     * 连接查询返回集合并分页
     */
    <DTO, P extends IPage<?>> IPage<DTO> selectJoinListPage(P page, Class<DTO> clazz, MyLambdaQueryWrapper<T> wrapper);


    /**
     * ignore
     */
    default Map<String, Object> selectJoinMap(MyWrapperFunc<T> wrapperFunc) {
        return selectJoinMap(wrapperFunc.apply(new MyLambdaQueryWrapper<>()));
    }

    /**
     * 连接查询返回Map
     */
    Map<String, Object> selectJoinMap(MyLambdaQueryWrapper<T> wrapper);

    /**
     * ignore
     */
    default List<Map<String, Object>> selectJoinMaps(MyWrapperFunc<T> wrapperFunc) {
        return selectJoinMaps(wrapperFunc.apply(new MyLambdaQueryWrapper<>()));
    }

    /**
     * 连接查询返回Map集合
     */
    List<Map<String, Object>> selectJoinMaps(MyLambdaQueryWrapper<T> wrapper);

    /**
     * ignore
     */
    default IPage<Map<String, Object>> selectJoinMapsPage(IPage<Map<String, Object>> page, MyWrapperFunc<T> wrapperFunc) {
        return selectJoinMapsPage(page, wrapperFunc.apply(new MyLambdaQueryWrapper<>()));
    }

    /**
     * 连接查询返回Map集合并分页
     */
    IPage<Map<String, Object>> selectJoinMapsPage(IPage<Map<String, Object>> page, MyLambdaQueryWrapper<T> wrapper);

}
