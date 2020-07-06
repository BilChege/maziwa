package app.netrix.ngorika;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import app.netrix.ngorika.bluetooth.NgBluetooth;
import app.netrix.ngorika.data.DatabaseHandler;
import app.netrix.ngorika.pojo.Farmer;
import app.netrix.ngorika.pojo.Routes;
import app.netrix.ngorika.retrofit.Connect;
import app.netrix.ngorika.services.ServiceRequests;
import app.netrix.ngorika.utils.AppPreferences;
import app.netrix.ngorika.utils.FarmersAdapter;
import app.netrix.ngorika.utils.MyTextWatcher;
import app.netrix.ngorika.utils.RoutesAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;

    ArrayList<Routes> routesArrayList = new ArrayList<Routes>();
    DatabaseHandler databaseHandler;
    ProgressDialog progressDialog;
    AppPreferences preferences;
    @BindView(R.id.txtroutes)
    TextView _routeText;
    @BindView(R.id.spinner)
    Spinner routesSpinner;
    @BindView(R.id.btn_next)
    Button _btnNext;
    @BindView(R.id.txtfarmers)
    TextView _farmerText;
    public @BindView(R.id.autoCompleteText)
    AutoCompleteTextView
    autoText;
    public static ArrayList<Farmer> farmerArrayList = new ArrayList<Farmer>();
    public static  ArrayAdapter<Farmer> myAdapter;
    public static FarmersAdapter farmersAdapter;
    NgBluetooth mapsBluetooth;
    AsyncTask connectionTask;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        hideViews();
        databaseHandler = new DatabaseHandler(getApplicationContext());
        preferences = new AppPreferences(getApplicationContext());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.milkicon);

    this.mapsBluetooth = ((NgorikaApplication)getApplication()).mapsBluetooth;
        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

        showViews();
        loadRoutes(routesArrayList);
        _btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               try{
                   if (preferences.getRoute() > 0 && preferences.getFarmerId() > 0) {
//                    startActivity(new Intent(MainActivity.this, MilkWeightActivity.class));

                       if (!mBluetoothAdapter.isEnabled()) {
                           Intent enableBluetooth = new Intent(
                                   BluetoothAdapter.ACTION_REQUEST_ENABLE);
                           startActivityForResult(enableBluetooth, 0);
                       }
                       else{

                           connectionTask = mapsBluetooth.attemptConnection(MainActivity.this);
                       }


                   } else {
                       Toast.makeText(MainActivity.this, "Select a route and Farmer", Toast.LENGTH_LONG).show();
                   }

               }
               catch (Exception e){
                   Toast.makeText(MainActivity.this, "Error Finding Scale", Toast.LENGTH_LONG).show();
               }
            }
        });


    }

    public void loadRoutes(ArrayList<Routes> routesArrayList) {
        showViews();
        try {
            if (routesArrayList.isEmpty()) {
                routesArrayList = databaseHandler.getAllRoutes();
            }
            routesArrayList.set(0, (new Routes("Select A Route", 0)));
            setRoutesAdapter(routesArrayList);
        } catch (Exception e) {

        }


    }


    private void setRoutesAdapter(final ArrayList<Routes> routesArrayList) {

        RoutesAdapter routesAdapter = new RoutesAdapter(this, android.R.layout.simple_spinner_item, routesArrayList);
        routesAdapter.notifyDataSetChanged();

        routesSpinner.setAdapter(routesAdapter);

        routesSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                if (position != 0) {
                    Routes selectedClass = routesArrayList.get(position);
                    preferences.setRoute(selectedClass.getId());
                    preferences.setRouteName(selectedClass.getRoute());
                    autoText.setText("");
                    updateActionBar();
                    _farmerText.setText("Select Farmer " + "(" + databaseHandler.getFarmerCount(preferences.getRoute()) + ")");
                    //farmers exist locally..TODO run on separate Thread
                    farmerArrayList = loadFarmers(preferences.getRoute());
                    if (farmerArrayList.size() > 0) {
                        loadFarmer(farmerArrayList);
                        showFarmerViews();
                    }


                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });
    }

    private void updateActionBar() {
        getSupportActionBar().setTitle(preferences.getRouteName());
    }

    public ArrayList<Farmer> loadFarmers(int route){
   return databaseHandler.getAllFarmers(route);
}
    private void loadFarmer(final ArrayList<Farmer> farmers){
        farmerArrayList =farmers;
        if(farmers==null||farmers.size()==0 ){
            autoText.setText("No farmers Found");
        }
        else
       autoText.addTextChangedListener(new MyTextWatcher(this));

//        myAdapter = new ArrayAdapter<Farmers>(this, android.R.layout.simple_dropdown_item_1line, farmers);
       farmersAdapter = new FarmersAdapter(this, android.R.layout.simple_spinner_item, farmers);
        autoText.setAdapter(myAdapter);

        autoText.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
                HideKeyBoard();
                Farmer selectedClass = farmers.get(position);
                preferences.setFarmerName(selectedClass.getName());
                preferences.setFarmerId(selectedClass.getId());
                _btnNext.setVisibility(View.VISIBLE);
//                autoText.setText(str);

            }
        });

    }
    private void HideKeyBoard() {
        try {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (this.getCurrentFocus() != null) {
                inputManager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        if (preferences.getUsername() == "") {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        startUploadService();
        /*if (preferences.isFarmerLoaded()) {
            Intent intent = new Intent(this, LoadFarmersActivity.class);
            startActivity(intent);
        }*/
        farmerArrayList = databaseHandler.getAllFarmers(preferences.getRoute());
        loadFarmer(farmerArrayList);
        if(preferences.getRoute() > 0 ){
          showFarmerViews();
            hideViews();
            updateActionBar();
            _farmerText.setText("Select Farmer "+"(" + databaseHandler.getFarmerCount(preferences.getRoute())+")");
            if(!preferences.getFarmerName().equals("") && preferences.getFarmerId()>0){
                _btnNext.setVisibility(View.VISIBLE);
            }
        }
        else{
            getSupportActionBar().setTitle(R.string.app_name);
        }

    }
    private void hideViews() {
        routesSpinner.setVisibility(View.GONE);
        _routeText.setVisibility(View.GONE);
        _btnNext.setVisibility(View.GONE);
    }

    private void showViews() {
        routesSpinner.setVisibility(View.VISIBLE);
        _routeText.setVisibility(View.VISIBLE);
    }

    private void showFarmerViews() {
//        farmerSpinner.setVisibility(View.VISIBLE);
        _farmerText.setVisibility(View.VISIBLE);
        autoText.setVisibility(View.VISIBLE);
    }

    private void hideFarmerViews() {
        _btnNext.setVisibility(View.GONE);
//        farmerSpinner.setVisibility(View.GONE);
        _farmerText.setVisibility(View.GONE);
        autoText.setVisibility(View.GONE);

    }
    private void startUploadService(){
        try {
            Intent service = new Intent(MainActivity.this, ServiceRequests.class);
            startService(service);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
//            preferences.clearPrefs();
            preferences.setUsername("");
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            return true;
        }
        if(id==R.id.action_loadRoutes){
            showViews();
        }

        if (id==R.id.action_mrequisition){
            if (Connect.isNetworkAvailable(getApplicationContext())) {
                startActivity(new Intent(MainActivity.this,MaterialRequisition.class));
            }
            else{
                Toast.makeText(MainActivity.this,"No Internet Connection found",Toast.LENGTH_LONG).show();
            }
        }
        if(id==R.id.action_loadfarmers){
            if (Connect.isNetworkAvailable(getApplicationContext())) {
                startActivity(new Intent(MainActivity.this,LoadFarmersActivity.class));
            }
            else{
                Toast.makeText(MainActivity.this,"No Internet Connection found",Toast.LENGTH_LONG).show();
            }
        }
        if(id==R.id.action_unsent){
                startActivity(new Intent(MainActivity.this,UnsentRecords.class));
        }

        return super.onOptionsItemSelected(item);
    }

    public void bluetoothConnection_callback() {
        System.out.println("GUI Bluetooth Connection callback...");
        if (mapsBluetooth.isConnected())
            Toast.makeText(MainActivity.this,"Connection Established",Toast.LENGTH_LONG).show();

        else
            Toast.makeText(MainActivity.this,"Cannot Connect to device",Toast.LENGTH_LONG).show();

        // Switch to new activity given successful bluetooth connection
        Intent intent = new Intent(this, MilkWeightActivity.class);
        startActivity(intent);
    }
    void findBT() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
                Toast.makeText(MainActivity.this,"No bluetooth adapter available",Toast.LENGTH_LONG).show();

            }
            else{
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBluetooth = new Intent(
                            BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBluetooth, 0);
                }

                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
                        .getBondedDevices();
                if (pairedDevices.size() > 0) {
                    for (BluetoothDevice device : pairedDevices) {
                        if (device.getName().equals("WLT2564F")||device.getName().startsWith("WLT")) {
                            mmDevice = device;
                            break;
                        }
                    }
                }//
                try {
                    // Standard SerialPortService ID
                    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
                    mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
                    mmSocket.connect();
                    mmOutputStream = mmSocket.getOutputStream();
                    mmInputStream = mmSocket.getInputStream();
                    Intent i =new Intent();
                    Toast.makeText(MainActivity.this,"Bluetooth connection opened",Toast.LENGTH_LONG).show();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
