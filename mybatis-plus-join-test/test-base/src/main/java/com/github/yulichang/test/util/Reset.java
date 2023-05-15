package com.github.yulichang.test.util;

import com.github.yulichang.toolkit.SpringContentUtils;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;

import java.io.InputStreamReader;

public class Reset {

    @SuppressWarnings({"DataFlowIssue", "resource"})
    public static void reset() {
        SqlSession session = SpringContentUtils.getBean(SqlSessionTemplate.class)
                .getSqlSessionFactory().openSession(true);
        ScriptRunner runner = new ScriptRunner(session.getConnection());
        runner.setLogWriter(null);
        runner.runScript(new InputStreamReader(
                Reset.class.getClassLoader().getResourceAsStream("db/schema.sql")));
        runner.runScript(new InputStreamReader(
                Reset.class.getClassLoader().getResourceAsStream("db/data.sql")));
        session.commit();
    }
}
