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

import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.liferay.functional.FieldValidator.*;

/**
 * @author Carlos Sierra Andrés
 */
public class MapValidator<T>
	implements Functor<T>, Applicative<MapValidator<?>, T>,
	Monad<MapValidator<?>, T> {

	private Function1<Map<String, String>, Validation<T>> _f;

	MapValidator(Function1<Map<String, String>, Validation<T>> f) {
		_f = f;
	}

	@Override
	public <S> MapValidator<S> fmap(Function1<T, S> g) {
		return new MapValidator<>(
			h -> (Validation<S>) _f.apply(h).fmap(g));
	}

	@Override
	public <S, U> MapValidator<U> apply(
		Applicative<MapValidator<?>, S> ap) {

		return new MapValidator<>(
			h -> (Validation<U>) _f.apply(h).<S, U>apply(((MapValidator<S>) ap)._f.apply(h)));
	}

	@Override
	public <S> MapValidator<S> pure(S s) {
		return new MapValidator<>(h -> new Validation.Success<>(s));
	}

	@Override
	public <S> MapValidator<S> flatMap(
		Function1<T, Monad<MapValidator<?>, S>> fun) {

		return new MapValidator<>(
			h -> (Validation<S>) _f.apply(h).flatMap(fun.andThen((Function<Monad<MapValidator<?>, S>, Validation<S>>) mv -> ((MapValidator<S>)mv)._f.apply(h))));
	}

	public static <T> MapValidator<T> mv(
		String fieldName, FieldValidator<Optional<String>, T> fv) {

		return new MapValidator<>(
			h -> fv.validate(fieldName, Optional.ofNullable(h.get(fieldName))));
	}

	public Validation<T> runValidation(Map<String, String> input) {
		return _f.apply(input);
	}

	public static void main(String[] args) {
		FieldValidator<Optional<String>, Integer> isThereANumber =
			FieldValidator.<String>notEmpty().compose(safeInt);

		MapValidator<Integer> month = mv(
			"month", isThereANumber.compose(between(0, 13)));

		MapValidator<Integer> year =
			mv("year", isThereANumber.compose(between(1900, 10000)));

		MapValidator<Integer> day = month.flatMap(
			m -> {
				if (Arrays.asList(1, 3, 5, 7, 8, 10, 12).contains(m)) {
					return mv("day", isThereANumber.compose(between(0, 32)));
				}
				else if (Arrays.asList(4, 6, 9, 11).contains(m)) {
					return mv("day", isThereANumber.compose(between(0, 31)));
				}
				else {
					return mv("day", isThereANumber.compose(between(0, 28)));
				}
		});


		MapValidator<Date> calendarMapValidator =
			(MapValidator<Date>)Applicative.lift(
				GregorianCalendar::new,
					year.fmap(x -> x - 1900),
					month.fmap(x -> x - 1),
					day).
				fmap(GregorianCalendar::getTime);

		System.out.println(
			calendarMapValidator.runValidation(
				new HashMap<String, String>() {{
					put("day", "11");
					put("month", "08");
					put("year", "1979");
				}}));
	}
}
