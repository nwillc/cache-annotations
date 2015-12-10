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
import org.junit.Test;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheValue;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class KeyInvocationContextTest {

    @Test
    public void testGetKeyParametersSimple1() throws Exception {
        Method method = Simple1.class.getDeclaredMethods()[0];
        MethodSignature methodSignature = mock(MethodSignature.class);
        when(methodSignature.getMethod()).thenReturn(method);
        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        when(pjp.getSignature()).thenReturn(methodSignature);
        Object[] args = new Object[3];
        args[0] = "foo";
        args[1] = 1L;
        args[2] = "bar";
        when(pjp.getArgs()).thenReturn(args);
        CachePut annotation = method.getAnnotation(CachePut.class);
        KeyInvocationContext<CachePut> keyInvocationContext = new KeyInvocationContext<>(pjp, annotation, AnnotationType.PUT);

        assertThat(keyInvocationContext.getKeyParameters()).hasSize(3);
    }

    @Test
    public void testGetKeyParametersSimple2() throws Exception {
        Method method = Simple2.class.getDeclaredMethods()[0];
        MethodSignature methodSignature = mock(MethodSignature.class);
        when(methodSignature.getMethod()).thenReturn(method);
        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        when(pjp.getSignature()).thenReturn(methodSignature);
        Object[] args = new Object[3];
        args[0] = "foo";
        args[1] = 1L;
        args[2] = "bar";
        when(pjp.getArgs()).thenReturn(args);
        CachePut annotation = method.getAnnotation(CachePut.class);
        KeyInvocationContext<CachePut> keyInvocationContext = new KeyInvocationContext<>(pjp, annotation, AnnotationType.PUT);

        assertThat(keyInvocationContext.getKeyParameters()).hasSize(2);
    }

    @Test
    public void testGetKeyParametersSimple3() throws Exception {
        Method method = Simple3.class.getDeclaredMethods()[0];
        MethodSignature methodSignature = mock(MethodSignature.class);
        when(methodSignature.getMethod()).thenReturn(method);
        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        when(pjp.getSignature()).thenReturn(methodSignature);
        Object[] args = new Object[3];
        args[0] = "foo";
        args[1] = 1L;
        args[2] = "bar";
        when(pjp.getArgs()).thenReturn(args);
        CachePut annotation = method.getAnnotation(CachePut.class);
        KeyInvocationContext<CachePut> keyInvocationContext = new KeyInvocationContext<>(pjp, annotation, AnnotationType.PUT);

        assertThat(keyInvocationContext.getKeyParameters()).hasSize(1);
        assertThat(keyInvocationContext.getValueParameter().getParameterPosition()).isEqualTo(2);
    }

    public static class Simple1 {
        @CachePut(cacheName = "foo")
        public void method(String str, Long value, String noise) {
        }
    }


    public static class Simple2 {
        @CachePut(cacheName = "foo")
        public void method(String str, Long value, @CacheValue String noise) {
        }
    }

    public static class Simple3 {
        @CachePut(cacheName = "foo")
        public void method(@CacheKey String str, Long value, @CacheValue String noise) {
        }
    }
}