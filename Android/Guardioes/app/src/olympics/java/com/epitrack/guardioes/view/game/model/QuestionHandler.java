package com.epitrack.guardioes.view.game.model;

import android.app.Activity;
import android.content.Context;

import com.epitrack.guardioes.request.GameRequester;
import com.epitrack.guardioes.request.base.RequestListener;
import com.epitrack.guardioes.view.dialog.LoadDialog;
import com.epitrack.guardioes.view.game.IQuestion;

import java.util.LinkedList;
import java.util.List;

public class QuestionHandler {

    private static final int FIRST = 0;

    private List<Question> questionList = new LinkedList<>();

    private QuestionHandler() {

    }

    private static final class LazyLoader {
        private static final QuestionHandler INSTANCE = new QuestionHandler();
    }

    public static QuestionHandler with() {
        return LazyLoader.INSTANCE;
    }

    private void setQuestionList(final List<Question> questionList) {
        this.questionList = questionList;
    }

    private class QuestionRequester implements RequestListener<List<Question>> {

        private final Context context;
        private final IQuestion listener;

        private final LoadDialog loadDialog = new LoadDialog();

        public QuestionRequester(final Context context, final IQuestion listener) {
            this.context = context;
            this.listener = listener;
        }

        @Override
        public void onStart() {

            if (context instanceof Activity) {
                loadDialog.show(((Activity) context).getFragmentManager(), LoadDialog.TAG);
            }
        }

        @Override
        public void onError(final Exception e) {

            loadDialog.dismiss();
        }

        @Override
        public void onSuccess(final List<Question> questionList) {

            loadDialog.dismiss();

            listener.onQuestion(questionList.remove(FIRST));

            setQuestionList(questionList);
        }
    }

    public void requestQuestion(final Context context, final IQuestion listener) {

        if (questionList.isEmpty()) {

            new GameRequester(context).getQuestion(new QuestionRequester(context, listener));

        } else {

            listener.onQuestion(questionList.remove(FIRST));
        }
    }
}
