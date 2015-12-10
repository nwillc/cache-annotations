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
import javax.cache.annotation.CacheValue;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InvocationParameterTest {

    @Test
    public void testConstructor() throws Exception {
        Method method = Simple.class.getDeclaredMethods()[0];
        MethodSignature methodSignature = mock(MethodSignature.class);
        when(methodSignature.getMethod()).thenReturn(method);
        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        when(pjp.getSignature()).thenReturn(methodSignature);
        Object[] args = new Object[3];
        args[0] = "foo";
        args[1] = 1L;
        args[2] = "bar";
        when(pjp.getArgs()).thenReturn(args);

        InvocationParameter[] invocationParameters = InvocationParameter.getParameters(pjp);
        assertThat(invocationParameters).isNotNull();
        assertThat(invocationParameters).hasSize(3);
    }

    public static class Simple<P extends String> {
        public void method(@CacheKey P str, @CacheValue Long value, String noise) {}
    }
}