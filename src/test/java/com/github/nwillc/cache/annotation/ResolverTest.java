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

import org.junit.Before;
import org.junit.Test;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.annotation.CachePut;
import javax.cache.configuration.MutableConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

public class ResolverTest {
    private Resolver resolver;

    @Before
    public void setUp() throws Exception {
        resolver = new Resolver();
    }

    @Test
    public void testUnknownCache() throws Exception {
        InvocationContext<CachePut> context = new InvocationContext<>(null, null, null, "foo", null, null);
        Cache<Object, Object> cache = resolver.resolveCache(context);

        assertThat(cache).isNull();
    }

    @Test
    public void testKnowCache() throws Exception {
        CacheManager cacheManager = Caching.getCachingProvider().getCacheManager();
        cacheManager.createCache("foo", new MutableConfiguration<>());
        InvocationContext<CachePut> context = new InvocationContext<>(null, null, null, "foo", null, null);
        Cache<Object, Object> cache = resolver.resolveCache(context);

        assertThat(cache).isNotNull();
        cacheManager.destroyCache("foo");
    }
}