package com.epitrack.guardioes.view.account;

import com.epitrack.guardioes.model.User;

/**
 * @author Igor Morais
 */
public interface AccountListener {

    void onCancel();

    void onError();

    void onNotFound(User user);

    void onSuccess(User user);
}
