package com.github.yulichang.processor.matedata;

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

    private final Conf conf;

    private Set<FieldInfo> fields;

    private String tagClassName;
    private String tagPackageName;
    private String tagTablesName;
    private String tagTablesPackageName;

    public TableInfo(Conf conf, String className, String simpleClassName) {
        this.conf = conf;
        this.className = className;
        this.simpleClassName = simpleClassName;
    }

    /**
     * 生成的类名
     */
    public String getTagClassName() {
        if (tagClassName == null) {
            tagClassName = parse(conf.getClassName(), this.simpleClassName);
        }
        return tagClassName;
    }

    /**
     * 生成类的路径
     */
    public String getTagClassPackage() {
        if (tagPackageName == null) {
            tagPackageName = parse(conf.getClassPackage(), this.classPackage);
        }
        return tagPackageName;
    }

    /**
     * 获取Tables的字段名
     */
    public String getTagTablesName() {
        if (tagTablesName == null) {
            tagTablesName = parse(conf.getTablesClassName(), this.simpleClassName);
        }
        return tagTablesName;
    }

    /**
     * 获取Tables的路径
     */
    public String getTagTablesPackageName() {
        if (tagTablesPackageName == null) {
            tagTablesPackageName = parse(conf.getTablasClassPackage(), this.classPackage);
        }
        return tagTablesPackageName;
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

    public String getClassPackage1() {
        return classPackage;
    }

    public void setClassPackage(String classPackage) {
        this.classPackage = classPackage;
    }

    public boolean isGenTables() {
        return this.conf.isGenTables();
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
