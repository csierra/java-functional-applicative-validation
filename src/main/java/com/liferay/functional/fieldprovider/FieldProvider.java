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

    interface SafeGetter<F extends Monoid<F>, F2 extends Monoid<F2>> {
        <T, R> Validation<R, F2> safeGet(
            String fieldName, Class<T> clazz, Validator<T, R, F> validator);
    }

    default <F2 extends Monoid<F2>> SafeGetter<F, F2> getSafeGetter(
        Function2<String, F, F2> map) {

        return new SafeGetter<F, F2>() {
            @Override
            public <T, R> Validation<R, F2> safeGet(
                String fieldName, Class<T> clazz,
                Validator<T, R, F> validator) {

                return get(fieldName, clazz).flatMap(validator).
                    mapFailures(map.curried().apply(fieldName));
            }
        };
    }

    default SafeGetter<F, F> getSafeGetter() {

        return new SafeGetter<F, F>() {
            @Override
            public <T, R> Validation<R, F> safeGet(
                String fieldName, Class<T> clazz,
                Validator<T, R, F> validator) {

                return get(fieldName, clazz).flatMap(validator);
            }
        };
    }

}
