package com.r00t.dynamiczone.util;

import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class ReflectionMethodUtils {

    public static void doWithMethodParameters(Method method, Object[] methodArgs,
                                              BiConsumer<Parameter, Object> callback, Predicate<Parameter> filter) {
        Parameter[] parameters = method.getParameters();
        Assert.isTrue(parameters.length == methodArgs.length,
                      "Parameters size has to be equal with method arguments size");

        for (int x = 0; x < parameters.length; x++) {
            if (filter != null && !filter.test(parameters[x]))
                continue;
            callback.accept(parameters[x], methodArgs[x]);
        }
    }

    public static void doWithMethodParametersIndexes(Method method, Object[] methodArgs,
                                                     BiConsumer<Integer, Object> callback,
                                                     Predicate<Parameter> filter) {
        Parameter[] parameters = method.getParameters();
        Assert.isTrue(parameters.length == methodArgs.length,
                      "Parameters size has to be equal with method arguments size");

        for (int x = 0; x < parameters.length; x++) {
            if (filter != null && !filter.test(parameters[x]))
                continue;
            callback.accept(x, methodArgs[x]);
        }
    }
}
