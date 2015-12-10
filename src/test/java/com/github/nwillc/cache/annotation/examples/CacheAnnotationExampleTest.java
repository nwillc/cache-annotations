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

package com.github.nwillc.cache.annotation.examples;

import com.github.nwillc.cache.annotation.GeneratedKey;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

public class CacheAnnotationExampleTest {
    protected Cache<GeneratedKey, String> cache;
    private CacheAnnotationExample<Long, String> cacheAnnotationExample;

    @Before
    public void setUp() throws Exception {
        CacheManager cacheManager = Caching.getCachingProvider().getCacheManager();
        cache = cacheManager.getCache(CacheAnnotationExample.CACHE_NAME);
        if (cache == null) {
            cache = cacheManager.createCache(CacheAnnotationExample.CACHE_NAME, new MutableConfiguration<>());
        }
        assertThat(cache).isNotNull();
        cacheAnnotationExample = new CacheAnnotationExample<>();
    }

    @After
    public void tearDown() throws Exception {
        cache.close();
    }

    @Test
    public void shouldCut() throws Exception {
        assertThat(cache).isEmpty();
        cacheAnnotationExample.put(1L, "foo");
        assertThat(cacheAnnotationExample.getMap().get(1L)).isEqualTo("foo");
        assertThat(cache).hasSize(1);
        GeneratedKey key = new GeneratedKey(1L);
        assertThat(cache.get(key)).isEqualTo("foo");
    }

    @Test
    public void shouldGet() throws Exception {
        cacheAnnotationExample.getMap().put(1L, "foo");
        assertThat(cache).isEmpty();
        assertThat(cacheAnnotationExample.get(1L)).isEqualTo("foo");
        assertThat(cache).hasSize(1);
        cacheAnnotationExample.getMap().clear();
        assertThat(cacheAnnotationExample.get(1L)).isEqualTo("foo");
    }

    @Test
    public void testRemoveAll() throws Exception {
        assertThat(cache).isEmpty();
        assertThat(cacheAnnotationExample.getMap()).isEmpty();
        cacheAnnotationExample.put(0L, "bar");
        cacheAnnotationExample.put(1L, "baz");
        assertThat(cache).hasSize(2);
        assertThat(cacheAnnotationExample.getMap()).hasSize(2);
        cacheAnnotationExample.clear();
        assertThat(cache).isEmpty();
        assertThat(cacheAnnotationExample.getMap()).isEmpty();
    }

    @Test
    public void testRemove() throws Exception {
        assertThat(cache).isEmpty();
        assertThat(cacheAnnotationExample.getMap()).isEmpty();
        cacheAnnotationExample.put(0L, "bar");
        cacheAnnotationExample.put(1L, "baz");
        assertThat(cache).hasSize(2);
        assertThat(cacheAnnotationExample.getMap()).hasSize(2);
        cacheAnnotationExample.remove(1L);
        assertThat(cache).hasSize(1);
    }
}
