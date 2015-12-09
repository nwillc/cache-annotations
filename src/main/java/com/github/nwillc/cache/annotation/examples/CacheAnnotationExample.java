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

package com.github.nwillc.cache.annotation.examples;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheRemove;
import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;
import java.util.HashMap;
import java.util.Map;

@CacheDefaults(cacheName = CacheAnnotationExample.CACHE_NAME)
public class CacheAnnotationExample<K,V>  {
    public static final String CACHE_NAME = "example";
	private Map<K,V> map = new HashMap<>();

	@CachePut(cacheName = CACHE_NAME)
	public void put(@CacheKey K key, @CacheValue V value) {
		map.put(key, value);
	}

    @CacheResult(cacheName = CACHE_NAME)
    public V get(K key) {
        return map.get(key);
    }

    @CacheRemove
    public V remove(K key) {
       return map.remove(key);
    }

    @CacheRemoveAll(cacheName = CACHE_NAME)
    public void clear() {
        map.clear();
    }

	public Map<K, V> getMap() {
		return map;
	}
}
