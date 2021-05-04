package com.example.h4_selfie.Views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.h4_selfie.Presenters.TakeSelfiePresenter;
import com.example.h4_selfie.R;

import java.io.File;

/**
 * Activity for taking images and displying them in ImageView object.
 * Implements TakeSelfiePresenter.ViewContract as Presenter
 *
 * @author BTO
 * @version 1.0
 * @since 1.0
 */
public class TakeSelfieActivity extends AppCompatActivity implements TakeSelfiePresenter.ViewContract {

    // Presenter
    private TakeSelfiePresenter presenter;

    private ImageView mainDisplayImageView;
    private Button takePictureButton;

    public TakeSelfieActivity(){
        this.presenter = new TakeSelfiePresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainDisplayImageView = this.findViewById(R.id.mainDisplayImageView);
        takePictureButton = this.findViewById(R.id.takePictureButton);

        takePictureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                presenter.takePictureBtnClicked();
            }
        });

    }

    @Override
    public void startCameraIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            // Create the File where the photo should go
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File photoFile = presenter.createImageFile(storageDir);;

            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, presenter.REQUEST_IMAGE_CAPTURE);
            }
        }

    }

    /**
     * Runs onActivityResult when Intents are completed.
     *
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == presenter.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            presenter.imageActivityResult();
        }
    }

    /**
     * Displays local file provided to ImageView.
     *
     * @param filepath Path to local file.
     *
     */
    @Override
    public void displayFileToImage(String filepath) {
        // Convert here your uri to bitmap then set it.//
        Bitmap bitmap = BitmapFactory.decodeFile(filepath);

        // Rotate bitmap
        bitmap = this.getRotatedBitmap(bitmap, 90);
        mainDisplayImageView.setImageBitmap(bitmap);
    }


    /**
     * Rotates supplied bitmap, by supplied degrees.
     *
     * @param bitmap bitmap to rotate.
     * @param rotationInDegrees Degrees by which to rotate bitmap by.
     *
     * @return Bitmap rotated bitmap
     */
    private Bitmap getRotatedBitmap(Bitmap bitmap, int rotationInDegrees){
        //rotate bitmap
        Matrix matrix = new Matrix();
        matrix.postRotate(rotationInDegrees);
        //create new rotated bitmap
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        return bitmap;
    }

    /**
     * Displays error text.
     *
     * @param errorMessage Error text to display.
     *
     */
    @Override
    public void displayErrorMessage(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

}