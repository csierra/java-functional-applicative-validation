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

package com.liferay.functional.fieldprovider;

import com.liferay.functional.validation.Fail;
import com.liferay.functional.validation.Validator;

import java.util.Map;
import java.util.Optional;

/**
 * @author Carlos Sierra Andrés
 */
public class MapStringProvider implements StringProvider {

    private Map<String, String> _map;

    public MapStringProvider(Map<String, String> map) {
        _map = map;
    }

    @Override
    public Optional<String> get(String name) {
        return Optional.ofNullable(_map.get(name));
    }

    @Override
    public Validator<Object, FieldProvider, Fail> safeFieldProvider() {
        return FieldProvider.safeCast(Map.class).map(MapStringProvider::new);
    }

}
