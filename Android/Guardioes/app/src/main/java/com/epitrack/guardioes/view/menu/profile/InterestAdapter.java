package com.epitrack.guardioes.view.menu.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.request.base.Method;
import com.epitrack.guardioes.request.old.Requester;
import com.epitrack.guardioes.request.old.SimpleRequester;
import com.epitrack.guardioes.utility.Constants;
import com.epitrack.guardioes.view.IMenu;
import com.epitrack.guardioes.view.MenuListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * @author Igor Morais
 */
public class InterestAdapter extends ArrayAdapter<IMenu> {

    private final MenuListener listener;
    private Context context;
    SingleUser singleUser = SingleUser.getInstance();
    JSONArray hashtags = singleUser.getHashtags();
    SharedPreferences sharedPreferences = null;
    boolean bError = false;

    public InterestAdapter(final Context context, final MenuListener listener, final IMenu[] menuArray) {
        super(context, 0, menuArray);

        this.context = context;
        this.listener = listener;
    }

    public static class ViewHolder {

        TextView textViewName;
        SwitchCompat switchInterest;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup viewGroup) {
        View view = convertView;

        final ViewHolder viewHolder;

        if (view == null) {

            view = LayoutInflater.from(viewGroup.getContext())
                                 .inflate(R.layout.interest_item, viewGroup, false);

            viewHolder = new ViewHolder();

            viewHolder.textViewName = (TextView) view.findViewById(R.id.text_view_name);
            viewHolder.switchInterest = (SwitchCompat) view.findViewById(R.id.switch_interest);

            viewHolder.switchInterest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String idHashtag = InterestTag.getBy(getItem(position).getId()).getIdApi();

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("hashtag_id", idHashtag);

                        SimpleRequester simpleRequester = new SimpleRequester();
                        simpleRequester.setMethod(Method.POST);
                        simpleRequester.setJsonObject(jsonObject);

                        String jsonStr;

                        if (viewHolder.switchInterest.isChecked()) {
                            simpleRequester.setUrl(Requester.API_URL + "user/hashtags/add");
                            jsonStr = simpleRequester.execute(simpleRequester).get();

                            JSONObject jsonObjectError = new JSONObject(jsonStr);

                            if (jsonObjectError.get("error").toString().equals("true")) {
                                bError = true;
                            }

                        } else {
                            simpleRequester.setUrl(Requester.API_URL + "user/hashtags/remove");
                            jsonStr = simpleRequester.execute(simpleRequester).get();

                            JSONObject jsonObjectError = new JSONObject(jsonStr);

                            if (jsonObjectError.get("error").toString().equals("true")) {
                                bError = true;
                            }
                        }

                        if (!bError) {
                            updateHashtags();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            });

            view.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) view.getTag();
        }


        viewHolder.switchInterest.setChecked(false);
        if (hashtags.length() > 0) {
            for (int i = 0; i < hashtags.length(); i++) {
                try {
                    JSONObject jsonObjectHashtah = new JSONObject(hashtags.getString(i));
                    if (jsonObjectHashtah.get("id").toString().equals(InterestTag.getBy(1).getIdApi())) {
                        if (position == 0) {
                            viewHolder.switchInterest.setChecked(true);
                        }
                    } else if (jsonObjectHashtah.get("id").toString().equals(InterestTag.getBy(2).getIdApi())) {
                        if (position == 1) {
                            viewHolder.switchInterest.setChecked(true);
                        }
                    } else if (jsonObjectHashtah.get("id").toString().equals(InterestTag.getBy(3).getIdApi())) {
                        if (position == 2) {
                            viewHolder.switchInterest.setChecked(true);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        viewHolder.textViewName.setText(getItem(position).getName());

        return view;
    }

    private void updateHashtags() {
        SimpleRequester sendPostRequest = new SimpleRequester();
        sendPostRequest.setUrl(Requester.API_URL + "user/lookup/");
        sendPostRequest.setMethod(Method.GET);

        String jsonStr;
        try {
            jsonStr = sendPostRequest.execute(sendPostRequest).get();

            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.get("error").toString().equals("false")) {

                sharedPreferences = context.getSharedPreferences(Constants.Pref.PREFS_NAME, 0);

                JSONObject jsonObjectUser = jsonObject.getJSONObject("data");
                singleUser.setHashtags(jsonObjectUser.getJSONArray("hashtags"));
                hashtags = singleUser.getHashtags();

                SharedPreferences settings = context.getSharedPreferences(Constants.Pref.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(Constants.Pref.PREFS_NAME, singleUser.getUserToken());
                editor.commit();

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
