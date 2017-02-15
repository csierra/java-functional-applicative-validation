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

import java.util.Optional;

import static com.liferay.functional.validation.Validation.just;
import static com.liferay.functional.validation.Validator.verify;

/**
 * @author Alejandro Hernández
 */
public class ValidatorUtil {
	public static final Validator<String, String, Fail> NOT_EMPTY =
		verify(s -> s.length() > 0, "must not be empty");

	public static Validator<Integer, Integer, Fail> GREATER_THAN(int number) {
		return verify(n -> n > number, "must be greater than " + number);
	}

	public static <T> Validator<Optional<T>, T, Fail> MANDATORY() {
		return input -> {
			if (input.isPresent()) {
				return just(input.get());
			}
			else {
				return new Validation.Failure<>(new Fail("should not be empty"));
			}
		};
	}
}
