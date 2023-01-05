package com.github.yulichang.toolkit;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class TableList {

    /**
     * 所有关联的的表
     */
    private List<Node> all = new ArrayList<>();

    /**
     * 主表类型
     */
    private Class<?> rootClass;

    /**
     * 关联表
     */
    private List<Node> child = new ArrayList<>();

    /**
     * 添加关联表
     *
     * @param pIndex   上级索引 可以为null
     * @param clazz    关联表类
     * @param hasAlias 是否有别名
     * @param alias    别名
     * @param index    索引
     */
    public void put(Integer pIndex, Class<?> clazz, boolean hasAlias, String alias, int index) {
        Node n = new Node(clazz, hasAlias, alias, pIndex, index);
        all.add(n);
        if (Objects.isNull(pIndex)) {
            //一级节点
            child.add(n);
        } else {
            Node node = getByIndex(pIndex);
            node.put(n);
        }
    }

    public String getPrefix(int index) {
        return null;
    }

    public String getPrefixOther(int index) {
        return null;
    }


    private Node getByIndex(int index) {
        return all.stream().filter(i -> i.getIndex() == index).findFirst().orElse(null);
    }

    @Data
    public static class Node {

        /**
         * 关联表类型
         */
        private Class<?> clazz;

        /**
         * 是否有别名
         */
        private boolean hasAlias;

        /**
         * 表别名
         */
        private String alias;

        /**
         * 上级index
         */
        private Integer pIndex;

        /**
         * 表序号
         */
        private int index;

        /**
         * 子集
         */
        private List<Node> list;

        public Node(Class<?> clazz, boolean hasAlias, String alias, Integer pIndex, int index) {
            this.clazz = clazz;
            this.hasAlias = hasAlias;
            this.alias = alias;
            this.pIndex = pIndex;
            this.index = index;
        }

        public void put(Node node) {
            if (Objects.isNull(list)) {
                list = new ArrayList<>();
            }
            list.add(node);
        }
    }
}
