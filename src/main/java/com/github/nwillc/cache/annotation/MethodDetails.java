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
import org.aspectj.lang.reflect.MethodSignature;

import javax.cache.annotation.CacheMethodDetails;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class MethodDetails<A extends Annotation> implements CacheMethodDetails<A> {
    private final Method method;
    private final Set<Annotation> annotations;
    private final A cacheAnnotation;
    private final String cacheName;
    private final CacheAnnotationType cacheAnnotationType;

    public MethodDetails(ProceedingJoinPoint joinPoint, A cacheAnnotation, CacheAnnotationType cat) {
        method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        this.annotations = Arrays.stream(method.getDeclaredAnnotations()).collect(Collectors.toSet());
        this.cacheAnnotation = cacheAnnotation;
        this.cacheName = cat.cacheName(cacheAnnotation, joinPoint.getTarget());
        this.cacheAnnotationType = cat;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public Set<Annotation> getAnnotations() {
        return annotations;
    }

    @Override
    public A getCacheAnnotation() {
        return cacheAnnotation;
    }

    @Override
    public String getCacheName() {
        return cacheName;
    }

    protected CacheAnnotationType getCacheAnnotationType() {
        return cacheAnnotationType;
    }
}
