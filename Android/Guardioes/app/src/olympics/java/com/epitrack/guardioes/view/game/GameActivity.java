package com.epitrack.guardioes.view.game;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.manager.PrefManager;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.epitrack.guardioes.view.game.model.Phase;

/**
 * @author Igor Morais
 */
public class GameActivity extends BaseAppCompatActivity {

    private static final String TAG = GameActivity.class.getSimpleName();

    private static final String GAME_FRAGMENT = "game_fragment";
    private static final String PLAY_FRAGMENT = "play_fragment";
    private static final String ENERGY_FRAGMENT = "energy_fragment";

    private GameFragment gameFragment;
    private PlayFragment playFragment;
    private EnergyFragment energyFragment;

    private Phase phase;
    private User user;

    @Override
    protected void onCreate(@Nullable final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.game);

        getEnergyFragment().setEnergy(getUser().getEnergy());

        getFragmentManager().beginTransaction().hide(getPlayFragment()).commit();
    }

    private GameFragment getGameFragment() {

        if (gameFragment == null) {
            gameFragment = (GameFragment) getFragmentManager().findFragmentByTag(GAME_FRAGMENT);
        }

        return gameFragment;
    }

    private PlayFragment getPlayFragment() {

        if (playFragment == null) {
            playFragment = (PlayFragment) getFragmentManager().findFragmentByTag(PLAY_FRAGMENT);
        }

        return playFragment;
    }

    private EnergyFragment getEnergyFragment() {

        if (energyFragment == null) {
            energyFragment = (EnergyFragment) getFragmentManager().findFragmentByTag(ENERGY_FRAGMENT);
        }

        return energyFragment;
    }

    public User getUser() {

        if (user == null) {
            user = new PrefManager(this).get(Constants.Pref.USER, User.class);
        }

        return user;
    }

    public final Phase getPhase() {

        if (phase == null) {
            phase = (Phase) getIntent().getSerializableExtra(Constants.Bundle.PHASE);
        }

        return phase;
    }

    public void onCorrect(final int piece) {

        getPlayFragment().setPiece(piece);

        getFragmentManager().beginTransaction().hide(getGameFragment()).show(getPlayFragment()).commit();
    }
}
