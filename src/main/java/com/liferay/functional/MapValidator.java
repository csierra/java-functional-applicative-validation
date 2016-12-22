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
	implements Validator<Map<String, String>, T> {

	private Function<Map<String, String>, Validation<T>> _f;

	MapValidator(Function<Map<String, String>, Validation<T>> f) {
		_f = f;
	}

	@Override
	public <S> MapValidator<S> map(Function<T, S> g) {
		return new MapValidator<>(
			h -> _f.apply(h).map(g));
	}

	public <S, U> MapValidator<U> apply(
		MapValidator<S> ap) {

		return new MapValidator<>(
			h -> _f.apply(h).<S, U>apply(ap._f.apply(h)));
	}

	public <S> MapValidator<S> flatMap(
		Function<T, MapValidator<S>> fun) {

		return new MapValidator<>(
			h ->  _f.apply(h).flatMap(fun.andThen(mv -> mv._f.apply(h))));
	}

	public static <T> MapValidator<T> mv(
		String fieldName, FieldValidator<Optional<String>, T> fv) {

		return new MapValidator<>(
			h -> fv.validate(
				new Field<>(fieldName, Optional.ofNullable(h.get(fieldName)))));
	}

	public Validation<T> validate(Map<String, String> input) {
		return _f.apply(input);
	}

	static FieldValidator<Optional<String>, Integer> isThereANumber =
		FieldValidator.<String>notEmpty().compose(FieldValidator.safeInt);

	public static MapValidator<Integer> dayUpTo(int upperLimit) {
		return mv("day", isThereANumber.compose(between(0, upperLimit)));
	}

	public static <A, B, RESULT> MapValidator<RESULT> apply(Function2<A, B, RESULT> fun, MapValidator<A> a, MapValidator<B> b) {
		return a.map(fun.curried()).apply(b);
	}

	public static <A, B, C, RESULT> MapValidator<RESULT> apply(Function3<A, B, C, RESULT> fun, MapValidator<A> a, MapValidator<B> b, MapValidator<C> c) {
		return a.map(fun.curried()).apply(b).apply(c);
	}

	public static <A, B, C, D, RESULT> MapValidator<RESULT> apply(Function4<A, B, C, D, RESULT> fun, MapValidator<A> a, MapValidator<B> b, MapValidator<C> c, MapValidator<D> d) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d);
	}

	public static <A, B, C, D, E, RESULT> MapValidator<RESULT> apply(Function5<A, B, C, D, E, RESULT> fun, MapValidator<A> a, MapValidator<B> b, MapValidator<C> c, MapValidator<D> d, MapValidator<E> e) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e);
	}

	public static <A, B, C, D, E, F, RESULT> MapValidator<RESULT> apply(Function6<A, B, C, D, E, F, RESULT> fun, MapValidator<A> a, MapValidator<B> b, MapValidator<C> c, MapValidator<D> d, MapValidator<E> e, MapValidator<F> f) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f);
	}

	public static <A, B, C, D, E, F, G, RESULT> MapValidator<RESULT> apply(Function7<A, B, C, D, E, F, G, RESULT> fun, MapValidator<A> a, MapValidator<B> b, MapValidator<C> c, MapValidator<D> d, MapValidator<E> e, MapValidator<F> f, MapValidator<G> g) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g);
	}

	public static <A, B, C, D, E, F, G, H, RESULT> MapValidator<RESULT> apply(Function8<A, B, C, D, E, F, G, H, RESULT> fun, MapValidator<A> a, MapValidator<B> b, MapValidator<C> c, MapValidator<D> d, MapValidator<E> e, MapValidator<F> f, MapValidator<G> g, MapValidator<H> h) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h);
	}

	public static <A, B, C, D, E, F, G, H, I, RESULT> MapValidator<RESULT> apply(Function9<A, B, C, D, E, F, G, H, I, RESULT> fun, MapValidator<A> a, MapValidator<B> b, MapValidator<C> c, MapValidator<D> d, MapValidator<E> e, MapValidator<F> f, MapValidator<G> g, MapValidator<H> h, MapValidator<I> i) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i);
	}

	public static <A, B, C, D, E, F, G, H, I, J, RESULT> MapValidator<RESULT> apply(Function10<A, B, C, D, E, F, G, H, I, J, RESULT> fun, MapValidator<A> a, MapValidator<B> b, MapValidator<C> c, MapValidator<D> d, MapValidator<E> e, MapValidator<F> f, MapValidator<G> g, MapValidator<H> h, MapValidator<I> i, MapValidator<J> j) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, RESULT> MapValidator<RESULT> apply(Function11<A, B, C, D, E, F, G, H, I, J, K, RESULT> fun, MapValidator<A> a, MapValidator<B> b, MapValidator<C> c, MapValidator<D> d, MapValidator<E> e, MapValidator<F> f, MapValidator<G> g, MapValidator<H> h, MapValidator<I> i, MapValidator<J> j, MapValidator<K> k) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, RESULT> MapValidator<RESULT> apply(Function12<A, B, C, D, E, F, G, H, I, J, K, L, RESULT> fun, MapValidator<A> a, MapValidator<B> b, MapValidator<C> c, MapValidator<D> d, MapValidator<E> e, MapValidator<F> f, MapValidator<G> g, MapValidator<H> h, MapValidator<I> i, MapValidator<J> j, MapValidator<K> k, MapValidator<L> l) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, RESULT> MapValidator<RESULT> apply(Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, RESULT> fun, MapValidator<A> a, MapValidator<B> b, MapValidator<C> c, MapValidator<D> d, MapValidator<E> e, MapValidator<F> f, MapValidator<G> g, MapValidator<H> h, MapValidator<I> i, MapValidator<J> j, MapValidator<K> k, MapValidator<L> l, MapValidator<M> m) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, RESULT> MapValidator<RESULT> apply(Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, RESULT> fun, MapValidator<A> a, MapValidator<B> b, MapValidator<C> c, MapValidator<D> d, MapValidator<E> e, MapValidator<F> f, MapValidator<G> g, MapValidator<H> h, MapValidator<I> i, MapValidator<J> j, MapValidator<K> k, MapValidator<L> l, MapValidator<M> m, MapValidator<N> n) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RESULT> MapValidator<RESULT> apply(Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RESULT> fun, MapValidator<A> a, MapValidator<B> b, MapValidator<C> c, MapValidator<D> d, MapValidator<E> e, MapValidator<F> f, MapValidator<G> g, MapValidator<H> h, MapValidator<I> i, MapValidator<J> j, MapValidator<K> k, MapValidator<L> l, MapValidator<M> m, MapValidator<N> n, MapValidator<O> o) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RESULT> MapValidator<RESULT> apply(Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RESULT> fun, MapValidator<A> a, MapValidator<B> b, MapValidator<C> c, MapValidator<D> d, MapValidator<E> e, MapValidator<F> f, MapValidator<G> g, MapValidator<H> h, MapValidator<I> i, MapValidator<J> j, MapValidator<K> k, MapValidator<L> l, MapValidator<M> m, MapValidator<N> n, MapValidator<O> o, MapValidator<P> p) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RESULT> MapValidator<RESULT> apply(Function17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RESULT> fun, MapValidator<A> a, MapValidator<B> b, MapValidator<C> c, MapValidator<D> d, MapValidator<E> e, MapValidator<F> f, MapValidator<G> g, MapValidator<H> h, MapValidator<I> i, MapValidator<J> j, MapValidator<K> k, MapValidator<L> l, MapValidator<M> m, MapValidator<N> n, MapValidator<O> o, MapValidator<P> p, MapValidator<Q> q) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RESULT> MapValidator<RESULT> apply(Function18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RESULT> fun, MapValidator<A> a, MapValidator<B> b, MapValidator<C> c, MapValidator<D> d, MapValidator<E> e, MapValidator<F> f, MapValidator<G> g, MapValidator<H> h, MapValidator<I> i, MapValidator<J> j, MapValidator<K> k, MapValidator<L> l, MapValidator<M> m, MapValidator<N> n, MapValidator<O> o, MapValidator<P> p, MapValidator<Q> q, MapValidator<R> r) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RESULT> MapValidator<RESULT> apply(Function19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RESULT> fun, MapValidator<A> a, MapValidator<B> b, MapValidator<C> c, MapValidator<D> d, MapValidator<E> e, MapValidator<F> f, MapValidator<G> g, MapValidator<H> h, MapValidator<I> i, MapValidator<J> j, MapValidator<K> k, MapValidator<L> l, MapValidator<M> m, MapValidator<N> n, MapValidator<O> o, MapValidator<P> p, MapValidator<Q> q, MapValidator<R> r, MapValidator<S> s) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RESULT> MapValidator<RESULT> apply(Function20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RESULT> fun, MapValidator<A> a, MapValidator<B> b, MapValidator<C> c, MapValidator<D> d, MapValidator<E> e, MapValidator<F> f, MapValidator<G> g, MapValidator<H> h, MapValidator<I> i, MapValidator<J> j, MapValidator<K> k, MapValidator<L> l, MapValidator<M> m, MapValidator<N> n, MapValidator<O> o, MapValidator<P> p, MapValidator<Q> q, MapValidator<R> r, MapValidator<S> s, MapValidator<T> t) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RESULT> MapValidator<RESULT> apply(Function21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RESULT> fun, MapValidator<A> a, MapValidator<B> b, MapValidator<C> c, MapValidator<D> d, MapValidator<E> e, MapValidator<F> f, MapValidator<G> g, MapValidator<H> h, MapValidator<I> i, MapValidator<J> j, MapValidator<K> k, MapValidator<L> l, MapValidator<M> m, MapValidator<N> n, MapValidator<O> o, MapValidator<P> p, MapValidator<Q> q, MapValidator<R> r, MapValidator<S> s, MapValidator<T> t, MapValidator<U> u) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RESULT> MapValidator<RESULT> apply(Function22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RESULT> fun, MapValidator<A> a, MapValidator<B> b, MapValidator<C> c, MapValidator<D> d, MapValidator<E> e, MapValidator<F> f, MapValidator<G> g, MapValidator<H> h, MapValidator<I> i, MapValidator<J> j, MapValidator<K> k, MapValidator<L> l, MapValidator<M> m, MapValidator<N> n, MapValidator<O> o, MapValidator<P> p, MapValidator<Q> q, MapValidator<R> r, MapValidator<S> s, MapValidator<T> t, MapValidator<U> u, MapValidator<V> v) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u).apply(v);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RESULT> MapValidator<RESULT> apply(Function23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RESULT> fun, MapValidator<A> a, MapValidator<B> b, MapValidator<C> c, MapValidator<D> d, MapValidator<E> e, MapValidator<F> f, MapValidator<G> g, MapValidator<H> h, MapValidator<I> i, MapValidator<J> j, MapValidator<K> k, MapValidator<L> l, MapValidator<M> m, MapValidator<N> n, MapValidator<O> o, MapValidator<P> p, MapValidator<Q> q, MapValidator<R> r, MapValidator<S> s, MapValidator<T> t, MapValidator<U> u, MapValidator<V> v, MapValidator<W> w) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u).apply(v).apply(w);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, RESULT> MapValidator<RESULT> apply(Function24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, RESULT> fun, MapValidator<A> a, MapValidator<B> b, MapValidator<C> c, MapValidator<D> d, MapValidator<E> e, MapValidator<F> f, MapValidator<G> g, MapValidator<H> h, MapValidator<I> i, MapValidator<J> j, MapValidator<K> k, MapValidator<L> l, MapValidator<M> m, MapValidator<N> n, MapValidator<O> o, MapValidator<P> p, MapValidator<Q> q, MapValidator<R> r, MapValidator<S> s, MapValidator<T> t, MapValidator<U> u, MapValidator<V> v, MapValidator<W> w, MapValidator<X> x) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u).apply(v).apply(w).apply(x);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, RESULT> MapValidator<RESULT> apply(Function25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, RESULT> fun, MapValidator<A> a, MapValidator<B> b, MapValidator<C> c, MapValidator<D> d, MapValidator<E> e, MapValidator<F> f, MapValidator<G> g, MapValidator<H> h, MapValidator<I> i, MapValidator<J> j, MapValidator<K> k, MapValidator<L> l, MapValidator<M> m, MapValidator<N> n, MapValidator<O> o, MapValidator<P> p, MapValidator<Q> q, MapValidator<R> r, MapValidator<S> s, MapValidator<T> t, MapValidator<U> u, MapValidator<V> v, MapValidator<W> w, MapValidator<X> x, MapValidator<Y> y) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u).apply(v).apply(w).apply(x).apply(y);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RESULT> MapValidator<RESULT> apply(Function26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RESULT> fun, MapValidator<A> a, MapValidator<B> b, MapValidator<C> c, MapValidator<D> d, MapValidator<E> e, MapValidator<F> f, MapValidator<G> g, MapValidator<H> h, MapValidator<I> i, MapValidator<J> j, MapValidator<K> k, MapValidator<L> l, MapValidator<M> m, MapValidator<N> n, MapValidator<O> o, MapValidator<P> p, MapValidator<Q> q, MapValidator<R> r, MapValidator<S> s, MapValidator<T> t, MapValidator<U> u, MapValidator<V> v, MapValidator<W> w, MapValidator<X> x, MapValidator<Y> y, MapValidator<Z> z) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u).apply(v).apply(w).apply(x).apply(y).apply(z);
	}

	public static void main(String[] args) {


		MapValidator<Integer> month = mv(
			"month", isThereANumber.compose(between(0, 13)));

		MapValidator<Integer> year =
			mv("year", isThereANumber.compose(between(1900, 10000)));

		MapValidator<Integer> day = month.flatMap(
			m -> {
				if (Arrays.asList(1, 3, 5, 7, 8, 10, 12).contains(m)) {
					return dayUpTo(32);
				}
				else if (Arrays.asList(4, 6, 9, 11).contains(m)) {
					return dayUpTo(31);
				}
				else {
					return dayUpTo(29);
				}
		});


		MapValidator<Date> calendarMapValidator =
			apply(
				GregorianCalendar::new,
					year.map(x -> x - 1900),
					month.map(x -> x - 1),
					day).
				map(GregorianCalendar::getTime);

		System.out.println(
			calendarMapValidator.validate(
				new HashMap<String, String>() {{
					put("day", "28");
					put("month", "02");
					put("year", "1979");
				}}));
	}
}
