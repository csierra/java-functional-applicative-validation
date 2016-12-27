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

import java.util.function.Function;

/**
 * @author Carlos Sierra Andrés
 */
public interface Validation<T, F extends Monoid<F>> {

	public boolean isSuccess();

	public T get();

	public F failures();

	public <S> Validation<S, F> map(Function<T, S> fun);

	public <F2 extends Monoid<F2>> Validation<T, F2> mapFailures(
		Function<F, F2> fun);

	public <S, U> Validation<U, F> apply(Validation<S, F> ap);

	public <S> Validation<S, F> flatMap(Function<T, Validation<S, F>> fun);

	class Success<T, F extends Monoid<F>> implements Validation<T, F> {

		private final T _t;

		public Success(T t) {
			_t = t;
		}

		public <S> Validation<S, F> map(Function<T, S> fun) {
			return new Success<>(fun.apply(_t));
		}

		@Override
		public <F2 extends Monoid<F2>> Validation<T, F2> mapFailures(
			Function<F, F2> fun) {

			return (Validation<T, F2>)this;
		}

		@Override
		public <S, U> Validation<U, F> apply(Validation<S, F> ap) {
			return ap.map((Function<S, U>)_t);
		}

		@Override
		public String toString() {
			return "Success {" + _t + '}';
		}

		public <S> Validation<S, F> flatMap(Function<T, Validation<S, F>> fun) {
			return fun.apply(_t);
		}

		@Override
		public boolean isSuccess() {
			return true;
		}

		@Override
		public T get() {
			return _t;
		}

		@Override
		public F failures() {
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Success<?, ?> success = (Success<?, ?>) o;

			return _t.equals(success._t);
		}

		@Override
		public int hashCode() {
			return _t.hashCode();
		}

	}

	class Failure<T, F extends Monoid<F>> implements Validation<T, F> {

		private F _failure;

		public Failure(F failure) {
			_failure = failure;
		}

		@Override
		public <S> Validation<S, F> map(
			Function<T, S> fun) {

			return (Validation)this;
		}

		@Override
		public <F2 extends Monoid<F2>> Validation<T, F2> mapFailures(
			Function<F, F2> fun) {

			return new Failure<>(fun.apply(_failure));
		}

		public <S, U> Validation<U, F> apply(Validation<S, F> ap) {
			if (ap instanceof Failure) {
				return new Failure<>((F)_failure.mappend(((Failure) ap)._failure));
			}

			else {
				return (Validation)this;
			}
		}

		@Override
		public String toString() {
			return "Failure {" + "Reasons: " + _failure.toString() + '}';
		}

		@Override
		public <S> Validation<S, F> flatMap(
			Function<T, Validation<S, F>> fun) {

			return (Validation<S, F>)this;
		}

		@Override
		public boolean isSuccess() {
			return false;
		}

		@Override
		public T get() {
			throw new IllegalStateException();
		}

		@Override
		public F failures() {
			return _failure;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Failure<?, ?> failure = (Failure<?, ?>) o;

			return _failure.equals(failure._failure);
		}

		@Override
		public int hashCode() {
			return _failure.hashCode();
		}

	}

	public static <A, B, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function2<A, B, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b) {
		return a.map(fun.curried()).apply(b);
	}

	public static <A, B, C, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function3<A, B, C, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c) {
		return a.map(fun.curried()).apply(b).apply(c);
	}

	public static <A, B, C, D, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function4<A, B, C, D, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d);
	}

	public static <A, B, C, D, E, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function5<A, B, C, D, E, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e);
	}

	public static <A, B, C, D, E, F, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function6<A, B, C, D, E, F, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f);
	}

	public static <A, B, C, D, E, F, G, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function7<A, B, C, D, E, F, G, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g);
	}

	public static <A, B, C, D, E, F, G, H, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function8<A, B, C, D, E, F, G, H, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h);
	}

	public static <A, B, C, D, E, F, G, H, I, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function9<A, B, C, D, E, F, G, H, I, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i);
	}

	public static <A, B, C, D, E, F, G, H, I, J, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function10<A, B, C, D, E, F, G, H, I, J, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function11<A, B, C, D, E, F, G, H, I, J, K, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function12<A, B, C, D, E, F, G, H, I, J, K, L, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t, Validation<U, FAILURE> u) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t, Validation<U, FAILURE> u, Validation<V, FAILURE> v) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u).apply(v);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t, Validation<U, FAILURE> u, Validation<V, FAILURE> v, Validation<W, FAILURE> w) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u).apply(v).apply(w);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t, Validation<U, FAILURE> u, Validation<V, FAILURE> v, Validation<W, FAILURE> w, Validation<X, FAILURE> x) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u).apply(v).apply(w).apply(x);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t, Validation<U, FAILURE> u, Validation<V, FAILURE> v, Validation<W, FAILURE> w, Validation<X, FAILURE> x, Validation<Y, FAILURE> y) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u).apply(v).apply(w).apply(x).apply(y);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t, Validation<U, FAILURE> u, Validation<V, FAILURE> v, Validation<W, FAILURE> w, Validation<X, FAILURE> x, Validation<Y, FAILURE> y, Validation<Z, FAILURE> z) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u).apply(v).apply(w).apply(x).apply(y).apply(z);
	}

}
