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

package com.github.nwillc.cache.annotation.aspects;

import com.github.nwillc.cache.annotation.CacheRegistry;

import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheResolverFactory;
import java.lang.annotation.Annotation;

public class CacheAspect {
    private final CacheRegistry cacheRegistry = CacheRegistry.getInstance();

    public CacheRegistry getCacheRegistry() {
        return cacheRegistry;
    }

    public enum CacheAnnotationType {
        PUT {
            @Override
            public String cacheName(Annotation a, Object target) {
                if (a instanceof CachePut) {
                    return ((CachePut)a).cacheName();
                }
                return null;
            }

            @Override
            public Class<? extends CacheResolverFactory> cacheResolverFactory(Annotation a, Object target) {
                if (a instanceof CachePut) {
                    return ((CachePut)a).cacheResolverFactory();
                }
                return null;
            }
        };

        abstract public String cacheName(Annotation a, Object target);
        abstract public Class<? extends CacheResolverFactory> cacheResolverFactory(Annotation a, Object target);
    }
}
