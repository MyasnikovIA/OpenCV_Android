package ru.miacomsoft.opencv_015;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity {
    WebCamOpenCV cam;
    CameraBridgeViewBase cameraBridgeViewBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!PermissionHelper.cameraPermissionsGranted(this)) {
            PermissionHelper.checkAndRequestCameraPermissions(this);
        } else {
            start(1);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        cam.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cam.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        cam.onDestroy();
    }


    public void start(int CamId) {
        if (cameraBridgeViewBase != null) {
            cameraBridgeViewBase.disableView();
        } else {
            cameraBridgeViewBase = (JavaCameraView) findViewById(R.id.CameraView);
            cam = new WebCamOpenCV(this, cameraBridgeViewBase);
        }
        cam.loopFrame(MainActivity::MyFrame);
        cam.start(CamId);
    }


    public static Mat MyFrame(Mat frame) {
         Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGBA2GRAY);
         Imgproc.Canny(frame, frame, 80, 60);
         return frame;
     }



}