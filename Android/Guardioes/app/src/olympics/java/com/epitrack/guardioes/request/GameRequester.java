package com.epitrack.guardioes.request;

import android.content.Context;

import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.request.base.BaseRequester;
import com.epitrack.guardioes.request.base.Method;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Igor Morais
 */
public class GameRequester extends BaseRequester {

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

        final String url = RequesterConfig.URL + "/game/questions/?lang=pt_BR";

        listener.onStart();

        new Requester(getContext()).request(Method.GET, url, new FutureCallback<Response<String>>() {

            @Override
            public void onCompleted(final Exception error, final Response<String> response) {

                if (error == null) {

                    if (isSuccess(response)) {

                        final List<Question> questionList = new ArrayList<>();

                        try {

                            for (final JsonNode jsonNode : new ObjectMapper().readTree(response.getResult()).get(Constants.Json.QUESTION_LIST)) {

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
}
