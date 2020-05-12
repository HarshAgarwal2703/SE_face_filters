package com.gsrathoreniks.facefilter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.gsrathoreniks.facefilter.camera.CameraSourcePreview;
import com.gsrathoreniks.facefilter.camera.GraphicOverlay;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

import static android.view.View.GONE;

public class FaceFilterActivity extends AppCompatActivity {
    TextGraphic mTextGraphic;

    private final Thread observer = new Thread("observer") {

        {
            setDaemon(true);
        }

        public void run() {

            while( !isInterrupted() ) {
                /*
                TextGraphic mTextGraphic = new TextGraphic(mGraphicOverlay);
                mGraphicOverlay.add(mTextGraphic);*/
                //mTextGraphic.updateText(2);
            }

        };
    };

    private static final String TAG = "FaceTracker";

    private CameraSource mCameraSource = null;
    private int typeFace = 0;
    private int typeFlash = 0;
    private boolean flashmode = false;
    private Camera camera;

    private static final int MASK[] = {
            R.id.no_filter,
            R.id.hair,
            R.id.op,
            R.id.snap,
            R.id.glasses2,
            R.id.glasses3,
            R.id.glasses4,
            R.id.glasses5,
            R.id.mask,
            R.id.mask2,
            R.id.mask3,
            R.id.dog,
            R.id.cat2
    };

    private CameraSourcePreview mPreview;
    private GraphicOverlay mGraphicOverlay;

