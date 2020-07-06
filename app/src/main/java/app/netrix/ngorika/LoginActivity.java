package app.netrix.ngorika;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import app.netrix.ngorika.data.DatabaseHandler;
import app.netrix.ngorika.pojo.User;
import app.netrix.ngorika.retrofit.Connect;
import app.netrix.ngorika.retrofit.RConsumer;
import app.netrix.ngorika.utils.ActionError;
import app.netrix.ngorika.utils.ActionSuccess;
import app.netrix.ngorika.utils.AppPreferences;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    ProgressDialog progressDialog;
    AppPreferences preferences;
    int resultCount;
    parseJson getRoutes;
    parseFarmersJson task;
    DatabaseHandler databaseHandler;
    @BindView(R.id.input_username) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        databaseHandler=new DatabaseHandler(getApplicationContext());
        preferences=new AppPreferences(getApplicationContext());
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });


    }
    private void showProgress(){
        System.out.println("-------------------- > SHOW DIALOG");
        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
    }
    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(true);
        final  String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();
        User user=databaseHandler.getPassword(email);
        String pass=user.getPassword();
        if(pass!=null){
            System.out.println(" ----------------> MOBILE LOCAL DB NOT NULL ");
            if(pass.equals(password)) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                preferences.setUsername(email);
                preferences.setUserID(user.getUserid());
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(LoginActivity.this,"Incorrect credentials",Toast.LENGTH_LONG).show();
            }
        }
        else{
            if (Connect.isNetworkAvailable(LoginActivity.this)) {
                System.out.println(" ----------------> NETWORK IS AVAILABLE ");
                showProgress();
                RConsumer.login(LoginActivity.this,email, password, new ActionSuccess() {
                    @Override
                    public void action(String json){
                        try {
                            System.out.println(" ----------------->EMAIL : "+email+" PASSWORD : "+password);
                            JSONObject jsonObject = new JSONObject(json);
                            boolean error = jsonObject.getBoolean("error");
                            if (!error) {
                                int userID=jsonObject.getInt("userId");
                                onLoginSuccess(email,json,userID);
                                databaseHandler.insertUser(password,email,userID);
                            } else {
                                progressDialog.dismiss();
                                onLoginFailed();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new ActionError() {
                    @Override
                    public void action() {
                        System.out.println(" --------------------> ACTION FAILURE");
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }
                });
            }
            else{
                Toast.makeText(LoginActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
            }
        }





    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!preferences.getUsername().equals("")){
            Intent intent = new Intent(this, MainActivity.class);

            startActivity(intent);


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }
    public int configRoutes(String json){
        int size=0;
        try{

            getRoutes=new parseJson(json);
            preferences.reloadPrefs();
            getRoutes.execute();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return preferences.getRouteCount();
    }
    public void onLoginSuccess(String username,String json,int userID ) {
        _loginButton.setEnabled(true);
        preferences.setUsername(username);
        preferences.setUserID(userID);
        if(!preferences.isRoutesConfiged()){
            configRoutes(json);
        }
        else{
            if (progressDialog != null)
                progressDialog.dismiss();
            if(preferences.isFarmerLoaded()){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("FROMMAIN","YES");
                startActivity(intent);
                finish();
            }
            else{
                loadFarmers();
            }
        }


    }

    private void loadFarmers() {
        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage("Loading farmers ... ");
        dialog.show();
        RConsumer.getAllFarmers(LoginActivity.this,0, new ActionSuccess(){
            @Override
            public void action(String json) {
                onSuccess(json);
            }
        }, new ActionError() {
            @Override
            public void action() {
                dialog.dismiss();
                System.out.println("--------------------> LOAD FARMERS ON-ERROR");
                Toast.makeText(LoginActivity.this,"An error occurred while loading farmers",Toast.LENGTH_SHORT).show();
            }
        },dialog);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("FROMMAIN","YES");
        startActivity(intent);
        finish();
    }

    private void onSuccess(String json){task=new parseFarmersJson(json);
        task.execute();
    }

    private class parseFarmersJson extends AsyncTask<Void,Void,Integer>{
        String json;
        public parseFarmersJson(String json){
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
                Toast.makeText(LoginActivity.this,"Farmers Loaded successfully",Toast.LENGTH_SHORT).show();
                preferences.setIsFarmerLoaded(true);
            }

            else{
                Toast.makeText(LoginActivity.this,"No farmers were found",Toast.LENGTH_LONG).show();
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

    private class parseJson extends AsyncTask<Void,Void,Void>{
        String json;
        public parseJson(String json) {
            this.json=json;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Loading routes...");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(progressDialog!=null){
                progressDialog.dismiss();
            }
            preferences.setRoutesConfiged(true);
            if(preferences.isFarmerLoaded()){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("FROMMAIN","YES");
                startActivity(intent);
                finish();
            }
            else{
                loadFarmers();
            }

        }

        @Override
        protected Void doInBackground(Void... params) {
            try{
                if(json!=null){

                    JSONObject jsonObject=new JSONObject(json);
                    JSONArray jsonArray=jsonObject.getJSONArray("routes");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject row=jsonArray.getJSONObject(i);
                        String routeName=row.getString("route");
                        int id=row.getInt("id");
                        long count=databaseHandler.insertRoute(routeName, id);
                        setResultCount((int) (long) count);

                    }
                    preferences.setRouteCount((int) (long) getResultCount());
                    Log.d("Count","CountRoutes:"+getResultCount());

                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() ) {
            //        if (email.isEmpty() ) {

            _emailText.setError("enter a valid username");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 ) {
            _passwordText.setError("between 4  alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }
}
