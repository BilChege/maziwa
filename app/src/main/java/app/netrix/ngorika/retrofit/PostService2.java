package app.netrix.ngorika.retrofit;

import org.json.JSONObject;

import java.util.List;

import app.netrix.ngorika.pojo.MaterialRequisitionObj;
import app.netrix.ngorika.pojo.MilkCan;
import app.netrix.ngorika.pojo.MrResponse;
import app.netrix.ngorika.pojo.Workshift;
import retrofit2.Call;
import retrofit2.http.GET;

public interface PostService2 {

    @retrofit2.http.POST("inventory/materialReq")
    Call<MrResponse> makeRequisition(@retrofit2.http.Body MaterialRequisitionObj materialRequisition);
    @GET("milkrecords/milkcans")
    Call<List<MilkCan>> getMilkCans();
    @GET("milkrecords/workshifts")
    Call<List<Workshift>> getWorkShifts();
}