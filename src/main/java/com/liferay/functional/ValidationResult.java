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

import javaslang.Function1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Carlos Sierra Andrés
 */
public interface ValidationResult<T> extends DefaultValidationResult<T> {

	public boolean isPresent();

	public T get();

	public List<String> failures();

	class Success<T> implements ValidationResult<T> {

		private T _t;

		public Success(T t) {
			_t = t;
		}

		@Override
		public <S> Applicative<ValidationResult<?>, S> fmap(
			Function1<T, S> fun) {

			return new Success<>(fun.apply(_t));
		}

		@Override
		public <S, U> Applicative<ValidationResult<?>, U> apply(
			Applicative<ValidationResult<?>, S> ap) {

			return ap.fmap((Function1<S, U>)_t);
		}

		@Override
		public String toString() {
			return "Success {" + _t + '}';
		}


		@Override
		public <S> Monad<ValidationResult<?>, S> flatMap(
			Function1<T, Monad<ValidationResult<?>, S>> fun) {

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
		public List<String> failures() {
			return null;
		}
	}

	class Failure<T> implements ValidationResult<T> {

		private List<String> _reasons;

		public Failure(List<String> reasons) {
			_reasons = reasons;
		}

		@Override
		public <S> Applicative<ValidationResult<?>, S> fmap(
			Function1<T, S> fun) {

			return (ValidationResult)this;
		}

		@Override
		public <S, U> Applicative<ValidationResult<?>, U> apply(
			Applicative<ValidationResult<?>, S> ap) {

			if (ap instanceof Failure) {
				Failure failure = (Failure) ap;

				ArrayList<String> reasons = new ArrayList<>();

				reasons.addAll(this._reasons);
				reasons.addAll(failure._reasons);

				return new Failure<>(reasons);
			}

			else {
				return (ValidationResult)this;
			}
		}

		@Override
		public String toString() {
			return "Failure {" + "Reasons: " + _reasons + '}';
		}

		@Override
		public <S> Monad<ValidationResult<?>, S> flatMap(
			Function1<T, Monad<ValidationResult<?>, S>> fun) {

			return (ValidationResult<S>)this;
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
		public List<String> failures() {
			return _reasons;
		}
	}

	static void main(String[] args) {
		ValidationResult<MyClass> carlos =
			(ValidationResult<MyClass>)Applicative.lift(
				MyClass::new,
					new Failure<>(Collections.singletonList("Age must be a number")),
					new Failure<>(Collections.singletonList("DNI must have 9 digits")));

		System.out.println(carlos);
	}



}
