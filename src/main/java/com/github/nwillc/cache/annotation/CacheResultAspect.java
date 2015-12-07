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

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import javax.cache.Cache;
import javax.cache.Caching;
import javax.cache.annotation.CacheResult;

@Aspect
public class CacheResultAspect {
	@Around("execution(* *(..)) && @annotation(cacheResult)")
	public Object get(ProceedingJoinPoint joinPoint, CacheResult cacheResult) throws Throwable {
        Cache<Object, Object> cache = Caching.getCachingProvider().getCacheManager().getCache(cacheResult.cacheName());
        Object[] args = joinPoint.getArgs();
        Object value = cache.get(args[0]);
        if (value != null) {
           return value;
        }
        value = joinPoint.proceed();
        if (value != null) {
            cache.put(args[0], value);
        }
		return value;
	}
}
