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

import com.github.nwillc.cache.annotation.examples.CachePutExample;
import org.junit.Before;
import org.junit.Test;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

public class CachePutAspectTest {
	private Cache<Long,String> cache;

	@Before
	public void setUp() throws Exception {
		CacheManager cacheManager = Caching.getCachingProvider().getCacheManager();
		cache = cacheManager.getCache(CachePutExample.CACHE_NAME);
		if (cache == null) {
			cache = cacheManager.createCache(CachePutExample.CACHE_NAME, new MutableConfiguration<>());
		}
		assertThat(cache).isNotNull();
	}

	@Test
	public void shouldCut() throws Exception {
		CachePutExample<Long,String> foo = new CachePutExample<>();

		assertThat(cache).isEmpty();
		foo.put(1L, "foo");
		assertThat(foo.getMap().get(1L)).isEqualTo("foo");
		assertThat(cache).hasSize(1);
		assertThat(cache.get(1L)).isEqualTo("foo");
	}


}