    private static final int RC_HANDLE_GMS = 9001;
    // permission request codes need to be < 256
    private static final int RC_HANDLE_CAMERA_PERM = 2;


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_face_filter);

        mPreview = (CameraSourcePreview) findViewById(R.id.preview);
        mGraphicOverlay = (GraphicOverlay) findViewById(R.id.faceOverlay);
        //mTextGraphic = new TextGraphic(mGraphicOverlay);
        //mGraphicOverlay.add(mTextGraphic);

        ImageButton face = (ImageButton) findViewById(R.id.face);
        face.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(findViewById(R.id.scrollView).getVisibility() == GONE){
                    findViewById(R.id.scrollView).setVisibility(View.VISIBLE);
                    ((ImageButton) findViewById(R.id.face)).setImageResource(R.drawable.face_select);
                }else{
                    findViewById(R.id.scrollView).setVisibility(GONE);
                    ((ImageButton) findViewById(R.id.face)).setImageResource(R.drawable.face);
                }
            }
        });

        ImageButton no_filter = (ImageButton) findViewById(R.id.no_filter);
        no_filter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background);
                typeFace = 0;
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background_select);
            }
        });

        ImageButton hair = (ImageButton) findViewById(R.id.hair);
        hair.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background);
                typeFace = 1;
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background_select);
            }
        });

        ImageButton op = (ImageButton) findViewById(R.id.op);
        op.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background);
                typeFace = 2;
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background_select);
            }
        });

        ImageButton snap = (ImageButton) findViewById(R.id.snap);
        snap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background);
                typeFace = 3;
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background_select);
            }
        });

        ImageButton glasses2 = (ImageButton) findViewById(R.id.glasses2);
        glasses2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background);
                typeFace = 4;
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background_select);
            }
        });

        ImageButton glasses3 = (ImageButton) findViewById(R.id.glasses3);
        glasses3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background);
                typeFace = 5;
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background_select);
            }
        });

        ImageButton glasses4 = (ImageButton) findViewById(R.id.glasses4);
        glasses4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background);
                typeFace = 6;
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background_select);
            }
        });

        ImageButton glasses5 = (ImageButton) findViewById(R.id.glasses5);
        glasses5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background);
                typeFace = 7;
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background_select);
            }
        });

        ImageButton mask = (ImageButton) findViewById(R.id.mask);
        mask.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background);
                typeFace = 8;
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background_select);
            }
        });

        ImageButton mask2 = (ImageButton) findViewById(R.id.mask2);
        mask2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background);
                typeFace = 9;
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background_select);
            }
        });

        ImageButton mask3 = (ImageButton) findViewById(R.id.mask3);
        mask3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background);
                typeFace = 10;
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background_select);
            }
        });

        ImageButton dog = (ImageButton) findViewById(R.id.dog);
        dog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background);
                typeFace = 11;
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background_select);
            }
        });

        ImageButton cat2 = (ImageButton) findViewById(R.id.cat2);
        cat2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background);
                typeFace = 12;
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background_select);
            }
        });

        ImageButton button = (ImageButton) findViewById(R.id.change);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPreview.release();
                createCameraSource(CameraSource.CAMERA_FACING_FRONT);

                try {
                    mPreview.start(mCameraSource, mGraphicOverlay);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                mCameraSource.release();
//                if (mCameraSource.getCameraFacing() == CameraSource.CAMERA_FACING_FRONT) {
////                    changeCameraButton.setText("FRONT CAMERA");
//                    if (mCameraSource != null) {
//                        mCameraSource.release();
//                    }
//                    createCameraSource(CameraSource.CAMERA_FACING_BACK);
//                } else {
////                    changeCameraButton.setText("BACK CAMERA");
//                    if (mCameraSource != null) {
//                        mCameraSource.release();
//                    }
//                    createCameraSource(CameraSource.CAMERA_FACING_FRONT);
//                }
            }
        });

        final ImageButton flash = (ImageButton) findViewById(R.id.flash);
        flash.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                camera = getCamera(mCameraSource);
                if (camera != null) {
                    try {
                        Camera.Parameters param = camera.getParameters();
                        param.setFlashMode(!flashmode ? Camera.Parameters.FLASH_MODE_TORCH : Camera.Parameters.FLASH_MODE_OFF);
                        camera.setParameters(param);
                        flashmode = !flashmode;
//                        if (flashmode) {
//                            showToast("Flash Switched ON");
//                        } else {
//                            showToast("Flash Switched Off");
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
//                int blue;
//                if(flashmode==false){
//                    flashmode = true;
//                    blue = 0;
//                }else{
//                    flashmode = false;
//                    blue = 255;
//                }
//                flash.setColorFilter(Color.argb(255, 255, 255, blue));
            }
        });

        ImageButton camera = (ImageButton) findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                takeImage();
                onPause();
            }
        });



        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource(CameraSource.CAMERA_FACING_BACK);
        } else {
            requestCameraPermission();
        }
    }

    private void takeImage() {
        try{

            mCameraSource.takePicture(null, new CameraSource.PictureCallback() {

                private File imageFile;
                @Override
                public void onPictureTaken(byte[] bytes) {
                    try {
                        if(bytes==null){
                            Log.d(TAG, "onPictureTaken: nulllll");
                        }
                        // convert byte array into bitmap
                        Bitmap loadedImage = null;
                        Bitmap rotatedBitmap = null;
                        loadedImage = BitmapFactory.decodeByteArray(bytes, 0,
                                bytes.length);

                        // rotate Image
                        Matrix rotateMatrix = new Matrix();
                        rotateMatrix.postRotate(getWindowManager().getDefaultDisplay().getRotation());
                        rotatedBitmap = Bitmap.createBitmap(loadedImage, 0, 0,
                                loadedImage.getWidth(), loadedImage.getHeight(),
                                rotateMatrix, false);
                        String state = Environment.getExternalStorageState();
                        File folder = null;
                        if (state.contains(Environment.MEDIA_MOUNTED)) {
                            folder = new File(Environment
                                    .getExternalStorageDirectory()+"/faceFilter");
                        } else {
                            folder = new File(Environment
                                    .getExternalStorageDirectory() + "/faceFilter");
                        }

                        boolean success = true;
                        if (!folder.exists()) {
                            success = folder.mkdirs();
                        }
                        if (success) {
                            java.util.Date date = new java.util.Date();
                            imageFile = new File(folder.getAbsolutePath()
                                    + File.separator
                                    //+ new Timestamp(date.getTime()).toString()
                                    + "Image.jpg");

                            imageFile.createNewFile();
                        } else {
                            Toast.makeText(getBaseContext(), "Image Not saved",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        ByteArrayOutputStream ostream = new ByteArrayOutputStream();

                        // save image into gallery
                        rotatedBitmap = resize(rotatedBitmap, 800, 600);
                        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);

                        FileOutputStream fout = new FileOutputStream(imageFile);
                        fout.write(ostream.toByteArray());
                        fout.close();
                        ContentValues values = new ContentValues();

                        values.put(MediaStore.Images.Media.DATE_TAKEN,
                                System.currentTimeMillis());
                        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                        values.put(MediaStore.MediaColumns.DATA,
                                imageFile.getAbsolutePath());

                        setResult(Activity.RESULT_OK); //add this
                        finish();
                    } catch (Exception e) {
                        Log.d(TAG, "onPictureTaken: catched");
                        e.printStackTrace();
                    }
                }
            });

        }catch (Exception ex){
        }

    }

    private Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }


    private void requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };

        Snackbar.make(mGraphicOverlay, R.string.permission_camera_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, listener)
                .show();
    }


    private void createCameraSource(int cameraFacing) {

        Context context = getApplicationContext();
        FaceDetector detector = new FaceDetector.Builder(context)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.ACCURATE_MODE)
                .build();

        detector.setProcessor(
                new MultiProcessor.Builder<>(new GraphicFaceTrackerFactory())
                        .build());

        //new MultiProcessor.Builder<>(new GraphicTextTrackerFactory()).build();

        if (!detector.isOperational()) {
            // Note: The first time that an app using face API is installed on a device, GMS will
            // download a native library to the device in order to do detection.  Usually this
            // completes before the app is run for the first time.  But if that download has not yet
            // completed, then the above call will not detect any faces.
            //
            // isOperational() can be used to check if the required native library is currently
            // available.  The detector will automatically become operational once the library
            // download completes on device.
            Log.w(TAG, "Face detector dependencies are not yet available.");
        }
