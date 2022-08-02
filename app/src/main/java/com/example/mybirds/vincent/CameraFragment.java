package com.example.mybirds.vincent;

import androidx.annotation.NonNull;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraInfo;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.MeteringPoint;
import androidx.camera.core.MeteringPointFactory;
import androidx.camera.core.Preview;
import androidx.camera.core.VideoCapture;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.mybirds.R;
import com.example.mybirds.dan.NewPostModel;
import com.example.mybirds.alexis.activities.FeedFragment;
import com.example.mybirds.alexis.activities.NotificationActivity;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CameraFragment extends Fragment {

    private final Executor executor = Executors.newSingleThreadExecutor();
    private final int REQUEST_CODE_PERMISSIONS = 1001;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"" +
            "android.permission.CAMERA",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.RECORD_AUDIO",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION"};

    ProcessCameraProvider cameraProvider;
    CameraControl cameraControl;
    CameraInfo cameraInfo;
    Preview preview;
    ImageAnalysis imageAnalysis;

    ImageCapture imageCapture;
    VideoCapture videoCapture;

    LocationManager locationManager = null;

    private String locationProvider;
    private double latitude;
    private double longitude;

    private PreviewView mPreviewView;
    private ImageView imgCapture;
    private ImageView imgFlashOnOff;
    private ImageView imgSwitchCamera;
    private ImageView pictureView;
    private ImageView focusView;
    private TextView textCounter;
    private TextView hintTextView;
    private SeekBar zoomSlider;
    private LinearLayout mediaModificationOptions;

    private ImageView pictureTaken;
    private VideoView videoTaken;
    private ImageView backButton;
    private ImageView checkButton;

    Bitmap picture;

    private final Handler myHandler = new Handler();

    ObjectAnimator rotateAnim;
    ObjectAnimator fadeOutAnim;
    ObjectAnimator fadeInAnim;

    private boolean longClick = false;
    private int flashMode;
    private CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();

        mPreviewView = view.findViewById(R.id.previewView);
        imgCapture = view.findViewById(R.id.imgCapture);
        imgFlashOnOff = view.findViewById(R.id.imgFlashOnOff);
        imgSwitchCamera = view.findViewById(R.id.imgSwitchCamera);
        textCounter = view.findViewById(R.id.textCounter);
        hintTextView = view.findViewById(R.id.hintTextView);
        pictureView = view.findViewById(R.id.pictureView);
        focusView = view.findViewById(R.id.focusView);
        zoomSlider = view.findViewById(R.id.zoomSlider);
        pictureTaken = view.findViewById(R.id.pictureTaken);
        videoTaken = view.findViewById(R.id.videoTaken);
        backButton = view.findViewById(R.id.backButton);
        checkButton = view.findViewById(R.id.checkButton);
        mediaModificationOptions = view.findViewById(R.id.MediaModificationOptions);


        rotateAnim = ObjectAnimator.ofFloat(imgSwitchCamera, "rotation", 0f, -180f);
        rotateAnim.setDuration(600);
        rotateAnim.setInterpolator(new DecelerateInterpolator());

        fadeOutAnim = ObjectAnimator.ofFloat(imgFlashOnOff, "alpha", 1f, 0f);
        fadeOutAnim.setDuration(250);
        fadeOutAnim.setInterpolator(new AccelerateInterpolator());

        fadeInAnim = ObjectAnimator.ofFloat(imgFlashOnOff, "alpha", 0f, 1f);
        fadeInAnim.setDuration(250);
        fadeInAnim.setInterpolator(new DecelerateInterpolator());

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationProvider = "";

        if (allPermissionsGranted()) {
            startCamera();
            initialiserLocalisation();
        } else {
            ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }

    private void startCamera() {
        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(getActivity());

        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                initializeUseCases();
                bindPreview(cameraProvider);
                createListeners();
            } catch (ExecutionException | InterruptedException e) {
                // This should never be reached
            }
        }, ContextCompat.getMainExecutor(getActivity()));
    }

    @SuppressLint("RestrictedApi")
    private void initializeUseCases() {
        preview = new Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .build();

        imageAnalysis = new ImageAnalysis.Builder()
                .build();

        imageCapture = new ImageCapture.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(getActivity().getWindowManager().getDefaultDisplay().getRotation())
                .build();

        videoCapture = new VideoCapture.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(getActivity().getWindowManager().getDefaultDisplay().getRotation())
                .build();

//        BokehImageCaptureExtender bokehImageCapture = BokehImageCaptureExtender.create(imageBuilder);
//
//        if (bokehImageCapture.isExtensionAvailable(cameraSelector)) {
//            bokehImageCapture.enableExtension(cameraSelector);
//        }

//        HdrImageCaptureExtender hdrImageCaptureExtender = HdrImageCaptureExtender.create(imageBuilder);
//
//        if (hdrImageCaptureExtender.isExtensionAvailable(cameraSelector)) {
//            hdrImageCaptureExtender.enableExtension(cameraSelector);
//        }
    }

    @SuppressLint({"ClickableViewAccessibility", "RestrictedApi"})
    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();

        Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);

        cameraControl = camera.getCameraControl();
        cameraInfo = camera.getCameraInfo();

        preview.setSurfaceProvider(mPreviewView.getSurfaceProvider());
    }

    @SuppressLint({"ClickableViewAccessibility", "RestrictedApi"})
    private void createListeners() {
        imgCapture.setOnClickListener(v -> {
            mediaModificationOptions.setVisibility(View.INVISIBLE);
            pictureView.bringToFront();
            pictureView.setVisibility(View.VISIBLE);
            myHandler.postDelayed(hidePictureView, 300);
            SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.FRANCE);
            File file = new File(getMediaDirectoryName(), mDateFormat.format(new Date()) + ".jpg");
            ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
            imageCapture.takePicture(outputFileOptions, executor, new ImageCapture.OnImageSavedCallback() {
                @Override
                public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                    Log.d("Info", "Image saved successfully");
                    getActivity().runOnUiThread(() -> showPicture(file));
                }

                @Override
                public void onError(@NonNull ImageCaptureException error) {
                    error.printStackTrace();
                }
            });
        });

