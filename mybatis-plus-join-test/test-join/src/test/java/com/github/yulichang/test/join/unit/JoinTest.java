package com.github.yulichang.test.join.unit;

import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.github.yulichang.test.join.dto.UserDTO;
import com.github.yulichang.test.join.entity.AddressDO;
import com.github.yulichang.test.join.entity.AreaDO;
import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.util.Reset;
import com.github.yulichang.test.util.ThreadLocalUtils;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.JoinAbstractLambdaWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.github.yulichang.wrapper.interfaces.MConsumer;
import com.github.yulichang.wrapper.interfaces.MFunction;
import com.github.yulichang.wrapper.interfaces.QueryJoin;
import org.apache.ibatis.logging.nologging.NoLoggingImpl;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@SpringBootTest
public class JoinTest {


    @BeforeEach
    void setUp() {
        Reset.reset();
    }

    @Test
    void joinTest() {
        ThreadLocalUtils.set("""
                SELECT t.id,
                       t.pid,
                       t.`name`,
                       t.`json`,
                       t.sex,
                       t.head_img,
                       t.create_time,
                       t.address_id,
                       t.address_id2,
                       t.del,
                       t.create_by,
                       t.update_by
                FROM `user` t
                    LEFT JOIN
                       (SELECT
                           tt.id,
                           tt.user_id,
                           tt.area_id,
                           tt.tel,
                           tt.address,
                           tt.del
                       FROM address tt
                           LEFT JOIN
                               (SELECT
                                   t.id,
                                   t.province,
                                   t.city,
                                   t.area,
                                   t.postcode,
                                   t.del
                               FROM area t
                               WHERE t.del = false
                                   AND (t.id >= ?)) t1
                           ON (t1.id = tt.area_id)
                       WHERE tt.del = false
                           AND t1.del = false
                           AND (tt.id >= ?)) t1 ON (t1.user_id = t.id)
                WHERE t.del = false
                    AND t1.del = false
                    AND (t1.id <= ?)
                """);
        JoinWrappers.lambda(UserDO.class)
                .selectAll()
                .leftJoin(AddressDO.class, t -> t
                        .setAlias("tt")
                        .selectAll()
                        .leftJoin(AreaDO.class, tt -> tt
                                .selectAll()
                                .ge(AreaDO::getId, -1), AreaDO::getId, AddressDO::getAreaId)
                        .ge(AddressDO::getId, 0), AddressDO::getUserId, UserDO::getId)
                .le(AddressDO::getId, 10000)
                .list(UserDTO.class);
    }

    @Test
    void joinTest2() {
        ThreadLocalUtils.set("""
                SELECT
                    t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time,
                    t.address_id, t.address_id2, t.del, t.create_by, t.update_by
                FROM `user` t
                LEFT JOIN address t1 ON t1.id > ?
                WHERE t.del = false AND (t.id <= ?)
                """);
        JoinWrappers.lambda(UserDO.class)
                .selectAll()
                .leftJoin("address t1 on t1.id > {0}", 0)
                .le(AddressDO::getId, 10000)
                .list(UserDTO.class);

        ThreadLocalUtils.set("""
                SELECT
                    t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time,
                    t.address_id, t.address_id2, t.del, t.create_by, t.update_by
                FROM `user` t
                LEFT JOIN address t1 ON t1.id > 0
                WHERE t.del = false AND (t.id <= ?)
                """);
        JoinWrappers.lambda(UserDO.class)
                .selectAll()
                .leftJoin("address t1 on t1.id > 0")
                .le(AddressDO::getId, 10000)
                .list(UserDTO.class);
    }



