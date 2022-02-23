# OpenCV_Android
Кукла для проектов OpenCV на Android


<img src="https://github.com/MyasnikovIA/OpenCV_Android/blob/main/img/foto.png?raw=true"/>


Запуск на TV приставки: https://youtu.be/-4sQv0wLtrU <br>
[![](http://img.youtube.com/vi/QrpkW_bTKHI/0.jpg)](https://youtu.be/-4sQv0wLtrU "")

Запуск на HUAWEI телефоне: https://youtu.be/lI21Y_gO5IU <br>
[![](http://img.youtube.com/vi/QrpkW_bTKHI/0.jpg)](https://youtu.be/lI21Y_gO5IU "")


build.gradle:
```
dependencies {
     ***
    implementation 'com.quickbirdstudios:opencv:3.4.1'  // openCV
     ***
}
```

manifest:
```xml

    ***   
    <uses-permission android:name="android.permission.CAMERA"/>
    <uses-feature android:name="android.hardware.camera" android:required="false"/>
    <uses-feature android:name="android.hardware.camera.autofocus" android:required="false"/>
    <uses-feature android:name="android.hardware.camera.front" android:required="false"/>
    <uses-feature android:name="android.hardware.camera.front.autofocus" android:required="false"/>
	***
	
```

activity_main.xml:
```xml

    <org.opencv.android.JavaCameraView
        android:id="@+id/CameraView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:visibility="visible"
        tools:layout_editor_absoluteX="0dp"
        tools:layout_editor_absoluteY="34dp" />
		
```

MainActivity:
```java

   WebCamOpenCV cam;
    CameraBridgeViewBase cameraBridgeViewBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //получить разрешение на использование камеры
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
```