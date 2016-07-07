package com.epitrack.guardioes.view;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.base.dialog.BaseDialogFragment;

public class LanguageDialog extends BaseDialogFragment implements AdapterView.OnItemClickListener {

    public static final String TAG = LanguageDialog.class.getSimpleName();

    private ListView listView;

    private ILanguage listener;

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setCancelable(false);
    }

    @Override
    public int getLayout() {
        return R.layout.language_dialog;
    }

    @Override
    public void findView(final View view) {
        super.findView(view);

        listView = (ListView) view.findViewById(R.id.list_view);

        listView.setAdapter(new LanguageAdapter(getActivity(), getActivity().getResources().getStringArray(R.array.language_array)));
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {

        dismiss();

        listener.onLanguage(Language.getBy(position + 1));
    }

    public LanguageDialog setListener(final ILanguage listener) {
        this.listener = listener;

        return this;
    }

    public interface ILanguage {

        void onLanguage(Language language);
    }
}
