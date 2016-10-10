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

/**
 * @author Carlos Sierra Andrés
 */
public class Compose<T, AP1, AP2>
	implements Applicative<Compose<?, AP1, AP2>, T> {

	private final Applicative<AP1, T> _ap1;
	private final Applicative<AP2, T> _ap2;

	public Compose(Applicative<AP1, T> ap1, Applicative<AP2, T> ap2) {
		_ap1 = ap1;
		_ap2 = ap2;
	}

	@Override
	public <S> Applicative<Compose<?, AP1, AP2>, S> fmap(Function1<T, S> fun) {
		Applicative<AP1, S> fmap1 = _ap1.fmap(fun);
		Applicative<AP2, S> fmap2 = _ap2.fmap(fun);

		return null;
	}

	@Override
	public <S, U> Applicative<Compose<?, AP1, AP2>, U> apply(Applicative<Compose<?, AP1, AP2>, S> ap) {
		return null;
	}

	@Override
	public <S> Applicative<Compose<?, AP1, AP2>, S> pure(S s) {
		return null;
	}
}
