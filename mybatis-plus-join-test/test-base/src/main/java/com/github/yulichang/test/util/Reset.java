package com.github.yulichang.test.util;

import com.github.yulichang.toolkit.SpringContentUtils;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Reset {

    @SuppressWarnings({"DataFlowIssue"})
    public static void reset() {
        SqlSession session = SpringContentUtils.getBean(SqlSessionTemplate.class)
                .getSqlSessionFactory().openSession(true);
        ScriptRunner runner = new ScriptRunner(session.getConnection());
        runner.setLogWriter(null);
        runner.runScript(new InputStreamReader(
                Reset.class.getClassLoader().getResourceAsStream("db/schema.sql"), StandardCharsets.UTF_8));
        runner.runScript(new InputStreamReader(
                Reset.class.getClassLoader().getResourceAsStream("db/data.sql"), StandardCharsets.UTF_8));
        session.commit();
        session.close();
    }
}
