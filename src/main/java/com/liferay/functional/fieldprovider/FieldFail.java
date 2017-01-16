/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.functional.fieldprovider;

import com.liferay.functional.Monoid;
import com.liferay.functional.validation.Fail;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Carlos Sierra Andrés
 */
public class FieldFail implements Monoid<FieldFail> {

    final Map<String, Monoid> _failures;

    public FieldFail(String field, Fail fail) {
        this();

        _failures.put(field, fail);
    }

    public FieldFail() {
        _failures = new HashMap<>();
    }

    public FieldFail(String fieldName, FieldFail nested) {
        this();

        _failures.put(fieldName, nested);
    }

    FieldFail(Map<String, Monoid> failures) {
        _failures = failures;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldFail fieldFail = (FieldFail) o;

        return _failures.equals(fieldFail._failures);

    }

    @Override
    public int hashCode() {
        return _failures.hashCode();
    }

    public FieldFail(String field, String message) {
        this(field, new Fail(message));
    }

    @Override
    public FieldFail mappend(Monoid<FieldFail> other) {
        Map<String, Monoid> collect = Stream.of(
            ((FieldFail) other)._failures, _failures).
            map(Map::entrySet).
            flatMap(Collection::stream).collect(Collectors.toMap(
            Map.Entry::getKey, Map.Entry::getValue, Monoid::mappend
        ));

        return new FieldFail(collect);
    }

    @Override
    public String toString() {
        return "FieldFail{" +
            "_failures=" + _failures +
            '}';
    }

    public static FieldFail fromFail(String fieldName, Fail fail) {
        return new FieldFail(fieldName, fail);
    }
}
