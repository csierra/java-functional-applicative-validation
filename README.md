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

It is possible to adapt an existing validator to work on a different model. Let's take our `between` and `notBlank` validators as example. If we want to use those validators to work on an existing model we can use `adapt`:

```
class User {
    String name;
	Integer age;

	public User(Integer age, String name) {
		this.age = age;
		this.name = name;
	}
	
    public Integer getAge() {
		return age;
	}

	public String getName() {
		return name;
	}
}

Validator<User, Integer, FieldFail> safeUserAge = between(8, 99).adapt(User::getAge, f -> new FieldFail("age", f));

Validator<User, String, FieldFail> safeUserName = notBlank.adapt(User::getName, f -> new FieldFail("name", f));

so now we have two validators that operate on User. We can further adapt those so they return `User` instead of their respective types using `partials`. We can also use `FieldFail.fromFail` to save some characters:

```
Validator<User, User, FieldFail> userValidator = Validator.partials(
    between(8, 99).adapt(User::getAge, fromFail("age")),
	notBlank.adapt(User::getName, fromFail("name"))
)
```

and now we can validate the user and collect either the result or the set of failures. 

```
Validation<User, FieldFail> validation = userValidator.validate(user);
```

If we don't have a user instance we can use the _Applicative Functor_ nature of `Validation` to create the user only when the fields pass validation.

```
Validation<User, Fail> validation = Validation.apply(
	User::new, 
	between(8, 99).validate(10),
	notBlank.validate("aName"));
);

### Field Providers 

Since it is a very common situation to get input data from `Map<String, String>` or `Map<String, Object>` like structures the library supports the concept of `FieldProvider`. This interface provides methods to work with validators more conveniently:

```
StringProvider sp;

Adaptor<Fail> adaptor = sp.getAdaptor(FieldFail::new); //Adapt the errors to contain field info

Validation<String, FieldFail> safeName = Adaptor.safeGet(
    adaptor, "name", 
	compose(required(), notBlank));
	
Validation<Integer, FieldFail> age = Adaptor.safeGet(
	adaptor, "age", 
	compose(required(), isANumber(), between(8, 99)));

```

`FieldProvider` and `Adaptor` provide a way to traverse nested structures using `Adaptor.getAdaptor(String fieldName)`. You can also use `Adaptor.focus(String ... fields)` to point to a nested structure. In case of invalid structure the error will be returned in the `Validation` type. 


### Forcing validation in business logic

One use case for the validation library would be to force the validation of the parameters of a function invocation. We can force this by using specific types for the parameters. This types can have `package private` constructor visibility and may only be accessed by a `public static` function that returns a `Validation<Type>`. By using this idiom we can decouple the business logic from the validation logic and still be sure that the invocation of the function is going to be safe (unless mischevious uses of reflection and such):

```

public class Service {

	public Result doSomething(Name name, Age age) {}
	
	static class Name {
		String _name;
		
		Name(String name) {
			_name = name;
		}
	}
	
	static class Age {
		int _age;
		
		Age(int age) {
			_age = age;
		}
	}
	
	public static Validation<Name, FieldFail> name(String name) {
		//apply all validations here
		return notBlank.
			validate(name).
			mapFailures(FieldFail.fromFail("name")).
			map(Name::new);
	}
	
	public static Validation<Age, FieldFail> name(int age) {
		//apply all validations here
		return between(0, 99).
			validate(age).
			mapFailures(FieldFail.fromFail("age")).
			map(Age::new);
	}
	
}

```

then we can invoke our service using: 

```

Service service = new Service();

Validation<Result, FieldFail> result = Validation.apply(
	service::doSomething, Service.name("carlos"), Service.age(38));

```

if we have unit tests in the package we can test the logic bypassing the validation step. 










