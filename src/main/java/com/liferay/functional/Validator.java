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

import com.liferay.functional.ValidationResult.Failure;
import com.liferay.functional.ValidationResult.Success;
import javaslang.Function1;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Carlos Sierra Andrés
 */
public interface Validator<T, R> extends Functor<R>{

	ValidationResult<R> validate(T input);

	default Validator<T, R> and(Validator<T, R> other) {
		return input -> (ValidationResult<R>)validate(input).flatMap(
			o -> other.validate(input));
	}

	default <S> Validator<T, S> fmap(Function1<R, S> fun) {

		return input -> {
			ValidationResult<R> result = validate(input);

			return (ValidationResult<S>)result.fmap(fun);
		};
	}

	default <S> Validator<T, S> compose(Validator<R, S> validator) {
		return input -> (ValidationResult<S>) validate(input).flatMap(
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
					Collections.singletonList(error.apply(input)));
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

	static Validator<String, String> endsWith (String suffix) {
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
				return (ValidationResult<Optional<T>>)
					validator.validate(input.get()).fmap(Optional::of);
			}
		};
	}

	Validator<String, Integer> safeInt =
		isANumber.fmap(Integer::parseInt);

	static <T> Validator<Optional<T>, T> notEmpty() {
		return input -> {
			if (input.isPresent()) {
				return new Success<>(input.get());
			}
			else {
				return new Failure<>(
					Collections.singletonList("Input is empty"));
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

	static void main(String[] args) {
//		Validator<String, Integer> mayorDeEdad = safeInt.compose(
//			greaterThan(18).and(lowerThan(90)));
//
//		Validator<Optional<String>, String> dni =
//			isThere(String.class).compose(hasLength(9).and(endsWith("D")));
//
//		ValidationResult<String> safeDni = dni.validate(
//			Optional.of("50211539D"));
//
//		ValidationResult<MyClass> result =
//			(ValidationResult<MyClass>)Applicative.lift(
//				MyClass::new, mayorDeEdad.validate("15"), safeDni
//			);
//
//		System.out.println(result);

		Validator<String, Integer> safeInt = isANumber.fmap(Integer::parseInt);

		Validator<Optional<String>, Integer> isThereAnInteger =
			Validator.<String>notEmpty().compose(safeInt);

		Validator<Optional<String>, Integer> safeDay =
			isThereAnInteger.compose(
					lowerThan(32).and(greaterThan(0)));

		Validator<Optional<String>, Integer> safeMonth =
			isThereAnInteger.compose(
					lowerThan(13).and(greaterThan(0))).fmap(x -> x - 1);

		Validator<Optional<String>, Integer> safeYear =
			isThereAnInteger.compose(
					lowerThan(10000).and(greaterThan(1900))).fmap(x -> x - 1900);

		System.out.println(
			Applicative.lift(
				GregorianCalendar::new,
				safeYear.validate(Optional.of("pepe")),
				safeMonth.validate(Optional.of("8")),
				safeDay.validate(Optional.empty())).
			fmap(GregorianCalendar::getTime));

		ValidationResult<User> carlos = (ValidationResult<User>)
			Applicative.lift(
				User::new,
				new Success<>("carlos"),
				ifPresent(
					longerThan(2)).validate(Optional.of("jr.")),
				Applicative.lift(
					GregorianCalendar::new,
					safeYear.validate(Optional.of("1979")),
					safeMonth.validate(Optional.of("8")),
					safeDay.validate(Optional.of("11"))).
					fmap(GregorianCalendar::getTime));

		if (carlos.isPresent()) {
			System.out.println("YAY: " + carlos.get().toString());
		}
		else {
			System.out.println("BOOOH:" + Arrays.asList(carlos.failures()));
		}

	}

}
