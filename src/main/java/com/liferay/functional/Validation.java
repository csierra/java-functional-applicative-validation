package com.liferay.functional; /**
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

import com.liferay.functional.OptionalInstance.MyClass;
import com.liferay.functional.ValidationResult.Failure;
import com.liferay.functional.ValidationResult.Success;
import javaslang.Function1;

import java.util.Collections;


/**
 * @author Carlos Sierra Andrés
 */
public interface Validation<T, R> {

	public ValidationResult<R> validate(T input);

	public default Validation<T, R> and(Validation<T, R> other) {
		return input -> (ValidationResult<R>)validate(input).flatMap(
			o -> other.validate(input));
	}

	public default <S> Validation<T, S> flatMap(
		Function1<T, Validation<R, S>> fun) {

		return input -> {
			ValidationResult<R> result = validate(input);
			Validation<R, S> validation = fun.apply(input);

			return (ValidationResult<S>)result.flatMap(validation::validate);
		};
	}

	public default <S> Validation<T, S> compose(Validation<R, S> validation) {
		return input -> (ValidationResult<S>) validate(input).flatMap(
			validation::validate);
	}

	public static Validation<String, String> nueveletras = input -> {
		if (input.length() == 9) {
			return new Success<>(input);
		}
		else {
			return new Failure<>(
				Collections.singletonList("DNI must have 9 characters"));
		}
	};

	public static Validation<String, String> isANumber = input -> {
		try {
			Integer.parseInt(input);

			return new Success<>(input);
		}
		catch (Exception e) {
			return new Failure<>(Collections.singletonList(
				input + " is not a number"));
		}
	};

	public static Validation<Integer, Integer> greaterThan(int i) {
		return input -> {
			if (input > i) {
				return new Success<>(i);
			}
			else {
				return new Failure<>(Collections.singletonList(
					input +" must be greater than " + i));
			}
		};
	}

	public static Validation<String, String> startsWith(String prefix) {
		return (input) -> {
			if (input.startsWith(prefix)) {
				return new Success<>(prefix);
			}
			else return new Failure<>(
				Collections.singletonList(
					input + " does not start with " + prefix));
		};
	}

	public static Validation<String, String> endsWith(String suffix) {
		return (input) -> {
			if (input.endsWith(suffix)) {
				return new Success<>(input);
			}
			else return new Failure<>(
				Collections.singletonList(
					input + " does not start with " + suffix));
		};
	}

	public static Validation<String, Integer> safeInt =
		isANumber.flatMap(n -> o -> new Success<>(Integer.parseInt(n)));

	public static void main(String[] args) {

		Validation<String, Integer> mayorDeEdad = safeInt.compose(
			greaterThan(18));

		Validation<String, String> dni = nueveletras.and(endsWith("D"));

		ValidationResult<String> safeDni = dni.validate("50111539D");

		ApplicativeInstance<ValidationResult<?>> applicativeInstance =
			new ApplicativeInstance<ValidationResult<?>>() {};

		Applicative<ValidationResult<?>, MyClass> result =
			applicativeInstance.lift(
				MyClass::new,
					mayorDeEdad.validate("19"),
					safeDni);

		System.out.println(result);

	}
}
