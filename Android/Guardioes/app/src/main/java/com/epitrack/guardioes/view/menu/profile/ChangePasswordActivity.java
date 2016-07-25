package com.epitrack.guardioes.view.menu.profile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.helper.DialogBuilder;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.UserRequester;
import com.epitrack.guardioes.request.base.RequestListener;
import com.epitrack.guardioes.view.dialog.LoadDialog;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePasswordActivity extends AppCompatActivity {

    @Bind(R.id.edit_text_old_password)
    EditText editOldPassword;

    @Bind(R.id.edit_text_password)
    EditText edtNewPassword;

    @Bind(R.id.edit_text_confirm_password)
    EditText edtConfirmNewPassword;

    private User user;
    private final LoadDialog loadDialog = new LoadDialog();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_activity);
        ButterKnife.bind(this);

        this.user = Parcels.unwrap(getIntent().getParcelableExtra(Constants.Bundle.USER));
    }

    private boolean isValid(String oldPassword, String newPassword, String confirmNewPassword){
        if (newPassword.length() < 6){
            Toast.makeText(this, getResources().getString(R.string.password_fail), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (newPassword.length() < 6){
            Toast.makeText(this, getResources().getString(R.string.password_fail), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!newPassword.equals(confirmNewPassword)){
            Toast.makeText(this, getResources().getString(R.string.password_not_equal), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    @OnClick(R.id.button_redfine_password)
    public void onChangePassword(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        String oldPassword = this.editOldPassword.getText().toString();
        String password = this.edtNewPassword.getText().toString();
        String confirmPassword = this.edtConfirmNewPassword.getText().toString();
        loadDialog.show(getFragmentManager(), LoadDialog.TAG);

        if (this.isValid(oldPassword, password, confirmPassword)){
            new UserRequester(this).changePassword(this.user, oldPassword, password, new RequestListener<String>() {
                @Override
                public void onStart() {

                }

                @Override
                public void onError(Exception e) {
                    loadDialog.dismiss();
                }

                @Override
                public void onSuccess(String type) {
                    loadDialog.dismiss();
                    finish();
                }
            });
        }
    }
}
