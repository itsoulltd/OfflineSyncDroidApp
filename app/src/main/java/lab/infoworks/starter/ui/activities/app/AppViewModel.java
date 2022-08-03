package lab.infoworks.starter.ui.activities.app;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.util.List;

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
        Data data = new Data.Builder()
                .putString("baseUrl", baseUrl)
                .putString("jwt-token", "---")
                .build();
        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
        //Example of OneTime WorkerRequest:
        WorkRequest request = new OneTimeWorkRequest.Builder(RiderSyncWorker.class)
                .setInputData(data)
                .addTag("getPhotos")
                .setConstraints(constraints)
                .build();
        //Example of Repeatable WorkRequest:
        /*request = new PeriodicWorkRequest.Builder(EncryptedFileFetchingWorker.class, 15, TimeUnit.MINUTES)
                .setInputData(data)
                .addTag("getPhotos")
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance(getContext()).cancelAllWorkByTag("getPhotos");*/
        //
        WorkManager.getInstance(getApplication().getApplicationContext()).enqueue(request);
    }
}
