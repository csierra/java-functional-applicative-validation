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
import com.liferay.functional.validation.Validation;
import com.liferay.functional.validation.Validator;

import java.util.function.Function;

/**
 * @author Carlos Sierra Andrés
 */
public interface FieldProvider<F extends Monoid<F>> {

    public <T> Validation<T, F> get(String name, Class<T> clazz);

    Validation<FieldProvider<F>, F> getProvider(String name);

    static <T, F extends Monoid<F>> SafeCast<T, F> safeCast(
        Class<T> clazz, Function<String, F> error) {

        return input -> {
            try {
                return new Validation.Success<>(clazz.cast(input));
            }
            catch (ClassCastException cce) {
                return new Validation.Failure<>(
                    error.apply("can't be casted to " + clazz));
            }
        };
    }

    interface Adaptor<F2 extends Monoid<F2>, F extends Monoid<F>> {
        <T, R> Validation<R, F> safeGet(
            String fieldName, Class<T> clazz, Validator<T, R, F2> validator);
    }

    default <F2 extends Monoid<F2>> Adaptor<F2, F> getAdaptor(
        Function2<String, F2, F> map) {

        return new Adaptor<F2, F>() {
            @Override
            public <T, R> Validation<R, F> safeGet(
                String fieldName, Class<T> clazz,
                Validator<T, R, F2> validator) {

                return get(fieldName, clazz).flatMap(
                    validator.adapt(x -> x, map.curried().apply(fieldName)));
            }

        };
    }

}
