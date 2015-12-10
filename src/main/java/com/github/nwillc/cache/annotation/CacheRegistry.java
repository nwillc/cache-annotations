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

import javax.cache.Cache;
import javax.cache.annotation.CacheResolver;
import javax.cache.annotation.CacheResolverFactory;
import javax.cache.annotation.GeneratedCacheKey;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class CacheRegistry {
    private final Map<Annotation, Cache<GeneratedCacheKey, Object>> registry = new ConcurrentHashMap<>();

    public static CacheRegistry getInstance() {
        return Instance.instance;
    }

    public Cache<GeneratedCacheKey, Object> get(Annotation key) {
        Cache<GeneratedCacheKey, Object> cache = registry.get(key);
        if (cache != null && cache.isClosed()) {
            cache = null;
        }
        return cache;
    }

    public Cache<GeneratedCacheKey, Object> register(Annotation key,
                                          InvocationContext<? extends Annotation> invocationContext, AnnotationType cat)
            throws InstantiationException, IllegalAccessException {
        CacheResolverFactory cacheResolverFactory = cat.cacheResolverFactory(key, invocationContext.getTarget()).newInstance();
        CacheResolver cacheResolver = cacheResolverFactory.getCacheResolver(invocationContext);
        Cache<GeneratedCacheKey, Object> cache = cacheResolver.resolveCache(invocationContext);
        registry.put(key, cache);
        return cache;
    }

    private static class Instance {
        private static final CacheRegistry instance = new CacheRegistry();
    }


}
