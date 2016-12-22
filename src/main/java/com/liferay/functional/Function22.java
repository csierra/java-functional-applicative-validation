package com.liferay.functional;

import java.util.function.Function;

/**
* @generated
*/
@FunctionalInterface
public interface Function22<A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,RESULT> {
    
    public RESULT apply(A a,B b,C c,D d,E e,F f,G g,H h,I i,J j,K k,L l,M m,N n,O o,P p,Q q,R r,S s,T t,U u,V v);
    
    default public Function<A,Function<B,Function<C,Function<D,Function<E,Function<F,Function<G,Function<H,Function<I,Function<J,Function<K,Function<L,Function<M,Function<N,Function<O,Function<P,Function<Q,Function<R,Function<S,Function<T,Function<U,Function<V,RESULT>>>>>>>>>>>>>>>>>>>>>> curried() {
        return a -> b -> c -> d -> e -> f -> g -> h -> i -> j -> k -> l -> m -> n -> o -> p -> q -> r -> s -> t -> u -> v -> apply(a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v);
    }
}
