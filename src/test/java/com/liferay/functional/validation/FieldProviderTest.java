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

import com.liferay.functional.Function2;
import com.liferay.functional.Monoid;
import com.liferay.functional.fieldprovider.FieldProvider;
import com.liferay.functional.fieldprovider.FieldProvider.SafeGetter;
import com.liferay.functional.validation.Validation.Failure;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.liferay.functional.validation.Validator.predicate;

/**
 * @author Carlos Sierra Andrés
 */
public class FieldProviderTest {

    static class MapFieldProvider implements FieldProvider<FieldFail> {

        private Map<String, ?> _map;
        private Function2<String, String, FieldFail> _error;

        MapFieldProvider(
            Map<String, ?> map, Function2<String, String, FieldFail> error) {

            _map = map;
            _error = error;
        }

        @Override
        public <T> Validation<T, FieldFail> get(String name, Class<T> clazz) {
            Object value = _map.get(name);

            Function<String, FieldFail> fieldFailFunction =
                _error.curried().apply(name);

            if (value == null) {
                return new Failure<>(
                    fieldFailFunction.apply("key has no value"));
            }

            return FieldProvider.safeCast(
                clazz, fieldFailFunction).validate(value);
        }

        @Override
        public Validation<FieldProvider<FieldFail>, FieldFail> getProvider(
            String name) {

            return Validation.apply(
                MapFieldProvider::new, get(name, Map.class),
                new Validation.Success<>(_error));
        }
    }

    static class FieldFail implements Monoid<FieldFail> {
        final Map<String, Collection<Fail>> _failures;

        public FieldFail(Map<String, Collection<Fail>> failures) {
            _failures = failures;
        }

        public FieldFail(String field, Collection<Fail> fails) {
            this();

            _failures.put(field, fails);
        }

        public FieldFail() {
            _failures = new HashMap<>();
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
            this(field, Collections.singletonList(new Fail(message)));
        }

        @Override
        public FieldFail mappend(Monoid<FieldFail> other) {
            FieldFail fieldFail = new FieldFail(_failures);

            fieldFail._failures.putAll(((FieldFail)other)._failures);

            return fieldFail;
        }

        @Override
        public String toString() {
            return "FieldFail{" +
                "_failures=" + _failures +
                '}';
        }
    }

    @Test
    public void testFieldProvider() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("test", "testValueLongerThan10");

        MapFieldProvider fieldProvider = new MapFieldProvider(
            map, FieldFail::new);

        Validator<String, String, Fail> longerThan10 =
            predicate(
                s -> s.length() > 10, s -> new Fail("must be longer than 10"));

        SafeGetter<Fail, FieldFail> safeGetter = fieldProvider.getSafeGetter(
            (field, fail) ->
                new FieldFail(field, Collections.singletonList(fail)));

        Validation<String, FieldFail> validation =
            safeGetter.safeGet("test", String.class, longerThan10);

        Assert.assertEquals(
            new Validation.Success<>("testValueLongerThan10"), validation);
    }

    @Test
    public void testFieldProviderWhenNull() {
        HashMap<String, Object> map = new HashMap<>();

        MapFieldProvider fieldProvider = new MapFieldProvider(
            map, FieldFail::new);

        Validator<String, String, Fail> longerThan10 =
            predicate(
                s -> s.length() > 10, s -> new Fail("must be longer than 10"));

        SafeGetter<Fail, FieldFail> safeGetter = fieldProvider.getSafeGetter(
            (field, fail) ->
                new FieldFail(field, Collections.singletonList(fail)));

        Validation<String, FieldFail> validation =
            safeGetter.safeGet("test", String.class, longerThan10);

        Assert.assertEquals(
            new Failure<String, FieldFail>(
                new FieldFail("test", "key has no value")),
            validation);
    }

    @Test
    public void testFieldProviderWhenFailure() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("test", "testValue");

        MapFieldProvider fieldProvider = new MapFieldProvider(
            map, FieldFail::new);

        Validator<String, String, Fail> longerThan10 =
            predicate(
                s -> s.length() > 10, s -> new Fail("must be longer than 10"));

        SafeGetter<Fail, FieldFail> safeGetter = fieldProvider.getSafeGetter(
            (field, fail) ->
                new FieldFail(field, Collections.singletonList(fail)));

        Validation<String, FieldFail> validation =
            safeGetter.safeGet("test", String.class, longerThan10);

        Assert.assertEquals(
            new Failure<String, FieldFail>(
                new FieldFail("test", "must be longer than 10")),
            validation);
    }

    @Test
    public void testFieldProviderWhenNested() {
        HashMap<String, Object> map = new HashMap<>();

        HashMap<String, Object> nested = new HashMap<>();

        map.put("nested", nested);

        nested.put("test", "testValueLongerThan10");

        FieldProvider<FieldFail> fieldProvider = new MapFieldProvider(
            map, FieldFail::new);

        Validator<String, String, Fail> longerThan10 =
            predicate(
                s -> s.length() > 10, s -> new Fail("must be longer than 10"));

        Validation<String, FieldFail> validation =
            fieldProvider.getProvider("nested").flatMap(
                fp -> {
                SafeGetter<Fail, FieldFail> safeGetter =
                    fp.getSafeGetter(
                        (field, fail) ->
                            new FieldFail(
                                field, Collections.singletonList(fail)));


                return safeGetter.safeGet("test", String.class, longerThan10);
            });

        Assert.assertEquals(
            new Validation.Success<>("testValueLongerThan10"), validation);
    }

    @Test
    public void testFieldProviderWhenNestedIsNull() {
        HashMap<String, Object> map = new HashMap<>();

        FieldProvider<FieldFail> fieldProvider = new MapFieldProvider(
            map, FieldFail::new);

        Validator<String, String, Fail> longerThan10 =
            predicate(
                s -> s.length() > 10, s -> new Fail("must be longer than 10"));

        Validation<String, FieldFail> validation =
            fieldProvider.getProvider("nested").flatMap(fp -> {
                SafeGetter<Fail, FieldFail> safeGetter =
                    fp.getSafeGetter(
                        (field, fail) ->
                            new FieldFail(
                                field, Collections.singletonList(fail)));

                return safeGetter.safeGet("test", String.class, longerThan10);
            });

        Assert.assertEquals(
            new Validation.Failure<>(
                new FieldFail("nested", "key has no value")),
            validation);
    }

    @Test
    public void testFieldProviderWhenNestedhasWrongType() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("nested", "wrong");

        FieldProvider<FieldFail> fieldProvider = new MapFieldProvider(
            map, FieldFail::new);

        Validator<String, String, Fail> longerThan10 =
            predicate(
                s -> s.length() > 10, s -> new Fail("must be longer than 10"));

        Validation<String, FieldFail> validation =
            fieldProvider.getProvider("nested").flatMap(fp -> {
                SafeGetter<Fail, FieldFail> safeGetter =
                    fp.getSafeGetter(
                        (field, fail) ->
                            new FieldFail(
                                field, Collections.singletonList(fail)));

                return safeGetter.safeGet("test", String.class, longerThan10);
            });

        Assert.assertEquals(
            new Validation.Failure<>(
                new FieldFail(
                    "nested", "can't be casted to interface java.util.Map")),
            validation);
    }
}
