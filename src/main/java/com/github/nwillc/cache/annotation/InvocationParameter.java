/*
 * Copyright (c) 2015, nwillc@gmail.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.github.nwillc.cache.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import javax.cache.annotation.CacheInvocationParameter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class InvocationParameter implements CacheInvocationParameter {
    private final int parameterPosition;
    private final Class<?> rawType;
    private final Set<Annotation> annotations;
    private Object value;

    private InvocationParameter(int parameterPosition, Class<?> rawType, Object value, Set<Annotation> annotations) {
        this.parameterPosition = parameterPosition;
        this.rawType = rawType;
        this.value = value;
        this.annotations = annotations;
    }

    public static InvocationParameter[] getParameters(ProceedingJoinPoint pjp) {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        List<InvocationParameter> parameters = new ArrayList<>();

        for (int i = 0; i < method.getParameterCount(); i++) {
            parameters.add(new InvocationParameter(i,
                    method.getParameterTypes()[i],
                    pjp.getArgs()[i],
                    Arrays.stream(method.getParameterAnnotations()[i]).collect(Collectors.toSet())));
        }
        InvocationParameter[] pArray = new InvocationParameter[parameters.size()];
        return parameters.toArray(pArray);
    }

    public static <P extends CacheInvocationParameter> void updateValues(ProceedingJoinPoint pjp, P[] parameters) {
        for (P invocationParameter : parameters) {
            if (invocationParameter instanceof InvocationParameter) {
                ((InvocationParameter) invocationParameter).setValue(pjp.getArgs()[invocationParameter.getParameterPosition()]);
            }
        }
    }

    @Override
    public Class<?> getRawType() {
        return rawType;
    }

    @Override
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public Set<Annotation> getAnnotations() {
        return annotations;
    }

    @Override
    public int getParameterPosition() {
        return parameterPosition;
    }
}
