package lab.infoworks.libshared.domain.remote.api;

import com.infoworks.lab.rest.models.SearchQuery;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FilesApiService {

    @POST("/upload/image/base64")
    Call<Map> uploadBase64Image(@Body SearchQuery query);

    @GET("/download/image/base64")
    Call<Map> downloadBase64Image(@Query("fileName") String filename);
}
