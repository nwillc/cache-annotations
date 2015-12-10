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
import com.github.nwillc.cache.annotation.InvocationContext;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.annotation.Annotation;

class CacheAspect {
    private final ContextRegistry contextRegistry = ContextRegistry.getInstance();

    ContextRegistry.Context getContext(Annotation key, ProceedingJoinPoint pjp, AnnotationType cat) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        ContextRegistry.Context context = contextRegistry.getContext(key);
        if (context == null) {
            InvocationContext<? extends Annotation> invocationContext = new InvocationContext<>(pjp, key, cat);
            context = contextRegistry.register(key, invocationContext, cat);
        }
        return context;
    }

}
