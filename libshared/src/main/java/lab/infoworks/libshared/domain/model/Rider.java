package lab.infoworks.libshared.domain.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;
import java.util.Map;

@Entity(tableName = "rider", ignoredColumns = {"status", "error", "message", "payload", "event", "classType", "_isAutoIncremented"})
public class Rider extends ResponseExt {

    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String name;
    private String geoHash;
    private Integer age;
    private String gender;
    private String email;
    private boolean isSynced = false;
    //
    private List<Map<String, String>> images;
    private boolean isImageSynced = false;

    @Ignore
    public Rider() {}

    @Ignore
    public Rider(String name, String geoHash) {
        this.name = name;
        this.geoHash = geoHash;
    }

    @Ignore
    public Rider(String json) {
        super(json);
    }

    public Rider(Integer id, String name, String geoHash, Integer age, String gender, String email) {
        this.id = id;
        this.name = name;
        this.geoHash = geoHash;
        this.age = age;
        this.gender = gender;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public Rider setName(String name) {
        this.name = name;
        return this;
    }

    public String getGeoHash() {
        return geoHash;
    }

    public Rider setGeoHash(String geoHash) {
        this.geoHash = geoHash;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public Rider setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Rider setEmail(String email) {
        this.email = email;
        return this;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }

    public List<Map<String, String>> getImages() {
        return images;
    }

    public void setImages(List<Map<String, String>> images) {
        this.images = images;
    }

    public boolean isImageSynced() {
        return isImageSynced;
    }

    public void setImageSynced(boolean imageSynced) {
        isImageSynced = imageSynced;
    }
}
