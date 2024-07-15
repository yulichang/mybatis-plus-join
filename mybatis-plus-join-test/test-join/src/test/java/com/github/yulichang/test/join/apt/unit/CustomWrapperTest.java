package com.github.yulichang.test.join.apt.unit;

import com.github.yulichang.apt.BaseColumn;
import com.github.yulichang.apt.Column;
import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.join.entity.apt.UserDOCol;
import com.github.yulichang.test.join.mapper.UserMapper;
import com.github.yulichang.test.util.Reset;
import com.github.yulichang.test.util.ThreadLocalUtils;
import com.github.yulichang.toolkit.Ref;
import com.github.yulichang.wrapper.apt.AptQueryWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Objects;

@SpringBootTest
public class CustomWrapperTest {

    @Autowired
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        Reset.reset();
    }


    //自定义wrapper扩展
    public static class CWrapper<T> extends AptQueryWrapper<T> {

        public CWrapper(BaseColumn<T> baseColumn) {
            super(baseColumn);
        }

        public CWrapper(BaseColumn<T> baseColumn, T entity) {
            super(baseColumn, entity);
        }

        public static <T> CWrapper<T> toCWrapper() {
            return null;
        }

        @Override
        public CWrapper<T> eqIfExists(Column column, Object val) {
            super.eq(Objects.nonNull(val), column, val);
            return this;
        }
    }

    @Test
    void testWrapperCustomer() {
        ThreadLocalUtils.set("SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, t.address_id2, t.del, t.create_by, t.update_by FROM `user` t WHERE t.del = false AND (t.id = ?)");
        UserDOCol u = new UserDOCol();
        CWrapper<UserDO> wrapper = new CWrapper<>(u)
                .selectAll()
                .toChildren(CWrapper::toCWrapper)
                .eqIfExists(u.id, 1);
        List<UserDO> dos = userMapper.selectList(wrapper);
        dos.forEach(System.out::println);

        UserDOCol u1 = new UserDOCol();
        ThreadLocalUtils.set("SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, t.address_id2, t.del, t.create_by, t.update_by FROM `user` t WHERE t.del = false");
        CWrapper<UserDO> wrapper1 = new CWrapper<>(u1)
                .selectAll()
                .toChildren(new Ref<CWrapper<UserDO>>())
                .eqIfExists(u1.id, null);
        List<UserDO> dos1 = userMapper.selectList(wrapper1);
        dos1.forEach(System.out::println);
    }
}
