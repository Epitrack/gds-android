package com.epitrack.guardioes.manager;

/**
 * @author Igor Morais
 */
public interface AsyncListener<T> {

    void onStart();

    void onEnd(T entity);
}
