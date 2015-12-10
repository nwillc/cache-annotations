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
import org.junit.Before;
import org.junit.Test;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.annotation.CachePut;
import javax.cache.configuration.MutableConfiguration;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResolverTest {
    private static final String CACHE_NAME = "foo";
    private Resolver resolver;
    private InvocationContext<CachePut> context;

    @Before
    public void setUp() throws Exception {
        resolver = new Resolver();
        Method method = getClass().getMethods()[0];
        MethodSignature methodSignature = mock(MethodSignature.class);
        when(methodSignature.getMethod()).thenReturn(method);
        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        when(pjp.getSignature()).thenReturn(methodSignature);
        CachePut cachePut = mock(CachePut.class);
        when(cachePut.cacheName()).thenReturn(CACHE_NAME);
        context = new InvocationContext<>(pjp, cachePut, CacheAnnotationType.PUT);
    }

    @Test
    public void testUnknownCache() throws Exception {
        Cache<Object, Object> cache = resolver.resolveCache(context);
        assertThat(cache).isNull();
    }

    @Test
    public void testKnowCache() throws Exception {
        CacheManager cacheManager = Caching.getCachingProvider().getCacheManager();
        cacheManager.createCache(CACHE_NAME, new MutableConfiguration<>());
        Cache<Object, Object> cache = resolver.resolveCache(context);

        assertThat(cache).isNotNull();
        cacheManager.destroyCache(CACHE_NAME);
    }
}