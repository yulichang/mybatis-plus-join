package com.github.yulichang.processor.matedata;

import com.github.yulichang.annotation.Table;
import com.github.yulichang.processor.utils.StringUtil;

import java.util.Set;

/**
 * @author yulichang
 * @since 1.5.0
 */
@SuppressWarnings("unused")
public class TableInfo {

    public TableInfo(Table tableAnnotation) {
        this.tableAnnotation = tableAnnotation;
    }

    private String className;

    private String simpleClassName;

    private String classComment;

    private String classPackage;

    private Table tableAnnotation;

    private Set<FieldInfo> fields;

    /**
     * 生成的类名
     */
    public String getTagClassName() {
        String tag = simpleClassName;
        if (StringUtil.isNotEmpty(tableAnnotation.prefix()) || StringUtil.isNotEmpty(tableAnnotation.suffix())) {
            tag = tableAnnotation.prefix() + tag + tableAnnotation.suffix();
        } else {
            tag = String.format(tableAnnotation.format(), tag);
        }
        return tag;
    }

    /**
     * 生成类的路径
     */
    public String getTagPackage() {
        return classPackage + "." + tableAnnotation.packageName();
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSimpleClassName() {
        return simpleClassName;
    }

    public void setSimpleClassName(String simpleClassName) {
        this.simpleClassName = simpleClassName;
    }

    public String getClassComment() {
        return classComment;
    }

    public void setClassComment(String classComment) {
        this.classComment = classComment;
    }

    public String getClassPackage() {
        return classPackage;
    }

    public void setClassPackage(String classPackage) {
        this.classPackage = classPackage;
    }

    public Table getTableAnnotation() {
        return tableAnnotation;
    }

    public void setTableAnnotation(Table tableAnnotation) {
        this.tableAnnotation = tableAnnotation;
    }

    public Set<FieldInfo> getFields() {
        return fields;
    }

    public void setFields(Set<FieldInfo> fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "TableInfo{" +
                "className='" + className + '\'' +
                ", simpleClassName='" + simpleClassName + '\'' +
                ", classComment='" + classComment + '\'' +
                ", classPackage='" + classPackage + '\'' +
                ", fields=" + fields +
                '}';
    }
}
