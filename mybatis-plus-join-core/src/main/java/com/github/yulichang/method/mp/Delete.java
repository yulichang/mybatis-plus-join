package com.github.yulichang.method.mp;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * Delete 兼容MP原生方法
 */
public class Delete extends com.baomidou.mybatisplus.core.injector.methods.Delete implements BaseMethod {

    public Delete() {
        super();
    }

    @SuppressWarnings("unused")
    public Delete(String name) {
        super(name);
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        return super.injectMappedStatement(mapperClass, modelClass, tableInfo);
    }
}
