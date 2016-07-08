package com.epitrack.guardioes.model;

/**
 * Created by IgorMorais on 6/12/15.
 */

import com.epitrack.guardioes.R;

public enum Symptom {

    FEVER (R.string.symptom_febre),
    FEVER1 (R.string.symptom_tosse),
    FEVER2 (R.string.symptom_nauseas),
    FEVER3 (R.string.symptom_manchas),
    FEVER4 (R.string.symptom_dorjuntas),
    FEVER5 (R.string.symptom_diarreia),
    FEVER6 (R.string.symptom_dorcorpo),
    FEVER7 (R.string.symptom_sangramento),
    FEVER8 (R.string.symptom_dorcabeca),
    FEVER9 (R.string.symptom_olhosvermelhos),
    FEVER10 (R.string.symptom_coceira),
    FEVER11 (R.string.symptom_contato),
    FEVER12 (R.string.symptom_procureiservicosaude),
    FEVER13 (R.string.symptom_estivefora);

    private final int name;

    Symptom(final int name) {
        this.name = name;
    }

    public int getName() {
        return name;
    }
}
