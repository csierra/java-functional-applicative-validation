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

import java.util.function.Function;

/**
 * @author Carlos Sierra Andrés
 */
public interface Applicative<AP extends Applicative, T> {

    <S, U> Applicative<AP, U> apply(Applicative<AP, S> ap);

    <S> Applicative<AP, S> map(Function<T, S> fun);

    public static <AP extends Applicative<AP, ?>, A, B, RESULT> Applicative<AP, RESULT> combine(Function2<A, B, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b) {
        return a.map(fun.curried()).apply(b);
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, RESULT> Applicative<AP, RESULT> combine(Function3<A, B, C, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c) {
        return a.map(fun.curried()).apply(b).apply(c);
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, RESULT> Applicative<AP, RESULT> combine(Function4<A, B, C, D, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d) {
        return a.map(fun.curried()).apply(b).apply(c).apply(d);
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, RESULT> Applicative<AP, RESULT> combine(Function5<A, B, C, D, E, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e) {
        return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e);
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, RESULT> Applicative<AP, RESULT> combine(Function6<A, B, C, D, E, F, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f) {
        return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f);
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, RESULT> Applicative<AP, RESULT> combine(Function7<A, B, C, D, E, F, G, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g) {
        return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g);
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, RESULT> Applicative<AP, RESULT> combine(Function8<A, B, C, D, E, F, G, H, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h) {
        return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h);
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, RESULT> Applicative<AP, RESULT> combine(Function9<A, B, C, D, E, F, G, H, I, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i) {
        return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i);
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, J, RESULT> Applicative<AP, RESULT> combine(Function10<A, B, C, D, E, F, G, H, I, J, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i, Applicative<AP, J> j) {
        return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j);
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, J, K, RESULT> Applicative<AP, RESULT> combine(Function11<A, B, C, D, E, F, G, H, I, J, K, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i, Applicative<AP, J> j, Applicative<AP, K> k) {
        return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k);
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, J, K, L, RESULT> Applicative<AP, RESULT> combine(Function12<A, B, C, D, E, F, G, H, I, J, K, L, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i, Applicative<AP, J> j, Applicative<AP, K> k, Applicative<AP, L> l) {
        return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l);
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, J, K, L, M, RESULT> Applicative<AP, RESULT> combine(Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i, Applicative<AP, J> j, Applicative<AP, K> k, Applicative<AP, L> l, Applicative<AP, M> m) {
        return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m);
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, J, K, L, M, N, RESULT> Applicative<AP, RESULT> combine(Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i, Applicative<AP, J> j, Applicative<AP, K> k, Applicative<AP, L> l, Applicative<AP, M> m, Applicative<AP, N> n) {
        return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n);
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RESULT> Applicative<AP, RESULT> combine(Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i, Applicative<AP, J> j, Applicative<AP, K> k, Applicative<AP, L> l, Applicative<AP, M> m, Applicative<AP, N> n, Applicative<AP, O> o) {
        return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o);
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RESULT> Applicative<AP, RESULT> combine(Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i, Applicative<AP, J> j, Applicative<AP, K> k, Applicative<AP, L> l, Applicative<AP, M> m, Applicative<AP, N> n, Applicative<AP, O> o, Applicative<AP, P> p) {
        return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p);
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RESULT> Applicative<AP, RESULT> combine(Function17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i, Applicative<AP, J> j, Applicative<AP, K> k, Applicative<AP, L> l, Applicative<AP, M> m, Applicative<AP, N> n, Applicative<AP, O> o, Applicative<AP, P> p, Applicative<AP, Q> q) {
        return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q);
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RESULT> Applicative<AP, RESULT> combine(Function18<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i, Applicative<AP, J> j, Applicative<AP, K> k, Applicative<AP, L> l, Applicative<AP, M> m, Applicative<AP, N> n, Applicative<AP, O> o, Applicative<AP, P> p, Applicative<AP, Q> q, Applicative<AP, R> r) {
        return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r);
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RESULT> Applicative<AP, RESULT> combine(Function19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i, Applicative<AP, J> j, Applicative<AP, K> k, Applicative<AP, L> l, Applicative<AP, M> m, Applicative<AP, N> n, Applicative<AP, O> o, Applicative<AP, P> p, Applicative<AP, Q> q, Applicative<AP, R> r, Applicative<AP, S> s) {
        return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s);
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RESULT> Applicative<AP, RESULT> combine(Function20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i, Applicative<AP, J> j, Applicative<AP, K> k, Applicative<AP, L> l, Applicative<AP, M> m, Applicative<AP, N> n, Applicative<AP, O> o, Applicative<AP, P> p, Applicative<AP, Q> q, Applicative<AP, R> r, Applicative<AP, S> s, Applicative<AP, T> t) {
        return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t);
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RESULT> Applicative<AP, RESULT> combine(Function21<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i, Applicative<AP, J> j, Applicative<AP, K> k, Applicative<AP, L> l, Applicative<AP, M> m, Applicative<AP, N> n, Applicative<AP, O> o, Applicative<AP, P> p, Applicative<AP, Q> q, Applicative<AP, R> r, Applicative<AP, S> s, Applicative<AP, T> t, Applicative<AP, U> u) {
        return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u);
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RESULT> Applicative<AP, RESULT> combine(Function22<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i, Applicative<AP, J> j, Applicative<AP, K> k, Applicative<AP, L> l, Applicative<AP, M> m, Applicative<AP, N> n, Applicative<AP, O> o, Applicative<AP, P> p, Applicative<AP, Q> q, Applicative<AP, R> r, Applicative<AP, S> s, Applicative<AP, T> t, Applicative<AP, U> u, Applicative<AP, V> v) {
        return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u).apply(v);
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RESULT> Applicative<AP, RESULT> combine(Function23<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i, Applicative<AP, J> j, Applicative<AP, K> k, Applicative<AP, L> l, Applicative<AP, M> m, Applicative<AP, N> n, Applicative<AP, O> o, Applicative<AP, P> p, Applicative<AP, Q> q, Applicative<AP, R> r, Applicative<AP, S> s, Applicative<AP, T> t, Applicative<AP, U> u, Applicative<AP, V> v, Applicative<AP, W> w) {
        return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u).apply(v).apply(w);
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, RESULT> Applicative<AP, RESULT> combine(Function24<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i, Applicative<AP, J> j, Applicative<AP, K> k, Applicative<AP, L> l, Applicative<AP, M> m, Applicative<AP, N> n, Applicative<AP, O> o, Applicative<AP, P> p, Applicative<AP, Q> q, Applicative<AP, R> r, Applicative<AP, S> s, Applicative<AP, T> t, Applicative<AP, U> u, Applicative<AP, V> v, Applicative<AP, W> w, Applicative<AP, X> x) {
        return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u).apply(v).apply(w).apply(x);
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, RESULT> Applicative<AP, RESULT> combine(Function25<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i, Applicative<AP, J> j, Applicative<AP, K> k, Applicative<AP, L> l, Applicative<AP, M> m, Applicative<AP, N> n, Applicative<AP, O> o, Applicative<AP, P> p, Applicative<AP, Q> q, Applicative<AP, R> r, Applicative<AP, S> s, Applicative<AP, T> t, Applicative<AP, U> u, Applicative<AP, V> v, Applicative<AP, W> w, Applicative<AP, X> x, Applicative<AP, Y> y) {
        return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u).apply(v).apply(w).apply(x).apply(y);
    }

    public static <AP extends Applicative<AP, ?>, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RESULT> Applicative<AP, RESULT> combine(Function26<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, RESULT> fun, Applicative<AP, A> a, Applicative<AP, B> b, Applicative<AP, C> c, Applicative<AP, D> d, Applicative<AP, E> e, Applicative<AP, F> f, Applicative<AP, G> g, Applicative<AP, H> h, Applicative<AP, I> i, Applicative<AP, J> j, Applicative<AP, K> k, Applicative<AP, L> l, Applicative<AP, M> m, Applicative<AP, N> n, Applicative<AP, O> o, Applicative<AP, P> p, Applicative<AP, Q> q, Applicative<AP, R> r, Applicative<AP, S> s, Applicative<AP, T> t, Applicative<AP, U> u, Applicative<AP, V> v, Applicative<AP, W> w, Applicative<AP, X> x, Applicative<AP, Y> y, Applicative<AP, Z> z) {
        return a.map(fun.curried()).apply(b).apply(c).apply(d).apply(e).apply(f).apply(g).apply(h).apply(i).apply(j).apply(k).apply(l).apply(m).apply(n).apply(o).apply(p).apply(q).apply(r).apply(s).apply(t).apply(u).apply(v).apply(w).apply(x).apply(y).apply(z);
    }

}
