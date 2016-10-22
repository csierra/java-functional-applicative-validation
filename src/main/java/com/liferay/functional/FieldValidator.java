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

package com.liferay.functional;

import javaslang.Function1;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Carlos Sierra Andrés
 */
public interface FieldValidator<T, R>
	extends Validator<FieldValidator.Field<T>, R> {

	class Field<T> implements Functor<T> {

		public Field(String name, T t) {
			this.name = name;
			this.t = t;
		}

		final public String name;

		final public T t;

		@Override
		public <S> Field<S> fmap(Function1<T, S> function) {
			return new Field<>(name, function.apply(t));
		}

		@Override
		public String toString() {
			return "Field[" + name + ']';
		}

	}

	static FieldValidator<Integer, Integer> between(int min, int max) {
		return greaterThan(min).and(lowerThan(max));
	}

	default FieldValidator<T, R> and(FieldValidator<T, R> other) {
		return (Field<T> field) ->
			(Validation<R>)validate(field).
				flatMap(o -> other.validate(field));
	}

	default <S> FieldValidator<T, S> compose(FieldValidator<R, S> validator) {
		return field -> (Validation<S>)
			validate(field).
				flatMap(i -> validator.validate(new Field<>(i.toString(), i)));
	}

	default <S> FieldValidator<T, S> fmap(Function1<R, S> fun) {

		return field -> {
			Validation<R> result = validate(field);

			return (Validation<S>)result.fmap(fun);
		};
	}

	static <T> FieldValidator<T, T> predicate(
		Predicate<T> predicate, Function<String, String> error) {

		return field -> {
			if (predicate.test(field.t)) {
				return new Validation.Success<>(field.t);
			}
			else {
				return new Validation.Failure<>(
					Collections.singleton(error.apply(field.name)));
			}
		};
	}

	static FieldValidator<String, String> hasLength(int length) {
		return predicate(
			input -> input.length() == length,
			fieldName -> fieldName + " must have " + length + " letters");
	}

	static FieldValidator<String, String> longerThan(int length) {
		return predicate(
			input -> input.length() > length,
			fieldName -> fieldName + " must have " + length + " letters");
	}

	FieldValidator<String, String> isANumber = predicate(
		input -> {
			try {
				Integer.parseInt(input);

				return true;
			}
			catch (Exception e) {
				return false;
			}
		}, fieldName -> fieldName + " is not a number");

	static FieldValidator<Integer, Integer> greaterThan(int min) {
		return predicate(
			input -> input > min,
			fieldName -> fieldName + " must be greater than" + min
		);
	}

	static FieldValidator<Integer, Integer> lowerThan(int max) {
		return predicate(
			input -> input < max,
			fieldName -> fieldName + " should be lower than " + max
		);
	}

	static FieldValidator<String, String> startsWith(String prefix) {
		return predicate(
			input -> input.startsWith(prefix),
			fieldName -> fieldName + " should start with " + prefix
		);
	}

	static FieldValidator<String, String> endsWith (String suffix) {
		return predicate(
			input -> input.endsWith(suffix),
			fieldName -> fieldName + " should start with " + suffix
		);
	}

	static <T> FieldValidator<Optional<T>, Optional<T>> ifPresent(
		FieldValidator<T, T> validator) {

		return field -> {
			if (!field.t.isPresent()) {
				return new Validation.Success<>(field.t);
			}
			else {
				return (Validation<Optional<T>>)
					validator.validate(field.fmap(Optional::get)).
					fmap(Optional::of);
			}
		};
	}

	FieldValidator<String, Integer> safeInt = isANumber.fmap(Integer::parseInt);

	static <T> FieldValidator<Optional<T>, T> notEmpty() {
		return field -> {
			if (field.t.isPresent()) {
				return new Validation.Success<>(field.t.get());
			}
			else {
				return new Validation.Failure<>(
					Collections.singleton(field.name + " is empty"));
			}
		};
	}

}
