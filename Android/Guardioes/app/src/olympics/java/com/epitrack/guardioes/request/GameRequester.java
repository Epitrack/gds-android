package com.epitrack.guardioes.request;

import android.content.Context;

import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.helper.DateFormat;
import com.epitrack.guardioes.helper.Logger;
import com.epitrack.guardioes.helper.Utility;
import com.epitrack.guardioes.manager.PrefManager;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.base.BaseRequester;
import com.epitrack.guardioes.request.base.Method;
import com.epitrack.guardioes.request.base.RequestException;
import com.epitrack.guardioes.request.base.RequestListener;
import com.epitrack.guardioes.request.base.Requester;
import com.epitrack.guardioes.request.base.RequesterConfig;
import com.epitrack.guardioes.view.game.model.Option;
import com.epitrack.guardioes.view.game.model.Question;
import com.epitrack.guardioes.view.game.model.Score;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Igor Morais
 */
public class GameRequester extends BaseRequester {

    private static final String TAG = GameRequester.class.getSimpleName();

    public GameRequester(final Context context) {
        super(context);
    }

    public void getScore(final RequestListener<List<Score>> listener) {

        final String url = RequesterConfig.URL + "/game/ranking/";

        listener.onStart();

        new Requester(getContext()).request(Method.GET, url, new FutureCallback<Response<String>>() {

            @Override
            public void onCompleted(final Exception error, final Response<String> response) {

                if (error == null) {

                    if (isSuccess(response)) {

                        final List<Score> scoreList = new ArrayList<>();

                        try {

                            for (final JsonNode jsonNode : new ObjectMapper().readTree(response.getResult())) {

                                final Score score = new Score();

                                score.setPosition(jsonNode.get(Constants.Json.POSITION).asInt());
                                score.setCountry(jsonNode.get(Constants.Json.COUNTRY).asText());
                                score.setUrl(jsonNode.get(Constants.Json.URL).asText());

                                scoreList.add(score);
                            }

                            listener.onSuccess(scoreList);

                        } catch (IOException e) {
                            listener.onError(null);
                        }
                    }

                } else {

                    listener.onError(error);
                }
            }
        });
    }

    public void getQuestion(final RequestListener<List<Question>> listener) {

        final String url = "http://rest.guardioesdasaude.org/game/questions/?lang=pt_BR";

        listener.onStart();

        new Requester(getContext()).request(Method.GET, url, new FutureCallback<Response<String>>() {

            @Override
            public void onCompleted(final Exception error, final Response<String> response) {

                if (error == null) {

                    if (isSuccess(response)) {

                        final List<Question> questionList = new ArrayList<>();

                        try {

                            for (final JsonNode jsonNode : new ObjectMapper().readTree(response.getResult())) {

                                final Question question = new Question();

                                question.setTitle(jsonNode.get(Constants.Json.TITLE).asText());

                                for (final JsonNode json : jsonNode.get(Constants.Json.OPTION_LIST)) {

                                    final Option option = new Option();

                                    option.setOption(json.get(Constants.Json.OPTION).asText());
                                    option.setCorrect(json.get(Constants.Json.CORRECT).asBoolean());

                                    question.add(option);
                                }

                                questionList.add(question);
                            }

                            listener.onSuccess(questionList);

                        } catch (IOException e) {
                            listener.onError(null);
                        }
                    }

                } else {

                    listener.onError(error);
                }
            }
        });
    }

    public void update(final int energy, final int level, final Map<Integer, Boolean> pieceMap, final User user, final RequestListener<User> listener) {

        final String url = "http://rest.guardioesdasaude.org/user/lookup/";

        final Map<String, Object> bodyMap = new HashMap<>();

        bodyMap.put("nick", user.getNick());
        bodyMap.put("email", user.getEmail());
        bodyMap.put("password", user.getPassword());
        bodyMap.put("client", user.getClient());
        bodyMap.put("dob", DateFormat.getDate(user.getDob()));
        bodyMap.put("gender", user.getGender());
        bodyMap.put("app_token", user.getAppToken());
        bodyMap.put("race", user.getRace());
        bodyMap.put("platform", user.getPlatform());
        bodyMap.put("picture", user.getImage());
        bodyMap.put("gcm_token", user.getGcmToken());
        //bodyMap.put("xp", user.getEnergy());
        //bodyMap.put("level", user.getLevel());
        //bodyMap.put("puzzleMatriz", toIntArray(user.getPieceMap()));

        listener.onStart();

        new Requester(getContext()).request(Method.GET, url, getAuthHeaderMap(), bodyMap, new FutureCallback<Response<String>>() {

            @Override
            public void onCompleted(final Exception error, final Response<String> response) {

                if (error == null) {

                    if (isSuccess(response)) {

                        try {

                            final JSONObject json = new JSONObject(response.getResult());

                            if (json.getBoolean("error")) {

                                listener.onError(new RequestException());

                            } else {

                                loadUser(json.getJSONObject("data"));

                                final User user = SingleUser.getInstance();

                                if (new PrefManager(getContext()).put(Constants.Pref.USER, user)) {

                                    listener.onSuccess(user);
                                }
                            }

                        } catch (final JSONException e) {
                            Logger.logDebug(TAG, e.getMessage());

                            listener.onError(new RequestException());
                        }

                    } else {

                        listener.onError(new RequestException());
                    }

                } else {

                    listener.onError(error);
                }
            }
        });
    }

    private int[] toIntArray(final Map<Integer, Boolean> pieceMap) {

        final int[] pieceArray = new int[9];

        for (int i = 0; i < pieceArray.length; i++) {
            pieceArray[i] = pieceMap.get(i) ? 1 : 0;
        }

        return pieceArray;
    }

    private Map<Integer, Boolean> toMap(final int[] pieceArray) {

        final Map<Integer, Boolean> pieceMap = new HashMap<>();

        for (int i = 0; i < pieceArray.length; i++) {
            pieceMap.put(i, pieceArray[i] == 1);
        }

        return pieceMap;
    }

    // TODO: Refactor soon..
    private void loadUser(JSONObject json) {

        try {

            final SingleUser user = SingleUser.getInstance();

            user.setNick(json.getString("nick"));
            user.setEmail(json.getString("email"));
            user.setGender(json.getString("gender"));
            user.setId(json.getString("id"));
            user.setRace(json.getString("race"));
            user.setDob(json.getString("dob"));
            user.setUserToken(json.getString("token"));

            try {
                user.setPath(json.getString("file"));

            } catch (Exception e) {
            }

            try {
                user.setImage(json.getInt("picture"));

            } catch (Exception e) {

            }

            try {

                user.setHashtags(Utility.toList(json.getJSONArray("hashtags")));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (json.has("xp")) {

                user.setEnergy(json.getInt("xp"));
            }

            if (json.has("level")) {
                user.setEnergy(json.getInt("level"));
            }

            if (json.has("puzzleMatriz")) {

               // user.setPieceMap(toMap(json.ge));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
