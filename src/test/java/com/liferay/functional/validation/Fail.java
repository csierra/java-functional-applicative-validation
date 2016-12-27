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

package com.liferay.functional.validation;

import com.liferay.functional.Monoid;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Carlos Sierra Andrés
 */
public class Fail implements Monoid<Fail> {

    final Set<String> _failures;

    public Fail(Set<String> failures) {
        _failures = failures;
    }

    public Fail(String... failures) {
        _failures = new HashSet<>(Arrays.asList(failures));
    }

    public Fail prependAll(String prefix) {
        return new Fail(_failures.stream().map(s -> prefix + s).collect(
            Collectors.toSet()));
    }

    public Fail(String error) {
        _failures = Collections.singleton(error);
    }

    @Override
    public Fail mappend(Monoid<Fail> other) {
        Set<String> otherFailures = ((Fail) other)._failures;

        Stream<String> stream = Stream.concat(
            _failures.stream(), otherFailures.stream());

        return new Fail(stream.collect(Collectors.toSet()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fail fail = (Fail) o;

        return _failures.equals(fail._failures);
    }

    @Override
    public int hashCode() {
        return _failures.hashCode();
    }

    @Override
    public String toString() {
        return "Fail{" +
            "_failures=" + _failures +
            '}';
    }
}
