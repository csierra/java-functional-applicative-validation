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
public interface FieldProvider<T> {

    Optional<T> get(String name);

    static <T, S> Validator<? extends FieldProvider<T>, S> adapt(
        String name, Validator<Optional<T>, S> validator) {

        return validator.adapt(
            fp -> fp.get(name),
            f -> "While validating field '" + name + "':" + f);
    }
}
