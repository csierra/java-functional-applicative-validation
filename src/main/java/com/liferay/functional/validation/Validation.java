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

import com.liferay.functional.CheckedFunction10;
import com.liferay.functional.CheckedFunction11;
import com.liferay.functional.CheckedFunction12;
import com.liferay.functional.CheckedFunction13;
import com.liferay.functional.CheckedFunction14;
import com.liferay.functional.CheckedFunction15;
import com.liferay.functional.CheckedFunction16;
import com.liferay.functional.CheckedFunction17;
import com.liferay.functional.CheckedFunction18;
import com.liferay.functional.CheckedFunction19;
import com.liferay.functional.CheckedFunction2;
import com.liferay.functional.CheckedFunction20;
import com.liferay.functional.CheckedFunction21;
import com.liferay.functional.CheckedFunction22;
import com.liferay.functional.CheckedFunction23;
import com.liferay.functional.CheckedFunction24;
import com.liferay.functional.CheckedFunction25;
import com.liferay.functional.CheckedFunction26;
import com.liferay.functional.CheckedFunction3;
import com.liferay.functional.CheckedFunction4;
import com.liferay.functional.CheckedFunction5;
import com.liferay.functional.CheckedFunction6;
import com.liferay.functional.CheckedFunction7;
import com.liferay.functional.CheckedFunction8;
import com.liferay.functional.CheckedFunction9;
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

	public <S> Validation<S, F> applyTo(Validation<Function<T, S>, F> ap);

	public <S> Validation<S, F> flatApplyTo(
		Validation<Function<T, Validation<S, F>>, F> applicative);

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
		public <S> Validation<S, F> applyTo(
			Validation<Function<T, S>, F> applicative) {

			return applicative.flatMap(this::map);
		}

		@Override
		public <S> Validation<S, F> flatApplyTo(
			Validation<Function<T, Validation<S, F>>, F> applicative) {

			return applicative.flatMap(this::flatMap);
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

		@Override
		public <S> Validation<S, F> applyTo(Validation<Function<T, S>, F> ap) {
			if (ap instanceof Failure) {
				return new Failure<>((F)_failure.mappend(((Failure) ap)._failure));
			}

			else {
				return (Validation)this;
			}
		}

		@Override
		public <S> Validation<S, F> flatApplyTo(
			Validation<Function<T, Validation<S, F>>, F> ap) {

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

	static <T, F extends Monoid<F>> Validation<T, F> just(T t) {
		return new Success<>(t);
	}

	public static <A, B, RESULT, FAILURE extends Monoid<FAILURE>> Function2<A, B, Validation<RESULT, FAILURE>> wrap(CheckedFunction2<A, B, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b) -> {
			try {
				return new Success<>(fun.apply(a, b));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, RESULT> Function2<A, B, RESULT> ignore(CheckedFunction2<A, B, RESULT> fun) {
		return (a, b) -> {
			try {
				return fun.apply(a, b);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function2<A, B, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b) {
		return b.applyTo(a.applyTo(just((A aa) -> fun.curried().apply(aa))));
	}

	public static <A, B, C, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function3<A, B, C, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c) {
		return c.applyTo(Validation.apply((A aa, B bb) -> fun.curried().apply(aa).apply(bb), a, b));
	}

	public static <A, B, C, D, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function4<A, B, C, D, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d) {
		return d.applyTo(Validation.apply((A aa, B bb, C cc) -> fun.curried().apply(aa).apply(bb).apply(cc), a, b, c));
	}

	public static <A, B, C, D, E, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function5<A, B, C, D, E, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e) {
		return e.applyTo(Validation.apply((A aa, B bb, C cc, D dd) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd), a, b, c, d));
	}

	public static <A, B, C, D, E, F, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function6<A, B, C, D, E, F, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f) {
		return f.applyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee), a, b, c, d, e));
	}

	public static <A, B, C, D, E, F, G, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function7<A, B, C, D, E, F, G, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g) {
		return g.applyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff), a, b, c, d, e, f));
	}

	public static <A, B, C, D, E, F, G, H, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function8<A, B, C, D, E, F, G, H, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h) {
		return h.applyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg), a, b, c, d, e, f, g));
	}

	public static <A, B, C, D, E, F, G, H, I, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function9<A, B, C, D, E, F, G, H, I, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i) {
		return i.applyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh), a, b, c, d, e, f, g, h));
	}

	public static <A, B, C, D, E, F, G, H, I, J, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function10<A, B, C, D, E, F, G, H, I, J, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j) {
		return j.applyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii), a, b, c, d, e, f, g, h, i));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function11<A, B, C, D, E, F, G, H, I, J, K, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k) {
		return k.applyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj), a, b, c, d, e, f, g, h, i, j));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function12<A, B, C, D, E, F, G, H, I, J, K, L, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l) {
		return l.applyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk), a, b, c, d, e, f, g, h, i, j, k));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m) {
		return m.applyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll), a, b, c, d, e, f, g, h, i, j, k, l));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n) {
		return n.applyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm), a, b, c, d, e, f, g, h, i, j, k, l, m));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o) {
		return o.applyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn), a, b, c, d, e, f, g, h, i, j, k, l, m, n));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p) {
		return p.applyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q) {
		return q.applyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r) {
		return r.applyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s) {
		return s.applyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t) {
		return t.applyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t, Validation<U, FAILURE> u) {
		return u.applyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss, T tt) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss).apply(tt), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t, Validation<U, FAILURE> u, Validation<V, FAILURE> v) {
		return v.applyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss, T tt, U uu) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss).apply(tt).apply(uu), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t, Validation<U, FAILURE> u, Validation<V, FAILURE> v, Validation<W, FAILURE> w) {
		return w.applyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss, T tt, U uu, V vv) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss).apply(tt).apply(uu).apply(vv), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t, Validation<U, FAILURE> u, Validation<V, FAILURE> v, Validation<W, FAILURE> w, Validation<X, FAILURE> x) {
		return x.applyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss, T tt, U uu, V vv, W ww) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss).apply(tt).apply(uu).apply(vv).apply(ww), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t, Validation<U, FAILURE> u, Validation<V, FAILURE> v, Validation<W, FAILURE> w, Validation<X, FAILURE> x, Validation<Y, FAILURE> y) {
		return y.applyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss, T tt, U uu, V vv, W ww, X xx) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss).apply(tt).apply(uu).apply(vv).apply(ww).apply(xx), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> apply(Function26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RESULT> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t, Validation<U, FAILURE> u, Validation<V, FAILURE> v, Validation<W, FAILURE> w, Validation<X, FAILURE> x, Validation<Y, FAILURE> y, Validation<Z, FAILURE> z) {
		return z.applyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss, T tt, U uu, V vv, W ww, X xx, Y yy) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss).apply(tt).apply(uu).apply(vv).apply(ww).apply(xx).apply(yy), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y));
	}

	/*public static <A, B, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function2<A, B, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b) {
		return b.flatApplyTo(a.applyTo(just(fun.curried())));
	}*/

	public static <A, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function<A, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a) {
		Validation<Function<A, Validation<RESULT, FAILURE>>, FAILURE> just = just(fun);

		Validation<RESULT, FAILURE> validation = a.flatApplyTo(just);

		return validation;
	}

	public static <A, B, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(
		Function2<A, B, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b) {

		return b.flatApplyTo(a.applyTo(just((A aa) -> fun.curried().apply(aa))));
	}

	public static <A, B, C, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function3<A, B, C, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c) {
		return c.flatApplyTo(Validation.apply((A aa, B bb) -> fun.curried().apply(aa).apply(bb), a, b));
	}

	public static <A, B, C, D, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function4<A, B, C, D, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d) {
		return d.flatApplyTo(Validation.apply((A aa, B bb, C cc) -> fun.curried().apply(aa).apply(bb).apply(cc), a, b, c));
	}

	public static <A, B, C, D, E, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function5<A, B, C, D, E, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e) {
		return e.flatApplyTo(Validation.apply((A aa, B bb, C cc, D dd) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd), a, b, c, d));
	}

	public static <A, B, C, D, E, F, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function6<A, B, C, D, E, F, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f) {
		return f.flatApplyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee), a, b, c, d, e));
	}

	public static <A, B, C, D, E, F, G, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function7<A, B, C, D, E, F, G, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g) {
		return g.flatApplyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff), a, b, c, d, e, f));
	}

	public static <A, B, C, D, E, F, G, H, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function8<A, B, C, D, E, F, G, H, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h) {
		return h.flatApplyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg), a, b, c, d, e, f, g));
	}

	public static <A, B, C, D, E, F, G, H, I, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function9<A, B, C, D, E, F, G, H, I, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i) {
		return i.flatApplyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh), a, b, c, d, e, f, g, h));
	}

	public static <A, B, C, D, E, F, G, H, I, J, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function10<A, B, C, D, E, F, G, H, I, J, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j) {
		return j.flatApplyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii), a, b, c, d, e, f, g, h, i));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function11<A, B, C, D, E, F, G, H, I, J, K, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k) {
		return k.flatApplyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj), a, b, c, d, e, f, g, h, i, j));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function12<A, B, C, D, E, F, G, H, I, J, K, L, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l) {
		return l.flatApplyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk), a, b, c, d, e, f, g, h, i, j, k));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m) {
		return m.flatApplyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll), a, b, c, d, e, f, g, h, i, j, k, l));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n) {
		return n.flatApplyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm), a, b, c, d, e, f, g, h, i, j, k, l, m));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o) {
		return o.flatApplyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn), a, b, c, d, e, f, g, h, i, j, k, l, m, n));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p) {
		return p.flatApplyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q) {
		return q.flatApplyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r) {
		return r.flatApplyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s) {
		return s.flatApplyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t) {
		return t.flatApplyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t, Validation<U, FAILURE> u) {
		return u.flatApplyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss, T tt) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss).apply(tt), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t, Validation<U, FAILURE> u, Validation<V, FAILURE> v) {
		return v.flatApplyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss, T tt, U uu) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss).apply(tt).apply(uu), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t, Validation<U, FAILURE> u, Validation<V, FAILURE> v, Validation<W, FAILURE> w) {
		return w.flatApplyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss, T tt, U uu, V vv) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss).apply(tt).apply(uu).apply(vv), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t, Validation<U, FAILURE> u, Validation<V, FAILURE> v, Validation<W, FAILURE> w, Validation<X, FAILURE> x) {
		return x.flatApplyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss, T tt, U uu, V vv, W ww) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss).apply(tt).apply(uu).apply(vv).apply(ww), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t, Validation<U, FAILURE> u, Validation<V, FAILURE> v, Validation<W, FAILURE> w, Validation<X, FAILURE> x, Validation<Y, FAILURE> y) {
		return y.flatApplyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss, T tt, U uu, V vv, W ww, X xx) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss).apply(tt).apply(uu).apply(vv).apply(ww).apply(xx), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t, Validation<U, FAILURE> u, Validation<V, FAILURE> v, Validation<W, FAILURE> w, Validation<X, FAILURE> x, Validation<Y, FAILURE> y, Validation<Z, FAILURE> z) {
		return z.flatApplyTo(Validation.apply((A aa, B bb, C cc, D dd, E ee, F ff, G gg, H hh, I ii, J jj, K kk, L ll, M mm, N nn, O oo, P pp, Q qq, R rr, S ss, T tt, U uu, V vv, W ww, X xx, Y yy) -> fun.curried().apply(aa).apply(bb).apply(cc).apply(dd).apply(ee).apply(ff).apply(gg).apply(hh).apply(ii).apply(jj).apply(kk).apply(ll).apply(mm).apply(nn).apply(oo).apply(pp).apply(qq).apply(rr).apply(ss).apply(tt).apply(uu).apply(vv).apply(ww).apply(xx).apply(yy), a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y));
	}

	/*public static <A, B, C, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function3<A, B, C, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c) {
		return c.flatApplyTo(b.applyTo(a.applyTo(just(fun.curried()))));
	}

	public static <A, B, C, D, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function4<A, B, C, D, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d) {
		return d.flatApplyTo(c.applyTo(b.applyTo(a.applyTo(just(fun.curried())))));
	}

	public static <A, B, C, D, E, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function5<A, B, C, D, E, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e) {
		return e.flatApplyTo(d.applyTo(c.applyTo(b.applyTo(a.applyTo(just(fun.curried()))))));
	}

	public static <A, B, C, D, E, F, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function6<A, B, C, D, E, F, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f) {
		return f.flatApplyTo(e.applyTo(d.applyTo(c.applyTo(b.applyTo(a.applyTo(just(fun.curried())))))));
	}

	public static <A, B, C, D, E, F, G, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function7<A, B, C, D, E, F, G, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g) {
		return g.flatApplyTo(f.applyTo(e.applyTo(d.applyTo(c.applyTo(b.applyTo(a.applyTo(just(fun.curried()))))))));
	}

	public static <A, B, C, D, E, F, G, H, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function8<A, B, C, D, E, F, G, H, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h) {
		return h.flatApplyTo(g.applyTo(f.applyTo(e.applyTo(d.applyTo(c.applyTo(b.applyTo(a.applyTo(just(fun.curried())))))))));
	}

	public static <A, B, C, D, E, F, G, H, I, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function9<A, B, C, D, E, F, G, H, I, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i) {
		return i.flatApplyTo(h.applyTo(g.applyTo(f.applyTo(e.applyTo(d.applyTo(c.applyTo(b.applyTo(a.applyTo(just(fun.curried()))))))))));
	}

	public static <A, B, C, D, E, F, G, H, I, J, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function10<A, B, C, D, E, F, G, H, I, J, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j) {
		return j.flatApplyTo(i.applyTo(h.applyTo(g.applyTo(f.applyTo(e.applyTo(d.applyTo(c.applyTo(b.applyTo(a.applyTo(just(fun.curried())))))))))));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function11<A, B, C, D, E, F, G, H, I, J, K, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k) {
		return k.flatApplyTo(j.applyTo(i.applyTo(h.applyTo(g.applyTo(f.applyTo(e.applyTo(d.applyTo(c.applyTo(b.applyTo(a.applyTo(just(fun.curried()))))))))))));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function12<A, B, C, D, E, F, G, H, I, J, K, L, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l) {
		return l.flatApplyTo(k.applyTo(j.applyTo(i.applyTo(h.applyTo(g.applyTo(f.applyTo(e.applyTo(d.applyTo(c.applyTo(b.applyTo(a.applyTo(just(fun.curried())))))))))))));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m) {
		return m.flatApplyTo(l.applyTo(k.applyTo(j.applyTo(i.applyTo(h.applyTo(g.applyTo(f.applyTo(e.applyTo(d.applyTo(c.applyTo(b.applyTo(a.applyTo(just(fun.curried()))))))))))))));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n) {
		return n.flatApplyTo(m.applyTo(l.applyTo(k.applyTo(j.applyTo(i.applyTo(h.applyTo(g.applyTo(f.applyTo(e.applyTo(d.applyTo(c.applyTo(b.applyTo(a.applyTo(just(fun.curried())))))))))))))));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o) {
		return o.flatApplyTo(n.applyTo(m.applyTo(l.applyTo(k.applyTo(j.applyTo(i.applyTo(h.applyTo(g.applyTo(f.applyTo(e.applyTo(d.applyTo(c.applyTo(b.applyTo(a.applyTo(just(fun.curried()))))))))))))))));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p) {
		return p.flatApplyTo(o.applyTo(n.applyTo(m.applyTo(l.applyTo(k.applyTo(j.applyTo(i.applyTo(h.applyTo(g.applyTo(f.applyTo(e.applyTo(d.applyTo(c.applyTo(b.applyTo(a.applyTo(just(fun.curried())))))))))))))))));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q) {
		return q.flatApplyTo(p.applyTo(o.applyTo(n.applyTo(m.applyTo(l.applyTo(k.applyTo(j.applyTo(i.applyTo(h.applyTo(g.applyTo(f.applyTo(e.applyTo(d.applyTo(c.applyTo(b.applyTo(a.applyTo(just(fun.curried()))))))))))))))))));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r) {
		return r.flatApplyTo(q.applyTo(p.applyTo(o.applyTo(n.applyTo(m.applyTo(l.applyTo(k.applyTo(j.applyTo(i.applyTo(h.applyTo(g.applyTo(f.applyTo(e.applyTo(d.applyTo(c.applyTo(b.applyTo(a.applyTo(just(fun.curried())))))))))))))))))));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s) {
		return s.flatApplyTo(r.applyTo(q.applyTo(p.applyTo(o.applyTo(n.applyTo(m.applyTo(l.applyTo(k.applyTo(j.applyTo(i.applyTo(h.applyTo(g.applyTo(f.applyTo(e.applyTo(d.applyTo(c.applyTo(b.applyTo(a.applyTo(just(fun.curried()))))))))))))))))))));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t) {
		return t.flatApplyTo(s.applyTo(r.applyTo(q.applyTo(p.applyTo(o.applyTo(n.applyTo(m.applyTo(l.applyTo(k.applyTo(j.applyTo(i.applyTo(h.applyTo(g.applyTo(f.applyTo(e.applyTo(d.applyTo(c.applyTo(b.applyTo(a.applyTo(just(fun.curried())))))))))))))))))))));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t, Validation<U, FAILURE> u) {
		return u.flatApplyTo(t.applyTo(s.applyTo(r.applyTo(q.applyTo(p.applyTo(o.applyTo(n.applyTo(m.applyTo(l.applyTo(k.applyTo(j.applyTo(i.applyTo(h.applyTo(g.applyTo(f.applyTo(e.applyTo(d.applyTo(c.applyTo(b.applyTo(a.applyTo(just(fun.curried()))))))))))))))))))))));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t, Validation<U, FAILURE> u, Validation<V, FAILURE> v) {
		return v.flatApplyTo(u.applyTo(t.applyTo(s.applyTo(r.applyTo(q.applyTo(p.applyTo(o.applyTo(n.applyTo(m.applyTo(l.applyTo(k.applyTo(j.applyTo(i.applyTo(h.applyTo(g.applyTo(f.applyTo(e.applyTo(d.applyTo(c.applyTo(b.applyTo(a.applyTo(just(fun.curried())))))))))))))))))))))));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t, Validation<U, FAILURE> u, Validation<V, FAILURE> v, Validation<W, FAILURE> w) {
		return w.flatApplyTo(v.applyTo(u.applyTo(t.applyTo(s.applyTo(r.applyTo(q.applyTo(p.applyTo(o.applyTo(n.applyTo(m.applyTo(l.applyTo(k.applyTo(j.applyTo(i.applyTo(h.applyTo(g.applyTo(f.applyTo(e.applyTo(d.applyTo(c.applyTo(b.applyTo(a.applyTo(just(fun.curried()))))))))))))))))))))))));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t, Validation<U, FAILURE> u, Validation<V, FAILURE> v, Validation<W, FAILURE> w, Validation<X, FAILURE> x) {
		return x.flatApplyTo(w.applyTo(v.applyTo(u.applyTo(t.applyTo(s.applyTo(r.applyTo(q.applyTo(p.applyTo(o.applyTo(n.applyTo(m.applyTo(l.applyTo(k.applyTo(j.applyTo(i.applyTo(h.applyTo(g.applyTo(f.applyTo(e.applyTo(d.applyTo(c.applyTo(b.applyTo(a.applyTo(just(fun.curried())))))))))))))))))))))))));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t, Validation<U, FAILURE> u, Validation<V, FAILURE> v, Validation<W, FAILURE> w, Validation<X, FAILURE> x, Validation<Y, FAILURE> y) {
		return y.flatApplyTo(x.applyTo(w.applyTo(v.applyTo(u.applyTo(t.applyTo(s.applyTo(r.applyTo(q.applyTo(p.applyTo(o.applyTo(n.applyTo(m.applyTo(l.applyTo(k.applyTo(j.applyTo(i.applyTo(h.applyTo(g.applyTo(f.applyTo(e.applyTo(d.applyTo(c.applyTo(b.applyTo(a.applyTo(just(fun.curried()))))))))))))))))))))))))));
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RESULT, FAILURE extends Monoid<FAILURE>> Validation<RESULT, FAILURE> flatApply(Function26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, Validation<RESULT, FAILURE>> fun, Validation<A, FAILURE> a, Validation<B, FAILURE> b, Validation<C, FAILURE> c, Validation<D, FAILURE> d, Validation<E, FAILURE> e, Validation<F, FAILURE> f, Validation<G, FAILURE> g, Validation<H, FAILURE> h, Validation<I, FAILURE> i, Validation<J, FAILURE> j, Validation<K, FAILURE> k, Validation<L, FAILURE> l, Validation<M, FAILURE> m, Validation<N, FAILURE> n, Validation<O, FAILURE> o, Validation<P, FAILURE> p, Validation<Q, FAILURE> q, Validation<R, FAILURE> r, Validation<S, FAILURE> s, Validation<T, FAILURE> t, Validation<U, FAILURE> u, Validation<V, FAILURE> v, Validation<W, FAILURE> w, Validation<X, FAILURE> x, Validation<Y, FAILURE> y, Validation<Z, FAILURE> z) {
		return z.flatApplyTo(y.applyTo(x.applyTo(w.applyTo(v.applyTo(u.applyTo(t.applyTo(s.applyTo(r.applyTo(q.applyTo(p.applyTo(o.applyTo(n.applyTo(m.applyTo(l.applyTo(k.applyTo(j.applyTo(i.applyTo(h.applyTo(g.applyTo(f.applyTo(e.applyTo(d.applyTo(c.applyTo(b.applyTo(a.applyTo(just(fun.curried())))))))))))))))))))))))))));
		} */

	public static <A, B, C, RESULT, FAILURE extends Monoid<FAILURE>> Function3<A, B, C, Validation<RESULT, FAILURE>> wrap(CheckedFunction3<A, B, C, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c) -> {
			try {
				return new Success<>(fun.apply(a, b, c));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, RESULT> Function3<A, B, C, RESULT> ignore(CheckedFunction3<A, B, C, RESULT> fun) {
		return (a, b, c) -> {
			try {
				return fun.apply(a, b, c);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, RESULT, FAILURE extends Monoid<FAILURE>> Function4<A, B, C, D, Validation<RESULT, FAILURE>> wrap(CheckedFunction4<A, B, C, D, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, RESULT> Function4<A, B, C, D, RESULT> ignore(CheckedFunction4<A, B, C, D, RESULT> fun) {
		return (a, b, c, d) -> {
			try {
				return fun.apply(a, b, c, d);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, RESULT, FAILURE extends Monoid<FAILURE>> Function5<A, B, C, D, E, Validation<RESULT, FAILURE>> wrap(CheckedFunction5<A, B, C, D, E, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, RESULT> Function5<A, B, C, D, E, RESULT> ignore(CheckedFunction5<A, B, C, D, E, RESULT> fun) {
		return (a, b, c, d, e) -> {
			try {
				return fun.apply(a, b, c, d, e);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, RESULT, FAILURE extends Monoid<FAILURE>> Function6<A, B, C, D, E, F, Validation<RESULT, FAILURE>> wrap(CheckedFunction6<A, B, C, D, E, F, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, RESULT> Function6<A, B, C, D, E, F, RESULT> ignore(CheckedFunction6<A, B, C, D, E, F, RESULT> fun) {
		return (a, b, c, d, e, f) -> {
			try {
				return fun.apply(a, b, c, d, e, f);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, RESULT, FAILURE extends Monoid<FAILURE>> Function7<A, B, C, D, E, F, G, Validation<RESULT, FAILURE>> wrap(CheckedFunction7<A, B, C, D, E, F, G, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, RESULT> Function7<A, B, C, D, E, F, G, RESULT> ignore(CheckedFunction7<A, B, C, D, E, F, G, RESULT> fun) {
		return (a, b, c, d, e, f, g) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, RESULT, FAILURE extends Monoid<FAILURE>> Function8<A, B, C, D, E, F, G, H, Validation<RESULT, FAILURE>> wrap(CheckedFunction8<A, B, C, D, E, F, G, H, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, RESULT> Function8<A, B, C, D, E, F, G, H, RESULT> ignore(CheckedFunction8<A, B, C, D, E, F, G, H, RESULT> fun) {
		return (a, b, c, d, e, f, g, h) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, RESULT, FAILURE extends Monoid<FAILURE>> Function9<A, B, C, D, E, F, G, H, I, Validation<RESULT, FAILURE>> wrap(CheckedFunction9<A, B, C, D, E, F, G, H, I, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, RESULT> Function9<A, B, C, D, E, F, G, H, I, RESULT> ignore(CheckedFunction9<A, B, C, D, E, F, G, H, I, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, RESULT, FAILURE extends Monoid<FAILURE>> Function10<A, B, C, D, E, F, G, H, I, J, Validation<RESULT, FAILURE>> wrap(CheckedFunction10<A, B, C, D, E, F, G, H, I, J, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i, j) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i, j));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, RESULT> Function10<A, B, C, D, E, F, G, H, I, J, RESULT> ignore(CheckedFunction10<A, B, C, D, E, F, G, H, I, J, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i, j) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i, j);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, RESULT, FAILURE extends Monoid<FAILURE>> Function11<A, B, C, D, E, F, G, H, I, J, K, Validation<RESULT, FAILURE>> wrap(CheckedFunction11<A, B, C, D, E, F, G, H, I, J, K, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i, j, k) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i, j, k));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, RESULT> Function11<A, B, C, D, E, F, G, H, I, J, K, RESULT> ignore(CheckedFunction11<A, B, C, D, E, F, G, H, I, J, K, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i, j, k) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i, j, k);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, RESULT, FAILURE extends Monoid<FAILURE>> Function12<A, B, C, D, E, F, G, H, I, J, K, L, Validation<RESULT, FAILURE>> wrap(CheckedFunction12<A, B, C, D, E, F, G, H, I, J, K, L, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i, j, k, l) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i, j, k, l));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, RESULT> Function12<A, B, C, D, E, F, G, H, I, J, K, L, RESULT> ignore(CheckedFunction12<A, B, C, D, E, F, G, H, I, J, K, L, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i, j, k, l) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i, j, k, l);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, RESULT, FAILURE extends Monoid<FAILURE>> Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, Validation<RESULT, FAILURE>> wrap(CheckedFunction13<A, B, C, D, E, F, G, H, I, J, K, L, M, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, RESULT> Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, RESULT> ignore(CheckedFunction13<A, B, C, D, E, F, G, H, I, J, K, L, M, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, RESULT, FAILURE extends Monoid<FAILURE>> Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, Validation<RESULT, FAILURE>> wrap(CheckedFunction14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, RESULT> Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, RESULT> ignore(CheckedFunction14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RESULT, FAILURE extends Monoid<FAILURE>> Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Validation<RESULT, FAILURE>> wrap(CheckedFunction15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RESULT> Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RESULT> ignore(CheckedFunction15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RESULT, FAILURE extends Monoid<FAILURE>> Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Validation<RESULT, FAILURE>> wrap(CheckedFunction16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RESULT> Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RESULT> ignore(CheckedFunction16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RESULT, FAILURE extends Monoid<FAILURE>> Function17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Validation<RESULT, FAILURE>> wrap(CheckedFunction17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RESULT> Function17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RESULT> ignore(CheckedFunction17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RESULT, FAILURE extends Monoid<FAILURE>> Function18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Validation<RESULT, FAILURE>> wrap(CheckedFunction18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RESULT> Function18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RESULT> ignore(CheckedFunction18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RESULT, FAILURE extends Monoid<FAILURE>> Function19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Validation<RESULT, FAILURE>> wrap(CheckedFunction19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RESULT> Function19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RESULT> ignore(CheckedFunction19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RESULT, FAILURE extends Monoid<FAILURE>> Function20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Validation<RESULT, FAILURE>> wrap(CheckedFunction20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RESULT> Function20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RESULT> ignore(CheckedFunction20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RESULT, FAILURE extends Monoid<FAILURE>> Function21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Validation<RESULT, FAILURE>> wrap(CheckedFunction21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RESULT> Function21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RESULT> ignore(CheckedFunction21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RESULT, FAILURE extends Monoid<FAILURE>> Function22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, Validation<RESULT, FAILURE>> wrap(CheckedFunction22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RESULT> Function22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RESULT> ignore(CheckedFunction22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RESULT, FAILURE extends Monoid<FAILURE>> Function23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, Validation<RESULT, FAILURE>> wrap(CheckedFunction23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RESULT> Function23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RESULT> ignore(CheckedFunction23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, RESULT, FAILURE extends Monoid<FAILURE>> Function24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Validation<RESULT, FAILURE>> wrap(CheckedFunction24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, RESULT> Function24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, RESULT> ignore(CheckedFunction24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, RESULT, FAILURE extends Monoid<FAILURE>> Function25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Validation<RESULT, FAILURE>> wrap(CheckedFunction25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, RESULT> Function25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, RESULT> ignore(CheckedFunction25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RESULT, FAILURE extends Monoid<FAILURE>> Function26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, Validation<RESULT, FAILURE>> wrap(CheckedFunction26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RESULT> fun, Function<Exception, FAILURE> error) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z) -> {
			try {
				return new Success<>(fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z));
			}
			catch (Exception ex) {
				return new Failure<>(error.apply(ex));
			}
		};
	}

	public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RESULT> Function26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RESULT> ignore(CheckedFunction26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RESULT> fun) {
		return (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z) -> {
			try {
				return fun.apply(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z);
			}
			catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

}