    @Test
    @SuppressWarnings("deprecation")
    void joinTest1() {
        //remove log
        TableInfoHelper.getTableInfo(UserDO.class).getConfiguration().setLogImpl(NoLoggingImpl.class);
        //@formatter:off
        w(w -> w.leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId));
        w(w -> w.leftJoin(AddressDO.class, AddressDO::getUserId, "t", UserDO::getId));
        w(w -> w.leftJoin(AddressDO.class, on -> on.eq(AddressDO::getUserId, UserDO::getId)));
        w(w -> w.leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId, ext -> ext));
        w(w -> w.leftJoin(AddressDO.class, (on, ext) -> on.eq(AddressDO::getUserId, UserDO::getId)));
        w(w -> w.leftJoin(AddressDO.class, "a", AddressDO::getUserId, UserDO::getId));
        w(w -> w.leftJoin(AddressDO.class, "b", AddressDO::getUserId, "t", UserDO::getId));
        w(w -> w.leftJoin(AddressDO.class, "c", on -> on.eq(AddressDO::getUserId, UserDO::getId)));
        w(w -> w.leftJoin(AddressDO.class, "d", AddressDO::getUserId, UserDO::getId, ext -> ext));
        w(w -> w.leftJoin(AddressDO.class, "e", (on, e) -> on.eq(AddressDO::getUserId, UserDO::getId)));

        w(w -> w.leftJoin(AddressDO.class, t -> {}, AddressDO::getUserId, UserDO::getId));
        w(w -> w.leftJoin(AddressDO.class, t -> {}, AddressDO::getUserId, "t", UserDO::getId));
        w(w -> w.leftJoin(AddressDO.class, t -> {}, on -> on.eq(AddressDO::getUserId, UserDO::getId)));
        w(w -> w.leftJoin(AddressDO.class, t -> {}, AddressDO::getUserId, UserDO::getId, ext -> ext));
        w(w -> w.leftJoin(AddressDO.class, t -> {}, (on, ext) -> on.eq(AddressDO::getUserId, UserDO::getId)));
        w(w -> w.leftJoin(AddressDO.class, t -> {}, "a", AddressDO::getUserId, UserDO::getId));
        w(w -> w.leftJoin(AddressDO.class, t -> {}, "b", AddressDO::getUserId, "t", UserDO::getId));
        w(w -> w.leftJoin(AddressDO.class, t -> {}, "c", on -> on.eq(AddressDO::getUserId, UserDO::getId)));
        w(w -> w.leftJoin(AddressDO.class, t -> {}, "d", AddressDO::getUserId, UserDO::getId, ext -> ext));
        w(w -> w.leftJoin(AddressDO.class, t -> {}, "e", (on, e) -> on.eq(AddressDO::getUserId, UserDO::getId)));

        w(w -> w.rightJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId));
        w(w -> w.rightJoin(AddressDO.class, AddressDO::getUserId, "t", UserDO::getId));
        w(w -> w.rightJoin(AddressDO.class, on -> on.eq(AddressDO::getUserId, UserDO::getId)));
        w(w -> w.rightJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId, ext -> ext));
        w(w -> w.rightJoin(AddressDO.class, (on, ext) -> on.eq(AddressDO::getUserId, UserDO::getId)));
        w(w -> w.rightJoin(AddressDO.class, "a", AddressDO::getUserId, UserDO::getId));
        w(w -> w.rightJoin(AddressDO.class, "b", AddressDO::getUserId, "t", UserDO::getId));
        w(w -> w.rightJoin(AddressDO.class, "c", on -> on.eq(AddressDO::getUserId, UserDO::getId)));
        w(w -> w.rightJoin(AddressDO.class, "d", AddressDO::getUserId, UserDO::getId, ext -> ext));
        w(w -> w.rightJoin(AddressDO.class, "e", (on, e) -> on.eq(AddressDO::getUserId, UserDO::getId)));

        w(w -> w.rightJoin(AddressDO.class, t -> {}, AddressDO::getUserId, UserDO::getId));
        w(w -> w.rightJoin(AddressDO.class, t -> {}, AddressDO::getUserId, "t", UserDO::getId));
        w(w -> w.rightJoin(AddressDO.class, t -> {}, on -> on.eq(AddressDO::getUserId, UserDO::getId)));
        w(w -> w.rightJoin(AddressDO.class, t -> {}, AddressDO::getUserId, UserDO::getId, ext -> ext));
        w(w -> w.rightJoin(AddressDO.class, t -> {}, (on, ext) -> on.eq(AddressDO::getUserId, UserDO::getId)));
        w(w -> w.rightJoin(AddressDO.class, t -> {}, "a", AddressDO::getUserId, UserDO::getId));
        w(w -> w.rightJoin(AddressDO.class, t -> {}, "b", AddressDO::getUserId, "t", UserDO::getId));
        w(w -> w.rightJoin(AddressDO.class, t -> {}, "c", on -> on.eq(AddressDO::getUserId, UserDO::getId)));
        w(w -> w.rightJoin(AddressDO.class, t -> {}, "d", AddressDO::getUserId, UserDO::getId, ext -> ext));
        w(w -> w.rightJoin(AddressDO.class, t -> {}, "e", (on, e) -> on.eq(AddressDO::getUserId, UserDO::getId)));

        w(w -> w.innerJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId));
        w(w -> w.innerJoin(AddressDO.class, AddressDO::getUserId, "t", UserDO::getId));
        w(w -> w.innerJoin(AddressDO.class, on -> on.eq(AddressDO::getUserId, UserDO::getId)));
        w(w -> w.innerJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId, ext -> ext));
        w(w -> w.innerJoin(AddressDO.class, (on, ext) -> on.eq(AddressDO::getUserId, UserDO::getId)));
        w(w -> w.innerJoin(AddressDO.class, "a", AddressDO::getUserId, UserDO::getId));
        w(w -> w.innerJoin(AddressDO.class, "b", AddressDO::getUserId, "t", UserDO::getId));
        w(w -> w.innerJoin(AddressDO.class, "c", on -> on.eq(AddressDO::getUserId, UserDO::getId)));
        w(w -> w.innerJoin(AddressDO.class, "d", AddressDO::getUserId, UserDO::getId, ext -> ext));
        w(w -> w.innerJoin(AddressDO.class, "e", (on, e) -> on.eq(AddressDO::getUserId, UserDO::getId)));

        w(w -> w.innerJoin(AddressDO.class, t -> {}, AddressDO::getUserId, UserDO::getId));
        w(w -> w.innerJoin(AddressDO.class, t -> {}, AddressDO::getUserId, "t", UserDO::getId));
        w(w -> w.innerJoin(AddressDO.class, t -> {}, on -> on.eq(AddressDO::getUserId, UserDO::getId)));
        w(w -> w.innerJoin(AddressDO.class, t -> {}, AddressDO::getUserId, UserDO::getId, ext -> ext));
        w(w -> w.innerJoin(AddressDO.class, t -> {}, (on, ext) -> on.eq(AddressDO::getUserId, UserDO::getId)));
        w(w -> w.innerJoin(AddressDO.class, t -> {}, "a", AddressDO::getUserId, UserDO::getId));
        w(w -> w.innerJoin(AddressDO.class, t -> {}, "b", AddressDO::getUserId, "t", UserDO::getId));
        w(w -> w.innerJoin(AddressDO.class, t -> {}, "c", on -> on.eq(AddressDO::getUserId, UserDO::getId)));
        w(w -> w.innerJoin(AddressDO.class, t -> {}, "d", AddressDO::getUserId, UserDO::getId, ext -> ext));
        w(w -> w.innerJoin(AddressDO.class, t -> {}, "e", (on, e) -> on.eq(AddressDO::getUserId, UserDO::getId)));

        f(w -> w.fullJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId));
        f(w -> w.fullJoin(AddressDO.class, AddressDO::getUserId, "t", UserDO::getId));
        f(w -> w.fullJoin(AddressDO.class, on -> on.eq(AddressDO::getUserId, UserDO::getId)));
        f(w -> w.fullJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId, ext -> ext));
        f(w -> w.fullJoin(AddressDO.class, (on, ext) -> on.eq(AddressDO::getUserId, UserDO::getId)));
        f(w -> w.fullJoin(AddressDO.class, "a", AddressDO::getUserId, UserDO::getId));
        f(w -> w.fullJoin(AddressDO.class, "b", AddressDO::getUserId, "t", UserDO::getId));
        f(w -> w.fullJoin(AddressDO.class, "c", on -> on.eq(AddressDO::getUserId, UserDO::getId)));
        f(w -> w.fullJoin(AddressDO.class, "d", AddressDO::getUserId, UserDO::getId, ext -> ext));
        f(w -> w.fullJoin(AddressDO.class, "e", (on, e) -> on.eq(AddressDO::getUserId, UserDO::getId)));

        w(w -> w.join("left join", AddressDO.class, AddressDO::getUserId, UserDO::getId));
        w(w -> w.join("left join", AddressDO.class, AddressDO::getUserId, "t", UserDO::getId));
        w(w -> w.join("left join", AddressDO.class, on -> on.eq(AddressDO::getUserId, UserDO::getId)));
        w(w -> w.join("left join", AddressDO.class, AddressDO::getUserId, UserDO::getId, ext -> ext));
        w(w -> w.join("left join", AddressDO.class, (on, ext) -> on.eq(AddressDO::getUserId, UserDO::getId)));
        w(w -> w.join("left join", AddressDO.class, "a", AddressDO::getUserId, UserDO::getId));
        w(w -> w.join("left join", AddressDO.class, "b", AddressDO::getUserId, "t", UserDO::getId));
        w(w -> w.join("left join", AddressDO.class, "c", on -> on.eq(AddressDO::getUserId, UserDO::getId)));
        w(w -> w.join("left join", AddressDO.class, "d", AddressDO::getUserId, UserDO::getId, ext -> ext));
        w(w -> w.join("left join", AddressDO.class, "e", (on, e) -> on.eq(AddressDO::getUserId, UserDO::getId)));

        w(w -> w.join("left join", AddressDO.class, t -> {}, AddressDO::getUserId, UserDO::getId));
        w(w -> w.join("left join", AddressDO.class, t -> {}, AddressDO::getUserId, "t", UserDO::getId));
        w(w -> w.join("left join", AddressDO.class, t -> {}, on -> on.eq(AddressDO::getUserId, UserDO::getId)));
        w(w -> w.join("left join", AddressDO.class, t -> {}, AddressDO::getUserId, UserDO::getId, ext -> ext));
        w(w -> w.join("left join", AddressDO.class, t -> {}, (on, ext) -> on.eq(AddressDO::getUserId, UserDO::getId)));
        w(w -> w.join("left join", AddressDO.class, t -> {}, "a", AddressDO::getUserId, UserDO::getId));
        w(w -> w.join("left join", AddressDO.class, t -> {}, "b", AddressDO::getUserId, "t", UserDO::getId));
        w(w -> w.join("left join", AddressDO.class, t -> {}, "c", on -> on.eq(AddressDO::getUserId, UserDO::getId)));
        w(w -> w.join("left join", AddressDO.class, t -> {}, "d", AddressDO::getUserId, UserDO::getId, ext -> ext));
        w(w -> w.join("left join", AddressDO.class, t -> {}, "e", (on, e) -> on.eq(AddressDO::getUserId, UserDO::getId)));

        //@formatter:on
        assert set.size() == 90;
        //reset log
        TableInfoHelper.getTableInfo(UserDO.class).getConfiguration().setLogImpl(StdOutImpl.class);
    }

    private final Set<String> set = new TreeSet<>();

    private MPJLambdaWrapper<UserDO> w() {
        return new MPJLambdaWrapper<>(UserDO.class) {
            @Override
            public <R> MPJLambdaWrapper<UserDO> join(String keyWord, Class<R> clazz, MConsumer<MPJLambdaWrapper<R>> table, String tableAlias,
                                                     BiConsumer<JoinAbstractLambdaWrapper<UserDO, ?>, MPJLambdaWrapper<UserDO>> consumer) {
                String line = Arrays.stream(Thread.getAllStackTraces().get(Thread.currentThread()))
                        .filter(f -> Objects.equals(QueryJoin.class.getName(), f.getClassName()))
                        .map(f -> f.getLineNumber() + "")
                        .collect(Collectors.joining("-"));
                if (!set.contains(line)) {
                    set.add(line);
                    return super.join(keyWord, clazz, table, tableAlias, consumer);
                }
                throw new RuntimeException("repeat line number " + line);
            }
        };
    }

    private void w(MFunction<MPJLambdaWrapper<UserDO>> consumer) {
        consumer.apply(w().selectAll()).list(UserDTO.class);
    }

    private void f(MFunction<MPJLambdaWrapper<UserDO>> consumer) {
        consumer.apply(w().selectAll());
    }
}
