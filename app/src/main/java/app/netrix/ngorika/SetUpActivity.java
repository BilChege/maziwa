package app.netrix.ngorika;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import app.netrix.ngorika.data.DatabaseHandler;
import app.netrix.ngorika.pojo.Farmer;
import app.netrix.ngorika.services.GetFarmers;
import app.netrix.ngorika.utils.AppPreferences;
import app.netrix.ngorika.utils.FarmersAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SetUpActivity extends AppCompatActivity {
    @BindView(R.id.txtfarmers)
    TextView _farmerText;
    @BindView(R.id.spinnerfarmer) Spinner farmerSpinner;
    GetFarmers getFarmers;
    ArrayList<Farmer> farmerArrayList = new ArrayList<Farmer>();
    ProgressDialog progressDialog;
    DatabaseHandler databaseHandler;
    AppPreferences preferences;
    showFarmers showFarmers;
    showFarmersDB showFarmersDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_set_up);
        ButterKnife.bind(this);
        preferences=new AppPreferences(this);
        databaseHandler=new DatabaseHandler(this);
        getFarmers=new GetFarmers(SetUpActivity.this,preferences.getRoute());
        if(databaseHandler.getFarmerCount(preferences.getRoute())==0){
            if(showFarmers!=null)
                showFarmers.cancel(true);
            showFarmers=new showFarmers();
            showFarmers.execute();
        }
        else{
            getFromDB();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        hideViews();
        if(databaseHandler.getFarmerCount(preferences.getRoute())==0){
            if(showFarmers!=null)
                showFarmers.cancel(true);
            showFarmers=new showFarmers();
            showFarmers.execute();
        }
        else{
            getFromDB();
        }
    }

    private class  showFarmers extends AsyncTask<Void,Void,ArrayList<Farmer>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SetUpActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }


        @Override
        protected void onPostExecute(ArrayList<Farmer> farmers) {
            super.onPostExecute(farmers);
//            if(progressDialog!=null){
            progressDialog.dismiss();

            setAdapter(farmers);
        }

        @Override
        protected ArrayList<Farmer> doInBackground(Void... params) {
            farmerArrayList =databaseHandler.getAllFarmers(preferences.getRoute());
            if(farmerArrayList.isEmpty()){
                farmerArrayList =getFarmers.loadFromServer();
            }
            return farmerArrayList;
        }
    }
    private void setAdapter(final ArrayList<Farmer> farmerArrayList){
        Log.d("farmers", "farmers:Adp" + farmerArrayList.size());
        if(!farmerArrayList.isEmpty()){
            showViews();
            final FarmersAdapter farmersAdapter = new FarmersAdapter(this,android.R.layout.simple_spinner_item, farmerArrayList);
            farmerSpinner.setAdapter(farmersAdapter);
            farmersAdapter.notifyDataSetChanged();

            farmerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    Farmer selectedClass = farmerArrayList.get(position);
                    preferences.setRoute(selectedClass.getId());
                    Log.d("Selected", "Selected:" + selectedClass.getName());


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }

            });
        }

    }
    private void showViews(){
       farmerSpinner.setVisibility(View.VISIBLE);
        _farmerText.setVisibility(View.VISIBLE);
    }
    private void hideViews(){
        farmerSpinner.setVisibility(View.GONE);
        _farmerText.setVisibility(View.GONE);
    }
    private void getFromDB(){
        showViews();

        if(showFarmersDB!=null)
            showFarmersDB.cancel(true);
        showFarmersDB=new showFarmersDB();
        showFarmersDB.execute();

    }
    private class  showFarmersDB extends AsyncTask<Void,Void,ArrayList<Farmer>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SetUpActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Refreshing...");
            progressDialog.show();
        }


        @Override
        protected void onPostExecute(ArrayList<Farmer> farmers) {
            super.onPostExecute(farmers);
//            if(progressDialog!=null){
            progressDialog.dismiss();

            setAdapter(farmers);
        }

        @Override
        protected ArrayList<Farmer> doInBackground(Void... params) {
            Log.d("FarmerDB","farmerDB:"+preferences.getRoute());
            farmerArrayList =databaseHandler.getAllFarmers(preferences.getRoute());

            return farmerArrayList;
        }
    }

}
