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
import com.liferay.functional.validation.Fail;
import com.liferay.functional.validation.Validation;
import com.liferay.functional.validation.Validation.Failure;
import com.liferay.functional.validation.Validator;

import java.util.Collections;
import java.util.Optional;

import static com.liferay.functional.validation.Validation.just;

/**
 * @author Carlos Sierra Andrés
 */
public interface FieldProvider {

    <T> Optional<T> get(String name);

    Validator<Object, FieldProvider, Fail> safeFieldProvider();

    static <T> SafeCast<T, Fail> safeCast(Class<T> clazz) {
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

    public static <T> Validator<Optional<T>, T, Fail> mandatory() {
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

    interface Adaptor<F extends Monoid<F>> {
        <T, R> Validation<R, FieldFail> safeGet(
            String fieldName, Validator<Optional<T>, R, F> validator);

        Validation<Adaptor<F>, FieldFail> getAdaptor(String fieldName);

        static <T, R, F extends Monoid<F>>
        Validation<R, FieldFail> safeGet(
            Adaptor<F> adaptor, String fieldName,
            Validator <Optional<T>, R, F> validator) {

            return adaptor.safeGet(fieldName, validator);
        }

        static <T, R, F extends Monoid<F>>
        Validation<R, FieldFail> safeGet(
            Validation<Adaptor<F>, FieldFail> adaptor, String fieldName,
            Validator<Optional<T>, R, F> validator) {

            return adaptor.flatMap(a -> a.safeGet(fieldName, validator));
        }

        static <F extends Monoid<F>>
        Validation<Adaptor<F>, FieldFail> focus(
            Adaptor<F> adaptor, String ... fields) {

            if (fields.length == 0) {
                return new Validation.Success<>(adaptor);
            }

            Validation<Adaptor<F>, FieldFail> current = adaptor.getAdaptor(
                fields[0]);

            if (fields.length == 1) {
                return current;
            }

            for (int i = 1; i < fields.length; i++) {
                String field = fields[i];

                current = current.flatMap(fp -> fp.getAdaptor(field));
            }

            return current;
        }
    }

    default <F extends Monoid<F>> Adaptor<F> getAdaptor(
        Function2<String, F, FieldFail> map) {

        return new Adaptor<F>() {

            @Override
            public <T, R> Validation<R, FieldFail> safeGet(
                String fieldName, Validator<Optional<T>, R, F> validator) {

                return validator.validate(get(fieldName)).mapFailures(
                    map.curried().apply(fieldName));
            }

            @Override
            public Validation<Adaptor<F>, FieldFail> getAdaptor(
                String fieldName) {

                return mandatory().
                    compose(safeFieldProvider()).
                    validate(get(fieldName)).
                    mapFailures(
                        f -> new FieldFail("nested", Collections.singleton(f))).
                    map(fp -> fp.getAdaptor(map));
            }
        };
    }

}
