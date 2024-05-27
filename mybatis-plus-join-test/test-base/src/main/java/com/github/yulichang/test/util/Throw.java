package com.github.yulichang.test.util;

import com.baomidou.mybatisplus.annotation.DbType;
import org.springframework.jdbc.BadSqlGrammarException;

import java.util.Arrays;

public class Throw {


    public static void tryDo(DoSomething doSomething) {
        try {
            doSomething.doSomething();
        } catch (BadSqlGrammarException e) {
            if (DbTypeUtil.getDbType() != DbType.H2) {
                throw e;
            }
        }
    }

    public static void tryDo(DoSomething doSomething, DbType... ignore) {
        try {
            doSomething.doSomething();
        } catch (BadSqlGrammarException e) {
            if (DbTypeUtil.getDbType() != DbType.H2 && Arrays.stream(ignore).noneMatch(n -> n == DbTypeUtil.getDbType())) {
                throw e;
            }
        }
    }

    public static void tryDoIgnore(DoSomething doSomething) {
        try {
            doSomething.doSomething();
        } catch (BadSqlGrammarException ignore) {
        }
    }

    @FunctionalInterface
    public interface DoSomething {
        void doSomething();
    }
}
