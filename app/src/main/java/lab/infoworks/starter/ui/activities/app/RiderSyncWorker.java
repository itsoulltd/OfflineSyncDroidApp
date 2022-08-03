package lab.infoworks.starter.ui.activities.app;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.HashMap;
import java.util.Map;

import lab.infoworks.libshared.domain.repository.definition.RiderRepository;
import lab.infoworks.libshared.notifications.NotificationCenter;

public class RiderSyncWorker extends Worker {

    public static final String TAG = RiderSyncWorker.class.getSimpleName();
    private String baseUrl;
    private String jwtToken;
    private RiderRepository repository;

    public RiderSyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.baseUrl = workerParams.getInputData().getString("baseUrl");
        this.jwtToken = workerParams.getInputData().getString("jwt-token");
    }

    public RiderRepository getRepository() {
        if (repository == null){
            repository = RiderRepository.create(getApplicationContext());
        }
        return repository;
    }

    @NonNull
    @Override
    public Result doWork() {
        //TODO:
        Map<String, Object> data = new HashMap<>();
        data.put("sync", "success");
        NotificationCenter.postNotification(getApplicationContext(), "RIDER_DATA_SYNC", data);
        return Result.success();
    }
}
