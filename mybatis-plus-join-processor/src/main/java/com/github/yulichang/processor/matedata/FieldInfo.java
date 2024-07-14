package com.github.yulichang.processor.matedata;

import java.util.Objects;

/**
 * @author yulichang
 * @since 1.5.0
 */
@SuppressWarnings("unused")
public class FieldInfo {

    /**
     * 属性名。
     */
    private String property;

    /**
     * 注释。
     */
    private String comment;

    public FieldInfo(String property, String comment) {
        this.property = property;
        this.comment = comment;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldInfo fieldInfo = (FieldInfo) o;
        return Objects.equals(property, fieldInfo.property);
    }

    @Override
    public int hashCode() {
        return Objects.hash(property);
    }

}
