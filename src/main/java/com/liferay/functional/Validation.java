package com.liferay.functional; /**
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

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * @author Carlos Sierra Andrés
 */
public interface Validation<T> {

	public boolean isPresent();

	public T get();

	public Set<String> failures();

	public <S> Validation<S> map(Function<T, S> fun);

	public <S, U> Validation<U> apply(Validation<S> ap);

	public <S> Validation<S> flatMap(Function<T, Validation<S>> fun);

	class Success<T> implements Validation<T> {

		private T _t;

		public Success(T t) {
			_t = t;
		}

		public <S> Validation<S> map(
			Function<T, S> fun) {

			return new Success<>(fun.apply(_t));
		}

		@Override
		public <S, U> Validation<U> apply(Validation<S> ap) {

			return ap.map((Function<S, U>)_t);
		}

		@Override
		public String toString() {
			return "Success {" + _t + '}';
		}

		public <S> Validation<S> flatMap(
			Function<T, Validation<S>> fun) {

			return fun.apply(_t);
		}

		@Override
		public boolean isPresent() {
			return true;
		}

		@Override
		public T get() {
			return _t;
		}

		@Override
		public Set<String> failures() {
			return null;
		}

	}

	class Failure<T> implements Validation<T> {

		private Set<String> _reasons;

		public Failure(Set<String> reasons) {
			_reasons = reasons;
		}

		@Override
		public <S> Validation<S> map(
			Function<T, S> fun) {

			return (Validation)this;
		}

		public <S, U> Validation<U> apply(Validation<S> ap) {

			if (ap instanceof Failure) {
				Failure failure = (Failure) ap;

				Set<String> reasons = new HashSet<>();

				reasons.addAll(this._reasons);
				reasons.addAll(failure._reasons);

				return new Failure<>(reasons);
			}

			else {
				return (Validation)this;
			}
		}

		@Override
		public String toString() {
			return "Failure {" + "Reasons: " + _reasons + '}';
		}

		@Override
		public <S> Validation<S> flatMap(
			Function<T, Validation<S>> fun) {

			return (Validation<S>)this;
		}

		@Override
		public boolean isPresent() {
			return false;
		}

		@Override
		public T get() {
			return null;
		}

		@Override
		public Set<String> failures() {
			return _reasons;
		}
	}

	public static <A, B, RESULT> Validation<RESULT> apply(Function2<A, B, RESULT> fun, Validation<A> a, Validation<B> b) {
		return a.map(fun.curried()).apply(b);
	}

	public static <A, B, C, RESULT> Validation<RESULT> apply(Function3<A, B, C, RESULT> fun, Validation<A> a, Validation<B> b, Validation<C> c) {
		return a.map(fun.curried()).apply(b).apply(c);
	}