//        if(mCameraSource!=null){
//            mCameraSource.release();
//        }
        mCameraSource = new CameraSource.Builder(context, detector)
                .setRequestedPreviewSize(640, 480)
                .setAutoFocusEnabled(true)
                .setFacing(cameraFacing)
                .setRequestedFps(30.0f)
                .build();


        //observer.start();
        /*
        TextGraphic mTextGraphic = new TextGraphic(mGraphicOverlay);
        mGraphicOverlay.add(mTextGraphic);
        mTextGraphic.updateText(2);*/
    }


    @Override
    protected void onResume() {
        super.onResume();

        startCameraSource();
    }

    private static Camera getCamera(@NonNull CameraSource cameraSource) {
        Field[] declaredFields = CameraSource.class.getDeclaredFields();

        for (Field field : declaredFields) {
            if (field.getType() == Camera.class) {
                field.setAccessible(true);
                try {
                    Camera camera = (Camera) field.get(cameraSource);
                    if (camera != null) {
                        return camera;
                    }
                    return null;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return null;
    }
    @Override
    protected void onPause() {
        super.onPause();
        mPreview.stop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCameraSource != null) {
            mCameraSource.release();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission granted - initialize the camera source");
            // we have permission, so create the camerasource
            createCameraSource(CameraSource.CAMERA_FACING_BACK);
            return;
        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Face Tracker sample")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(R.string.ok, listener)
                .show();
    }


    private void startCameraSource() {

        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    private class GraphicTextTrackerFactory implements MultiProcessor.Factory<String> {
        @Override
        public Tracker<String> create(String face) {
            return new GraphicTextTracker(mGraphicOverlay);
        }
    }

    private class GraphicTextTracker extends Tracker<String> {
        private GraphicOverlay mOverlay;
        private TextGraphic mTextGraphic ;

        GraphicTextTracker(GraphicOverlay overlay) {
            mOverlay = overlay;
            mTextGraphic = new TextGraphic(overlay);
        }

        public void onUpdate() {
            mOverlay.add(mTextGraphic);
            mTextGraphic.updateText(3);
        }

        @Override
        public void onDone() {
            mOverlay.remove(mTextGraphic);
        }
    }



    private class GraphicFaceTrackerFactory implements MultiProcessor.Factory<Face> {
        @Override
        public Tracker<Face> create(Face face) {
            return new GraphicFaceTracker(mGraphicOverlay);
        }
    }


    private class GraphicFaceTracker extends Tracker<Face> {
        private GraphicOverlay mOverlay;
        private FaceGraphic mFaceGraphic;

        GraphicFaceTracker(GraphicOverlay overlay) {
            mOverlay = overlay;
            mFaceGraphic = new FaceGraphic(overlay,typeFace);
        }


        @Override
        public void onNewItem(int faceId, Face item) {
            mFaceGraphic.setId(faceId);
        }


        @Override
        public void onUpdate(FaceDetector.Detections<Face> detectionResults, Face face) {
            mOverlay.add(mFaceGraphic);
            mFaceGraphic.updateFace(face,typeFace);
        }


        @Override
        public void onMissing(FaceDetector.Detections<Face> detectionResults) {
            mOverlay.remove(mFaceGraphic);
        }


        @Override
        public void onDone() {
            mOverlay.remove(mFaceGraphic);
        }
    }
}
