package app.netrix.ngorika.services;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;

import app.netrix.ngorika.data.DatabaseHandler;
import app.netrix.ngorika.pojo.DataBundle;
import app.netrix.ngorika.retrofit.Connect;
import app.netrix.ngorika.retrofit.RConsumer;
import app.netrix.ngorika.utils.ActionError;
import app.netrix.ngorika.utils.ActionSuccess;


public class ServiceRequests extends IntentService {

    public static final String ACTION_UPLOAD_REG = "u_r";
	private Intent sender;
	private CountDownTimer countTime;
    private DatabaseHandler db;
    private Upload uploadTask;
    public ServiceRequests() {
        super("UploadRecord");
    }

    @Override
	public void onCreate()
	{
        Log.d("Service Request","Service Request On Create ");
		sender = new Intent();
		db=new DatabaseHandler(this);

	}
    @Override
    protected void onHandleIntent(Intent intent) {
    count();
    }
	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}
	@Override
	public void onStart(Intent intent, int startId)
	{
		count();
	}
	@Override
	public void onDestroy()
	{
		if (uploadTask != null)
			uploadTask.cancel(true);

		super.onDestroy();
	}
	private void count()
	{
		if (countTime != null)
			countTime.cancel();
		countTime = new CountDownTimer(60000, 60000)
		{
			@Override
			public void onTick(long arg0)
			{
			}

			@Override
			public void onFinish()
			{
				if ( Connect.isNetworkAvailable(ServiceRequests.this))
				{
					
					if( uploadTask!=null)
                        uploadTask.cancel(true);
                    uploadTask=new Upload();
                    uploadTask.execute();
				}
				else
				{
					count();
				}

			}
		};
		countTime.start();
	}
	public class Upload extends AsyncTask<Void, Void, String> {
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {

			super.onPreExecute();
		}
		@Override
		protected String doInBackground(Void... params) {
			int value = 0;
			String response = null;
			ArrayList<DataBundle> lstData=db.getunsentRecords();
			try{

			if (lstData != null && lstData.size() > 0)
			{
				for (DataBundle record : lstData){
                    final int recordID=record.getRecordId();
                    RConsumer.uploadRecord(ServiceRequests.this,record.getUserID(), record.getRouteId(),record.getFarmerID(), record.getWeight(), record.getShift(),record.getCannumber(),record.isAlcohal(),record.isPeroxide(),new ActionSuccess() {
                        @Override
                        public void action(String json) {

                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                boolean error = jsonObject.getBoolean("error");
                                String message = jsonObject.getString("message");

                                if (!error) {
                                    //upload was good,update
                                    db.updateUploadStat(recordID);
//                                    Log.d("Upload Service", "Upload Service Worked");

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

				response="no unsent Records";
			}


		}
			catch(Exception e){
				e.printStackTrace();
			}

			if (value > 0)
				sendUpdateForm();
			return response;

		}
		@Override
		protected void onPostExecute(String result) {
			count();
//						Log.d("Error","UPLOADMessage:"+result);

            if(result!=null&&result!=""&&result.length()>0)	{
//					Log.e("Registration:", "Candidate has not been registered");

				}
				else{


				}
			
			super.onPostExecute(result);
		}

	}


	private void sendUpdateForm()
	{
		sender.setAction(ACTION_UPLOAD_REG);
		sendBroadcast(sender);
	}

	

}


