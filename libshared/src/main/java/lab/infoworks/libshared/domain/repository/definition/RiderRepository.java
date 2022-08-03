package lab.infoworks.libshared.domain.repository.definition;

import android.content.Context;

import java.util.List;
import java.util.function.Consumer;

import lab.infoworks.libshared.domain.model.Rider;
import lab.infoworks.libshared.domain.repository.impl.RiderRepositoryImpl;

public interface RiderRepository {
    static RiderRepository create(Context context) { return new RiderRepositoryImpl(context);}
    void findRiders(Consumer<List<Rider>> consumer);
    void update(Rider rider);
    boolean isEmpty();
    void addSampleData(Context context);
}
