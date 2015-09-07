package com.epitrack.guardioes.view.tip;

import android.content.Intent;
import android.view.View;
import android.widget.ExpandableListView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.utility.Constants;
import com.epitrack.guardioes.view.BaseAppCompatActivity;
import com.epitrack.guardioes.view.IMenu;
import com.epitrack.guardioes.view.Phone;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * @author Igor Morais
 */
public class TipActivity extends BaseAppCompatActivity implements ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupClickListener {

    @Bind(R.id.expandable_list_view)
    ExpandableListView expandableListView;

    @Override
    protected void onCreate(final android.os.Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.tip);

        final Map<Integer, IMenu[]> menuMap = new HashMap<>();

        menuMap.put(Tip.TELEPHONE.getId(), Phone.values());

        expandableListView.setAdapter(new TipAdapter(Tip.values(), menuMap));

        expandableListView.setOnChildClickListener(this);
        expandableListView.setOnGroupClickListener(this);
    }

    @Override
    public boolean onChildClick(final ExpandableListView listView, final View view, final int groupPosition, final int childPosition, final long id) {

        final Phone phone = Phone.getBy(id);

        return false;
    }

    @Override
    public boolean onGroupClick(final ExpandableListView listView, final View view, final int groupPosition, final long id) {

        final Tip tip = Tip.getBy(id);

        if (tip.isActivity()) {

            final Intent intent = new Intent(this, tip.getMenuClass());

            if (tip == Tip.HOSPITAL || tip == Tip.PHARMACY) {
                intent.putExtra(Constants.Bundle.TIP, tip);
            }

            startActivity(intent);

            return true;
        }

        return false;
    }
}