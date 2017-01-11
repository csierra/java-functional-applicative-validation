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
import com.liferay.functional.fieldprovider.FieldFail;
import com.liferay.functional.fieldprovider.FieldProvider;
import com.liferay.functional.fieldprovider.FieldProvider.Adaptor;
import com.liferay.functional.validation.Validation.Failure;
import org.junit.Assert;
import org.junit.Test;

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

    @Test
    public void testFieldProvider() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("test", "testValueLongerThan10");

        MapFieldProvider fieldProvider = new MapFieldProvider(
            map, FieldFail::new);

        Validator<String, String, Fail> longerThan10 =
            predicate(
                s -> s.length() > 10, s -> new Fail("must be longer than 10"));

        Adaptor<Fail, FieldFail> adaptor = fieldProvider.getAdaptor(
            (field, fail) ->
                new FieldFail(field, Collections.singletonList(fail)));

        Validation<String, FieldFail> validation =
            adaptor.safeGet("test", String.class, longerThan10);

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

        Adaptor<Fail, FieldFail> adaptor = fieldProvider.getAdaptor(
            (field, fail) ->
                new FieldFail(field, Collections.singletonList(fail)));

        Validation<String, FieldFail> validation =
            adaptor.safeGet("test", String.class, longerThan10);

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

        Adaptor<Fail, FieldFail> adaptor = fieldProvider.getAdaptor(
            (field, fail) ->
                new FieldFail(field, Collections.singletonList(fail)));

        Validation<String, FieldFail> validation =
            adaptor.safeGet("test", String.class, longerThan10);

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
                Adaptor<Fail, FieldFail> adaptor =
                    fp.getAdaptor(
                        (field, fail) ->
                            new FieldFail(
                                field, Collections.singletonList(fail)));


                return adaptor.safeGet("test", String.class, longerThan10);
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
                Adaptor<Fail, FieldFail> adaptor =
                    fp.getAdaptor(
                        (field, fail) ->
                            new FieldFail(
                                field, Collections.singletonList(fail)));

                return adaptor.safeGet("test", String.class, longerThan10);
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
                Adaptor<Fail, FieldFail> adaptor =
                    fp.getAdaptor(
                        (field, fail) ->
                            new FieldFail(
                                field, Collections.singletonList(fail)));

                return adaptor.safeGet("test", String.class, longerThan10);
            });

        Assert.assertEquals(
            new Validation.Failure<>(
                new FieldFail(
                    "nested", "can't be casted to interface java.util.Map")),
            validation);
    }
}
