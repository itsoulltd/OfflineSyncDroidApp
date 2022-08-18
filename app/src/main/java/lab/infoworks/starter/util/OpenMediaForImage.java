package lab.infoworks.starter.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class OpenMediaForImage implements View.OnClickListener {

    private static final String TAG = "OpenMediaForImage";
    public static final int PERMISSION_REQUEST_OPEN_MEDIA = 5;
    public static final int REQUEST_CODE_OPEN_MEDIA_FOR_IMAGE = 105;
    protected Context context;

    protected Activity getActivity(){
        assert (context instanceof Activity);
        return ((Activity)context);
    }

    public OpenMediaForImage(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            getActivity().startActivityForResult(intent, REQUEST_CODE_OPEN_MEDIA_FOR_IMAGE);
        } catch (Exception e) {
            Log.d(TAG, "onClick: " + e.getMessage());
            Toast.makeText(context
                    ,"আপনার মোবাইলে কোন ক্যামেরা অ্যাাপ পাওয়া যায়নি। অনুগ্রহ করে যেকোন একটি ক্যামেরা অ্যাাপ ইন্সটল করে আবার চেস্টা করুন।"
                    , Toast.LENGTH_SHORT);
        }
    }
}
