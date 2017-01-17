# Typesafe Validation Library for Java 8

This is a typesafe validation library with an aim on composition, reuse and safety. Tools are provided to create, compose or enrich validator while maintaining type safety. 

## Getting started

The project is still in _alpha_ state and it is not yet published as an artifact. First clone this repo and issue `./gradlew install` from the root of the project to install it in your local maven repository. 

To use it in your maven project add
```
<dependency>
  <groupId>com.liferay.functional</groupiId>
  <artifactId>java8-functional</artifactId>
  <version>0.0.1.SNAPSHOT</version>
</dependency>
```

or in your gradle project:
```
compile 'com.liferay.functional:java8-functional:0.0.1.SNAPSHOT'
```

the project coordinates are very likely to change in the future.

## First validation

The core of the library is the `Validation<T, F>` interface. `T` parameter is the type contained in the validation while `F` parameter corresponds to the type of the failure. `F` is further constrained to implement `Monoid<F>` since one of the features of the library is to collect as much validation errors as possible. 

`Validation` has some methods to check for the success of failure of the validation. Just like with `Optional<T>` you can use `isSuccess()` to test for a success and then use either `get()` or `failures()` to collect the proper result. Using the incorrect method will lead to an `IllegalStateException`. You have been warned :-)

Nevertheless, to get the most from `Validation`, it is best to use its functor, applicative and monadic nature to combine it in different ways. 

`Validator<T, R, F>` interface is also provided to represent any `Function<T, Validation<R, F>>`. `Validator` is also functor, applicative and monad. `Validator.predicate` is a helper function that helps in the creation of `Validator` from `Predicate`:
```
Validator<String, String, Fail> notEmpty = predicate(
    (String s) -> s.length() > 0, s -> new Fail("must not be empty"));
```

and we can use this validator with any `String`:

```
Validation<String, Fail> failure = notEmpty.validate("");

Validation<String, Fail> success = notEmpty.validate("notEmpty");
```

new `Validator`s can be created from the composition of other Validators:
```
Validator<String, String, Fail> longerThan10 = predicate(
    (String s) -> s.length() > 10, s -> new Fail("must be longer than 10"));
    
    
Validator<String, String, String> composed = notBlank.compose(longerThan10);
```

we can create _safe casts_ by creating a validator that goes from one type to another:

```
Validator<String, Integer, Fail> safeToInt = input -> {
    try {
        return Validation.just(Integer.parseInt(input));
    }
    catch (NumberFormatException nfe) {
        return new Validation.Failure(new Fail("must be a number"));
    }
}

Validator<Integer, Integer, Fail> positive = predicate(
    i -> i > 0, i -> new Fail("must be positive"));
    
//Composer is a util to compose validator in a type safe way

Composer.compose(notBlank, safeToInt, positive).validate("1");
```

we can also compose validators using `and`:

```
Validator<Integer, Integer, Fail> greaterThan(int min) { ... }
Validator<Integer, Integer, Fail> lowerThan(int max) { ... }

Validator<Integer, Integer, Fail> between(int min, int max) {
    return greaterThan(min).and(lowerThan(max))
}
```

### Functional API

You can use `map(Function<T, R> fun)` to further modify the content of a successful computation. Just like with `Optional` a failure will just do nothing in this case:

``` 
Validation<Integer, Fail> success;

Validation<Integer, Fail> plusOne = success.map(i -> i + 1);
```
















