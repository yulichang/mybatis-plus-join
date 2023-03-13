package com.github.yulichang.toolkit;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
     * 主表别名
     */
    private String alias;

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

    public String getPrefix(Integer index, Class<?> clazz, boolean isLabel) {
        if (Objects.isNull(index)) {
            if (!isLabel) {
                if (rootClass == clazz) {
                    return alias;
                }
            }
            Node node = getByClassFirst(clazz);
            if (Objects.isNull(node)) {
                return alias;
            }
            return node.isHasAlias() ? node.getAlias() : (node.getAlias() + node.getIndex());
        }
        Node node = getByIndex(index);
        if (node.getClazz() != clazz) {
            Node dg = dg(node, clazz);
            if (Objects.nonNull(dg)) {
                node = dg;
            } else {
                if (rootClass != clazz) {
                    List<Node> list = getByClass(clazz);
                    for (int i = list.size() - 1; i >= 0; i--) {
                        Node n = list.get(i);
                        if (n.getClazz() == clazz) {
                            return n.hasAlias ? n.getAlias() : (n.getAlias() + n.getIndex());
                        }
                    }
                }
                return alias;
            }
        }
        if (node.hasAlias) {
            return node.getAlias();
        } else {
            return node.getAlias() + node.getIndex();
        }
    }

    public String getPrefixOther(Integer index, Class<?> clazz) {
        if (Objects.isNull(index)) {
            List<Node> list = getByClass(clazz);
            if (list.size() == 1 && list.get(0).getClazz() == clazz) {
                return alias;
            } else if (list.size() > 1) {
                for (Node n : list) {
                    if (n.getClazz() == clazz) {
                        return n.isHasAlias() ? n.getAlias() : (n.getAlias() + n.getIndex());
                    }
                }
                return alias;
            } else {
                return alias;
            }
        }
        Node node = getByIndex(index);
        Node dg = dg(node, node.getClazz());
        if (Objects.nonNull(dg)) {
            return dg.hasAlias ? dg.alias : (dg.alias + dg.getIndex());
        }
        if (Objects.equals(rootClass, node.getClazz())) {
            return alias;
        } else {
            List<Node> list = getByClass(node.getClazz());
            if (list.size() == 1) {
                Node n = list.get(0);
                if (n.getClazz() == node.getClazz()) {
                    return alias;
                } else {
                    return n.isHasAlias() ? n.getAlias() : (n.getAlias() + n.getIndex());
                }
            } else if (list.size() > 1) {
                for (Node n : list) {
                    if (n.getClazz() != node.getClazz()) {
                        return n.isHasAlias() ? n.getAlias() : (n.getAlias() + n.getIndex());
                    }
                }
                return alias;
            } else {
                return alias;
            }
        }
    }

    private Node dg(Node node, Class<?> clazz) {
        if (Objects.isNull(node.getPIndex())) {
            return null;
        } else {
            Node pNode = getByIndex(node.getPIndex());
            if (pNode.getClazz() == clazz) {
                return pNode;
            }
            return dg(pNode, clazz);
        }
    }

    public String getPrefixByClass(Class<?> clazz) {
        Node node = getByClassFirst(clazz);
        if (Objects.isNull(node)) {
            return alias;
        } else {
            return node.hasAlias ? node.getAlias() : (node.getAlias() + node.getIndex());
        }
    }

    private Node getByIndex(int index) {
        return all.stream().filter(i -> i.getIndex() == index).findFirst().orElse(null);
    }

    private Node getByClassFirst(Class<?> clazz) {
        return all.stream().filter(i -> i.getClazz() == clazz).findFirst().orElse(null);
    }

    private List<Node> getByClass(Class<?> clazz) {
        return all.stream().filter(i -> i.getClazz() == clazz).collect(Collectors.toList());
    }

    public void clear() {
        this.all.clear();
        this.child.clear();
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
