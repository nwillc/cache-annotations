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

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheResolverFactory;
import java.lang.annotation.Annotation;
import java.util.Optional;

public final class Utils {
    private static final CacheResolverFactory DEFAULT_FACTORY = new ResolverFactory();

	private Utils() {}

    public static CacheResolverFactory getCacheResolverFactory(Class<? extends CacheResolverFactory> resolverFactory, Class target)
            throws IllegalAccessException, InstantiationException {
        if (!resolverFactory.equals(CacheResolverFactory.class)) {
            return resolverFactory.newInstance();
        }

        Optional<CacheDefaults> cacheDefaults = getCacheDefaults(target);
        if (cacheDefaults.isPresent() && !cacheDefaults.get().cacheResolverFactory().equals(CacheResolverFactory.class)){
            return cacheDefaults.get().cacheResolverFactory().newInstance();
        }

        return DEFAULT_FACTORY;
    }

	public static Optional<CacheDefaults> getCacheDefaults(Class clz) {
		Annotation annotation = clz.getAnnotation(CacheDefaults.class);
		if (annotation instanceof CacheDefaults) {
			CacheDefaults cacheDefaults = (CacheDefaults)annotation;
			return Optional.of(cacheDefaults);
		}
		return Optional.empty();
	}
}
