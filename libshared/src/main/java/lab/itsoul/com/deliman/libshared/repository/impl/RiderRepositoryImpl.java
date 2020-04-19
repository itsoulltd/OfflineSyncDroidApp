package lab.itsoul.com.deliman.libshared.repository.impl;

import com.it.soul.lab.data.base.DataSource;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import lab.itsoul.com.deliman.libshared.datasource.RiderDataSource;
import lab.itsoul.com.deliman.libshared.model.Rider;
import lab.itsoul.com.deliman.libshared.repository.definition.RiderRepository;

public class RiderRepositoryImpl implements RiderRepository {

    private DataSource<Integer, Rider> dataSource = new RiderDataSource();

    public RiderRepositoryImpl() {}

    @Override
    public void findRiders(Consumer<List<Rider>> consumer) {
        if (consumer == null) return;
        int maxItem = dataSource.size();
        dataSource.readAsynch(0, maxItem, (riders) ->
                consumer.accept(Arrays.asList(riders))
        );
    }
}
