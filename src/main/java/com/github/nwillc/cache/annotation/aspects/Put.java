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

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import javax.cache.Cache;
import javax.cache.annotation.CachePut;

import static com.github.nwillc.cache.annotation.CacheAnnotationType.PUT;

@Aspect
public class Put extends CacheAspect {

    @Around("execution(* *(..)) && @annotation(cachePut)")
    public Object put(ProceedingJoinPoint joinPoint, CachePut cachePut) throws Throwable {
        Object result = joinPoint.proceed();
        Cache<Object, Object> cache = getCache(cachePut, joinPoint, PUT);
        Object[] args = joinPoint.getArgs();
        cache.put(args[0], args[1]);
        return result;
    }

}
