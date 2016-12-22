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

import java.util.HashMap;
import java.util.Optional;

import static com.liferay.functional.Validator.apply;
import static com.liferay.functional.Validator.hasLength;
import static com.liferay.functional.Validator.isANumber;

/**
 * @author Carlos Sierra Andrés
 */
public interface StringProvider extends FieldProvider<String> {

    static <S> Validator<StringProvider, S> adapt(
        String name, Validator<Optional<String>, S> validator) {

        return (Validator<StringProvider, S>) FieldProvider.adapt(
            name, validator);
    }

    public static String met(String a, String b) {
        return a + b;
    }

    public class User {
        String name;
        int age;

        public User(int age, String name) {
            this.age = age;
            this.name = name;
        }

        @Override
        public String toString() {
            return "User{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
        }

    }

    public static void main(String[] args) {

        Validator<StringProvider, String> safeName = StringProvider.adapt(
            "name", Validator.notEmpty());

        Validator<Optional<String>, Integer> validator =
            Validator.<String>notEmpty().compose(isANumber).map(
                Integer::parseInt);

        Validator<StringProvider, Integer> safeAge = StringProvider.adapt(
            "age", validator);

        HashMap<String, String> map = new HashMap<>();
        map.put("name", "Carlos");
        map.put("age", "pepe");

        Validator<StringProvider, User> userValidator = apply(
            User::new, safeAge, safeName);

        Validation<User> validation = userValidator.validate(
            key -> Optional.ofNullable(map.get(key)));

        System.out.println(validation);
    }

}
