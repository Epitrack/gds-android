package com.epitrack.guardioes.view.account;

import android.os.Bundle;

/**
 * @author Igor Morais
 */
public interface AccountListener {

    void onCancel();

    void onError();

    void onSuccess(final Bundle bundle);
}
