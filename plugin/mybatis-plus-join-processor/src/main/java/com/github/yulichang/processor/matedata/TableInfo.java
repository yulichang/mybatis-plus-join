package com.github.yulichang.processor.matedata;

import com.github.yulichang.annotation.Table;
import com.github.yulichang.apt.OgnlRoot;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.ognl.Ognl;
import org.apache.ibatis.ognl.OgnlContext;
import org.apache.ibatis.ognl.OgnlException;

import java.util.Set;

/**
 * @author yulichang
 * @since 1.5.0
 */
@SuppressWarnings("unused")
public class TableInfo {

    public static final String OGNL_PREFIX = "OGNL#";


    private final String className;

    private final String simpleClassName;

    private String classComment;

    private String classPackage;

    private final Table tableAnnotation;

    private Set<FieldInfo> fields;

    private String tagClassName;
    private String tagPackageName;

    public TableInfo(Table tableAnnotation, String className, String simpleClassName) {
        this.tableAnnotation = tableAnnotation;
        this.className = className;
        this.simpleClassName = simpleClassName;
    }

    /**
     * 生成的类名
     */
    public String getTagClassName() {
        if (tagClassName == null) {
            tagClassName = parse(tableAnnotation.value(), this.simpleClassName);
        }
        return tagClassName;
    }

    /**
     * 生成类的路径
     */
    public String getTagPackage() {
        if (tagPackageName == null) {
            tagPackageName = parse(tableAnnotation.packageName(), this.classPackage);
        }
        return tagPackageName;
    }


    private String parse(String expression, String source) {
        String tag;
        if (expression.toUpperCase().startsWith(OGNL_PREFIX)) {
            String ognl = expression.substring(OGNL_PREFIX.length());
            OgnlRoot root = new OgnlRoot(this.simpleClassName, this.classPackage);
            OgnlContext context = Ognl.createDefaultContext(root);
            try {
                return Ognl.getValue(ognl, context, context.getRoot()).toString();
            } catch (OgnlException e) {
                throw new BuilderException("Error evaluating expression '" + ognl + "'. Cause: " + e, e);
            }
        } else {
            tag = String.format(expression, source);
        }
        return tag;
    }

    public String getClassName() {
        return className;
    }

    public String getSimpleClassName() {
        return simpleClassName;
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
