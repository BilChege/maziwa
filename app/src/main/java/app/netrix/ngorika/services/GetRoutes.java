package app.netrix.ngorika.services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import app.netrix.ngorika.data.DatabaseHandler;
import app.netrix.ngorika.retrofit.Connect;
import app.netrix.ngorika.retrofit.RConsumer;
import app.netrix.ngorika.utils.ActionError;
import app.netrix.ngorika.utils.ActionSuccess;
import app.netrix.ngorika.utils.AppPreferences;

/**
 * Created by chrissie on 12/2/15.
 */
public class GetRoutes extends AsyncTask<Void,Void,Integer> {
    Context context;
    DatabaseHandler databaseHandler;
    AppPreferences preferences;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    int result;
    public GetRoutes(Context context){
        this.context=context;
        databaseHandler=new DatabaseHandler(context);
//        progressDialog = new ProgressDialog(context);
        preferences=new AppPreferences(context);

    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
       /* progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Routes...");
        progressDialog.show();*/
    }

    @Override
    protected void onPostExecute(Integer routes) {
        super.onPostExecute(routes);
        Log.d("Routes", "Routes:stat:" + preferences.getRouteCount());
        if(routes>0){
            Log.d("Routes", "Routes:stat:" + routes);


        }
    }

    @Override
    protected Integer doInBackground(Void... params) {
        Log.d("routes","routes");

        if (Connect.isNetworkAvailable(context)) {
            RConsumer.getRoutes(context,new ActionSuccess() {

                @Override
                public void action(String json) {
                    try{
                        if(json!=null){

                            JSONObject jsonObject=new JSONObject(json);
                            JSONArray jsonArray=jsonObject.getJSONArray("routes");
                            Log.d("Routes","Routes:Count"+jsonArray.length());
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject row=jsonArray.getJSONObject(i);
                                String name=row.getString("route");
                                int id=row.getInt("id");
                                long count=databaseHandler.insertRoute(name, id);
                                setResult((int) (long) count);
                            }
                            preferences.setRouteCount((int) (long) getResult());
                            Log.d("Routes","Routes##:"+getResult());

                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }, new ActionError() {
                @Override
                public void action() {

                }

            });
        }
        else{

            Log.d("Routes","Routes:No internet");
        }
        Log.d("Routes","Routes:fin:"+preferences.getRouteCount());
        return preferences.getRouteCount();
    }

}
