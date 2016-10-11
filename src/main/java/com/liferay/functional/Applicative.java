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
package com.liferay.functional;

import javaslang.Function1;
import javaslang.Function2;
import javaslang.Function3;
import javaslang.Function4;
import javaslang.Function5;
import javaslang.Function6;
import javaslang.Function7;
import javaslang.Function8;

/**
 * @author Carlos Sierra Andrés
 */
public interface Applicative<AP, T> extends Functor<T> {

	@Override
	<S> Applicative<AP, S> fmap(Function1<T, S> fun);

	<S, U> Applicative<AP, U> apply(Applicative<AP, S> ap);

	<S> Applicative<AP, S> pure(S s);

	public static <AP, A, T> Applicative<AP, T> lift(
		Function1<A, T> fun, Applicative<AP, A> ap) {

		return ap.pure(fun.curried()).apply(ap);
	}

	public static <AP, A, B, T> Applicative<AP, T> lift(
		Function2<A, B, T> fun, Applicative<AP, A> a, Applicative<AP, B> b) {

		return a.pure(fun.curried()).apply(a).apply(b);
	}

	public static <AP, A, B, C, T>  Applicative<AP, T> lift(
		Function3<A, B, C, T> fun, Applicative<AP, A> a, Applicative<AP, B> b,
		Applicative<AP, C> c) {

		return a.pure(fun.curried()).apply(a).apply(b).apply(c);
	}

	public static <AP, A, B, C, D, T> Applicative<AP, T> lift(
		Function4<A, B, C, D, T> fun, Applicative<AP, A> a,
		Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d) {

		return a.pure(fun.curried()).apply(a).apply(b).apply(c).apply(d);
	}

	public static <AP, A, B, C, D, E, T> Applicative<AP, T> lift(
		Function5<A, B, C, D, E, T> fun, Applicative<AP, A> a,
		Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d,
		Applicative<AP, E> e) {

		return a.pure(fun.curried()).
			apply(a).apply(b).apply(c).apply(d).apply(e);
	}

	public static <AP, A, B, C, D, E, F, T> Applicative<AP, T> lift(
		Function6<A, B, C, D, E, F, T> fun, Applicative<AP, A> a,
		Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d,
		Applicative<AP, E> e, Applicative<AP, F> f) {

		return a.pure(fun.curried()).
			apply(a).apply(b).apply(c).apply(d).apply(e).apply(f);
	}

	public static <AP, A, B, C, D, E, F, G, T> Applicative<AP, T> lift(
		Function7<A, B, C, D, E, F, G, T> fun, Applicative<AP, A> a,
		Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d,
		Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g) {

		return a.pure(fun.curried()).
			apply(a).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g);
	}

	public static <AP, A, B, C, D, E, F, G, H, T> Applicative<AP, T> lift(
		Function8<A, B, C, D, E, F, G, H, T> fun, Applicative<AP, A> a,
		Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d,
		Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g,
		Applicative<AP, H> h) {

		return a.pure(fun.curried()).
			apply(a).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).
			apply(h);
	}

}
