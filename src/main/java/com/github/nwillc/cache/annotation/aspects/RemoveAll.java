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
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import javax.cache.annotation.CacheRemoveAll;
import java.lang.annotation.Annotation;

import static com.github.nwillc.cache.annotation.AnnotationType.REMOVE_ALL;

@Aspect
public class RemoveAll extends CacheAspect {
    @Around("execution(* *(..)) && @annotation(cacheRemoveAll)")
    public Object removeAll(ProceedingJoinPoint joinPoint, CacheRemoveAll cacheRemoveAll) throws Throwable {
        return around(joinPoint, cacheRemoveAll, cacheRemoveAll.afterInvocation());
    }

    @Override
    protected void cacheAction(ProceedingJoinPoint joinPoint, Annotation annotation) throws Exception {
        ContextRegistry.Context context = getContext(annotation, joinPoint, REMOVE_ALL);
        context.getCache().clear();
    }
}
