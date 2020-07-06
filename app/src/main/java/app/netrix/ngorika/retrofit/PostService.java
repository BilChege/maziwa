package app.netrix.ngorika.retrofit;

import java.util.HashMap;
import java.util.Map;

import app.netrix.ngorika.MaterialRequisition;
import app.netrix.ngorika.pojo.MaterialRequisitionObj;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import retrofit2.Call;

public interface PostService {
    @FormUrlEncoded  @POST("/mobileuser/login")
    void login(@FieldMap Map<String, String> params, Callback<String> callback);
    @FormUrlEncoded    @POST("/routes")
    void routes(@FieldMap Map<String, String> params,Callback<String> callback);
    @FormUrlEncoded     @POST("/farmers")
    void farmers(@FieldMap Map<String, String> params,Callback<String> callback);
    @FormUrlEncoded     @POST("/milkrecords/milkRecord")
    void upload(@FieldMap Map<String, String> params,Callback<String> callback);
    @FormUrlEncoded     @POST("/mobileuser/Allfarmers")
    void syncFarmers(@FieldMap Map<String, String> params,Callback<String> callback);
    @POST("/inventory/materialReq")
    void makeRequisition(@Body MaterialRequisitionObj materialRequisitionObj, Callback<String> callback);
    @GET("/inventory/items")
    void getInventoryItems(@QueryMap HashMap<String,String> params, Callback<String> callback);
    @GET("/inventory/getclasses")
    void getInventoryClasses(Callback<String> callback);
    @GET("/inventory/itemqty/{id}")
    void getItemQty(@Path("id") int id,Callback<String> callback);
}