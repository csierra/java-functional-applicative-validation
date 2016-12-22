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
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Carlos Sierra Andrés
 */
public interface Validator<T, R> extends Applicative<Validator, R>{

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

	@Override
	default <S, U> Validator<T, U> apply(Applicative<Validator, S> ap) {
		return input -> validate(input).apply(
			((Validator<T, S>)ap).validate(input));
	}

	default public <S> Validator<T, S> flatMap(
		Function<R, Validator<T, S>> fun) {

		return input -> validate(input).flatMap(
			fun.andThen(v -> v.validate(input)));
	}

	default <S> Validator<T, S> compose(Validator<R, S> validator) {
		return input -> (Validation<S>) validate(input).flatMap(
			validator::validate);
	}

	default <F> Validator<F, R> adapt(
		Function<F, T> val, Function<String, String> errors) {

		return input -> {
			Validation<R> validation = validate(val.apply(input));

			if (!validation.isPresent()) {
				return new Failure<>(validation.failures().stream().map(errors).collect(Collectors.toSet()));
			}

			return validation;
		};
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

	public static <VAL, A, RESULT> Validator<VAL, RESULT> apply(Function1<A, RESULT> fun, Validator<VAL, A> a) {
		return a.map(fun.curried());
	}

	public static <VAL, A, B, RESULT> Validator<VAL, RESULT> apply(Function2<A, B, RESULT> fun, Validator<VAL, A> a, Validator<VAL, B> b) {
		return a.map(fun.curried()).apply(b);
	}

	public static <VAL, A, B, C, RESULT> Validator<VAL, RESULT> apply(Function3<A, B, C, RESULT> fun, Validator<VAL, A> a, Validator<VAL, B> b, Validator<VAL, C> c) {
		return a.map(fun.curried()).apply(b).apply(c);
	}

	public static <VAL, A, B, C, D, RESULT> Validator<VAL, RESULT> apply(Function4<A, B, C, D, RESULT> fun, Validator<VAL, A> a, Validator<VAL, B> b, Validator<VAL, C> c, Validator<VAL, D> d) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d);
	}

	public static <VAL, A, B, C, D, E, RESULT> Validator<VAL, RESULT> apply(Function5<A, B, C, D, E, RESULT> fun, Validator<VAL, A> a, Validator<VAL, B> b, Validator<VAL, C> c, Validator<VAL, D> d, Validator<VAL, E> e) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e);
	}

	public static <VAL, A, B, C, D, E, F, RESULT> Validator<VAL, RESULT> apply(Function6<A, B, C, D, E, F, RESULT> fun, Validator<VAL, A> a, Validator<VAL, B> b, Validator<VAL, C> c, Validator<VAL, D> d, Validator<VAL, E> e, Validator<VAL, F> f) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f);
	}

	public static <VAL, A, B, C, D, E, F, G, RESULT> Validator<VAL, RESULT> apply(Function7<A, B, C, D, E, F, G, RESULT> fun, Validator<VAL, A> a, Validator<VAL, B> b, Validator<VAL, C> c, Validator<VAL, D> d, Validator<VAL, E> e, Validator<VAL, F> f, Validator<VAL, G> g) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g);
	}

	public static <VAL, A, B, C, D, E, F, G, H, RESULT> Validator<VAL, RESULT> apply(Function8<A, B, C, D, E, F, G, H, RESULT> fun, Validator<VAL, A> a, Validator<VAL, B> b, Validator<VAL, C> c, Validator<VAL, D> d, Validator<VAL, E> e, Validator<VAL, F> f, Validator<VAL, G> g, Validator<VAL, H> h) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, RESULT> Validator<VAL, RESULT> apply(Function9<A, B, C, D, E, F, G, H, I, RESULT> fun, Validator<VAL, A> a, Validator<VAL, B> b, Validator<VAL, C> c, Validator<VAL, D> d, Validator<VAL, E> e, Validator<VAL, F> f, Validator<VAL, G> g, Validator<VAL, H> h, Validator<VAL, I> i) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, J, RESULT> Validator<VAL, RESULT> apply(Function10<A, B, C, D, E, F, G, H, I, J, RESULT> fun, Validator<VAL, A> a, Validator<VAL, B> b, Validator<VAL, C> c, Validator<VAL, D> d, Validator<VAL, E> e, Validator<VAL, F> f, Validator<VAL, G> g, Validator<VAL, H> h, Validator<VAL, I> i, Validator<VAL, J> j) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, J, K, RESULT> Validator<VAL, RESULT> apply(Function11<A, B, C, D, E, F, G, H, I, J, K, RESULT> fun, Validator<VAL, A> a, Validator<VAL, B> b, Validator<VAL, C> c, Validator<VAL, D> d, Validator<VAL, E> e, Validator<VAL, F> f, Validator<VAL, G> g, Validator<VAL, H> h, Validator<VAL, I> i, Validator<VAL, J> j, Validator<VAL, K> k) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, J, K, L, RESULT> Validator<VAL, RESULT> apply(Function12<A, B, C, D, E, F, G, H, I, J, K, L, RESULT> fun, Validator<VAL, A> a, Validator<VAL, B> b, Validator<VAL, C> c, Validator<VAL, D> d, Validator<VAL, E> e, Validator<VAL, F> f, Validator<VAL, G> g, Validator<VAL, H> h, Validator<VAL, I> i, Validator<VAL, J> j, Validator<VAL, K> k, Validator<VAL, L> l) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, J, K, L, M, RESULT> Validator<VAL, RESULT> apply(Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, RESULT> fun, Validator<VAL, A> a, Validator<VAL, B> b, Validator<VAL, C> c, Validator<VAL, D> d, Validator<VAL, E> e, Validator<VAL, F> f, Validator<VAL, G> g, Validator<VAL, H> h, Validator<VAL, I> i, Validator<VAL, J> j, Validator<VAL, K> k, Validator<VAL, L> l, Validator<VAL, M> m) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, J, K, L, M, N, RESULT> Validator<VAL, RESULT> apply(Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, RESULT> fun, Validator<VAL, A> a, Validator<VAL, B> b, Validator<VAL, C> c, Validator<VAL, D> d, Validator<VAL, E> e, Validator<VAL, F> f, Validator<VAL, G> g, Validator<VAL, H> h, Validator<VAL, I> i, Validator<VAL, J> j, Validator<VAL, K> k, Validator<VAL, L> l, Validator<VAL, M> m, Validator<VAL, N> n) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RESULT> Validator<VAL, RESULT> apply(Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RESULT> fun, Validator<VAL, A> a, Validator<VAL, B> b, Validator<VAL, C> c, Validator<VAL, D> d, Validator<VAL, E> e, Validator<VAL, F> f, Validator<VAL, G> g, Validator<VAL, H> h, Validator<VAL, I> i, Validator<VAL, J> j, Validator<VAL, K> k, Validator<VAL, L> l, Validator<VAL, M> m, Validator<VAL, N> n, Validator<VAL, O> o) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RESULT> Validator<VAL, RESULT> apply(Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RESULT> fun, Validator<VAL, A> a, Validator<VAL, B> b, Validator<VAL, C> c, Validator<VAL, D> d, Validator<VAL, E> e, Validator<VAL, F> f, Validator<VAL, G> g, Validator<VAL, H> h, Validator<VAL, I> i, Validator<VAL, J> j, Validator<VAL, K> k, Validator<VAL, L> l, Validator<VAL, M> m, Validator<VAL, N> n, Validator<VAL, O> o, Validator<VAL, P> p) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RESULT> Validator<VAL, RESULT> apply(Function17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RESULT> fun, Validator<VAL, A> a, Validator<VAL, B> b, Validator<VAL, C> c, Validator<VAL, D> d, Validator<VAL, E> e, Validator<VAL, F> f, Validator<VAL, G> g, Validator<VAL, H> h, Validator<VAL, I> i, Validator<VAL, J> j, Validator<VAL, K> k, Validator<VAL, L> l, Validator<VAL, M> m, Validator<VAL, N> n, Validator<VAL, O> o, Validator<VAL, P> p, Validator<VAL, Q> q) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RESULT> Validator<VAL, RESULT> apply(Function18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RESULT> fun, Validator<VAL, A> a, Validator<VAL, B> b, Validator<VAL, C> c, Validator<VAL, D> d, Validator<VAL, E> e, Validator<VAL, F> f, Validator<VAL, G> g, Validator<VAL, H> h, Validator<VAL, I> i, Validator<VAL, J> j, Validator<VAL, K> k, Validator<VAL, L> l, Validator<VAL, M> m, Validator<VAL, N> n, Validator<VAL, O> o, Validator<VAL, P> p, Validator<VAL, Q> q, Validator<VAL, R> r) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RESULT> Validator<VAL, RESULT> apply(Function19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RESULT> fun, Validator<VAL, A> a, Validator<VAL, B> b, Validator<VAL, C> c, Validator<VAL, D> d, Validator<VAL, E> e, Validator<VAL, F> f, Validator<VAL, G> g, Validator<VAL, H> h, Validator<VAL, I> i, Validator<VAL, J> j, Validator<VAL, K> k, Validator<VAL, L> l, Validator<VAL, M> m, Validator<VAL, N> n, Validator<VAL, O> o, Validator<VAL, P> p, Validator<VAL, Q> q, Validator<VAL, R> r, Validator<VAL, S> s) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RESULT> Validator<VAL, RESULT> apply(Function20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RESULT> fun, Validator<VAL, A> a, Validator<VAL, B> b, Validator<VAL, C> c, Validator<VAL, D> d, Validator<VAL, E> e, Validator<VAL, F> f, Validator<VAL, G> g, Validator<VAL, H> h, Validator<VAL, I> i, Validator<VAL, J> j, Validator<VAL, K> k, Validator<VAL, L> l, Validator<VAL, M> m, Validator<VAL, N> n, Validator<VAL, O> o, Validator<VAL, P> p, Validator<VAL, Q> q, Validator<VAL, R> r, Validator<VAL, S> s, Validator<VAL, T> t) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RESULT> Validator<VAL, RESULT> apply(Function21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RESULT> fun, Validator<VAL, A> a, Validator<VAL, B> b, Validator<VAL, C> c, Validator<VAL, D> d, Validator<VAL, E> e, Validator<VAL, F> f, Validator<VAL, G> g, Validator<VAL, H> h, Validator<VAL, I> i, Validator<VAL, J> j, Validator<VAL, K> k, Validator<VAL, L> l, Validator<VAL, M> m, Validator<VAL, N> n, Validator<VAL, O> o, Validator<VAL, P> p, Validator<VAL, Q> q, Validator<VAL, R> r, Validator<VAL, S> s, Validator<VAL, T> t, Validator<VAL, U> u) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RESULT> Validator<VAL, RESULT> apply(Function22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RESULT> fun, Validator<VAL, A> a, Validator<VAL, B> b, Validator<VAL, C> c, Validator<VAL, D> d, Validator<VAL, E> e, Validator<VAL, F> f, Validator<VAL, G> g, Validator<VAL, H> h, Validator<VAL, I> i, Validator<VAL, J> j, Validator<VAL, K> k, Validator<VAL, L> l, Validator<VAL, M> m, Validator<VAL, N> n, Validator<VAL, O> o, Validator<VAL, P> p, Validator<VAL, Q> q, Validator<VAL, R> r, Validator<VAL, S> s, Validator<VAL, T> t, Validator<VAL, U> u, Validator<VAL, V> v) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u).apply(v);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RESULT> Validator<VAL, RESULT> apply(Function23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RESULT> fun, Validator<VAL, A> a, Validator<VAL, B> b, Validator<VAL, C> c, Validator<VAL, D> d, Validator<VAL, E> e, Validator<VAL, F> f, Validator<VAL, G> g, Validator<VAL, H> h, Validator<VAL, I> i, Validator<VAL, J> j, Validator<VAL, K> k, Validator<VAL, L> l, Validator<VAL, M> m, Validator<VAL, N> n, Validator<VAL, O> o, Validator<VAL, P> p, Validator<VAL, Q> q, Validator<VAL, R> r, Validator<VAL, S> s, Validator<VAL, T> t, Validator<VAL, U> u, Validator<VAL, V> v, Validator<VAL, W> w) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u).apply(v).apply(w);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, RESULT> Validator<VAL, RESULT> apply(Function24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, RESULT> fun, Validator<VAL, A> a, Validator<VAL, B> b, Validator<VAL, C> c, Validator<VAL, D> d, Validator<VAL, E> e, Validator<VAL, F> f, Validator<VAL, G> g, Validator<VAL, H> h, Validator<VAL, I> i, Validator<VAL, J> j, Validator<VAL, K> k, Validator<VAL, L> l, Validator<VAL, M> m, Validator<VAL, N> n, Validator<VAL, O> o, Validator<VAL, P> p, Validator<VAL, Q> q, Validator<VAL, R> r, Validator<VAL, S> s, Validator<VAL, T> t, Validator<VAL, U> u, Validator<VAL, V> v, Validator<VAL, W> w, Validator<VAL, X> x) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u).apply(v).apply(w).apply(x);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, RESULT> Validator<VAL, RESULT> apply(Function25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, RESULT> fun, Validator<VAL, A> a, Validator<VAL, B> b, Validator<VAL, C> c, Validator<VAL, D> d, Validator<VAL, E> e, Validator<VAL, F> f, Validator<VAL, G> g, Validator<VAL, H> h, Validator<VAL, I> i, Validator<VAL, J> j, Validator<VAL, K> k, Validator<VAL, L> l, Validator<VAL, M> m, Validator<VAL, N> n, Validator<VAL, O> o, Validator<VAL, P> p, Validator<VAL, Q> q, Validator<VAL, R> r, Validator<VAL, S> s, Validator<VAL, T> t, Validator<VAL, U> u, Validator<VAL, V> v, Validator<VAL, W> w, Validator<VAL, X> x, Validator<VAL, Y> y) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u).apply(v).apply(w).apply(x).apply(y);
	}

	public static <VAL, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RESULT> Validator<VAL, RESULT> apply(Function26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RESULT> fun, Validator<VAL, A> a, Validator<VAL, B> b, Validator<VAL, C> c, Validator<VAL, D> d, Validator<VAL, E> e, Validator<VAL, F> f, Validator<VAL, G> g, Validator<VAL, H> h, Validator<VAL, I> i, Validator<VAL, J> j, Validator<VAL, K> k, Validator<VAL, L> l, Validator<VAL, M> m, Validator<VAL, N> n, Validator<VAL, O> o, Validator<VAL, P> p, Validator<VAL, Q> q, Validator<VAL, R> r, Validator<VAL, S> s, Validator<VAL, T> t, Validator<VAL, U> u, Validator<VAL, V> v, Validator<VAL, W> w, Validator<VAL, X> x, Validator<VAL, Y> y, Validator<VAL, Z> z) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u).apply(v).apply(w).apply(x).apply(y).apply(z);
	}

}
