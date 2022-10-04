package lab.infoworks.starter.ui.activities.app;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;

import com.infoworks.lab.rest.models.SearchQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lab.infoworks.libshared.domain.model.Rider;
import lab.infoworks.libshared.domain.remote.RemoteConfig;
import lab.infoworks.libshared.domain.remote.api.FilesApiService;
import lab.infoworks.libshared.domain.remote.interceptors.BearerTokenInterceptor;
import lab.infoworks.libshared.notifications.NotificationCenter;
import lab.infoworks.starter.BuildConfig;

public class RiderImageSyncWorker extends RiderSyncWorker {

    public static final String TAG = RiderImageSyncWorker.class.getSimpleName();

    public RiderImageSyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull @Override
    public Result doWork() {
        //
        //RemoteConfig.activateSslCertificatePinner(BuildConfig.domain_name, BuildConfig.ssl_public_key);
        FilesApiService fileService = RemoteConfig.getInstance(getBaseUrl() + "/files"
                , FilesApiService.class
                , new BearerTokenInterceptor(getJwtToken()));
        //
        getRepository().findRidersNotSynced((riders) -> {
            //Sync to Remote Service:
            for (Rider rider : riders) {
                if (rider.isImageSynced()) continue;
                //
                List<Map<String, String>> images = rider.getImages();
                for (Map<String, String> image : images) {
                    SearchQuery query = new SearchQuery();
                    for (Map.Entry<String, String> entry : image.entrySet()){
                        query.add(entry.getKey()).isEqualTo(entry.getValue());
                    }
                    fileService.uploadBase64Image(query);
                }
                //Update Rider:
                rider.setImageSynced(true);
                getRepository().update(rider);
            }
        });
        //
        Map<String, Object> data = new HashMap<>();
        data.put("sync", "success");
        NotificationCenter.postNotification(getApplicationContext(), "RIDER_IMAGE_SYNC", data);
        return Result.success();
    }
}
