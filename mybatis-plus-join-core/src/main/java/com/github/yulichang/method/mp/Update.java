package com.github.yulichang.method.mp;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * Delete 兼容MP原生方法
 */
public class Update extends com.baomidou.mybatisplus.core.injector.methods.Update implements BaseMethod {

    public Update() {
        super();
    }

    @SuppressWarnings("unused")
    public Update(String name) {
        super(name);
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        return super.injectMappedStatement(mapperClass, modelClass, tableInfo);
    }
}
