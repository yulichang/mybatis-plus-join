package com.github.yulichang.processor.matedata;

import com.github.yulichang.annotation.Table;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class Conf {

    private String className = "%sCol";
    private String classPackage = "%s.apt";
    private boolean genTables = true;
    private String tablasClassPackage = "%s.tables";
    private String tablesClassName = "%S";

    private boolean initFlag = false;

    private Conf(Conf conf) {
        this.className = conf.className;
        this.classPackage = conf.classPackage;
        this.genTables = conf.genTables;
        this.tablasClassPackage = conf.tablasClassPackage;
        this.tablesClassName = conf.tablesClassName;
        this.initFlag = conf.initFlag;
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
    }

    public static Conf getConf(Conf globalConf, Table table, Collection<String> keys) {
        if (keys == null || keys.isEmpty()) {
            return globalConf;
        }
        Conf conf = new Conf(globalConf);
        keys.forEach(key -> ConfItem.doIt(conf, key, table));
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

    public enum ConfItem {
        className("value", Table::value, (c, v) -> c.setClassName(v.toString())),
        packageName("classPackage", Table::classPackage, (c, v) -> c.setClassPackage(v.toString())),
        genTables("genTables", Table::genTables, (c, v) -> c.setGenTables((boolean) v)),
        tablasPackageName("tablesClassPackage", Table::tablesClassPackage, (c, v) -> c.setTablasClassPackage(v.toString())),
        tablesName("tablesClassName", Table::tablesClassName, (c, v) -> c.setTablesClassName(v.toString()));

        private final String action;

        private final Function<Table, Object> annoVal;

        private final BiConsumer<Conf, Object> doIt;

        ConfItem(String action, Function<Table, Object> annoVal, BiConsumer<Conf, Object> doIt) {
            this.action = action;
            this.annoVal = annoVal;
            this.doIt = doIt;
        }

        public static void doIt(Conf tableConf, String act, Table anno) {
            Arrays.stream(ConfItem.values()).filter(f -> f.action.equals(act)).findFirst()
                    .ifPresent(item -> item.doIt.accept(tableConf, item.annoVal.apply(anno)));
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
                ", initFlag=" + initFlag +
                '}';
    }
}
