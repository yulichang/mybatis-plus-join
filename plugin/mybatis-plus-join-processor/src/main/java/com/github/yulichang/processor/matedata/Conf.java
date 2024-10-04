package com.github.yulichang.processor.matedata;

import javax.annotation.processing.Filer;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Conf {

    private String className = "%sCol";
    private String classPackage = "%s.apt";
    private boolean genTables = true;
    private String tablasClassPackage = "%s.tables";
    private String tablesClassName = "%S";
    private boolean cache = true;

    private boolean enable = true;
    private String scanAnno = "";
    private String scanPackage = "";

    private boolean initFlag = false;

    private Conf(Conf conf) {
        this.className = conf.className;
        this.classPackage = conf.classPackage;
        this.genTables = conf.genTables;
        this.tablasClassPackage = conf.tablasClassPackage;
        this.tablesClassName = conf.tablesClassName;
        this.initFlag = conf.initFlag;
        this.cache = conf.cache;

        this.enable = conf.enable;
        this.scanAnno = conf.scanAnno;
        this.scanPackage = conf.scanPackage;
    }


    public Conf(Filer filer, Consumer<String> log) {
        try {
            FileObject confPath = filer.createResource(StandardLocation.CLASS_OUTPUT, "", "mybatis-plus-join");
            File file = new File(confPath.toUri()).getParentFile();
            int loop = 0;
            while (file != null && file.exists() && file.isDirectory()) {
                loop++;
                File[] files = file.listFiles();
                if (files != null && files.length > 0) {
                    File confFile = Arrays.stream(files).filter(f -> f.getName().equals("mybatis-plus-join.properties")).findFirst().orElse(null);
                    if (confFile != null && confFile.exists()) {
                        log.accept(String.format("use mybatis-plus-join.properties %s", confFile.getAbsolutePath()));
                        this.init(confFile);
                        break;
                    }
                }
                file = file.getParentFile();
                if (loop > 50) {
                    break;
                }
            }
            if (!this.initFlag) {
                log.accept("not find mybatis-plus-join.properties use default setting");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void init(File confFile) throws IOException {
        this.initFlag = true;
        Properties properties = new Properties();
        properties.load(Files.newInputStream(confFile.toPath()));
        this.className = properties.getOrDefault("className", this.className).toString();
        this.classPackage = properties.getOrDefault("classPackage", this.classPackage).toString();
        this.genTables = Boolean.parseBoolean(properties.getOrDefault("genTables", Boolean.toString(this.genTables)).toString());
        this.tablasClassPackage = properties.getOrDefault("tablasClassPackage", this.tablasClassPackage).toString();
        this.tablesClassName = properties.getOrDefault("tablesClassName", this.tablesClassName).toString();
        this.cache = Boolean.parseBoolean(properties.getOrDefault("cache", this.cache).toString());
        this.enable = Boolean.parseBoolean(properties.getOrDefault("enable", this.enable).toString());
        this.scanAnno = properties.getOrDefault("scanAnno", this.scanAnno).toString();
        this.scanPackage = properties.getOrDefault("scanPackage", this.scanPackage).toString();
    }

    public static Conf getConf(Conf globalConf, Map<? extends ExecutableElement, ? extends AnnotationValue> elementMap) {
        if (elementMap == null || elementMap.isEmpty()) {
            return globalConf;
        }
        Conf conf = new Conf(globalConf);
        elementMap.forEach((k, v) -> ConfItem.doIt(conf, k.getSimpleName().toString(), v));
        return conf;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassPackage() {
        return classPackage;
    }

    public void setClassPackage(String classPackage) {
        this.classPackage = classPackage;
    }

    public boolean isGenTables() {
        return genTables;
    }

    public void setGenTables(boolean genTables) {
        this.genTables = genTables;
    }

    public String getTablasClassPackage() {
        return tablasClassPackage;
    }

    public void setTablasClassPackage(String tablasClassPackage) {
        this.tablasClassPackage = tablasClassPackage;
    }

    public String getTablesClassName() {
        return tablesClassName;
    }

    public void setTablesClassName(String tablesClassName) {
        this.tablesClassName = tablesClassName;
    }

    public boolean isCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    public String getScanAnno() {
        return scanAnno;
    }

    public String getScanPackage() {
        return scanPackage;
    }

    public boolean isEnable() {
        return enable;
    }

    public enum ConfItem {
        className("value", (c, v) -> c.setClassName(v.toString())),
        packageName("classPackage", (c, v) -> c.setClassPackage(v.toString())),
        genTables("genTables", (c, v) -> c.setGenTables((boolean) v)),
        tablasPackageName("tablesClassPackage", (c, v) -> c.setTablasClassPackage(v.toString())),
        tablesName("tablesClassName", (c, v) -> c.setTablesClassName(v.toString())),
        cache("cache", (c, v) -> c.setCache((boolean) v));

        private final String action;

        private final BiConsumer<Conf, Object> doIt;

        ConfItem(String action, BiConsumer<Conf, Object> doIt) {
            this.action = action;
            this.doIt = doIt;
        }

        public static void doIt(Conf tableConf, String key, AnnotationValue value) {
            Arrays.stream(ConfItem.values()).filter(f -> f.action.equals(key)).findFirst()
                    .ifPresent(item -> item.doIt.accept(tableConf, value.getValue()));
        }
    }

    @Override
    public String toString() {
        return "Conf{" +
                "className='" + className + '\'' +
                ", classPackage='" + classPackage + '\'' +
                ", genTables=" + genTables +
                ", tablasClassPackage='" + tablasClassPackage + '\'' +
                ", tablesClassName='" + tablesClassName + '\'' +
                ", cache=" + cache +
                ", scanAnno='" + scanAnno + '\'' +
                ", scanPackage='" + scanPackage + '\'' +
                ", initFlag=" + initFlag +
                '}';
    }
}
