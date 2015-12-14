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
import javax.cache.annotation.CacheKeyGenerator;
import javax.cache.annotation.CacheKeyInvocationContext;
import javax.cache.annotation.CacheResolver;
import javax.cache.annotation.CacheResolverFactory;
import javax.cache.annotation.GeneratedCacheKey;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ContextRegistry {
    private final Map<GeneratedCacheKey, Context> registry = new ConcurrentHashMap<>();

    public static ContextRegistry getInstance() {
        return Instance.instance;
    }

    public Context getContext(Annotation annotation, Object target) {
        return registry.get(new GeneratedKey(annotation, target));
    }

    public Context register(Annotation annotation,
                            CacheKeyInvocationContext<? extends Annotation> keyInvocationContext,
                            AnnotationType cat)
            throws InstantiationException, IllegalAccessException {
        CacheResolverFactory cacheResolverFactory = cat.cacheResolverFactory(annotation, keyInvocationContext.getTarget()).newInstance();
        CacheResolver cacheResolver = cacheResolverFactory.getCacheResolver(keyInvocationContext);
        Cache<GeneratedCacheKey, Object> cache = cacheResolver.resolveCache(keyInvocationContext);
        Class<? extends CacheKeyGenerator> aClass = cat.cacheKeyGenerator(annotation, keyInvocationContext.getTarget());
        CacheKeyGenerator generator = aClass == null ? null : aClass.newInstance();
        Context context = new Context(cache,generator, keyInvocationContext);
        GeneratedCacheKey key = new GeneratedKey(annotation, keyInvocationContext.getTarget());
        registry.put(key, context);
        return context;
    }

    private static class Instance {
        private static final ContextRegistry instance = new ContextRegistry();
    }

    public static class Context {
        private final Cache<GeneratedCacheKey, Object> cache;
        private final CacheKeyGenerator keyGenerator;
        private final CacheKeyInvocationContext<? extends Annotation> keyInvocationContext;

        public Context(Cache<GeneratedCacheKey, Object> cache, CacheKeyGenerator keyGenerator,
                       CacheKeyInvocationContext<? extends Annotation> keyInvocationContext) {
            this.cache = cache;
            this.keyGenerator = keyGenerator;
            this.keyInvocationContext = keyInvocationContext;
        }

        public Cache<GeneratedCacheKey, Object> getCache() {
            return cache;
        }

        public CacheKeyGenerator getKeyGenerator() {
            return keyGenerator;
        }

        public CacheKeyInvocationContext<? extends Annotation> getKeyInvocationContext() {
            return keyInvocationContext;
        }
    }
}
