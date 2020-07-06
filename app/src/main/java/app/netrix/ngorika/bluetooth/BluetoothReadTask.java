package app.netrix.ngorika.bluetooth;

import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.List;

import app.netrix.ngorika.MilkWeightActivity;


class BluetoothReadTask extends AsyncTask<Void, Void, Exception> {

    private final WeakReference<MilkWeightActivity> activityReference;
    private final NgBluetooth mapsBluetooth;

    int receivedFileIndex = 0;
    List<Byte> receivedFileData;
    byte[] readBuffer;
    int readBufferPosition;
String data;
    // This is the ASCII code for a newline character
    final byte delimiter = 10;
    public BluetoothReadTask(NgBluetooth mapsBluetooth, MilkWeightActivity activityReference) {
        this.activityReference = new WeakReference<>(activityReference);
        this.mapsBluetooth = mapsBluetooth;

    }

    // Process bluetooth data in background
    @Override
    protected Exception doInBackground(Void... voids) {
        try {
            InputStream mmInputStream=mapsBluetooth.readData().getInputStream();

            int bytesAvailable = mmInputStream.available();
            if (bytesAvailable > 0) {
//                Log.d("InputStream","Input Stream"+bytesAvailable);
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
                        Log.d("haha","haha"+data);
                        setData(data);
                        readBufferPosition = 0;

                    } else {
                        readBuffer[readBufferPosition++] = b;
                    }
                }
            }

        }
        catch(Exception e)
        {
            return e;
        }

        return null;
    }

    // Once complete notify the GUI
    @Override
    protected void onPostExecute(Exception processingException)
    {
        activityReference.get().updateBluetothText(getData());

        //continue listening to data from the scaleunit
        BluetoothReadTask readTask = new BluetoothReadTask(mapsBluetooth, activityReference.get());
        readTask.execute();
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
