/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

import com.liferay.functional.Function2;
import com.liferay.functional.Monoid;
import com.liferay.functional.validation.Composer;
import com.liferay.functional.validation.Fail;
import com.liferay.functional.validation.Validation;
import com.liferay.functional.validation.Validation.Failure;
import com.liferay.functional.validation.Validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static com.liferay.functional.validation.Validation.just;

/**
 * @author Carlos Sierra Andrés
 */
public interface FieldProvider<T> {

    Optional<T> get(String name);

    <S> Validator<S, FieldProvider<T>, Fail> safeFieldProvider();

    static <C> SafeCast<C, Fail> safeCast(Class<C> clazz) {
        return input -> {
            try {
                return new Validation.Success<>(clazz.cast(input));
            }
            catch (ClassCastException cce) {
                return new Failure<>(
                    new Fail("can't be casted to " + clazz));
            }
        };
    }

    public static <R> Validator<Optional<R>, R, Fail> mandatory() {
        return input -> {
            if (input.isPresent()) {
                return just(input.get());
            }
            else {
                return new Failure<>(new Fail("should not be empty"));
            }
        };
    }

    public static <T> Validator<Optional<T>, Optional<T>, Fail> optional() {
        return Validation::just;
    }

    public static <T, R> Validator<Optional<T>, Optional<R>, Fail> ifPresent(
        Validator<T, R, Fail> validator) {

        return input -> {
            if (input.isPresent()) {
                return validator.validate(input.get()).map(
                    Optional::ofNullable);
            }
            else {
                return just(Optional.empty());
            }
        };

    }

    interface Adaptor<T, F extends Monoid<F>> {
        <R> Validation<R, FieldFail> safeGet(
            String fieldName, Validator<Optional<T>, R, F> validator);

        Validation<Adaptor<T, F>, FieldFail> getAdaptor(String fieldName);

        static <T, R, F extends Monoid<F>>
        Validation<R, FieldFail> safeGet(
            Adaptor<T, F> adaptor, String fieldName,
            Validator <Optional<T>, R, F> validator) {

            return adaptor.safeGet(fieldName, validator);
        }

        static <T, R, F extends Monoid<F>>
        Validation<R, FieldFail> safeGet(
            Validation<Adaptor<T, F>, FieldFail> adaptor, String fieldName,
            Validator<Optional<T>, R, F> validator) {

            return adaptor.flatMap(a -> a.safeGet(fieldName, validator));
        }

        static <T, F extends Monoid<F>>
        Validation<Adaptor<T, F>, FieldFail> focus(
            Adaptor<T, F> adaptor, String ... fields) {

            if (fields.length == 0) {
                return just(adaptor);
            }

            Validation<Adaptor<T, F>, FieldFail> current = adaptor.getAdaptor(
                fields[0]);

            if (fields.length == 1) {
                return current;
            }

            for (int i = 1; i < fields.length; i++) {
                String field = fields[i];

                current = current.flatMap(a -> a.getAdaptor(field));

            }

            return current;
        }
    }

    default <F extends Monoid<F>> Adaptor<T, F> getAdaptor(
        Function2<String, F, FieldFail> map) {

        return getAdaptor(map, Collections.emptyList());
    }

    default <F extends Monoid<F>> Adaptor<T, F> getAdaptor(
        Function2<String, F, FieldFail> map,
        Collection<String> stack) {

        return new Adaptor<T, F>() {

            @Override
            public <R> Validation<R, FieldFail> safeGet(
                String fieldName, Validator<Optional<T>, R, F> validator) {

                return validator.validate(get(fieldName)).mapFailures(
                    map.curried().apply(fieldName)).mapFailures(f -> {

                    for (String s : stack) {
                        f = new FieldFail(s, f);
                    }

                    return f;
                });
            }

            @Override
            public Validation<Adaptor<T, F>, FieldFail> getAdaptor(
                String fieldName) {

                ArrayList<String> objects = new ArrayList<>(stack);

                objects.add(0, fieldName);

                Validator<Optional<T>, FieldProvider<T>, Fail>
                    safeFieldProvider = Composer.compose(
                        mandatory(), safeFieldProvider());

                return safeFieldProvider.
                    validate(get(fieldName)).
                    map(fp -> fp.getAdaptor(map, objects)).mapFailures(
                    f -> new FieldFail(fieldName, f));
            }

        };
    }
}
