package com.example.visiondemo;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

public class MainActivity extends Activity implements View.OnClickListener{

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private MaterialButton takePhotoBtn;
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private MaterialButton detectBarcodeBtn;
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private TextView outputTv;
    private ImageView thumbnailIv;
    @SuppressWarnings("FieldCanBeLocal")
    private String photoPath;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        takePhotoBtn = findViewById(R.id.btn_take_photo);
        detectBarcodeBtn = findViewById(R.id.btn_detect_barcode);
        outputTv = findViewById(R.id.tv_output);
        thumbnailIv = findViewById(R.id.iv_thumbnail);
    }

    @Override
    protected void onResume() {
        super.onResume();
        takePhotoBtn.setOnClickListener(this);
        detectBarcodeBtn.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        takePhotoBtn.setOnClickListener(null);
        detectBarcodeBtn.setOnClickListener(null);
        super.onPause();
    }

    @Override
    protected void onActivityResult(final int requestCode,
                                    final int resultCode, final Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            final Bundle extras = data.getExtras();
            final Bitmap imageBitmap;
            if (extras != null) {
                imageBitmap = (Bitmap) extras.get("data");
                thumbnailIv.setImageBitmap(imageBitmap);
            }
        }
        setPic();
    }

    private void setPic() {
        // Get the dimensions of the View
        final int targetW = thumbnailIv.getWidth();

        // Get the dimensions of the bitmap
        final BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        final int photoW = bmOptions.outWidth;

        // Determine how much to scale down the image
        final int scaleFactor = photoW / targetW;

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        final Bitmap bitmap = BitmapFactory.decodeFile(photoPath, bmOptions);
        thumbnailIv.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(final View view) {
        if (view.getId() == R.id.btn_take_photo) {
            dispatchTakePictureIntent();
        } else if (view.getId() == R.id.btn_detect_barcode) decodeBarcode();
    }

    private void dispatchTakePictureIntent() {
        final Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (final IOException e) {
                Log.e(MainActivity.class.getSimpleName(), e.getMessage(), e.fillInStackTrace());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                final Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.visiondemo.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        final String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).
                format(new Date());
        final String imageFileName = "JPEG_" + timeStamp + "_";
        final File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (storageDir == null) {
            throw new IOException("Storage directory is null");
        }

        final File image = File.createTempFile(
                imageFileName,   /*prefix*/
                ".jpg",          /*suffix*/
                storageDir       /*directory*/
        );

        photoPath = image.getAbsolutePath();

        outputTv.setText("Path:" + photoPath);
        return image;
    }

    private void decodeBarcode() {
        // Get the dimensions of the bitmap
        final BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);

        // Determine how much to scale down the image
        final int scaleFactor = 1;

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        final Bitmap bitmap = BitmapFactory.decodeFile(photoPath, bmOptions);

        final FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        final FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance()
                .getVisionBarcodeDetector();
        detector.detectInImage(image)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                    @Override
                    public void onSuccess(final List<FirebaseVisionBarcode> barcodes) {
                        for (final FirebaseVisionBarcode code :
                                barcodes) {
                            final String rawValue = code.getRawValue();
                            outputTv.setText(outputTv.getText() + " raw value = " + rawValue);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull final Exception e) {
                        outputTv.setText(outputTv.getText() + " exception : " + e.getMessage());
                    }
                })
        .addOnCompleteListener(this, new OnCompleteListener<List<FirebaseVisionBarcode>>() {
            @Override
            public void onComplete(@NonNull final Task<List<FirebaseVisionBarcode>> task) {
                outputTv.setText(outputTv.getText() + " results = " + task.getResult().size() );
                for (final FirebaseVisionBarcode code :
                        task.getResult()) {
                    final String rawValue = code.getRawValue();
                    outputTv.setText(outputTv.getText() + " raw value = " + rawValue);
                }
            }
        });
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
    }
}
