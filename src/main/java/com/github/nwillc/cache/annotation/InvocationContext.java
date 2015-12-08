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

import com.github.nwillc.cache.annotation.aspects.CacheAspect;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.cache.annotation.CacheInvocationContext;
import javax.cache.annotation.CacheInvocationParameter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

public class InvocationContext<A extends Annotation>
        extends MethodDetails<A> implements CacheInvocationContext<A> {
    private final Object target;
    private final CacheInvocationParameter[] allParameters;

    public InvocationContext(ProceedingJoinPoint pjp, A cacheAnnotation, CacheAspect.CacheAnnotationType cat) {
        super(pjp, cacheAnnotation, cat);
        this.target = pjp.getTarget();
        allParameters = null;
    }

    public InvocationContext(
            Set<Annotation> annotations, Method method, A cacheAnnotation, String cacheName,
            CacheInvocationParameter[] allParameters, Object target) {
        super(annotations, method, cacheAnnotation, cacheName);
        this.allParameters = allParameters;
        this.target = target;
    }

    @Override
    public Object getTarget() {
        return target;
    }

    @Override
    public CacheInvocationParameter[] getAllParameters() {
        return allParameters;
    }

    @Override
    public <T> T unwrap(Class<T> cls) {
        return null;
    }
}
