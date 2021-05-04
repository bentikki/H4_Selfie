package com.example.h4_selfie.Presenters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Presenter for taking selfies and displaying them.
 *
 * @author BTO
 * @version 1.0
 * @since 1.0
 */
public class TakeSelfiePresenter {

    private ViewContract view;
    private File imageFile = null;

    public static final int REQUEST_IMAGE_CAPTURE = 1;

    /**
     * Return file object on created image.
     *
     * @return File object on temp image created
     */
    public File getImageFile() {
        if(this.imageFile == null) throw new NullPointerException();

        return this.imageFile;
    }

    /**
     * View contract - To be implemented by view.
     *
     * @author BTO
     * @version 1.0
     * @since 1.0
     */
    public interface ViewContract {
        void startCameraIntent();
        void displayErrorMessage(String errorMessage);
        void onActivityResult(int requestCode, int resultCode, Intent data);
        void displayFileToImage(String filepath);
    }

    /**
     * Presenter constructor.
     *
     * @param view Activity implementing ViewContract interface
     */
    public TakeSelfiePresenter(ViewContract view){
        this.view = view;
    }


    /**
     * Takes picture Intent from View.
     * Saves image in temp storage.
     *
     */
    public void takePictureBtnClicked(){
        this.view.startCameraIntent();
    }

    /**
     * Runs on activityResult
     */
    public void imageActivityResult() {

        // Convert here your uri to bitmap then set it.
        File imageFile = this.getImageFile();
        view.displayFileToImage(imageFile.getAbsolutePath());
    }


    /**
     * Creates local file at provided directory.
     *
     * @param localStorageDirectory localFileDirectory
     */
    public File createImageFile(File localStorageDirectory) {

        try {
            // Create an imageFile file name
            String imageFileName = this.createLocalFileName();

            imageFile = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    localStorageDirectory      /* directory */
            );
        } catch (IOException e) {
            sendError("Image could not be saved.");
        }

        return imageFile;
    }

    /**
     * Creates local file from image.
     *
     * @return  String with temporary file name.
     */
    private String createLocalFileName(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        return imageFileName;
    }

    /**
     * Displays error on view.
     *
     * @param String Contains error message to display.
     */
    public void sendError(String errorMessage){
        view.displayErrorMessage(errorMessage);
    }

}
