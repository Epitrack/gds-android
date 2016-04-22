package com.epitrack.guardioes.view.menu.profile;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.epitrack.guardioes.BuildConfig;
import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.helper.Extension;
import com.epitrack.guardioes.helper.FileHandler;
import com.epitrack.guardioes.helper.Logger;
import com.epitrack.guardioes.model.ProfileImage;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.google.android.gms.analytics.HitBuilders;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Igor Morais
 */
public class AvatarActivity extends BaseAppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = AvatarActivity.class.getSimpleName();

    private static final String PHOTO = "Photo";

    private static final int CAMERA = 0;
    private static final int GALLERY = 1;

    private static final int REQUEST_CODE_CAMERA = 7777;
    private static final int REQUEST_CODE_GALLERY = 8888;
    private static final int REQUEST_CODE_CROP_PHOTO = 9999;

    @Bind(R.id.grid_view)
    GridView gridView;

    private final SelectHandler handler = new SelectHandler();

    private ProfileImage profileImage = ProfileImage.getInstance();

    private File tempImage = new FileHandler().loadFile(PHOTO, Extension.PNG);

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.avatar);

        profileImage.setAvatar("");
        profileImage.setUri(null);

        gridView.setAdapter(new AvatarAdapter(this, Avatar.values()));

        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("Select Avatar Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.avatar, menu);

        return true;
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {
        handler.update(view, (Avatar) adapterView.getItemAtPosition(position));
    }

    public void onPhoto(final MenuItem menuItem) {

        if (!Dexter.isRequestOngoing()) {

            Dexter.checkPermissions(new MultiplePermissionsListener() {

                @Override
                public void onPermissionsChecked(final MultiplePermissionsReport permissionReport) {

                    if (permissionReport.areAllPermissionsGranted()) {

                        getPhoto();
                    }
                }

                @Override
                public void onPermissionRationaleShouldBeShown(final List<PermissionRequest> permissionList, final PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }

            }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    private void getPhoto() {

        new MaterialDialog.Builder(this)
                .items(R.array.image_array)
                .itemsCallback(new MaterialDialog.ListCallback() {

                    @Override
                    public void onSelection(final MaterialDialog dialog, final View view, final int which, final CharSequence text) {

                        if (which == CAMERA) {

                            final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempImage));

                            startActivityForResult(intent, REQUEST_CODE_CAMERA);

                        } else if (which == GALLERY) {

                            final Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                            startActivityForResult(intent, REQUEST_CODE_GALLERY);
                        }
                    }

                }).show();
    }

    @OnClick(R.id.button_photo)
    public void onSave() {

        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Select Avatar Button")
                .build());

        if (handler.getAvatar() == null) {

            // Mostrar messagem
            profileImage.setAvatar("");

        } else {

            profileImage.setAvatar(String.valueOf(handler.getAvatar().getId()));

            final Intent intent = new Intent();

            intent.putExtra(Constants.Bundle.AVATAR, handler.getAvatar());

            setResult(RESULT_OK, intent);

            finish();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {

        if (requestCode == Constants.RequestCode.IMAGE && resultCode == RESULT_OK) {
            finish();
        }
    }

    private class SelectHandler {

        private static final int COLOR_DESELECT = android.R.color.transparent;
        private static final int COLOR_SELECT = R.drawable.round_blue;

        private View view;
        private Avatar avatar;

        private void deselect() {

            if (view == null) {

                if (BuildConfig.DEBUG) {
                    Logger.logDebug(TAG, "The view is null.");
                }

            } else {

                view.setBackgroundResource(COLOR_DESELECT);
            }
        }

        private void select() {

            if (view == null) {

                if (BuildConfig.DEBUG) {
                    Logger.logDebug(TAG, "The view is null.");
                }

            } else {

                view.setBackgroundResource(COLOR_SELECT);
            }
        }

        public final Avatar getAvatar() {
            return avatar;
        }

        public final void update(final View view, final Avatar avatar) {

            deselect();

            this.view = view;
            this.avatar = avatar;

            select();
        }
    }
}
