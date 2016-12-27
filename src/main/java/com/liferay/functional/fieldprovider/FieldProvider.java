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

import com.liferay.functional.Monoid;
import com.liferay.functional.validation.Validator;

import java.util.Optional;
import java.util.function.BiFunction;

/**
 * @author Carlos Sierra Andrés
 */
public interface FieldProvider<T> {

    Optional<T> get(String name);

    static <T, S, F extends Monoid<F>, F2 extends Monoid<F2>>
    Validator<? extends FieldProvider<T>, S, F2> adapt(
        String name, Validator<Optional<T>, S, F> validator,
        BiFunction<String, F, F2> errors) {

        return validator.adapt(
            fp -> fp.get(name), f -> errors.apply(name, f));
    }

}
