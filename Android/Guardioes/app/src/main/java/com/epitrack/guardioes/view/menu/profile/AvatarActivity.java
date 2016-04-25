package com.epitrack.guardioes.view.menu.profile;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
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
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.epitrack.guardioes.view.base.ImageEditActivity;
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

    private File temp = new FileHandler().loadFile(PHOTO, Extension.PNG);

    private final SelectHandler handler = new SelectHandler();

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.avatar);

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
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.avatar, menu);

        return true;
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {
        handler.update(view, (Avatar) adapterView.getItemAtPosition(position));
    }

    public void onPhoto(final MenuItem item) {

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

                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(temp));

                            startActivityForResult(intent, REQUEST_CODE_CAMERA);

                        } else if (which == GALLERY) {

                            final Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                            startActivityForResult(intent, REQUEST_CODE_GALLERY);
                        }
                    }

                }).show();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {

        if (requestCode == REQUEST_CODE_CAMERA) {

            if (resultCode == RESULT_OK) {

                final Bundle bundle = new Bundle();

                bundle.putBoolean(Constants.Bundle.DELETE, true);

                bundle.putString(Constants.Bundle.PATH, temp.getPath());

                navigateForResult(ImageEditActivity.class, REQUEST_CODE_CROP_PHOTO, bundle);
            }

        } else if (requestCode == REQUEST_CODE_GALLERY) {

            if (resultCode == RESULT_OK) {

                final Bundle bundle = new Bundle();

                bundle.putString(Constants.Bundle.PATH, getPath(intent.getData()));

                navigateForResult(ImageEditActivity.class, REQUEST_CODE_CROP_PHOTO, bundle);
            }

        } else if (requestCode == REQUEST_CODE_CROP_PHOTO) {

            if (resultCode == RESULT_OK) {

                final String path = intent.getStringExtra(Constants.Bundle.PATH);

                final Intent i = new Intent();

                i.putExtra(Constants.Bundle.PATH, path);

                setResult(RESULT_OK, i);

                finish();
            }
        }
    }

    private String getPath(final Uri uri) {

        final String[] projection = { MediaStore.Images.Media.DATA };

        final Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if (cursor == null) {
            return null;

        } else {

            final String path = cursor.moveToFirst() ? cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)) : null;

            cursor.close();

            return path;
        }
    }

    @OnClick(R.id.button_photo)
    public void onSave() {

        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Select Avatar Button")
                .build());

        final Intent intent = new Intent();

        Avatar a = handler.getAvatar();

        intent.putExtra(Constants.Bundle.AVATAR, handler.getAvatar());

        setResult(RESULT_OK, intent);

        finish();
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
