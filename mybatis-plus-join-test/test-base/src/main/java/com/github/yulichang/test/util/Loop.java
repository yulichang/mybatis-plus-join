package com.github.yulichang.test.util;

import com.github.yulichang.wrapper.interfaces.DoSomething;

public final class Loop {

    public static void loop(DoSomething doSomething) {
        long a = System.currentTimeMillis();
        for (int i = 0; i < 1000_0000; i++) {
            if (i % 1000 == 0) {
                Runtime runtime = Runtime.getRuntime();
                long usedMemory = runtime.totalMemory() - runtime.freeMemory();
                long maxMemory = runtime.maxMemory();
                long freeMemory = runtime.freeMemory();
                System.out.println(i + "   " + (System.currentTimeMillis() - a));
                System.out.println("已用内存：" + usedMemory);
                System.out.println("最大内存：" + maxMemory);
                System.out.println("空闲内存：" + freeMemory);
                System.out.println("-----------------------------");
                a = System.currentTimeMillis();
            }
            doSomething.doIt();
        }
    }
}
