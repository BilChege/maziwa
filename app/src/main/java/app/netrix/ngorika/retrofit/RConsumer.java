package app.netrix.ngorika.retrofit;


import android.app.ProgressDialog;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import app.netrix.ngorika.pojo.MaterialRequisitionObj;
import app.netrix.ngorika.utils.ActionError;
import app.netrix.ngorika.utils.ActionSuccess;

/**
 * Created by chrissie on 12/3/15.
 */
public class RConsumer {
    public static void login(Context context,final String user,final String password,final ActionSuccess success,final ActionError err){
        Map<String,String>params=new HashMap<>();
        params.put("username",user);
        params.put("password",password);
        ConnectService.request(context,params,success,err,null);
    }
    public static void getRoutes(Context context,final ActionSuccess success,final ActionError err){
        Map<String,String>params=new HashMap<>();
        params.put("route","route");
        ConnectService.requestRoutes(context,params, success, err, null);

    }
    public static void getFarmers(Context context,int route,final ActionSuccess success,final ActionError err){
        Map<String,String>params=new HashMap<>();
        params.put("route", route + "");
        ConnectService.requestFarmers(context,params, success, err, null);

    }
    public static void getAllFarmers(Context context,int route,final ActionSuccess success,final ActionError err,ProgressDialog dialog){
        Map<String,String>params=new HashMap<>();
        params.put("route", route + "");
        ConnectService.syncAllFarmers(context,params, success, err, dialog);

    }
    public static void uploadRecord(Context context,int user,int route,int farmer,String weight,int shift,int cannumber,boolean alcohal,boolean peroxide,final ActionSuccess success,final ActionError err){
        Map<String,String>params=new HashMap<>();
        params.put("grader",user+"");
        params.put("farmer",farmer+"");
        params.put("weight",weight);
        params.put("bkNo",System.currentTimeMillis()+"");
        params.put("user",user+"");
        params.put("alcohal",alcohal ? "true" : "false");
        params.put("peroxide",peroxide ? "true" : "false");
        params.put("shift",String.valueOf(shift));
        params.put("cannumber",String.valueOf(cannumber));
        ConnectService.uploadRecord(context,params, success, err, null);
    }
    public static void getItemQuantity(Context context,int id,final ActionSuccess success,final ActionError error,ProgressDialog dialog){
        ConnectService.getItemQuantity(context,id,success,error,dialog);
    }
    public static void getInventoryItems(Context context, HashMap<String,String> params,final ActionSuccess success, final ActionError error, ProgressDialog dialog){
        ConnectService.getInventoryItems(context,params,success,error,dialog);
    }
    public static void getInventoryClasses(Context context,final ActionSuccess success,final ActionError error,ProgressDialog dialog){
        ConnectService.getInventoryClasses(context,success,error,dialog);
    }
    public static void postMRequisition(Context context, MaterialRequisitionObj materialRequisitionObj, final ActionSuccess success, final ActionError error, ProgressDialog dialog){
        ConnectService.postMaterialReq(context, materialRequisitionObj,success,error,dialog);
    }
}
