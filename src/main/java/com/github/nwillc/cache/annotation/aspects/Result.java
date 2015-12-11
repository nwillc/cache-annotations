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

import com.github.nwillc.cache.annotation.ContextRegistry;
import com.github.nwillc.cache.annotation.KeyInvocationContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import javax.cache.annotation.CacheResult;
import javax.cache.annotation.GeneratedCacheKey;

import static com.github.nwillc.cache.annotation.AnnotationType.RESULT;

@Aspect
public class Result extends CacheAspect {
    @Around("execution(* *(..)) && @annotation(cacheResult)")
    public Object result(ProceedingJoinPoint joinPoint, CacheResult cacheResult) throws Throwable {
        ContextRegistry.Context context = getContext(cacheResult, joinPoint, RESULT);
        KeyInvocationContext<CacheResult> keyInvocationContext = new KeyInvocationContext<>(joinPoint, cacheResult, RESULT);
        GeneratedCacheKey key = context.getKeyGenerator().generateCacheKey(keyInvocationContext);
        Object value = context.getCache().get(key);
        if (value != null) {
            return value;
        }
        value = joinPoint.proceed();
        if (value != null) {
            context.getCache().put(key, value);
        }
        return value;
    }
}
