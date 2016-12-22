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

import com.liferay.functional.Validation.Failure;
import com.liferay.functional.Validation.Success;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Carlos Sierra Andrés
 */
public interface Validator<T, R> {

	Validation<R> validate(T input);

	default Validator<T, R> and(Validator<T, R> other) {
		return input -> (Validation<R>)validate(input).flatMap(
			o -> other.validate(input));
	}

	default <S> Validator<T, S> map(Function<R, S> fun) {

		return input -> {
			Validation<R> result = validate(input);

			return (Validation<S>)result.map(fun);
		};
	}

	default <S> Validator<T, S> compose(Validator<R, S> validator) {
		return input -> (Validation<S>) validate(input).flatMap(
			validator::validate);
	}

	static <T> Validator<T, T> predicate(
		Predicate<T> predicate, Function<T, String> error) {

		return input -> {
			if (predicate.test(input)) {
				return new Success<>(input);
			}
			else {
				return new Failure<>(
					Collections.singleton(error.apply(input)));
			}
		};
	}

	static Validator<String, String> hasLength(int length) {
		return predicate(
			input -> input.length() == length,
			input -> input + " must have " + length + " letters");
	}

	static Validator<String, String> longerThan(int length) {
		return predicate(
			input -> input.length() > length,
			input -> input + " must have " + length + " letters");
	}

	Validator<String, String> isANumber = predicate(
		input -> {
			try {
				Integer.parseInt(input);

				return true;
			}
			catch (Exception e) {
				return false;
			}
		}, input -> input + " is not a number");

	static Validator<Integer, Integer> greaterThan(int min) {
		return predicate(
			input -> input > min,
			input -> input + " must be greater than" + min
		);
	}

	static Validator<Integer, Integer> lowerThan(int max) {
		return predicate(
			input -> input < max,
			input -> input + " should be lower than " + max
		);
	}

	static Validator<String, String> startsWith(String prefix) {
		return predicate(
			input -> input.startsWith(prefix),
			input -> input + " should start with " + prefix
		);
	}

	static Validator<String, String> endsWith(String suffix) {
		return predicate(
			input -> input.endsWith(suffix),
			input -> input + " should start with " + suffix
		);
	}

	static <T> Validator<Optional<T>, Optional<T>> ifPresent(
		Validator<T, T> validator) {

		return (Optional<T> input) -> {
			if (!input.isPresent()) {
				return new Success<>(input);
			}
			else {
				return (Validation<Optional<T>>)
					validator.validate(input.get()).map(Optional::of);
			}
		};
	}

	Validator<String, Integer> safeInt =
		isANumber.map(Integer::parseInt);

	static <T> Validator<Optional<T>, T> notEmpty() {
		return input -> {
			if (input.isPresent()) {
				return new Success<>(input.get());
			}
			else {
				return new Failure<>(
					Collections.singleton("Input is empty"));
			}
		};
	}

	static class User {

		String name;
		Optional<String> middle;
		Date date;

		public User(String name, Optional<String> middle, Date date) {
			this.date = date;
			this.middle = middle;
			this.name = name;
		}

		public Date getDate() {
			return date;
		}

		public Optional<String> getMiddle() {
			return middle;
		}

		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return "User{" +
				"date=" + date +
				", name='" + name + '\'' +
				", middle=" + middle +
				'}';
		}
	}

}
