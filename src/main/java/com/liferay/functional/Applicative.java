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

/**
 * @author Carlos Sierra Andrés
 */
public interface Applicative<AP, T> extends Functor<T> {

	@Override
	<S> Applicative<AP, S> fmap(Function1<T, S> fun);

	<S, U> Applicative<AP, U> apply(Applicative<AP, S> ap);

	<S> Applicative<AP, S> pure(S s);

}
