package lab.infoworks.libshared.repository.impl;

import android.content.Context;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.RequiresApi;

import com.it.soul.lab.data.base.DataSource;
import com.it.soul.lab.data.base.DataStorage;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import lab.infoworks.libshared.datasource.RiderDataSource;
import lab.infoworks.libshared.datasource.SampleData;
import lab.infoworks.libshared.model.Rider;
import lab.infoworks.libshared.repository.definition.RiderRepository;

public class RiderRepositoryImpl implements RiderRepository {

    private final DataSource<Integer, Rider> dataSource;

    public RiderRepositoryImpl(Context context) {
        this.dataSource = new RiderDataSource(context);
    }

    @Override @RequiresApi(Build.VERSION_CODES.N)
    public void findRiders(Consumer<List<Rider>> consumer) {
        if (consumer == null) return;
        final int maxItem = dataSource.size();
        //TODO: For Simulating Network: Delay-1 sec
        new Handler().postDelayed(() -> {
                    //Calling API:
                    dataSource.readAsync(0, maxItem, (riders) ->
                            consumer.accept(Arrays.asList(riders))
                    );
                }
                , 1000);
    }

    @Override
    public void addSampleData() {
        for (Rider rider : SampleData.getRiders()) {
            dataSource.add(rider);
        }
        ((DataStorage)dataSource).save(true);
    }

    @Override
    public boolean isEmpty() {
        return dataSource.size() <= 0;
    }
}