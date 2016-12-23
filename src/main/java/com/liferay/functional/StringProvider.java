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

package com.liferay.functional;

import java.util.Optional;

/**
 * @author Carlos Sierra Andrés
 */
public interface StringProvider extends FieldProvider<String> {

    static <S> Validator<StringProvider, S> adapt(
        String name, Validator<Optional<String>, S> validator) {

        return (Validator<StringProvider, S>) FieldProvider.adapt(
            name, validator);
    }

    public static String met(String a, String b) {
        return a + b;
    }

}
