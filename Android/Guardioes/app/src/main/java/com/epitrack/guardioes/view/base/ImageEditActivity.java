package com.epitrack.guardioes.view.base;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.edmodo.cropper.CropImageView;
import com.epitrack.guardioes.R;
import com.epitrack.guardioes.manager.AsyncListener;
import com.epitrack.guardioes.manager.AsyncSave;
import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.helper.DialogBuilder;
import com.epitrack.guardioes.helper.Extension;
import com.epitrack.guardioes.helper.ImageHelper;
import com.epitrack.guardioes.helper.Logger;
import com.epitrack.guardioes.view.dialog.LoadDialog;

import java.io.File;

import butterknife.Bind;

public class ImageEditActivity extends BaseAppCompatActivity {

    private static final String TAG = ImageEditActivity.class.getSimpleName();

    private static final String IMAGE = "Image";

    @Bind(R.id.image_view)
    CropImageView imageView;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.image_edit);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadImage(imageView);
    }

    @Override
    protected void onStop() {
        super.onStop();

        imageView.setImageBitmap(null);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.image_edit, menu);

        return true;
    }

    private void loadImage(final View view) {

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                final String path = getIntent().getStringExtra(Constants.Bundle.PATH);

                if (path == null) {

                    loadDialog();

                } else {

                    final int width = imageView.getWidth();
                    final int height = imageView.getHeight();

                    final Bitmap bitmap = loadBitmap(path, width, height);

                    if (bitmap == null) {

                        loadDialog();

                    } else {

                        imageView.setImageBitmap(bitmap);

                        if (getIntent().getBooleanExtra(Constants.Bundle.DELETE, false) && new File(path).delete()) {
                            Logger.logDebug(TAG, "The temporary file has deleted.");
                        }
                    }
                }

                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void loadDialog() {

        new DialogBuilder(ImageEditActivity.this).load()
                                                 .title(R.string.message_image_load)
                                                 .cancelable(false)
                                                 .positiveText(R.string.ok)
                                                 .onPositive(new MaterialDialog.SingleButtonCallback() {

                                                     @Override
                                                     public void onClick(@NonNull final MaterialDialog dialog, @NonNull final DialogAction which) {

                                                         setResult(RESULT_CANCELED);

                                                         finish();
                                                     }

                                                 }).build().show();
    }

    private Bitmap loadBitmap(final String path, final int width, final int height) {

        Bitmap bitmap = ImageHelper.scale(path, width, height);

        if (ImageHelper.getAngle(path) == ImageHelper.ANGLE_90) {
            bitmap = ImageHelper.rotate(bitmap, ImageHelper.ANGLE_90);

        } else if (ImageHelper.getAngle(path) == ImageHelper.ANGLE_180) {
            bitmap = ImageHelper.rotate(bitmap, ImageHelper.ANGLE_180);

        } else if (ImageHelper.getAngle(path) == ImageHelper.ANGLE_270) {
            bitmap = ImageHelper.rotate(bitmap, ImageHelper.ANGLE_270);
        }

        return bitmap;
    }

    public void onSave(final MenuItem item) {

        final Intent intent = getIntent();

        final String name = intent.hasExtra(Constants.Bundle.NAME) ? intent.getStringExtra(Constants.Bundle.NAME) : IMAGE;

        new AsyncSave(name, Extension.PNG, imageView.getCroppedImage()).setListener(new AsyncListener<String>() {

            final LoadDialog dialog = new LoadDialog();

            @Override
            public void onStart() {
                dialog.show(getFragmentManager(), LoadDialog.TAG);
            }

            @Override
            public void onEnd(final String path) {

                dialog.dismiss();

                imageView.setImageBitmap(null);

                final Intent intent = new Intent();

                intent.putExtra(Constants.Bundle.PATH, path);

                setResult(RESULT_OK, intent);

                finish();
            }

        }).execute();
    }
}