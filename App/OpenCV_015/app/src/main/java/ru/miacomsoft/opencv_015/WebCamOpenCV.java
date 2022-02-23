package ru.miacomsoft.opencv_015;

import android.view.SurfaceView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;


/**
 *  build.gradle
 *    implementation 'com.quickbirdstudios:opencv:3.4.1'  // openCV
 *    implementation 'com.quickbirdstudios:opencv:4.5.0'
 *
 *
 *  activity_main.xml:
 *     -//-
 *     <org.opencv.android.JavaCameraView
 *         android:id="@+id/CameraView"
 *         android:layout_width="match_parent"
 *         android:layout_height="match_parent"
 *         android:visibility="visible"
 *         tools:layout_editor_absoluteX="0dp"
 *         tools:layout_editor_absoluteY="34dp" />
 *     -//-
 *
 *   MainActivity:
 *
 *    WebCamOpenCV cam;
 *    CameraBridgeViewBase cameraBridgeViewBase;
 *
 *    if (cameraBridgeViewBase != null) {
 *        cameraBridgeViewBase.disableView();
 *    } else {
 *        cameraBridgeViewBase = (JavaCameraView) findViewById(R.id.CameraView);
 *        cam = new WebCamOpenCV(this,  cameraBridgeViewBase);
 *    }
 *    cam.loopFrame(MainActivity::MyFrame); // Обработка кадров
 *    cam.start(CamId);
 *
 *
 *     // Обработка кадров
 *     public static Mat MyFrame(Mat frame) {
 *         Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGBA2GRAY);
 *         Imgproc.Canny(frame, frame, 80, 60);
 *         return frame;
 *     }
 *
 *
 */
public class WebCamOpenCV implements CameraBridgeViewBase.CvCameraViewListener2{
    public interface CallbackFrame {
        public Mat call( Mat frame );
    }

    private AppCompatActivity appCompatActivity;
    private CameraBridgeViewBase cameraBridgeViewBase;
    private BaseLoaderCallback baseLoaderCallback;
    private boolean startCanny = false;
    private static final int CAMERA_ID_FRONT = 1;
    private static final int MAX_WIDTH = 800;
    private static final int MAX_HEIGHT = 600;


    private CallbackFrame callbackFrame = null;

    public WebCamOpenCV(AppCompatActivity appCompatActivity , CameraBridgeViewBase cameraBridgeViewBase) {
        this.appCompatActivity = appCompatActivity;
        this.cameraBridgeViewBase = cameraBridgeViewBase;
    }

    public void loopFrame(CallbackFrame callbackFrame ){
        this.callbackFrame = callbackFrame;
    }

    public void start(int CamId){
        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        cameraBridgeViewBase.setMaxFrameSize(MAX_WIDTH, MAX_HEIGHT);
        // *************** Выбрать камеру *****************
        // cameraBridgeViewBase.setCameraIndex(CAMERA_ID_FRONT); // Выбрать камеру
        cameraBridgeViewBase.setCameraIndex(CamId); // Выбрать камеру
        // ************************************************
        cameraBridgeViewBase.setCvCameraViewListener(this);
        // System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        baseLoaderCallback = new BaseLoaderCallback(appCompatActivity) {
            @Override
            public void onManagerConnected(int status) {
                super.onManagerConnected(status);
                switch (status) {
                    case BaseLoaderCallback.SUCCESS:
                        cameraBridgeViewBase.enableView();
                        break;
                    default:
                        super.onManagerConnected(status);
                        break;
                }
            }
        };
        baseLoaderCallback.onManagerConnected(baseLoaderCallback.SUCCESS);
    }

    /**
     * Обработка фрэймов (кадров)
     *  https://stackoverflow.com/questions/47497345/how-to-display-opencv-javacameraview-front-camera-in-mirror-image-android
     * @param inputFrame
     * @return
     */
    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat frame = inputFrame.rgba();
        if (callbackFrame!=null){
           return callbackFrame.call(frame);
        }
        //if (startCanny == true) {
        //    Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGBA2GRAY);
        //    //Imgproc.Canny(frame, frame, 100, 80);
        //    Imgproc.Canny(frame, frame, 80, 60);
        //}
        //if (rotat_180) {
        //    Core.rotate(frame, frame, Core.ROTATE_180);
        //}
        /*
        https://stackoverflow.com/questions/47497345/how-to-display-opencv-javacameraview-front-camera-in-mirror-image-android
        Mat rgba = inputFrame.rgba();
        Core.flip(rgba, rgba, 1);
        return rgba;
         */
        return frame;
    }


    @Override
    public void onCameraViewStarted(int width, int height) {

    }


    @Override
    public void onCameraViewStopped() {

    }


    protected void onResume() {
        if (!OpenCVLoader.initDebug()) {
            Toast.makeText(appCompatActivity, "There's a problem, yo!", Toast.LENGTH_SHORT).show();
        } else {
            if (baseLoaderCallback!=null) {
                baseLoaderCallback.onManagerConnected(baseLoaderCallback.SUCCESS);
            }
        }


    }

    protected void onPause() {
        // super.onPause();
        if (cameraBridgeViewBase != null) {
            cameraBridgeViewBase.disableView();
        }

    }


    protected void onDestroy() {
        // super.onDestroy();
        if (cameraBridgeViewBase != null) {
            cameraBridgeViewBase.disableView();
        }
    }
}
