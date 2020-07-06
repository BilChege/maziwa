package app.netrix.ngorika.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import app.netrix.ngorika.data.DatabaseHandler;
import app.netrix.ngorika.pojo.DataBundle;
import app.netrix.ngorika.retrofit.RConsumer;
import app.netrix.ngorika.utils.ActionError;
import app.netrix.ngorika.utils.ActionSuccess;

/**
 * Created by CW on 31/12/2015.
 */
public class UploadDataService extends AsyncTask<Void, Void, String> {
    ProgressDialog pd;
    DatabaseHandler db;
    Context context;
    ProgressDialog progressDialog;
    String response = null;
    int value = 0;
    public UploadDataService(Context context){
        db=new DatabaseHandler(context);
        this.context=context;
    }
    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        progressDialog=new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("uploading...");
        progressDialog.show();
    }
    @Override
    protected String doInBackground(Void... params) {


        ArrayList<DataBundle> lstData=db.getunsentRecords();
        try{

            if (lstData != null && lstData.size() > 0)
            {
                for (DataBundle record : lstData){
                    final int recordID=record.getRecordId();
                    RConsumer.uploadRecord(context,record.getUserID(), record.getRouteId(), record.getFarmerID(), record.getWeight(),record.getShift(),record.getCannumber(),record.isAlcohal(),record.isPeroxide(), new ActionSuccess() {
                        @Override
                        public void action(String json) {

                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                boolean error = jsonObject.getBoolean("error");
                                String message = jsonObject.getString("message");

                                if (!error) {
                                    //upload was good,update
                                    db.updateUploadStat(recordID);
                                    value++;
                                    response=value+"";
                                } else {
                                    //record hit server but could not be inserted
//                                    Log.d("Upload Service", "Upload Service Rejected Worked");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new ActionError() {
                        @Override
                        public void action() {
                            //An error occured while uploading eg network
//                            Log.d("Upload Service", "Upload Service Failed");
                        }
                    });
                }
            }
            else{

                response="none";
            }


        }
        catch(Exception e){
            e.printStackTrace();
        }


        return response;

    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        progressDialog.dismiss();
        if(result!=null) {
            if (result.equals("none")) {
                Toast.makeText(context, "No Unsent Records", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, result + "Records uploaded", Toast.LENGTH_LONG).show();
            }
        }
    }

}