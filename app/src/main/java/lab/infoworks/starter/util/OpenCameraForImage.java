package lab.infoworks.starter.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;

import androidx.core.content.ContextCompat;

import java.util.HashMap;
import java.util.Map;

import lab.infoworks.libshared.notifications.NotificationCenter;
import lab.infoworks.libshared.notifications.NotificationType;

public class OpenCameraForImage implements View.OnClickListener {

    private static final String TAG = "OpenCameraForImage";
    public static final int PERMISSION_REQUEST_OPEN_CAMERA = 3;
    public static final int REQUEST_CODE_OPEN_CAMERA_FOR_IMAGE = 103;
    protected Context context;

    protected Activity getActivity(){
        assert (context instanceof Activity);
        return ((Activity)context);
    }

    public OpenCameraForImage(Context context) {
        this.context = context;
    }

    protected void startCamera() {
        if (ContextCompat.checkSelfPermission(this.context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getActivity().requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_OPEN_CAMERA);
            }
        }else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                getActivity().startActivityForResult(takePictureIntent, REQUEST_CODE_OPEN_CAMERA_FOR_IMAGE);
            }
        }
    }

    @Override
    public void onClick(View view) {
        Map<String, Object> data = new HashMap<>();
        NotificationCenter.postNotification(context, NotificationType.OpenCameraForImage.name(), data);
        startCamera();
    }
}
