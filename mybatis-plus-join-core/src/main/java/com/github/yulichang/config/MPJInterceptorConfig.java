package com.github.yulichang.config;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.github.yulichang.interceptor.MPJInterceptor;
import com.github.yulichang.toolkit.InterceptorList;
import com.github.yulichang.toolkit.MybatisJoinPlusVersion;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.InterceptorChain;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 兼容 page helper 插件类
 *
 * @author yulichang
 */
public class MPJInterceptorConfig {


    private static final Log logger = LogFactory.getLog(MPJInterceptorConfig.class);

    public MPJInterceptorConfig(List<SqlSessionFactory> sqlSessionFactoryList, Boolean banner) {
        replaceInterceptorChain(sqlSessionFactoryList);
        if (banner) {
            //打印banner
            System.out.println(" _ _   |_  _ _|_. ___ _ |    _  .  _  .  _  \n" +
                    "| | |\\/|_)(_| | |_\\  |_)||_|_\\  | (_) | | | \n" +
                    "     /               |          /\n" +
                    "                                    " + MybatisJoinPlusVersion.getVersion());
        }
    }

    private void replaceInterceptorChain(List<SqlSessionFactory> sqlSessionFactoryList) {
        if (CollectionUtils.isEmpty(sqlSessionFactoryList)) {
            return;
        }
        for (SqlSessionFactory factory : sqlSessionFactoryList) {
            try {
                Field interceptorChain = Configuration.class.getDeclaredField("interceptorChain");
                interceptorChain.setAccessible(true);
                InterceptorChain chain = (InterceptorChain) interceptorChain.get(factory.getConfiguration());
                Field interceptors = InterceptorChain.class.getDeclaredField("interceptors");
                interceptors.setAccessible(true);
                @SuppressWarnings("unchecked")
                List<Interceptor> list = (List<Interceptor>) interceptors.get(chain);
                if (CollectionUtils.isEmpty(list)) {
                    interceptors.set(chain, new InterceptorList<>());
                } else {
                    interceptors.set(chain, new InterceptorList<>(list));
                }
                chain.addInterceptor(new MPJInterceptor());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                logger.error("初始化 MPJ 拦截器失败", e);
            }
        }
    }
}
