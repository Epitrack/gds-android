package com.epitrack.guardioes.view.welcome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.epitrack.guardioes.helper.Constants;

/**
 * @author Igor Morais
 */
public class WelcomePageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup viewGroup, final Bundle bundle) {

        final Welcome welcome = (Welcome) getArguments().getSerializable(Constants.Bundle.WELCOME);

        return inflater.inflate(welcome.getLayout(), viewGroup, false);
    }
}
