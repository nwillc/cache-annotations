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
import javax.cache.annotation.CacheKeyGenerator;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheRemove;
import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResolverFactory;
import javax.cache.annotation.CacheResult;
import java.lang.annotation.Annotation;
import java.util.Optional;

public enum AnnotationType {

    PUT {
        @Override
        public String cacheName(Annotation a) {
            return ((CachePut) a).cacheName();
        }

        @Override
        public Class<? extends CacheResolverFactory> cacheResolverFactory(Annotation a) {
            return ((CachePut) a).cacheResolverFactory();
        }

        @Override
        protected Class<? extends CacheKeyGenerator> cacheKeyGenerator(Annotation a) {
            return ((CachePut) a).cacheKeyGenerator();
        }
    },
    REMOVE {
        @Override
        public String cacheName(Annotation a) {
            return ((CacheRemove) a).cacheName();
        }

        @Override
        public Class<? extends CacheResolverFactory> cacheResolverFactory(Annotation a) {
            return ((CacheRemove) a).cacheResolverFactory();
        }

        @Override
        protected Class<? extends CacheKeyGenerator> cacheKeyGenerator(Annotation a) {
            return ((CacheRemove) a).cacheKeyGenerator();
        }
    },
    REMOVE_ALL {
        @Override
        public String cacheName(Annotation a) {
            return ((CacheRemoveAll) a).cacheName();
        }

        @Override
        public Class<? extends CacheResolverFactory> cacheResolverFactory(Annotation a) {
            return ((CacheRemoveAll) a).cacheResolverFactory();
        }

        @Override
        protected Class<? extends CacheKeyGenerator> cacheKeyGenerator(Annotation a) {
           return null;
        }
    },
    RESULT {
        @Override
        public String cacheName(Annotation a) {
            return ((CacheResult) a).cacheName();
        }

        @Override
        public Class<? extends CacheResolverFactory> cacheResolverFactory(Annotation a) {
            return ((CacheResult) a).cacheResolverFactory();
        }

        @Override
        protected Class<? extends CacheKeyGenerator> cacheKeyGenerator(Annotation a) {
            return ((CacheResult) a).cacheKeyGenerator();
        }
    };


    abstract protected String cacheName(Annotation a);
    abstract protected Class<? extends CacheResolverFactory> cacheResolverFactory(Annotation a);
    abstract protected Class<? extends CacheKeyGenerator> cacheKeyGenerator(Annotation a);

    public Class<? extends CacheKeyGenerator> cacheKeyGenerator(Annotation a, Object target) {
        Class<? extends CacheKeyGenerator> cacheKeyGenerator = cacheKeyGenerator(a);
        if (cacheKeyGenerator == null) {
            return null;
        }
        if (!cacheKeyGenerator.equals(CacheKeyGenerator.class)) {
            return cacheKeyGenerator;
        }
        Optional<CacheDefaults> cacheDefaults = AnnotationType.getCacheDefaults(target.getClass());
        if (cacheDefaults.isPresent() && !cacheDefaults.get().cacheKeyGenerator().equals(CacheKeyGenerator.class)) {
            return cacheDefaults.get().cacheKeyGenerator();
        }
        return KeyGenerator.class;
    }

    public String cacheName(Annotation a, Object target) {
        String cacheName = cacheName(a);
        if (!cacheName.equals("")) {
            return cacheName;
        }
        Optional<CacheDefaults> cacheDefaults = AnnotationType.getCacheDefaults(target.getClass());
        if (cacheDefaults.isPresent() && !cacheDefaults.get().cacheName().equals("")) {
            return cacheDefaults.get().cacheName();
        }
        return null;
    }

    public Class<? extends CacheResolverFactory> cacheResolverFactory(Annotation a, Object target) {
        Class<? extends CacheResolverFactory> cacheResolverFactory = cacheResolverFactory(a);
        if (!cacheResolverFactory.equals(CacheResolverFactory.class)) {
            return cacheResolverFactory;
        }
        Optional<CacheDefaults> cacheDefaults = AnnotationType.getCacheDefaults(target.getClass());
        if (cacheDefaults.isPresent() && !cacheDefaults.get().cacheResolverFactory().equals(CacheResolverFactory.class)) {
            return cacheDefaults.get().cacheResolverFactory();
        }
        return ResolverFactory.class;
    }

    private static Optional<CacheDefaults> getCacheDefaults(Class clz) {
        Annotation annotation = clz.getAnnotation(CacheDefaults.class);
        if (annotation instanceof CacheDefaults) {
            CacheDefaults cacheDefaults = (CacheDefaults) annotation;
            return Optional.of(cacheDefaults);
        }
        return Optional.empty();
    }
}