package com.github.yulichang.toolkit.support;

public class KotlinLambdaMeta implements LambdaMeta{
    private final String implMethodName;
    private final Class<?> instantiatedClass;

    public KotlinLambdaMeta(String implMethodName,Class<?> instantiatedClass){
        this.implMethodName=implMethodName;
        this.instantiatedClass=instantiatedClass;
    }

    @Override
    public String getImplMethodName() {
        return implMethodName;
    }

    @Override
    public Class<?> getInstantiatedClass() {
        return instantiatedClass;
    }
}