	public static <A, B, C, D, RESULT> Validation<RESULT> apply(Function4<A, B, C, D, RESULT> fun, Validation<A> a, Validation<B> b, Validation<C> c, Validation<D> d) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d);
	}

	public static <A, B, C, D, E, RESULT> Validation<RESULT> apply(Function5<A, B, C, D, E, RESULT> fun, Validation<A> a, Validation<B> b, Validation<C> c, Validation<D> d, Validation<E> e) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e);
	}

	public static <A, B, C, D, E, F, RESULT> Validation<RESULT> apply(Function6<A, B, C, D, E, F, RESULT> fun, Validation<A> a, Validation<B> b, Validation<C> c, Validation<D> d, Validation<E> e, Validation<F> f) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f);
	}

	public static <A, B, C, D, E, F, G, RESULT> Validation<RESULT> apply(Function7<A, B, C, D, E, F, G, RESULT> fun, Validation<A> a, Validation<B> b, Validation<C> c, Validation<D> d, Validation<E> e, Validation<F> f, Validation<G> g) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g);
	}

	public static <A, B, C, D, E, F, G, H, RESULT> Validation<RESULT> apply(Function8<A, B, C, D, E, F, G, H, RESULT> fun, Validation<A> a, Validation<B> b, Validation<C> c, Validation<D> d, Validation<E> e, Validation<F> f, Validation<G> g, Validation<H> h) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h);
	}

	public static <A, B, C, D, E, F, G, H, I, RESULT> Validation<RESULT> apply(Function9<A, B, C, D, E, F, G, H, I, RESULT> fun, Validation<A> a, Validation<B> b, Validation<C> c, Validation<D> d, Validation<E> e, Validation<F> f, Validation<G> g, Validation<H> h, Validation<I> i) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i);
	}

	public static <A, B, C, D, E, F, G, H, I, J, RESULT> Validation<RESULT> apply(Function10<A, B, C, D, E, F, G, H, I, J, RESULT> fun, Validation<A> a, Validation<B> b, Validation<C> c, Validation<D> d, Validation<E> e, Validation<F> f, Validation<G> g, Validation<H> h, Validation<I> i, Validation<J> j) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, RESULT> Validation<RESULT> apply(Function11<A, B, C, D, E, F, G, H, I, J, K, RESULT> fun, Validation<A> a, Validation<B> b, Validation<C> c, Validation<D> d, Validation<E> e, Validation<F> f, Validation<G> g, Validation<H> h, Validation<I> i, Validation<J> j, Validation<K> k) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, RESULT> Validation<RESULT> apply(Function12<A, B, C, D, E, F, G, H, I, J, K, L, RESULT> fun, Validation<A> a, Validation<B> b, Validation<C> c, Validation<D> d, Validation<E> e, Validation<F> f, Validation<G> g, Validation<H> h, Validation<I> i, Validation<J> j, Validation<K> k, Validation<L> l) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, RESULT> Validation<RESULT> apply(Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, RESULT> fun, Validation<A> a, Validation<B> b, Validation<C> c, Validation<D> d, Validation<E> e, Validation<F> f, Validation<G> g, Validation<H> h, Validation<I> i, Validation<J> j, Validation<K> k, Validation<L> l, Validation<M> m) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, RESULT> Validation<RESULT> apply(Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, RESULT> fun, Validation<A> a, Validation<B> b, Validation<C> c, Validation<D> d, Validation<E> e, Validation<F> f, Validation<G> g, Validation<H> h, Validation<I> i, Validation<J> j, Validation<K> k, Validation<L> l, Validation<M> m, Validation<N> n) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RESULT> Validation<RESULT> apply(Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RESULT> fun, Validation<A> a, Validation<B> b, Validation<C> c, Validation<D> d, Validation<E> e, Validation<F> f, Validation<G> g, Validation<H> h, Validation<I> i, Validation<J> j, Validation<K> k, Validation<L> l, Validation<M> m, Validation<N> n, Validation<O> o) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RESULT> Validation<RESULT> apply(Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RESULT> fun, Validation<A> a, Validation<B> b, Validation<C> c, Validation<D> d, Validation<E> e, Validation<F> f, Validation<G> g, Validation<H> h, Validation<I> i, Validation<J> j, Validation<K> k, Validation<L> l, Validation<M> m, Validation<N> n, Validation<O> o, Validation<P> p) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RESULT> Validation<RESULT> apply(Function17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RESULT> fun, Validation<A> a, Validation<B> b, Validation<C> c, Validation<D> d, Validation<E> e, Validation<F> f, Validation<G> g, Validation<H> h, Validation<I> i, Validation<J> j, Validation<K> k, Validation<L> l, Validation<M> m, Validation<N> n, Validation<O> o, Validation<P> p, Validation<Q> q) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RESULT> Validation<RESULT> apply(Function18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RESULT> fun, Validation<A> a, Validation<B> b, Validation<C> c, Validation<D> d, Validation<E> e, Validation<F> f, Validation<G> g, Validation<H> h, Validation<I> i, Validation<J> j, Validation<K> k, Validation<L> l, Validation<M> m, Validation<N> n, Validation<O> o, Validation<P> p, Validation<Q> q, Validation<R> r) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RESULT> Validation<RESULT> apply(Function19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RESULT> fun, Validation<A> a, Validation<B> b, Validation<C> c, Validation<D> d, Validation<E> e, Validation<F> f, Validation<G> g, Validation<H> h, Validation<I> i, Validation<J> j, Validation<K> k, Validation<L> l, Validation<M> m, Validation<N> n, Validation<O> o, Validation<P> p, Validation<Q> q, Validation<R> r, Validation<S> s) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RESULT> Validation<RESULT> apply(Function20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RESULT> fun, Validation<A> a, Validation<B> b, Validation<C> c, Validation<D> d, Validation<E> e, Validation<F> f, Validation<G> g, Validation<H> h, Validation<I> i, Validation<J> j, Validation<K> k, Validation<L> l, Validation<M> m, Validation<N> n, Validation<O> o, Validation<P> p, Validation<Q> q, Validation<R> r, Validation<S> s, Validation<T> t) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RESULT> Validation<RESULT> apply(Function21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RESULT> fun, Validation<A> a, Validation<B> b, Validation<C> c, Validation<D> d, Validation<E> e, Validation<F> f, Validation<G> g, Validation<H> h, Validation<I> i, Validation<J> j, Validation<K> k, Validation<L> l, Validation<M> m, Validation<N> n, Validation<O> o, Validation<P> p, Validation<Q> q, Validation<R> r, Validation<S> s, Validation<T> t, Validation<U> u) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RESULT> Validation<RESULT> apply(Function22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RESULT> fun, Validation<A> a, Validation<B> b, Validation<C> c, Validation<D> d, Validation<E> e, Validation<F> f, Validation<G> g, Validation<H> h, Validation<I> i, Validation<J> j, Validation<K> k, Validation<L> l, Validation<M> m, Validation<N> n, Validation<O> o, Validation<P> p, Validation<Q> q, Validation<R> r, Validation<S> s, Validation<T> t, Validation<U> u, Validation<V> v) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u).apply(v);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RESULT> Validation<RESULT> apply(Function23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RESULT> fun, Validation<A> a, Validation<B> b, Validation<C> c, Validation<D> d, Validation<E> e, Validation<F> f, Validation<G> g, Validation<H> h, Validation<I> i, Validation<J> j, Validation<K> k, Validation<L> l, Validation<M> m, Validation<N> n, Validation<O> o, Validation<P> p, Validation<Q> q, Validation<R> r, Validation<S> s, Validation<T> t, Validation<U> u, Validation<V> v, Validation<W> w) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u).apply(v).apply(w);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, RESULT> Validation<RESULT> apply(Function24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, RESULT> fun, Validation<A> a, Validation<B> b, Validation<C> c, Validation<D> d, Validation<E> e, Validation<F> f, Validation<G> g, Validation<H> h, Validation<I> i, Validation<J> j, Validation<K> k, Validation<L> l, Validation<M> m, Validation<N> n, Validation<O> o, Validation<P> p, Validation<Q> q, Validation<R> r, Validation<S> s, Validation<T> t, Validation<U> u, Validation<V> v, Validation<W> w, Validation<X> x) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u).apply(v).apply(w).apply(x);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, RESULT> Validation<RESULT> apply(Function25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, RESULT> fun, Validation<A> a, Validation<B> b, Validation<C> c, Validation<D> d, Validation<E> e, Validation<F> f, Validation<G> g, Validation<H> h, Validation<I> i, Validation<J> j, Validation<K> k, Validation<L> l, Validation<M> m, Validation<N> n, Validation<O> o, Validation<P> p, Validation<Q> q, Validation<R> r, Validation<S> s, Validation<T> t, Validation<U> u, Validation<V> v, Validation<W> w, Validation<X> x, Validation<Y> y) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u).apply(v).apply(w).apply(x).apply(y);
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RESULT> Validation<RESULT> apply(Function26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RESULT> fun, Validation<A> a, Validation<B> b, Validation<C> c, Validation<D> d, Validation<E> e, Validation<F> f, Validation<G> g, Validation<H> h, Validation<I> i, Validation<J> j, Validation<K> k, Validation<L> l, Validation<M> m, Validation<N> n, Validation<O> o, Validation<P> p, Validation<Q> q, Validation<R> r, Validation<S> s, Validation<T> t, Validation<U> u, Validation<V> v, Validation<W> w, Validation<X> x, Validation<Y> y, Validation<Z> z) {
		return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u).apply(v).apply(w).apply(x).apply(y).apply(z);
	}

}
