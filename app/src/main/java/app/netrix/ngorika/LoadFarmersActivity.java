package app.netrix.ngorika;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import app.netrix.ngorika.data.DatabaseHandler;
import app.netrix.ngorika.retrofit.Connect;
import app.netrix.ngorika.retrofit.RConsumer;
import app.netrix.ngorika.utils.ActionError;
import app.netrix.ngorika.utils.ActionSuccess;
import app.netrix.ngorika.utils.AppPreferences;

public class LoadFarmersActivity extends AppCompatActivity {
    ProgressDialog  progressDialog;
    DatabaseHandler databaseHandler;
    AppPreferences preferences;
    parseJson task;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_farmers);
        databaseHandler=new DatabaseHandler(LoadFarmersActivity.this);
        preferences=new AppPreferences(LoadFarmersActivity.this);

        if (Connect.isNetworkAvailable(getApplicationContext())){
            showProgress();
            RConsumer.getAllFarmers(LoadFarmersActivity.this,0, new ActionSuccess(){
                @Override
                public void action(String json) {
                    onSuccess(json);
                }
            }, new ActionError() {
                @Override
                public void action() {
                    System.out.println("--------------------> LOAD FARMERS ON-ERROR");
                   onErrror();
                }
            },null);
        }
    }
    private void onSuccess(String json){task=new parseJson(json);
        task.execute();
    }
    private void onErrror(){
      dismissDialog();
        Toast.makeText(LoadFarmersActivity.this,"An error Occured",Toast.LENGTH_LONG).show();
    }
    private void showProgress(){
        progressDialog = new ProgressDialog(LoadFarmersActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Farmers");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    private void dismissDialog(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        if (task != null) {
//            task.setListener(null); //prevent leaking this Fragment
        }
        super.onDestroy();
    }
    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return task; //saves instance for caching on rotation
    }
    private class parseJson extends AsyncTask<Void,Void,Integer>{
        String json;
        public parseJson(String json){
            System.out.println("------------------------> FARMERS' JSON : "+json);
            this.json=json;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();
            Log.d("result", "result:" + integer);
            if(integer>0){
                startActivity(new Intent(LoadFarmersActivity.this, MainActivity.class));
                preferences.setIsFarmerLoaded(true);
            }

            else{
                Toast.makeText(LoadFarmersActivity.this,"An Error Occured. No farmers were found",Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected Integer doInBackground(Void... params) {
           long count=0;
            try {
                if (json != null) {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray jsonArray = jsonObject.getJSONArray("farmers");
                    boolean errro=jsonObject.getBoolean("error");

                 if(!errro){
                     databaseHandler.deleteFarmers();
                     for (int i = 0; i < jsonArray.length(); i++) {
//                         Log.d("json","json:inloop: "+count);
                         JSONObject row = jsonArray.getJSONObject(i);
                         String name = row.getString("name");
                         String acc = row.getString("suppCode");
                         int id = row.getInt("id");
                         int route = row.getInt("route");
                         count = databaseHandler.insertFarmer(acc + ":" + name, id, route);

                     }
                 }

                }
            } catch (Exception e) {
                e.printStackTrace();
               count=0;
            }
            return (int) (long)count;
        }
    }
}
