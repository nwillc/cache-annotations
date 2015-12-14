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

import com.github.nwillc.cache.annotation.AnnotationType;
import com.github.nwillc.cache.annotation.ContextRegistry;
import com.github.nwillc.cache.annotation.KeyInvocationContext;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.cache.annotation.CacheKeyInvocationContext;
import javax.cache.annotation.GeneratedCacheKey;
import java.lang.annotation.Annotation;

import static com.github.nwillc.cache.annotation.InvocationParameter.updateValues;

class CacheAspect {
    private final ContextRegistry contextRegistry = ContextRegistry.getInstance();

    protected Object around(ProceedingJoinPoint pjp, Annotation annotation, boolean afterInvocation) throws Throwable {
        if (afterInvocation) {
            Object result = pjp.proceed();
            cacheAction(pjp, annotation);
            return result;
        } else {
            cacheAction(pjp, annotation);
            return pjp.proceed();
        }
    }

    protected void cacheAction(ProceedingJoinPoint joinPoint, Annotation annotation) throws Exception {
    }

    protected GeneratedCacheKey generateKey(ProceedingJoinPoint pjp, ContextRegistry.Context context) {
        synchronized (context.getKeyInvocationContext()) {
            updateValues(pjp, context.getKeyInvocationContext().getAllParameters());
            return context.getKeyGenerator().generateCacheKey(context.getKeyInvocationContext());
        }
    }

    ContextRegistry.Context getContext(Annotation annotation, ProceedingJoinPoint pjp, AnnotationType cat) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        ContextRegistry.Context context = contextRegistry.getContext(annotation, pjp.getTarget());
        if (context == null) {
            CacheKeyInvocationContext<? extends Annotation> invocationContext = new KeyInvocationContext<>(pjp, annotation, cat);
            context = contextRegistry.register(annotation, invocationContext, cat);
        }
        return context;
    }

}
