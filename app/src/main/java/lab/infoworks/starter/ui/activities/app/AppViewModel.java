package lab.infoworks.starter.ui.activities.app;

import android.app.Application;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lab.infoworks.libshared.domain.model.Rider;
import lab.infoworks.libshared.domain.model.VerificationResult;
import lab.infoworks.libshared.domain.repository.definition.RiderRepository;

public class AppViewModel extends AndroidViewModel {

    private MutableLiveData<VerificationResult> userStatusLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Rider>> riderLiveData = new MutableLiveData<>();

    private RiderRepository riderRepository = RiderRepository.create(getApplication().getApplicationContext());

    public AppViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<VerificationResult> getUserStatusObservable() {
        return userStatusLiveData;
    }

    public void verifyUser() {
        if(riderRepository.isEmpty()) riderRepository.addSampleData(getApplication());
        userStatusLiveData.postValue(new VerificationResult(true));
    }

    public LiveData<List<Rider>> getRiderObservable() {
        return riderLiveData;
    }

    public void findRiders() {
        riderRepository.findRiders((riders) -> riderLiveData.postValue(riders));
    }

    public void offlineSyncRider(String baseUrl){
        String encodedBaseUrl = Base64.encodeToString(baseUrl.getBytes(StandardCharsets.UTF_8)
                , Base64.URL_SAFE);
        Data data = new Data.Builder()
                .putString("baseUrl", encodedBaseUrl)
                .putString("jwt-token", "---")
                .build();
        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
        //Example of Repeatable WorkRequest:
        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(RiderSyncWorker.class, 15, TimeUnit.MINUTES)
                .setInputData(data)
                .addTag("sync_rider")
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance(getApplication().getApplicationContext()).enqueueUniquePeriodicWork("sync_rider"
                , ExistingPeriodicWorkPolicy.KEEP
                , request);
    }

    public void offlineSyncRiderImages(String baseUrl){
        String encodedBaseUrl = Base64.encodeToString(baseUrl.getBytes(StandardCharsets.UTF_8)
                , Base64.URL_SAFE);
        Data data = new Data.Builder()
                .putString("baseUrl", encodedBaseUrl)
                .putString("jwt-token", "---")
                .build();
        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
        //
        //Example of OneTime WorkerRequest:
        WorkRequest request = new OneTimeWorkRequest.Builder(RiderImageSyncWorker.class)
                .setInputData(data)
                .addTag("sync_rider_imae")
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance(getApplication().getApplicationContext()).cancelAllWorkByTag("sync_rider_image");
        WorkManager.getInstance(getApplication().getApplicationContext()).enqueue(request);
        //
    }
}
