package com.github.yulichang.toolkit;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 查询字段集合
 *
 * @author yulichang
 * @since 1.2.5
 */
public class CacheList<T extends UniqueObject> extends ArrayList<T> implements UniqueObject {

    private String uniqueKey;

    @Override
    public String getUniqueKey() {
        if (StringUtils.isBlank(uniqueKey)) {
            StringBuilder sb = new StringBuilder();
            for (UniqueObject ub : this) {
                sb.append(ub.getUniqueKey());
            }
            this.uniqueKey = sb.toString();
        }
        return this.uniqueKey;
    }

    @Override
    public boolean add(T t) {
        this.uniqueKey = null;
        return super.add(t);
    }

    @Override
    public boolean remove(Object o) {
        this.uniqueKey = null;
        return super.remove(o);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        this.uniqueKey = null;
        return super.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        this.uniqueKey = null;
        return super.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        this.uniqueKey = null;
        return super.removeAll(c);
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        this.uniqueKey = null;
        super.removeRange(fromIndex, toIndex);
    }

    @Override
    public void clear() {
        this.uniqueKey = null;
        super.clear();
    }
}
