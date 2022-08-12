package lab.infoworks.starter.ui.activities.app;

import android.content.Context;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.HashMap;
import java.util.Map;

import lab.infoworks.libshared.domain.model.Rider;
import lab.infoworks.libshared.domain.remote.RemoteConfig;
import lab.infoworks.libshared.domain.remote.api.RiderApiService;
import lab.infoworks.libshared.domain.remote.interceptors.BearerTokenInterceptor;
import lab.infoworks.libshared.domain.repository.definition.RiderRepository;
import lab.infoworks.libshared.notifications.NotificationCenter;

public class RiderSyncWorker extends Worker {

    public static final String TAG = RiderSyncWorker.class.getSimpleName();
    private String baseUrl;
    private String jwtToken;
    private RiderRepository repository;

    public RiderSyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        String encodedUrl = workerParams.getInputData().getString("baseUrl");
        this.baseUrl = new String(Base64.decode(encodedUrl, Base64.URL_SAFE));
        this.jwtToken = workerParams.getInputData().getString("jwt-token");
    }

    protected RiderRepository getRepository() {
        if (repository == null){
            repository = RiderRepository.create(getApplicationContext());
        }
        return repository;
    }

    @NonNull @Override
    public Result doWork() {
        //
        RiderApiService service = RemoteConfig.getInstance(this.baseUrl + "/rider"
                , RiderApiService.class
                , new BearerTokenInterceptor(this.jwtToken));
        //
        getRepository().findRidersNotSynced((riders) -> {
            //Sync to Remote Service:
            for (Rider rider : riders) {
                if (rider.isSynced()) continue;
                service.update(rider);
                //Update Rider:
                rider.setSynced(true);
                getRepository().update(rider);
            }
        });
        //
        Map<String, Object> data = new HashMap<>();
        data.put("sync", "success");
        NotificationCenter.postNotification(getApplicationContext(), "RIDER_DATA_SYNC", data);
        return Result.success();
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
