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

import javax.cache.annotation.CacheInvocationParameter;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheKeyInvocationContext;
import javax.cache.annotation.CacheValue;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class KeyInvocationContext<A extends Annotation>
        extends InvocationContext<A> implements CacheKeyInvocationContext<A> {
    private final CacheInvocationParameter[] keyParameters;
    private final CacheInvocationParameter valueParameter;

    public KeyInvocationContext(ProceedingJoinPoint pjp, A cacheAnnotation, AnnotationType cat) throws ClassNotFoundException {
        super(pjp, cacheAnnotation, cat);
        this.keyParameters = findKeyParameters();
        this.valueParameter = findValueParameter();
    }

    @Override
    public CacheInvocationParameter[] getKeyParameters() {
        return keyParameters;
    }

    @Override
    public CacheInvocationParameter getValueParameter() {
        return valueParameter;
    }

    private CacheInvocationParameter[] findKeyParameters() {
        List<CacheInvocationParameter> noAnnotations = new ArrayList<>();
        List<CacheInvocationParameter> keyAnnotations = new ArrayList<>();
        List<CacheInvocationParameter> valAnnotations = new ArrayList<>();

        for (CacheInvocationParameter parameter : getAllParameters()) {
            boolean found = false;
            for (Annotation annotation : parameter.getAnnotations()) {
                if (annotation instanceof CacheKey) {
                    keyAnnotations.add(parameter);
                    found = true;
                    break;
                }
                if (annotation instanceof CacheValue) {
                    valAnnotations.add(parameter);
                    found = true;
                    break;
                }
            }
            if (!found) {
                noAnnotations.add(parameter);
            }
        }

        if (keyAnnotations.size() > 0) {
            CacheInvocationParameter[] result = new CacheInvocationParameter[keyAnnotations.size()];
            return keyAnnotations.toArray(result);
        }

        if (valAnnotations.size() > 0) {
            CacheInvocationParameter[] result = new CacheInvocationParameter[noAnnotations.size()];
            return noAnnotations.toArray(result);
        }

        return getAllParameters();
    }

    private CacheInvocationParameter findValueParameter() {
        if (getAnnotationType() != AnnotationType.PUT) {
            return null;
        }

        for (CacheInvocationParameter parameter : getAllParameters()) {
            for (Annotation annotation : parameter.getAnnotations()) {
                if (annotation instanceof CacheValue) {
                    return parameter;
                }
            }
        }

        return null;
    }
}
