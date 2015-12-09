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
import com.github.nwillc.cache.annotation.InvocationContext;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.cache.Cache;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheRemove;
import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResolverFactory;
import javax.cache.annotation.CacheResult;
import java.lang.annotation.Annotation;

import static com.github.nwillc.cache.annotation.aspects.CacheAspect.CacheAnnotationType.PUT;

public class CacheAspect {
	private final CacheRegistry cacheRegistry = CacheRegistry.getInstance();

	protected Cache<Object, Object> getCache(Annotation key, ProceedingJoinPoint pjp, CacheAnnotationType cat) throws IllegalAccessException, InstantiationException {
		Cache<Object, Object> cache = cacheRegistry.get(key);
		if (cache == null) {
			InvocationContext invocationContext = new InvocationContext<>(pjp, key, cat);
			cache = cacheRegistry.register(key, invocationContext, cat);
		}
		return cache;
	}

	public enum CacheAnnotationType {
		PUT {
			@Override
			public String cacheName(Annotation a, Object target) {
				return ((CachePut) a).cacheName();
			}

			@Override
			public Class<? extends CacheResolverFactory> cacheResolverFactory(Annotation a, Object target) {
				return ((CachePut) a).cacheResolverFactory();
			}
		},
		REMOVE {
			@Override
			public String cacheName(Annotation a, Object target) {
				return ((CacheRemove)a).cacheName();
			}

			@Override
			public Class<? extends CacheResolverFactory> cacheResolverFactory(Annotation a, Object target) {
				return ((CacheRemove)a).cacheResolverFactory();
			}
		},
		REMOVE_ALL {
			@Override
			public String cacheName(Annotation a, Object target) {
				return ((CacheRemoveAll)a).cacheName();
			}

			@Override
			public Class<? extends CacheResolverFactory> cacheResolverFactory(Annotation a, Object target) {
				return ((CacheRemoveAll)a).cacheResolverFactory();
			}
		},
		RESULT {
			@Override
			public String cacheName(Annotation a, Object target) {
				return ((CacheResult)a).cacheName();
			}

			@Override
			public Class<? extends CacheResolverFactory> cacheResolverFactory(Annotation a, Object target) {
				return ((CacheResult)a).cacheResolverFactory();
			}
		};

		abstract public String cacheName(Annotation a, Object target);
		abstract public Class<? extends CacheResolverFactory> cacheResolverFactory(Annotation a, Object target);
	}
}
