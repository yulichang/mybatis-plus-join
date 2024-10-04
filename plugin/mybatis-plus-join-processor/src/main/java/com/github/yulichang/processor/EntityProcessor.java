package com.github.yulichang.processor;

import com.github.yulichang.processor.matedata.Conf;
import com.github.yulichang.processor.matedata.FieldInfo;
import com.github.yulichang.processor.matedata.TableInfo;
import com.github.yulichang.processor.utils.StringUtil;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.Writer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author yulichang
 * @since 1.5.0
 */
public class EntityProcessor extends AbstractProcessor {

    private static final String TABLE = "com.github.yulichang.annotation.Table";
    private static final String TABLE_FIELD = "com.baomidou.mybatisplus.annotation.TableField";
    private static final String TABLE_FIELD_EXIST = "exist";

    private static final String BASE_COLUMN = "com.github.yulichang.extension.apt.matedata.BaseColumn";
    private static final String COLUMN = "com.github.yulichang.extension.apt.matedata.Column";

    private Elements elementUtils;
    private Types typeUtils;
    private Messager messager;
    private Conf globalConf;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.elementUtils = processingEnv.getElementUtils();
        this.typeUtils = processingEnv.getTypeUtils();
        this.messager = processingEnv.getMessager();
        this.globalConf = new Conf(processingEnv.getFiler(), this::note);
    }

    private void note(String msg) {
        messager.printMessage(Diagnostic.Kind.NOTE, msg + " - " + UUID.randomUUID());
    }


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!roundEnv.processingOver()) {
            if (!globalConf.isEnable()) {
                return false;
            }
            Set<? extends Element> tables = roundEnv.getRootElements().stream().filter(i -> {
                List<? extends AnnotationMirror> mirrors = i.getAnnotationMirrors();
                if (mirrors != null && !mirrors.isEmpty()) {
                    if (mirrors.stream().anyMatch(m -> m.getAnnotationType().toString().equals(TABLE))) {
                        return true;
                    }
                    if (StringUtil.isNotEmpty(globalConf.getScanAnno())) {
                        if (mirrors.stream().anyMatch(m -> m.getAnnotationType().toString().equals(globalConf.getScanAnno()))) {
                            return true;
                        }
                    }
                }
                if (StringUtil.isNotEmpty(globalConf.getScanPackage())) {
                    if (i.getKind() != ElementKind.CLASS) {
                        return false;
                    }
                    if (i.getModifiers().contains(Modifier.ABSTRACT)) {
                        return false;
                    }
                    String pkg = elementUtils.getPackageOf(i).getQualifiedName().toString();
                    String[] scanPackages = globalConf.getScanPackage().split(",");
                    return Arrays.stream(scanPackages).anyMatch(s -> StringUtil.matches(pkg, s));
                }
                return false;
            }).collect(Collectors.toSet());
            if (!tables.isEmpty()) {
                note("mybatis plus join processor start");
                tables.stream().filter(f -> f instanceof TypeElement)
                        .map(f -> (TypeElement) f).map(this::createColumn).filter(TableInfo::isGenTables)
                        .collect(Collectors.groupingBy(TableInfo::getTagTablesPackageName))
                        .forEach(this::createTables);
            }
        }
        return false;
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportedAnnotationTypes = new HashSet<>();
        supportedAnnotationTypes.add(TABLE);
        if (StringUtil.isNotEmpty(globalConf.getScanAnno())) {
            supportedAnnotationTypes.add(globalConf.getScanAnno());
        }
        return supportedAnnotationTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * 生成Column类
     */
    private TableInfo createColumn(TypeElement element) {
        AnnotationMirror tb = element.getAnnotationMirrors().stream().filter(a ->
                a.getAnnotationType().asElement().toString().equals(TABLE)).findFirst().orElse(null);
        Conf conf = Optional.ofNullable(tb).map(t -> Conf.getConf(globalConf, t.getElementValues())).orElse(globalConf);
        TableInfo tableInfo = new TableInfo(conf, element.toString(), element.getSimpleName().toString());
        tableInfo.setClassPackage(elementUtils.getPackageOf(element).getQualifiedName().toString());
        tableInfo.setClassComment(elementUtils.getDocComment(element));

        List<FieldInfo> fieldInfos = new ArrayList<>();

        TypeElement currElement = element;
        do {
            fieldInfos.addAll(currElement.getEnclosedElements().stream().filter(e ->
                            // 过滤static字段
                            e.getKind() == ElementKind.FIELD && !e.getModifiers().contains(Modifier.STATIC))
                    .filter(e -> {
                        // 过滤 exist = false 的字段
                        AnnotationMirror tableField = e.getAnnotationMirrors().stream().filter(f ->
                                TABLE_FIELD.equals(f.getAnnotationType().toString())).findFirst().orElse(null);
                        if (tableField != null) {
                            Map<String, Object> propMap = tableField.getElementValues().entrySet().stream()
                                    .collect(Collectors.toMap(entry -> entry.getKey().getSimpleName().toString(), entry -> entry.getValue().getValue()));
                            Object exist = propMap.get(TABLE_FIELD_EXIST);
                            return exist == null || (boolean) exist;
                        }
                        return true;
                    })
                    .map(e -> new FieldInfo(e.toString(), elementUtils.getDocComment(e))).collect(Collectors.toList()));
            currElement = (TypeElement) typeUtils.asElement(currElement.getSuperclass());
        } while (currElement != null);

        tableInfo.setFields(fieldInfos);

        StringBuilderHelper content = new StringBuilderHelper(tableInfo)
                .addPackage(tableInfo.getTagClassPackage())
                .newLine()
                .addImport(true, BASE_COLUMN)
                .addImport(true, COLUMN)
                .addImport(true, tableInfo.getClassName())
                .newLine(tableInfo.isCache())
                .addImport(tableInfo.isCache(), Map.class.getName())
                .addImport(tableInfo.isCache(), Objects.class.getName())
                .addImport(tableInfo.isCache(), ConcurrentHashMap.class.getName())
                .newLine()
                .addClass(tableInfo.getClassComment(), tableInfo.getTagClassName(),
                        StringUtil.getSimpleName(BASE_COLUMN) + "<" + tableInfo.getSimpleClassName() + ">",
                        c -> c
                                .addConstructor()
                                .addFields()
                                .addMethod()
                                .addBuild()
                                .addCacheClass()
                );
        writerFile(tableInfo.getTagClassPackage() + "." + tableInfo.getTagClassName(), content.getContent());
        return tableInfo;
    }

    /**
     * 生成Tables
     */
    private void createTables(String tagPackage, List<TableInfo> tableInfos) {
        StringBuilderHelper content = new StringBuilderHelper();
        // package
        content.addPackage(tagPackage);
        content.newLine();
        // import
        tableInfos.forEach(tableInfo -> content.addImport(true, tableInfo.getTagClassPackage() + "." + tableInfo.getTagClassName()));
        content.newLine();
        // class
        String tables = "Tables";
        content.addClass(" tables", tables, "", c -> c
                .newLine()
                // 私有构造
                .addPrivateConstructor(tables)
                // 添加table字段
                .addTablesFields(tableInfos));

        writerFile(tagPackage + ".Tables", content.getContent());
    }

    private void writerFile(String fullClassName, String content) {
        try {
            JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(fullClassName);

            Writer writer = sourceFile.openWriter();
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            this.messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    public static class StringBuilderHelper {
        private final StringBuilder sb = new StringBuilder();
        private TableInfo tableInfo;

        public StringBuilderHelper() {
        }

        public StringBuilderHelper(TableInfo tableInfo) {
            this.tableInfo = tableInfo;
        }

        public StringBuilderHelper addPackage(String packageName) {
            sb.append("package ").append(packageName).append(";\n");
            return this;
        }

        public StringBuilderHelper addImport(boolean cond, String importName) {
            if (cond) {
                sb.append("import ").append(importName).append(";\n");
            }
            return this;
        }

        public StringBuilderHelper addClass(String comment, String tagClassName, String impl,
                                            Consumer<StringBuilderHelper> consumer) {
            addComment("", comment);
            sb.append("public class ").append(tagClassName);
            if (StringUtil.isNotEmpty(impl)) {
                sb.append(" extends ").append(impl);
            }
            sb.append(" {\n");
            consumer.accept(this);
            sb.append("}\n");
            return this;
        }

        public StringBuilderHelper addConstructor() {
            // 无参构造
            newLine();
            sb.append(String.format("\tpublic %s() {\n\t}\n", tableInfo.getTagClassName()));
            newLine();
            //有参构造
            sb.append("\tpublic ").append(tableInfo.getTagClassName()).append("(String alias) {\n" +
                    "\t\tsuper.alias = alias;\n" +
                    "\t}\n");
            newLine();
            return this;
        }

        /**
         * 私有构造
         */
        public StringBuilderHelper addPrivateConstructor(String tagClassName) {
            sb.append(String.format("\tprivate %s() {\n\t}\n", tagClassName));
            newLine();
            return this;
        }

        public StringBuilderHelper addFields() {
            tableInfo.getFields().forEach(fieldInfo -> {
                addComment("\t", fieldInfo.getComment());
                sb.append(String.format("\tpublic final Column %s = new Column(this, \"%s\");\n",
                        fieldInfo.getProperty(), fieldInfo.getProperty()));
                newLine();
            });
            return this;
        }

        public StringBuilderHelper addTablesFields(List<TableInfo> tableInfos) {
            tableInfos.forEach(tableInfo -> {
                addComment("\t", tableInfo.getClassComment());
                sb.append(String.format("\tpublic static final %s %s = %s.build();\n",
                        tableInfo.getTagClassName(),
                        String.format(tableInfo.getTagTablesName(), tableInfo.getSimpleClassName()),
                        tableInfo.getTagClassName()));
                newLine();
            });
            return this;
        }

        public StringBuilderHelper addMethod() {
            sb.append("\t@Override\n" +
                            "\tpublic Class<").append(tableInfo.getSimpleClassName()).append("> getColumnClass() {\n")
                    .append("\t\treturn ").append(tableInfo.getSimpleClassName()).append(".class;\n")
                    .append("\t}\n");
            newLine();
            return this;
        }

        public StringBuilderHelper addBuild() {
            sb.append("\tpublic static ").append(tableInfo.getTagClassName()).append(" build() {\n");
            sb.append("\t\treturn new ").append(tableInfo.getTagClassName()).append("();\n");
            sb.append("\t}\n");
            newLine();
            sb.append("\tpublic static ").append(tableInfo.getTagClassName()).append(" build(String alias) {\n");
            if (tableInfo.isCache()) {
                sb.append("\t\tObjects.requireNonNull(alias);\n");
                sb.append("\t\treturn Cache.CACHE.computeIfAbsent(alias, key -> new ").append(tableInfo.getTagClassName()).append("(key));\n");
            } else {
                sb.append("\t\treturn new ").append(tableInfo.getTagClassName()).append("(alias);\n");
            }
            sb.append("\t}\n");
            newLine();
            return this;
        }

        public StringBuilderHelper addCacheClass() {
            if (tableInfo.isCache()) {
                sb.append("\tpublic static class Cache {\n")
                        .append("\t\tprivate static final Map<String, ").append(tableInfo.getTagClassName()).append("> CACHE = new ConcurrentHashMap<>();\n")
                        .append("\t}\n");
                newLine();
            }
            return this;
        }

        private StringBuilderHelper addComment(String prefix, String comment) {
            if (StringUtil.isNotEmpty(comment)) {
                sb.append(prefix).append("/**\n");
                sb.append(Arrays.stream(comment.split("\n")).map(f -> prefix + " *" + f).collect(Collectors.joining("\n")));
                sb.append("\n").append(prefix).append(" */");
                newLine();
            }
            return this;
        }

        public StringBuilderHelper newLine() {
            sb.append("\n");
            return this;
        }

        public StringBuilderHelper newLine(boolean cond) {
            if (cond) {
                sb.append("\n");
            }
            return this;
        }

        public String getContent() {
            return sb.toString();
        }

    }
}