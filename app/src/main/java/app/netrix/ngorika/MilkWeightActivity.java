package app.netrix.ngorika;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import app.netrix.ngorika.bluetooth.NgBluetooth;
import app.netrix.ngorika.data.DatabaseHandler;
import app.netrix.ngorika.pojo.MilkCan;
import app.netrix.ngorika.pojo.Workshift;
import app.netrix.ngorika.retrofit.ConnectService;
import app.netrix.ngorika.utils.AppPreferences;
import butterknife.BindView;
import butterknife.ButterKnife;
import fr.ganfra.materialspinner.MaterialSpinner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by chrissie on 12/4/15.
 */
public class MilkWeightActivity extends AppCompatActivity {
    private static final String TAG = "MILKWEIGHTACTIVITY";
    TextView myLabel;

    // android built in classes for bluetooth operations
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    ProgressDialog progressDialog;
    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;
    AppPreferences preferences;
    LinearLayout mainLayout;
    @BindView(R.id.open)  Button openButton;
    @BindView(R.id.close)  Button closeButton;
    @BindView(R.id.upload)  Button uploadButton;
    @BindView(R.id.labelinformation)  TextView txtInfo;
    @BindView(R.id.etWeight) EditText etWeight;
    @BindView(R.id.milkCans) SearchableSpinner milkCansSpinner;
    @BindView(R.id.workShift) MaterialSpinner workShiftSpinner;
    @BindView(R.id.peroxide) CheckBox cbPeroxide;
    @BindView(R.id.alcohal) CheckBox cbAlcohal;
    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
    DatabaseHandler databaseHandler;
    String output;
    NgBluetooth mapsbluetooth;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milkweight);
        ButterKnife.bind(this);
        mainLayout = findViewById(R.id.sample_main_layout);
        preferences=new AppPreferences(MilkWeightActivity.this);
        uploadButton.setEnabled(true);
        databaseHandler=new DatabaseHandler(MilkWeightActivity.this);
        populateMilkCans();
        populateWorkShifts();
        this.mapsbluetooth = ((NgorikaApplication)getApplication()).mapsBluetooth;
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    uploadData();
                }
            }
        });
        try {
            String html=
                    ( "<font color=\"#00BCD4\">" + "Grader: " +"</font>"+preferences.getUsername()+ "<font color=\"#00BCD4\">" + "  Farmer: " +"</font>" +
                   preferences.getFarmerName()+ "<font color=\"#00BCD4\">" +"\n"+ " Route: " +"</font>"+preferences.getRouteName()  );
            txtInfo.setText(Html.fromHtml(html));
//                    "Grader:"+preferences.getPassword()+" Farmer: "+preferences.getFarmerName()+"\n Route: "+preferences.getRoute() );
            myLabel = (TextView) findViewById(R.id.label);

            // open bluetooth
           if (mapsbluetooth.isConnected()){
               beginListenForData();
               etWeight.setVisibility(View.INVISIBLE);
           } else {
               new AlertDialog.Builder(MilkWeightActivity.this)
                       .setTitle("Bluetooth connection failed")
                       .setMessage("You may have to enter the milk weight manually in the input provided.")
                       .setPositiveButton("Ok",null).show();
               etWeight.addTextChangedListener(new TextWatcher() {
                   @Override
                   public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                   }

                   @Override
                   public void onTextChanged(CharSequence s, int start, int before, int count) {
                       setOutput(s.toString());
                   }

                   @Override
                   public void afterTextChanged(Editable s) {

                   }
               });
           }
            openButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        findBT();
                        openBT();
                    } catch (IOException ex) {
                    }
                }
            });



            // close bluetooth connection
            closeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        closeBT();
                    } catch (IOException ex) {
                    }
                }
            });

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void populateWorkShifts() {
        final ProgressDialog dialog = new ProgressDialog(MilkWeightActivity.this);
        dialog.setMessage("Initializing work shifts ... ");
        dialog.setCancelable(false);
        dialog.show();
        Call<List<Workshift>> call = ConnectService.getNetClient().getWorkShifts();
        call.enqueue(new Callback<List<Workshift>>() {
            @Override
            public void onResponse(Call<List<Workshift>> call, Response<List<Workshift>> response) {
                dialog.dismiss();
                if (response != null){
                    int status = response.code();
                    if (status == 200){
                        try {
                            List<Workshift> workshifts = response.body();
                            if (!workshifts.isEmpty()){
                                ArrayAdapter<Workshift> workShiftsAdapter = new ArrayAdapter<Workshift>(MilkWeightActivity.this,android.R.layout.simple_spinner_item,workshifts);
                                workShiftsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                workShiftSpinner.setAdapter(workShiftsAdapter);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(MilkWeightActivity.this,"Error "+status+" occurred while fetching work shifts",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MilkWeightActivity.this,"There was no response from the server",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Workshift>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(MilkWeightActivity.this,"Failure : "+t.getMessage(),Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void populateMilkCans() {
        final ProgressDialog dialog = new ProgressDialog(MilkWeightActivity.this);
        dialog.setMessage("Initializing milk cans ... ");
        dialog.setCancelable(false);
        dialog.show();
        Call<List<MilkCan>> call = ConnectService.getNetClient().getMilkCans();
        call.enqueue(new Callback<List<MilkCan>>() {
            @Override
            public void onResponse(Call<List<MilkCan>> call, Response<List<MilkCan>> response) {
                dialog.dismiss();
                if (response != null){
                    int status = response.code();
                    if (status == 200){
                        try {
                            List<MilkCan> milkCans = response.body();
                            if (!milkCans.isEmpty()){
                                ArrayAdapter<MilkCan> milkCansAdapter = new ArrayAdapter<MilkCan>(MilkWeightActivity.this,android.R.layout.simple_spinner_item,milkCans);
                                milkCansAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                milkCansSpinner.setAdapter(milkCansAdapter);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(MilkWeightActivity.this,"Error "+status+" occurred while fetching milkcans",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MilkWeightActivity.this,"There was no response from the server",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MilkCan>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(MilkWeightActivity.this,"Failure : "+t.getMessage(),Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private boolean validate() {
        boolean valid = true;
        if (etWeight.getVisibility() == View.VISIBLE){
            if (TextUtils.isEmpty(etWeight.getText().toString())){
                etWeight.setError("Enter the weight");
                valid = false;
            }
        } else {
            if (getOutput() == null && !getOutput().isEmpty()){
                Toast.makeText(MilkWeightActivity.this,"Weight has not been captured yet",Toast.LENGTH_SHORT).show();
                valid = false;
            }
        }
        if (milkCansSpinner.getSelectedItem() == null){
            TextView textView = (TextView) milkCansSpinner.getSelectedView();
            textView.setError("Select Milk Can");
            valid = false;
        }
        if (workShiftSpinner.getSelectedItem().equals("Select Work Shift")){
            TextView textView = (TextView) workShiftSpinner.getSelectedView();
            textView.setError("Select the work shift");
            valid = false;
        }
        return valid;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_blue, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected  void onStart() {
        super.onStart();

    }
    void findBT() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
                Toast.makeText(MilkWeightActivity.this,"No bluetooth adapter available",Toast.LENGTH_LONG).show();

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
                        else{

                        }
                    }
                }//
                etWeight.setVisibility(View.INVISIBLE);
                myLabel.setText("Bluetooth Device Found");
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateBluetothText(String text) {
       try {
           myLabel.setText(text);
           if (text.length() > 0) {
               setOutput(text.substring(6, text.length()));
           }
       }
       catch (Exception e){

       }
    }

    // Tries to open a connection to the bluetooth printer device
    void openBT() throws IOException {
        try {
            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();
            etWeight.setVisibility(View.INVISIBLE);
            myLabel.setText("Bluetooth Opened");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // After opening a connection to bluetooth printer device,
    // we have to listen and check if a data were sent to be printed.
    void beginListenForData() {
        try {
            mapsbluetooth=((NgorikaApplication)getApplication()).mapsBluetooth;
            mmInputStream=mapsbluetooth.readData().getInputStream();
            final Handler handler = new Handler();

            // This is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {
                    while (!Thread.currentThread().isInterrupted()
                            && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();
                            if (bytesAvailable > 0) {
                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);
                                for (int i = 0; i < bytesAvailable; i++) {
                                    byte b = packetBytes[i];
                                    if (b == delimiter) {
                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length);
                                        final String data = new String(
                                                encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        handler.post(new Runnable() {
                                            public void run() {
                                                myLabel.setText(data);
                                                if(data.length()>0){
                                                    if(data.contains("lb")){
//                                                        setOutput("Adjust Unit on Scale to Kg");
                                                        Toast.makeText(MilkWeightActivity.this, "Adjust Unit on Scale to Kg", Toast.LENGTH_LONG).show();
                                                    }
                                                    else{
                                                        try{
                                                            String o=(data.substring(6,data.length()));
                                                            setOutput(o.substring(0,o.indexOf("k")));
                                                        }
                                                        catch (StringIndexOutOfBoundsException e){

                                                        }


                                                    }


                                                }

                                            }
                                        });
                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // Close the connection to bluetooth printer.
    void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmInputStream.close();
//            mmSocket.close();
//            myLabel.setText("Bluetooth Closed");
            myLabel.setText("Weight = " + getOutput());
            uploadButton.setEnabled(true);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadData() {

        try {
            closeBT();
        } catch (IOException ex) {
            Log.e(TAG, "uploadData: ", ex);
        }

        final int route = preferences.getRoute();
        final int farmer = preferences.getFarmerId();
        final int user = preferences.getUserID();
        MilkCan milkCan = (MilkCan) milkCansSpinner.getSelectedItem();
        final int cannumber = milkCan.getId();
        Workshift workshift = (Workshift) workShiftSpinner.getSelectedItem();
        final int shift = workshift.getId();
        final boolean alcohal = cbAlcohal.isSelected();
        final boolean peroxide = cbPeroxide.isSelected();
        final String weight=getOutput();
        final long timesaved=System.currentTimeMillis();
        if (route != 0 && farmer != 0 && user != 0 && getOutput() != "")
        {
            //save to db
          long result=  databaseHandler.insertBundle(farmer,route,user,cannumber,shift,alcohal,peroxide,weight,timesaved,preferences.getFarmerName());
            if(result>0){
                Toast.makeText(MilkWeightActivity.this, "Record saved", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MilkWeightActivity.this, MainActivity.class);
                intent.putExtra("FROMMAIN", "NO");
                startActivity(intent);
                preferences.setFarmerName("");
                preferences.setFarmerId(0);
            }
        }

        else{
            Toast.makeText(MilkWeightActivity.this, "Ensure All data is captured", Toast.LENGTH_LONG).show();
        }

    }
}
