package com.github.yulichang.relation;

import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.metadata.MPJTableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.base.mapper.wrapper.MappingQuery;
import com.github.yulichang.mapper.MPJTableFieldInfo;
import com.github.yulichang.mapper.MPJTableInfo;
import com.github.yulichang.toolkit.LambdaUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("all")
public class Relation {


    public static <T> List<T> list(List<T> data, List<SFunction<T, ?>> property) {
        if (CollectionUtils.isEmpty(data)) {
            return data;
        }
        Class<?> entityClass = data.get(0).getClass();
        MPJTableInfo tableInfo = MPJTableInfoHelper.getTableInfo(entityClass);
        if (tableInfo.isHasMappingOrField()) {
            boolean hasProperty = CollectionUtils.isNotEmpty(property);
            List<String> listProperty = hasProperty ? property.stream().map(LambdaUtils::getName).collect(
                    Collectors.toList()) : null;
            for (MPJTableFieldInfo fieldInfo : tableInfo.getFieldList()) {
                if (!hasProperty || listProperty.contains(fieldInfo.getProperty())) {
                    List<Object> itemList = data.stream().map(fieldInfo::thisFieldGet).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(itemList)) {
                        List<?> joinList = MappingQuery.mpjQueryList(fieldInfo.getJoinMapper(),
                                fieldInfo.isMappingEntity() && fieldInfo.isFieldIsMap(), SqlKeyword.IN,
                                fieldInfo.getJoinColumn(), itemList, fieldInfo);
                        data.forEach(i -> mpjBindData(i, fieldInfo, joinList));
                        fieldInfo.removeJoinField(joinList);
                        if (CollectionUtils.isEmpty(joinList)) {
                            continue;
                        }
                    } else {
                        data.forEach(i -> fieldInfo.fieldSet(i, new ArrayList<>()));
                    }
                }
            }
        }
        return data;
    }


    /**
     * 查询映射关系<br/>
     * 对结果进行二次查询<br/>
     * 可以自行查询然后在通过此方法进行二次查询<br/>
     * list为null或空，会查询全部映射关系<br/>
     *
     * @param t 第一次查询结果
     */
    public static <T> T one(T t, List<SFunction<T, ?>> property) {
        if (t == null) {
            return null;
        }
        MPJTableInfo tableInfo = MPJTableInfoHelper.getTableInfo(t.getClass());
        if (tableInfo.isHasMappingOrField()) {
            boolean hasProperty = CollectionUtils.isNotEmpty(property);
            List<String> list = hasProperty ? property.stream().map(LambdaUtils::getName).collect(
                    Collectors.toList()) : null;
            for (MPJTableFieldInfo fieldInfo : tableInfo.getFieldList()) {
                if (!hasProperty || list.contains(fieldInfo.getProperty())) {
                    Object obj = fieldInfo.thisFieldGet(t);
                    if (obj != null) {
                        List<?> joinList = MappingQuery.mpjQueryList(fieldInfo.getJoinMapper(),
                                fieldInfo.isFieldIsMap(), SqlKeyword.EQ, fieldInfo.getJoinColumn(), obj, fieldInfo);
                        mpjBindData(t, fieldInfo, joinList);
                        fieldInfo.removeJoinField(joinList);
                    }
                }
            }
        }
        return t;
    }

    public static <R> void mpjBindData(R t, MPJTableFieldInfo fieldInfo, List<?> joinList) {
        if (fieldInfo.isMappingEntity()) {
            List<?> list = joinList.stream().filter(j -> fieldInfo.joinFieldGet(j).equals(fieldInfo.thisFieldGet(t)))
                    .collect(Collectors.toList());
            MPJTableFieldInfo.bind(fieldInfo, t, list);
        }
        if (fieldInfo.isMappingField()) {
            MPJTableFieldInfo.bind(fieldInfo, t, joinList.stream().filter(j -> fieldInfo.joinFieldGet(j).equals(
                    fieldInfo.thisFieldGet(t))).map(fieldInfo::bindFieldGet).collect(Collectors.toList()));
        }
    }
}
