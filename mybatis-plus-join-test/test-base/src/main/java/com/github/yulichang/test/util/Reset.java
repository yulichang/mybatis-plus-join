package com.github.yulichang.test.util;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import com.github.yulichang.toolkit.SpringContentUtils;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;

public class Reset {

    @SuppressWarnings({"DataFlowIssue"})
    public static void reset() {
        SqlSession session = SpringContentUtils.getBean(SqlSessionTemplate.class)
                .getSqlSessionFactory().openSession(true);
        Connection connection = session.getConnection();
        ScriptRunner runner = new ScriptRunner(connection);
        String path = "db/";
        try {
            DbType dbType = JdbcUtils.getDbType(connection.getMetaData().getURL());
            path += DbTypeEnum.getPath(dbType);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        runner.setLogWriter(null);
        runner.runScript(new InputStreamReader(
                Reset.class.getClassLoader().getResourceAsStream(path + "schema.sql"), StandardCharsets.UTF_8));
        runner.runScript(new InputStreamReader(
                Reset.class.getClassLoader().getResourceAsStream(path + "data.sql"), StandardCharsets.UTF_8));
        session.commit();
        session.close();
    }
}
