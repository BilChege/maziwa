package app.netrix.ngorika.bluetooth;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.io.IOException;
import java.lang.ref.WeakReference;

import app.netrix.ngorika.MainActivity;


class BluetoothConnectTask extends AsyncTask<Void, Void, Void> {

    private final WeakReference<MainActivity> activity;
    private final NgBluetooth mapsBluetooth;
    ProgressDialog  progressDialog;
    public BluetoothConnectTask(NgBluetooth mapsBluetooth, MainActivity activity) {
        this.activity = new WeakReference<>(activity);
        this.mapsBluetooth = mapsBluetooth;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(activity.get())   ;
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("searching for Bluetoth");
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... noInput) {

        try {
            System.out.println("Bluetooth connection begun...");
            mapsBluetooth.doConnection(); // Blocking operation, can time out
            System.out.println("Made Bluetooth connection...");
        }
        catch (IOException e) { // Timeout
            System.out.println("Connection didn't succeed (" + e.getClass() + ":" + e.getMessage() + ")...");
            if (isCancelled() == false) // Don't reset if it was cancelled, it has already been reset
                mapsBluetooth.resetConnection();
        }

        return null;
    }

    // Once complete notify the GUI
    @Override
    protected void onPostExecute(Void noInput) {
        if (isCancelled() == false) // Don't callback if it was cancelled, the callback has already been done
            activity.get().bluetoothConnection_callback();
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onCancelled() {
        System.out.println("Bluetooth connection was cancelled...");
    }
}