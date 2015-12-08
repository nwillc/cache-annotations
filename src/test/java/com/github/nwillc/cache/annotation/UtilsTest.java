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


import com.github.nwillc.contracts.UtilityClassContract;
import org.junit.Test;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheResolverFactory;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class UtilsTest extends UtilityClassContract {
	private static final String CACHE_NAME = "foo";

	@Override
	public Class<?> getClassToTest() {
		return Utils.class;
	}

	@Test
	public void shouldReturnNoCacheDefaults() throws Exception {
		assertThat(Utils.getCacheDefaults(getClass()).isPresent()).isFalse();
	}

    @Test
    public void testGetCacheResolverFactoryDefault() throws Exception {
        CacheResolverFactory cacheResolverFactory = Utils.getCacheResolverFactory(CacheResolverFactory.class, String.class);

        assertThat(cacheResolverFactory).isNotNull();
        assertThat(cacheResolverFactory).isInstanceOf(ResolverFactory.class);
    }

    @Test
    public void testGetCacheResolverFactoryFromClass() throws Exception {
        CacheResolverFactory cacheResolverFactory = Utils.getCacheResolverFactory(CacheResolverFactory.class, Foo.class);

        assertThat(cacheResolverFactory).isNotNull();
        assertThat(cacheResolverFactory).isInstanceOf(SubResolverFactory.class);
    }

    @Test
    public void testGetCacheResolverFactoryFromMethod() throws Exception {
        CacheResolverFactory cacheResolverFactory = Utils.getCacheResolverFactory(SubResolverFactory.class, String.class);

        assertThat(cacheResolverFactory).isNotNull();
        assertThat(cacheResolverFactory).isInstanceOf(SubResolverFactory.class);
    }

    @Test
    public void testGetCacheDefaults() throws Exception {
        Optional<CacheDefaults> cacheDefaults = Utils.getCacheDefaults(Foo.class);
        assertThat(cacheDefaults.isPresent()).isTrue();
        assertThat(cacheDefaults.get().cacheName()).isEqualTo(CACHE_NAME);
    }

    @CacheDefaults(cacheName = CACHE_NAME, cacheResolverFactory = SubResolverFactory.class)
	class Foo {

	}

    static class SubResolverFactory extends ResolverFactory {}
}