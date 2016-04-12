package com.epitrack.guardioes.request.old;

/**
 * @author Igor Morais
 */
public interface RequestListener<T> {

    void onStart();

    void onError(Exception e);

    void onSuccess(T type);
}