//        imgCapture.setOnLongClickListener(v -> {
//            longClick = true;
//            hintTextView.setVisibility(View.INVISIBLE);
//            textCounter.setVisibility(View.VISIBLE);
//            imgSwitchCamera.setVisibility(View.GONE);
//            imgFlashOnOff.setVisibility(View.GONE);
//            scaleUpAnimation();
//            startTime = SystemClock.uptimeMillis();
//            myHandler.postDelayed(updateTimerThread, 0);
//            SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.FRANCE);
//            File file = new File(getMediaDirectoryName(), mDateFormat.format(new Date()) + ".mp4");
//            VideoCapture.OutputFileOptions outputFileOptions = new VideoCapture.OutputFileOptions.Builder(file).build();
//            videoCapture.startRecording(outputFileOptions, executor, new VideoCapture.OnVideoSavedCallback() {
//                @Override
//                public void onVideoSaved(@NonNull VideoCapture.OutputFileResults outputFileResults) {
//                    Log.d("Info", "Video saved successfully");
//                    getActivity().runOnUiThread(() -> showVideo(file));
//                }
//
//                @Override
//                public void onError(int videoCaptureError, @NonNull String message, @Nullable Throwable cause) {
//                    Log.e("Video capture error", message);
//                }
//            });
//            return true;
//        });

        imgCapture.setOnTouchListener((v, event) -> {
            if (longClick) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    stopRecording(videoCapture);
                    longClick = false;
                    return true;
                }
            }
            return false;
        });

        getView().setOnTouchListener((v2, event2) -> {
            if (event2.getAction() == MotionEvent.ACTION_UP && longClick) {
                stopRecording(videoCapture);
            }
            return true;
        });

        imgFlashOnOff.setOnClickListener(click -> {
            fadeOutAnim.start();
            myHandler.postDelayed(this::toggleFlash, 250);
        });

        imgSwitchCamera.setOnClickListener(click -> {
            rotateAnim.start();
            switchCamera();
        });

        GestureDetector simpleGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent event) {
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent event) {
                focusView.setX(event.getX() - focusView.getWidth() / (float) 2);
                focusView.setY(event.getY() - focusView.getHeight() / (float) 2);
                focusView.setVisibility(View.VISIBLE);
                myHandler.postDelayed(() -> focusView.setVisibility(View.INVISIBLE), 750);
                MeteringPointFactory meteringPointFactory = mPreviewView.getMeteringPointFactory();
                MeteringPoint point = meteringPointFactory.createPoint(event.getX(), event.getY());
                FocusMeteringAction action = new FocusMeteringAction.Builder(point).build();
                cameraControl.startFocusAndMetering(action);
                return true;
            }
        });

        ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(getActivity(), new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                float currentZoomRatio = cameraInfo.getZoomState().getValue().getZoomRatio();
                float delta = detector.getScaleFactor();
                cameraControl.setZoomRatio(currentZoomRatio * delta);
                zoomSlider.setProgress((int) (cameraInfo.getZoomState().getValue().getLinearZoom() * 100), true);
                return true;
            }
        });

        mPreviewView.setOnTouchListener((v, event) -> {
            boolean result = scaleGestureDetector.onTouchEvent(event);

            boolean isScaling = scaleGestureDetector.isInProgress();
            if (!isScaling) {
                result = simpleGestureDetector.onTouchEvent(event);
            }
            return result;
        });

        zoomSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cameraControl.setLinearZoom(progress / 100f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        backButton.setOnClickListener(v -> {
            this.videoTaken.setVisibility(View.INVISIBLE);
            this.pictureTaken.setVisibility(View.INVISIBLE);
            mediaModificationOptions.setVisibility(View.VISIBLE);
            backButton.setVisibility(View.INVISIBLE);
            checkButton.setVisibility(View.INVISIBLE);
            mPreviewView.setVisibility(View.VISIBLE);
        });

        checkButton.setOnClickListener(v -> {
            NewPostModel.getInstance().add(latitude, longitude, picture);
            backButton.performClick();
            ((VincentActivity)getActivity()).goMainPage();
            // TODO renvoyer la photo et la mettre dans le post
            // Utiliser ligne ci-dessous puis passer le bitmap à l'aide d'un intent (je suppose)
            // Drawable bitmap = pictureTaken.getDrawable();
            // Intent intent = new Intent(getContext(), FeedFragment.class);
        });

    }


    @SuppressLint("RestrictedApi")
    private void stopRecording(VideoCapture videoCapture) {
        videoCapture.stopRecording();
        scaleDownAnimation();
        hintTextView.setVisibility(View.VISIBLE);
        textCounter.setVisibility(View.INVISIBLE);
        imgSwitchCamera.setVisibility(View.VISIBLE);
        imgFlashOnOff.setVisibility(View.VISIBLE);
    }

    public String getMediaDirectoryName() {
        String app_folder_path = getActivity().getExternalFilesDir(null).toString() + "/Camera";
        File dir = new File(app_folder_path);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.d("Error", "Failed to create directory");
            }
        }
        return app_folder_path;
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
                bindPreview(cameraProvider);
                initialiserLocalisation();
            } else {
                Toast.makeText(getActivity(), "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
    }

    private void switchCamera() {
        if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
            cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;
        } else {
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
        }
        bindPreview(cameraProvider);
    }

    private void toggleFlash() {
        switch (flashMode) {
            case ImageCapture.FLASH_MODE_ON:
                flashMode = ImageCapture.FLASH_MODE_AUTO;
                imgFlashOnOff.setImageResource(R.drawable.ic_flash_auto);
                break;
            case ImageCapture.FLASH_MODE_AUTO:
                flashMode = ImageCapture.FLASH_MODE_OFF;
                imgFlashOnOff.setImageResource(R.drawable.ic_flash_off);
                break;
            case ImageCapture.FLASH_MODE_OFF:
                flashMode = ImageCapture.FLASH_MODE_ON;
                imgFlashOnOff.setImageResource(R.drawable.ic_flash_on);
                break;
        }
        fadeInAnim.start();
        imageCapture.setFlashMode(flashMode);
    }

    private void scaleUpAnimation() {
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(imgCapture, "scaleX", 2f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(imgCapture, "scaleY", 2f);
        scaleUpX.setDuration(100);
        scaleUpY.setDuration(100);
        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.play(scaleUpX).with(scaleUpY);

        scaleUpX.addUpdateListener(valueAnimator -> {
            View p = (View) imgCapture.getParent();
            p.invalidate();
        });
        scaleDown.start();
    }

    private void scaleDownAnimation() {
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(imgCapture, "scaleX", 1f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(imgCapture, "scaleY", 1f);
        scaleDownX.setDuration(100);
        scaleDownY.setDuration(100);
        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.play(scaleDownX).with(scaleDownY);

        scaleDownX.addUpdateListener(valueAnimator -> {

            View p = (View) imgCapture.getParent();
            p.invalidate();
        });
        scaleDown.start();
    }

    private long timeInMilliseconds = 0L, startTime = SystemClock.uptimeMillis(), updatedTime = 0L, timeSwapBuff = 0L;
    private final Runnable updateTimerThread = new Runnable() {
        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            timeSwapBuff = 0L;
            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;

            secs = secs % 60;
            textCounter.setText(String.format("%02d", mins) + ":" + String.format("%02d", secs));
            if (longClick)
                myHandler.postDelayed(this, 0);
        }
    };

    private final Runnable hidePictureView = new Runnable() {
        public void run() {
            pictureView.setVisibility(View.INVISIBLE);
        }
    };

    private void showPicture(File file) {
        String filePath = file.getPath();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        if (bitmap == null)
            Log.d("Error", "Bitmap is null");
        Bitmap rotatedBitmap = null;
        try {
            ExifInterface exif = new ExifInterface(filePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Matrix matrix = new Matrix();
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
                matrix.postRotate(90);
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
                matrix.postRotate(-90);
            rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA) {
            Matrix mirror = new Matrix();
            mirror.preScale(-1f,1f);
            rotatedBitmap = Bitmap.createBitmap(rotatedBitmap, 0, 0, rotatedBitmap.getWidth(), rotatedBitmap.getHeight(), mirror, true);
        }
        if (rotatedBitmap == null)
            Log.d("Error", "Bitmap is null");
        pictureTaken.setImageBitmap(rotatedBitmap);
        picture = rotatedBitmap;
        mPreviewView.setVisibility(View.INVISIBLE);
        backButton.setVisibility(View.VISIBLE);
        checkButton.setVisibility(View.VISIBLE);
        pictureTaken.setVisibility(View.VISIBLE);
    }

    private void showVideo(File file) {
        String filePath = file.getPath();
        mediaModificationOptions.setVisibility(View.INVISIBLE);
        mPreviewView.setVisibility(View.INVISIBLE);
        backButton.setVisibility(View.VISIBLE);
        checkButton.setVisibility(View.VISIBLE);
        videoTaken.setVisibility(View.VISIBLE);
        MediaController mediacontroller = new MediaController(getActivity());
        mediacontroller.setAnchorView(videoTaken);
        videoTaken.setMediaController(mediacontroller);
        videoTaken.setVideoPath(filePath);
        videoTaken.requestFocus();
        videoTaken.setOnPreparedListener(mp -> videoTaken.start());
        mediacontroller.show(0);
    }

    public boolean isBlocked() {
        return pictureTaken.getVisibility() == View.VISIBLE || videoTaken.getVisibility() == View.VISIBLE || textCounter.getVisibility() == View.VISIBLE;
    }

    public void performBack() {
        if (isBlocked())
            backButton.performClick();
    }

    private void initialiserLocalisation() {
        if (locationManager == null) {
            locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            if (locationManager == null) {
                return;
            }
        }

        Criteria critere = new Criteria();

        // Pour indiquer la précision voulue
        critere.setAccuracy(Criteria.ACCURACY_FINE);

        // Est-ce que le fournisseur doit être capable de donner une altitude ?
        critere.setAltitudeRequired(false);

        // Est-ce que le fournisseur doit être capable de donner une direction ?
        critere.setBearingRequired(false);

        // Est-ce que le fournisseur peut être payant ?
        critere.setCostAllowed(false);

        // Pour indiquer la consommation d'énergie demandée
        critere.setPowerRequirement(Criteria.NO_REQUIREMENT);

        // Est-ce que le fournisseur doit être capable de donner une vitesse ?
        critere.setSpeedRequired(false);

        locationProvider = locationManager.getBestProvider(critere, true);
        if(locationProvider.isEmpty()){
            List<String> providers = locationManager.getProviders(critere, true);
            if(providers.isEmpty()){
                for(String nameProvider : locationManager.getAllProviders()){
                    if(!locationManager.getProvider(nameProvider).hasMonetaryCost()){
                        locationProvider = nameProvider;
                        break;
                    }
                }
                if(locationProvider.isEmpty())
                    throw new RuntimeException("Aucun fournisseru disponible");
            }
            else{
                locationProvider = providers.get(0);
            }
        }

        if (!locationProvider.isEmpty()) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
            }

            locationManager.requestLocationUpdates(locationProvider, 6, 10, new LocationListener() {

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }

                @Override
                public void onLocationChanged(Location location) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            });

        }
    }
}