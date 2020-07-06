package app.netrix.ngorika;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app.netrix.ngorika.data.DatabaseHandler;
import app.netrix.ngorika.data.UnsentDataAdapter;
import app.netrix.ngorika.pojo.DataBundle;
import app.netrix.ngorika.retrofit.Connect;
import app.netrix.ngorika.services.UploadDataService;
import butterknife.BindView;
import butterknife.ButterKnife;


public class UnsentRecords extends AppCompatActivity {
    @BindView(android.R.id.empty) TextView tvNodata;
    @BindView(R.id.list_records)
    ListView list_records;
    UnsentDataAdapter adapter;
    ArrayList<DataBundle> lstData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unsent_records);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Refreshing", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                showRecords();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        showRecords();
    }
    private void showRecords(){
        GetUnsentRecord  task = new GetUnsentRecord(UnsentRecords.this);
        task.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.unsent, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.upload) {
            if(Connect.isNetworkAvailable(UnsentRecords.this)) {
                UploadDataService uploadDataService = new UploadDataService(UnsentRecords.this);
                uploadDataService.execute();
            }
            else{
                Toast.makeText(UnsentRecords.this, "No Internet connection", Toast.LENGTH_LONG).show();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private class GetUnsentRecord extends AsyncTask<Void,Void,Integer>{
        ProgressDialog progressDialog;
        DatabaseHandler db;
        public GetUnsentRecord(Context context){
            db=new DatabaseHandler(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(UnsentRecords.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("wait...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Integer count) {
            super.onPostExecute(count);
            if(progressDialog!=null){
                progressDialog.dismiss();
            }
            if(count>0){
                tvNodata.setVisibility(View.GONE);
                adapter = new UnsentDataAdapter(UnsentRecords.this, lstData);
                list_records.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                list_records.setAdapter(adapter);

            }
        }

        @Override
        protected Integer doInBackground(Void... params) {
            lstData=db.getunsentRecords();
            return lstData.size();

        }
    }
}
