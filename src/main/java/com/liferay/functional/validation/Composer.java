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

/**
 * @author Carlos Sierra Andrés
 */
public interface Composer {

    static <A, B, C, FAIL extends Monoid<FAIL>> Validator<A, C, FAIL> compose(
        Validator<A, B, FAIL> a, Validator<B, C, FAIL> b) {

        return a.compose(b);
    }

    static <A, B, C, D, FAIL extends Monoid<FAIL>> Validator<A, D, FAIL> compose(
        Validator<A, B, FAIL> a, Validator<B, C, FAIL> b, Validator<C, D, FAIL> c) {

        return a.compose(b).compose(c);
    }

    static <A, B, C, D, E, FAIL extends Monoid<FAIL>> Validator<A, E, FAIL> compose(
        Validator<A, B, FAIL> a, Validator<B, C, FAIL> b, Validator<C, D, FAIL> c, Validator<D, E, FAIL> d) {

        return a.compose(b).compose(c).compose(d);
    }

    static <A, B, C, D, E, F, FAIL extends Monoid<FAIL>> Validator<A, F, FAIL> compose(
        Validator<A, B, FAIL> a, Validator<B, C, FAIL> b, Validator<C, D, FAIL> c, Validator<D, E, FAIL> d, Validator<E, F, FAIL> e) {

        return a.compose(b).compose(c).compose(d).compose(e);
    }

}
