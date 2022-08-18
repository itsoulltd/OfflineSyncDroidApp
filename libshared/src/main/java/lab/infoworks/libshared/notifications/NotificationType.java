package lab.infoworks.libshared.notifications;

import com.google.gson.internal.bind.JsonTreeWriter;

public enum NotificationType {
    SIGN_IN_SUCCESSFULL,
    SIGN_IN_FAILED,
    SIGN_OUT_SUCCESSFULL,
    SIGN_OUT_FAILED,
    FORCE_SIGN_OUT,
    ACTION_PUSH_NOTIFICATION,
    OpenCameraForImage;
}
