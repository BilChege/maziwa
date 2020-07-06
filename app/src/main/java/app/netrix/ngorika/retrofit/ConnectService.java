package app.netrix.ngorika.retrofit;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


import app.netrix.ngorika.pojo.MaterialRequisitionObj;
import app.netrix.ngorika.utils.ActionError;
import app.netrix.ngorika.utils.ActionSuccess;
import app.netrix.ngorika.utils.StringConverter;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;


public class ConnectService {



	public static PostService getPostService() {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(2, TimeUnit.MINUTES);
        okHttpClient.setConnectTimeout(2, TimeUnit.MINUTES);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Connect.SERVER_LOCAL)
                .setConverter(new StringConverter())
                .setClient(new OkClient(okHttpClient))
                .build();
        return restAdapter.create(PostService.class);
    }

    public static PostService2 getNetClient(){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        okhttp3.OkHttpClient okHttpClient = new okhttp3.OkHttpClient.Builder().readTimeout(60,TimeUnit.SECONDS).connectTimeout(60,TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Connect.SERVER_LOCAL).addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        return retrofit.create(PostService2.class);
    }

    public static void request(final Context context, Map<String, String> params, final ActionSuccess success, final ActionError err, final ProgressDialog dialog){
        PostService service = getPostService();
       try{
           service.login(params, new Callback<String>() {
               @Override
               public void success(String json, Response response) {
                   if (dialog != null)
                       dialog.dismiss();
                   if (response != null){
                       int status = response.getStatus();
                       if (status == 200){
                           if (json != null) {
                               success.action(json);
                           } else {
                               if (err != null)
                                   err.action();
                           }
                       } else {
                           Toast.makeText(context,"Error "+status+" occurred",Toast.LENGTH_SHORT).show();
                       }
                   } else {
                       Toast.makeText(context,"There was no response from the server",Toast.LENGTH_SHORT).show();
                   }
               }

               @Override
               public void failure(RetrofitError error) {
                   System.out.println(" ------------------ > RETROFIT ERROR");
                   Toast.makeText(context,"Error occurred :"+error.getMessage(),Toast.LENGTH_SHORT).show();
                   Log.e(TAG, "failure: ",error );
                   if (dialog != null)
                       dialog.dismiss();
                   if (err != null)
                       err.action();
               }
           });
       }
       catch (Exception e){
           e.printStackTrace();
       }
       }
    public static void requestRoutes(final Context context, Map<String, String> params, final ActionSuccess success, final ActionError err, final ProgressDialog dialog){
        PostService service = getPostService();
        try{
            service.routes(params, new Callback<String>() {
                @Override
                public void success(String json, Response response) {
                    if (dialog != null)
                        dialog.dismiss();
                    if (response != null){
                        int status = response.getStatus();
                        if (status == 200){
                            if (json != null) {
                                success.action(json);
                            } else {
                                if (err != null)
                                    err.action();
                            }
                        } else {
                            Toast.makeText(context,"Error "+status+" occurred while fetching routes",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context,"There was no response from the service",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    if (dialog != null)
                        dialog.dismiss();
                    if (err != null)
                        err.action();
                    Toast.makeText(context,"Error occurred "+error.getMessage(),Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "failure: ", error);
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void requestFarmers(final Context context, Map<String, String> params, final ActionSuccess success, final ActionError err, final ProgressDialog dialog){
        PostService service = getPostService();
        try{
            service.farmers(params, new Callback<String>() {
                @Override
                public void success(String json, Response response) {
                    if (dialog != null)
                        dialog.dismiss();
                    if (response != null){
                        int status = response.getStatus();
                        if (status == 200){
                            if (json != null) {
                                success.action(json);
                            } else {
                                if (err != null)
                                    err.action();
                            }
                        } else {
                            Toast.makeText(context,"Error "+status+" occurred while fetching farmers",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context,"There was no response from the server",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    if (dialog != null)
                        dialog.dismiss();
                    if (err != null)
                        err.action();
                    Toast.makeText(context,"Error occurred : "+error.getMessage()+" while fetching farmers",Toast.LENGTH_SHORT).show();
                    Log.d("Routes", "Routes:Error" + error.toString());
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void uploadRecord(final Context context, Map<String, String> params, final ActionSuccess success, final ActionError err, final ProgressDialog dialog){
        PostService service = getPostService();
        try{
            service.upload(params, new Callback<String>() {
                @Override
                public void success(String json, Response response) {
                    if (dialog != null)
                        dialog.dismiss();
                    if (response != null){
                        int status = response.getStatus();
                        if (status == 200){
                            if (json != null) {
                                success.action(json);
                            } else {
                                if (err != null)

                                    err.action();
                            }
                        } else {
                            Toast.makeText(context,"Error "+status+" occurred",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context,"There was no response from the server",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d("haha", "haha:Error3" + error.toString());
                    if (dialog != null)
                        dialog.dismiss();
                    if (err != null)
                        err.action();
                    Toast.makeText(context,"Error occurred : "+error.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void postMaterialReq(final Context context, MaterialRequisitionObj materialRequisitionObj, final ActionSuccess success, final ActionError error, final ProgressDialog dialog){
        System.out.println(" --------->>> REQUEST MADE TO POST MATERIAL REQUISITION");
	    PostService postService = getPostService();
	    try{
	        postService.makeRequisition(materialRequisitionObj, new Callback<String>() {
                @Override
                public void success(String s, Response response) {
                    System.out.println(" --------->>> RESPONSE MADE TO POST MATERIAL REQUISITION SUCCESS");
                    if (dialog != null && dialog.isShowing()){
                        dialog.dismiss();
                    }
                    if (response != null){
                        int statusCode = response.getStatus();
                        if (statusCode == 200){
                            success.action(s);
                        } else {
                            Toast.makeText(context,"Error "+statusCode+" occurred while making requisition",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context,"There was no response from the server",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    if (dialog != null && dialog.isShowing()){
                        dialog.dismiss();
                    }
                    Toast.makeText(context,"Error occurred : "+error.getMessage(),Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "failure: ", error);
                }
            });
        } catch (Exception e){
	        e.printStackTrace();
        }
	}

	public static void getInventoryClasses(final Context context,final ActionSuccess success,final ActionError error,final ProgressDialog dialog){
	    PostService postService = getPostService();
	    try {
	        postService.getInventoryClasses(new Callback<String>() {
                @Override
                public void success(String s, Response response) {
                    if (dialog != null && dialog.isShowing()){
                        dialog.dismiss();
                    }
                    if (response != null){
                        int responseCode = response.getStatus();
                        if (responseCode == 200){
                            success.action(s);
                        } else {
                            Toast.makeText(context,"Error "+responseCode+" occurred while fetching inventory classes",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context,"There was no response from the server",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    if (dialog != null && dialog.isShowing()){
                        dialog.dismiss();
                    }
                    Toast.makeText(context,"Error occurred :"+error.getMessage()+" while fetching inventory classes",Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "failure: ", error);
                }
            });
        } catch (Exception e){
	        e.printStackTrace();
        }
    }

    public static void getItemQuantity(final Context context,int id,final ActionSuccess success,final ActionError error,final ProgressDialog dialog){
	    PostService postService = getPostService();
	    try {
	        postService.getItemQty(id, new Callback<String>() {
                @Override
                public void success(String s, Response response) {
                    if (dialog != null && dialog.isShowing()){
                        dialog.dismiss();
                    }
                    if (response != null){
                        int status = response.getStatus();
                        if (status == 200){
                            success.action(s);
                        } else {
                            Toast.makeText(context,"Error "+status+" occurred while checking quantity",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context,"There was no response from the service",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    if (dialog != null && dialog.isShowing()){
                        dialog.dismiss();
                    }
                    Toast.makeText(context,"Error occurred: "+error.getMessage()+" while getting item quantity",Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "failure: ", error);
                }
            });
        } catch (Exception e){
	        e.printStackTrace();
        }
    }

    public static void getInventoryItems(final Context context, HashMap<String,String> params, final ActionSuccess success, final ActionError error, final ProgressDialog dialog){
	    PostService postService = getPostService();
	    try {
	        postService.getInventoryItems(params,new Callback<String>() {
                @Override
                public void success(String s, Response response) {
                    if (dialog != null && dialog.isShowing()){
                        dialog.dismiss();
                    }
                    if (response != null){
                        int statusCode = response.getStatus();
                        if (statusCode == 200){
                            success.action(s);
                        } else {
                            Toast.makeText(context,"Error "+statusCode+" Occurred while fetching inventory items",Toast.LENGTH_SHORT).show();
                            System.out.println("Error "+statusCode+" Occurred");
                        }
                    } else {
                        Toast.makeText(context,"There was no response from the server",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    if (dialog != null && dialog.isShowing()){
                        dialog.dismiss();
                    }
                    Toast.makeText(context,"Error occurred: "+error.getMessage()+" while fetching inventory items",Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "failure: ",error );
                }
            });
        } catch (Exception e){
	        e.printStackTrace();
        }
    }

    public static void syncAllFarmers(final Context context, Map<String, String> params, final ActionSuccess success, final ActionError err, final ProgressDialog dialog){
        PostService service = getPostService();
        try{
            service.syncFarmers(params,new Callback<String>() {
                @Override
                public void success(String json, Response response) {
                    if (dialog != null)
                        dialog.dismiss();
                    if (response != null){
                        int status = response.getStatus();
                        if (status == 200){
                            if (json != null) {
                                success.action(json);
                            } else {
                                if (err != null)
                                    err.action();
                            }
                        } else {
                            Toast.makeText(context,"Error "+status+" occurred while fetching farmers",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context,"There was no response from the server",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(context,"Failure occurred "+error.getMessage()+" while fetching farmers",Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "failure: ", error);
                    if (dialog != null)
                        dialog.dismiss();
                    if (err != null)
                        err.action();
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
