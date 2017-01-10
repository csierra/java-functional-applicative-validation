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

package com.liferay.functional.validation; 

import com.liferay.functional.Applicative;
import com.liferay.functional.Function10;
import com.liferay.functional.Function11;
import com.liferay.functional.Function12;
import com.liferay.functional.Function13;
import com.liferay.functional.Function14;
import com.liferay.functional.Function15;
import com.liferay.functional.Function16;
import com.liferay.functional.Function17;
import com.liferay.functional.Function18;
import com.liferay.functional.Function19;
import com.liferay.functional.Function2;
import com.liferay.functional.Function20;
import com.liferay.functional.Function21;
import com.liferay.functional.Function22;
import com.liferay.functional.Function23;
import com.liferay.functional.Function24;
import com.liferay.functional.Function25;
import com.liferay.functional.Function26;
import com.liferay.functional.Function3;
import com.liferay.functional.Function4;
import com.liferay.functional.Function5;
import com.liferay.functional.Function6;
import com.liferay.functional.Function7;
import com.liferay.functional.Function8;
import com.liferay.functional.Function9;
import com.liferay.functional.Monoid;
import com.liferay.functional.validation.Validation.Failure;
import com.liferay.functional.validation.Validation.Success;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Carlos Sierra Andrés
 */
public interface Validator<T, R, F extends Monoid<F>> 
	extends Applicative<Validator, R>, Function<T, Validation<R, F>> {

	Validation<R, F> validate(T input);

	default Validator<T, R, F> and(Validator<T, R, F> other) {
		return input ->
			this.validate(input).map(x -> (Function)(y -> y)).apply(
				other.validate(input));
	}

	@Override
	default Validation<R, F> apply(T t) {
		return validate(t);
	}

	default <S> Validator<T, S, F> map(Function<R, S> fun) {

		return input -> {
			Validation<R, F> result = validate(input);

			return (Validation<S, F>)result.map(fun);
		};
	}

	static <T, F extends Monoid<F>> Validator<T, T, F> partial(
		Validator<T, ?, F> validator) {
		
		return input -> validator.validate(input).map(ign -> input);
	}

	@SafeVarargs
	static <T, F extends Monoid<F>> Validator<T, T, F> partials(
		Validator<T, ?, F> ... validators) {

		return Arrays.stream(validators).map(Validator::partial).reduce(
			Success::new, Validator::and);
	}

	@Override
	default <S, U> Validator<T, U, F> apply(Applicative<Validator, S> ap) {
		return input -> validate(input).apply(
			((Validator<T, S, F>)ap).validate(input));
	}

	default public <S> Validator<T, S, F> flatMap(
		Function<R, Validator<T, S, F>> fun) {

		return input -> validate(input).flatMap(
			fun.andThen(v -> v.validate(input)));
	}

	default <S> Validator<T, S, F> compose(Validator<R, S, F> validator) {
		return input -> (Validation<S, F>) validate(input).flatMap(
			validator::validate);
	}

	default <C, F2 extends Monoid<F2>> Validator<C, R, F2> adapt(
		Function<C, T> val, Function<F, F2> errors) {

		return input -> validate(val.apply(input)).mapFailures(errors);
	}

	default <C, F2 extends Monoid<F2>> Validator<C, R, F2> flatAdapt(
		Function<C, Validation<T, F>> val, Function<F, F2> errors) {

		return input -> val.apply(input).flatMap(this::validate).mapFailures(
			errors);
	}

	static <T, F extends Monoid<F>> Validator<T, T, F> predicate(
		Predicate<T> predicate, Function<T, F> error) {

		return input -> {
			if (predicate.test(input)) {
				return new Success<>(input);
			}
			else {
				return new Failure<>(error.apply(input));
			}
		};
	}

//	static Validator<String, String, FAILURE> hasLength(int length) {
//		return predicate(
//			input -> input.length() == length,
//			input -> input + " must have " + length + " letters");
//	}
//
//	static Validator<String, String, FAILURE> longerThan(int length) {
//		return predicate(
//			input -> input.length() > length,
//			input -> input + " must have " + length + " letters");
//	}
//
//	Validator<String, String, FAILURE> isANumber = predicate(
//		input -> {
//			try {
//				Integer.parseInt(input);
//
//				return true;
//			}
//			catch (Exception e) {
//				return false;
//			}
//		}, input -> input + " is not a number");
//
//	static Validator<Integer, Integer, FAILURE> greaterThan(int min) {
//		return predicate(
//			input -> input > min,
//			input -> input + " must be greater than" + min
//		);
//	}
//
//	static Validator<Integer, Integer, FAILURE> lowerThan(int max) {
//		return predicate(
//			input -> input < max,
//			input -> input + " should be lower than " + max
//		);
//	}
//
//	static Validator<String, String, FAILURE> startsWith(String prefix) {
//		return predicate(
//			input -> input.startsWith(prefix),
//			input -> input + " should start with " + prefix
//		);
//	}
//
//	static Validator<String, String, FAILURE> endsWith(String suffix) {
//		return predicate(
//			input -> input.endsWith(suffix),
//			input -> input + " should start with " + suffix
//		);
//	}
//
//	static <T> Validator<Optional<T>, Optional<T>> ifPresent(
//		Validator<T, T, FAILURE> validator) {
//
//		return (Optional<T> input) -> {
//			if (!input.isPresent()) {
//				return new Success<>(input);
//			}
//			else {
//				return (Validation<Optional<T>>)
//					validator.validate(input.get()).map(Optional::of);
//			}
//		};
//	}
//
//	Validator<String, Integer, FAILURE> safeInt = isANumber.map(Integer::parseInt);
//
//	static <T> Validator<Optional<T>, T> notEmpty() {
//		return input -> {
//			if (input.isPresent()) {
//				return new Success<>(input.get());
//			}
//			else {
//				return new Failure<>(
//					Collections.singleton("Input is empty"));
//			}
//		};
//	}
//
//	public static <VAL, A, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function1<A, RESULT> fun, Validator<VAL, A, FAILURE> a) {
//		return a.map(fun.curried());
//	}

	public static <VAL, A, B, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function2<A, B, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b) {
		return a.map(fun.curried()).apply(b);
	}

	public static <VAL, A, B, C, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function3<A, B, C, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c) {
		return a.map(fun.curried()).apply(b).apply(c);
	}

	public static <VAL, A, B, C, D, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function4<A, B, C, D, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d);
	}

	public static <VAL, A, B, C, D, E, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function5<A, B, C, D, E, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e);
	}

	public static <VAL, A, B, C, D, E, F, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function6<A, B, C, D, E, F, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f);
	}

	public static <VAL, A, B, C, D, E, F, G, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function7<A, B, C, D, E, F, G, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g);
	}

	public static <VAL, A, B, C, D, E, F, G, H, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function8<A, B, C, D, E, F, G, H, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function9<A, B, C, D, E, F, G, H, I, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, J, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function10<A, B, C, D, E, F, G, H, I, J, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i, Validator<VAL, J, FAILURE> j) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, J, K, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function11<A, B, C, D, E, F, G, H, I, J, K, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i, Validator<VAL, J, FAILURE> j, Validator<VAL, K, FAILURE> k) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, J, K, L, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function12<A, B, C, D, E, F, G, H, I, J, K, L, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i, Validator<VAL, J, FAILURE> j, Validator<VAL, K, FAILURE> k, Validator<VAL, L, FAILURE> l) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, J, K, L, M, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i, Validator<VAL, J, FAILURE> j, Validator<VAL, K, FAILURE> k, Validator<VAL, L, FAILURE> l, Validator<VAL, M, FAILURE> m) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, J, K, L, M, N, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i, Validator<VAL, J, FAILURE> j, Validator<VAL, K, FAILURE> k, Validator<VAL, L, FAILURE> l, Validator<VAL, M, FAILURE> m, Validator<VAL, N, FAILURE> n) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i, Validator<VAL, J, FAILURE> j, Validator<VAL, K, FAILURE> k, Validator<VAL, L, FAILURE> l, Validator<VAL, M, FAILURE> m, Validator<VAL, N, FAILURE> n, Validator<VAL, O, FAILURE> o) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i, Validator<VAL, J, FAILURE> j, Validator<VAL, K, FAILURE> k, Validator<VAL, L, FAILURE> l, Validator<VAL, M, FAILURE> m, Validator<VAL, N, FAILURE> n, Validator<VAL, O, FAILURE> o, Validator<VAL, P, FAILURE> p) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i, Validator<VAL, J, FAILURE> j, Validator<VAL, K, FAILURE> k, Validator<VAL, L, FAILURE> l, Validator<VAL, M, FAILURE> m, Validator<VAL, N, FAILURE> n, Validator<VAL, O, FAILURE> o, Validator<VAL, P, FAILURE> p, Validator<VAL, Q, FAILURE> q) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i, Validator<VAL, J, FAILURE> j, Validator<VAL, K, FAILURE> k, Validator<VAL, L, FAILURE> l, Validator<VAL, M, FAILURE> m, Validator<VAL, N, FAILURE> n, Validator<VAL, O, FAILURE> o, Validator<VAL, P, FAILURE> p, Validator<VAL, Q, FAILURE> q, Validator<VAL, R, FAILURE> r) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i, Validator<VAL, J, FAILURE> j, Validator<VAL, K, FAILURE> k, Validator<VAL, L, FAILURE> l, Validator<VAL, M, FAILURE> m, Validator<VAL, N, FAILURE> n, Validator<VAL, O, FAILURE> o, Validator<VAL, P, FAILURE> p, Validator<VAL, Q, FAILURE> q, Validator<VAL, R, FAILURE> r, Validator<VAL, S, FAILURE> s) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i, Validator<VAL, J, FAILURE> j, Validator<VAL, K, FAILURE> k, Validator<VAL, L, FAILURE> l, Validator<VAL, M, FAILURE> m, Validator<VAL, N, FAILURE> n, Validator<VAL, O, FAILURE> o, Validator<VAL, P, FAILURE> p, Validator<VAL, Q, FAILURE> q, Validator<VAL, R, FAILURE> r, Validator<VAL, S, FAILURE> s, Validator<VAL, T, FAILURE> t) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i, Validator<VAL, J, FAILURE> j, Validator<VAL, K, FAILURE> k, Validator<VAL, L, FAILURE> l, Validator<VAL, M, FAILURE> m, Validator<VAL, N, FAILURE> n, Validator<VAL, O, FAILURE> o, Validator<VAL, P, FAILURE> p, Validator<VAL, Q, FAILURE> q, Validator<VAL, R, FAILURE> r, Validator<VAL, S, FAILURE> s, Validator<VAL, T, FAILURE> t, Validator<VAL, U, FAILURE> u) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i, Validator<VAL, J, FAILURE> j, Validator<VAL, K, FAILURE> k, Validator<VAL, L, FAILURE> l, Validator<VAL, M, FAILURE> m, Validator<VAL, N, FAILURE> n, Validator<VAL, O, FAILURE> o, Validator<VAL, P, FAILURE> p, Validator<VAL, Q, FAILURE> q, Validator<VAL, R, FAILURE> r, Validator<VAL, S, FAILURE> s, Validator<VAL, T, FAILURE> t, Validator<VAL, U, FAILURE> u, Validator<VAL, V, FAILURE> v) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u).apply(v);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i, Validator<VAL, J, FAILURE> j, Validator<VAL, K, FAILURE> k, Validator<VAL, L, FAILURE> l, Validator<VAL, M, FAILURE> m, Validator<VAL, N, FAILURE> n, Validator<VAL, O, FAILURE> o, Validator<VAL, P, FAILURE> p, Validator<VAL, Q, FAILURE> q, Validator<VAL, R, FAILURE> r, Validator<VAL, S, FAILURE> s, Validator<VAL, T, FAILURE> t, Validator<VAL, U, FAILURE> u, Validator<VAL, V, FAILURE> v, Validator<VAL, W, FAILURE> w) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u).apply(v).apply(w);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i, Validator<VAL, J, FAILURE> j, Validator<VAL, K, FAILURE> k, Validator<VAL, L, FAILURE> l, Validator<VAL, M, FAILURE> m, Validator<VAL, N, FAILURE> n, Validator<VAL, O, FAILURE> o, Validator<VAL, P, FAILURE> p, Validator<VAL, Q, FAILURE> q, Validator<VAL, R, FAILURE> r, Validator<VAL, S, FAILURE> s, Validator<VAL, T, FAILURE> t, Validator<VAL, U, FAILURE> u, Validator<VAL, V, FAILURE> v, Validator<VAL, W, FAILURE> w, Validator<VAL, X, FAILURE> x) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u).apply(v).apply(w).apply(x);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i, Validator<VAL, J, FAILURE> j, Validator<VAL, K, FAILURE> k, Validator<VAL, L, FAILURE> l, Validator<VAL, M, FAILURE> m, Validator<VAL, N, FAILURE> n, Validator<VAL, O, FAILURE> o, Validator<VAL, P, FAILURE> p, Validator<VAL, Q, FAILURE> q, Validator<VAL, R, FAILURE> r, Validator<VAL, S, FAILURE> s, Validator<VAL, T, FAILURE> t, Validator<VAL, U, FAILURE> u, Validator<VAL, V, FAILURE> v, Validator<VAL, W, FAILURE> w, Validator<VAL, X, FAILURE> x, Validator<VAL, Y, FAILURE> y) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u).apply(v).apply(w).apply(x).apply(y);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RESULT, FAILURE extends Monoid<FAILURE>> Validator<VAL, RESULT, FAILURE> apply(Function26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RESULT> fun, Validator<VAL, A, FAILURE> a, Validator<VAL, B, FAILURE> b, Validator<VAL, C, FAILURE> c, Validator<VAL, D, FAILURE> d, Validator<VAL, E, FAILURE> e, Validator<VAL, F, FAILURE> f, Validator<VAL, G, FAILURE> g, Validator<VAL, H, FAILURE> h, Validator<VAL, I, FAILURE> i, Validator<VAL, J, FAILURE> j, Validator<VAL, K, FAILURE> k, Validator<VAL, L, FAILURE> l, Validator<VAL, M, FAILURE> m, Validator<VAL, N, FAILURE> n, Validator<VAL, O, FAILURE> o, Validator<VAL, P, FAILURE> p, Validator<VAL, Q, FAILURE> q, Validator<VAL, R, FAILURE> r, Validator<VAL, S, FAILURE> s, Validator<VAL, T, FAILURE> t, Validator<VAL, U, FAILURE> u, Validator<VAL, V, FAILURE> v, Validator<VAL, W, FAILURE> w, Validator<VAL, X, FAILURE> x, Validator<VAL, Y, FAILURE> y, Validator<VAL, Z, FAILURE> z) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u).apply(v).apply(w).apply(x).apply(y).apply(z);
	}

}
