package app.netrix.ngorika.bluetooth;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import app.netrix.ngorika.MainActivity;
import app.netrix.ngorika.MilkWeightActivity;
import app.netrix.ngorika.bluetooth.utils.Convert;
import app.netrix.ngorika.bluetooth.utils.Tuple;


public class NgBluetooth {
    //
    public static final int BUFFER_SIZE = 1024;
    public static final int BLUETOOTH_READ_DELAY = 10;
    public static final int TIME_OUT_MS = 60 * 1000; // 10 seconds


    public static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private BluetoothSocket socket;
    private BluetoothAdapter adapter;

    private MAPsBluetoothConnection connector;

    int filesReceivedCounter = 0;

    public NgBluetooth() {

        adapter = BluetoothAdapter.getDefaultAdapter();
    }

    //
    // Connecting to Bluetooth Devices
    //
    public boolean isConnected() {
        return socket != null;
    }

    public void resetConnection() {
        adapter = BluetoothAdapter.getDefaultAdapter();
        connector.cancelConnection();
        socket = null; // Socket closed in connector
    }

    public BluetoothConnectTask attemptConnection(MainActivity activity) {
        BluetoothConnectTask connect = new BluetoothConnectTask(this, activity);
        connect.execute();
        return connect;
    }

    // doConnection() is a blocking operation
    public void doConnection() throws IOException {
        if (connector != null) // Cancel whatever else might have existed
            connector.cancelConnection();

        connector = new MAPsBluetoothConnection(adapter);
        this.socket = connector.doConnection();
    }

    public boolean cancelConnection() {
        if (connector != null)
            return connector.cancelConnection();
        else
            return false;
    }

    // A class to track the connection variables for cancellation if needed
    class MAPsBluetoothConnection {
        BluetoothAdapter adapter;
        BluetoothServerSocket serverSocket;
        BluetoothSocket socket;
        BluetoothDevice mmDevice;

        MAPsBluetoothConnection(BluetoothAdapter adapter) {

            this.adapter = adapter;
        }

        BluetoothSocket doConnection() throws IOException {
            adapter = BluetoothAdapter.getDefaultAdapter();
            Set<BluetoothDevice> pairedDevices = adapter
                    .getBondedDevices();
            if (pairedDevices.size() > 0){
                for (BluetoothDevice device : pairedDevices) {
                    if (device.getName().equals("WLT2564F") || device.getName().startsWith("WLT")) {
                        mmDevice = device;
                        break;
                    }
                    else{
                        mmDevice=pairedDevices.iterator().next();

                    }

                }
            }
            BluetoothSocket mmSocket=null;
            if(mmDevice!=null){
                mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
                mmSocket.connect();
            }

            return mmSocket;
        }

        boolean cancelConnection() {
            try {
                if (serverSocket != null)
                    serverSocket.close();
                if (socket != null)
                    socket.close();

            } catch (IOException e) {
                return true;
            }

            return false;
        }
    }


    //
    // Reading
    //
    public BluetoothSocket readData() {
   /* InputStream mmInputStream=null;
    List<Byte> data=null ;
    try{
        *//*InputStream bluetoothStream = socket.getInputStream();
         mmInputStream=ocket.getInputStream();*//*
        InputStream bluetoothStream = socket.getInputStream();
        Tuple<Integer, byte[]> processedHeader = readSizeHeader(bluetoothStream);
        int dataSize = processedHeader.item1;
        byte[] initialData = processedHeader.item2;
        Log.d("size:", "size:" + dataSize);

       data = readData(bluetoothStream, initialData, dataSize);
    }
    catch (Exception e){

    }*/
        return socket;
    }

    public void listenForData(MilkWeightActivity notifyActivity) {
        BluetoothReadTask read = new BluetoothReadTask(this, notifyActivity);
        read.execute();
    }

    public Tuple<Integer, byte[]> readSizeHeader(InputStream bluetoothStream) throws IOException, InterruptedException {
        final int headerSize = 4;
        List<Byte> header = new ArrayList<>(4);

        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = 0;

        int steps = 0; // Limit the number of iterations
        while (steps < 5) {
            int filledBytes = bluetoothStream.read(buffer);
            byte[] headerBuffer = Arrays.copyOfRange(buffer, 0, filledBytes);

            // While buffer defines header
            if (filledBytes + bytesRead < headerSize) {
                header.addAll(Convert.toList(headerBuffer));
                bytesRead += filledBytes;
            }
            // Some of buffer defines header, the remaining buffer defines data
            else {
                // Complete assembling the header
                int numberHeaderBytes = headerSize - bytesRead;
                byte[] remainingHeader = Arrays.copyOfRange(headerBuffer, 0, numberHeaderBytes);
                header.addAll(Convert.toList(remainingHeader));
                int dataSize = Convert.buildInteger(header);

                // Return the rest of the buffer as data
                byte[] data = Arrays.copyOfRange(headerBuffer, numberHeaderBytes, headerBuffer.length);
                return new Tuple<>(dataSize, data);
            }

            Thread.sleep(BLUETOOTH_READ_DELAY);
        }

        throw new IOException("Bluetooth stream began to read no data on each step. Should never happen.");
    }

    public List<Byte> readData(InputStream bluetoothStream, byte[] initialData, int dataSize) throws IOException {
        List<Byte> data = new ArrayList<>(dataSize);
        initialData = Arrays.copyOfRange(initialData, 0, Math.min(dataSize, initialData.length));
        data.addAll(Convert.toList(initialData));
        int bytesRead = initialData.length;

        while (bytesRead < dataSize) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesFilled = bluetoothStream.read(buffer);

            // Truncate excess data and empty buffer space
            // TODO: Begin new read upon truncating data
            byte[] dataBuffer = Arrays.copyOfRange(buffer, 0, Math.min(bytesFilled, dataSize - bytesRead));
            data.addAll(Convert.toList(dataBuffer));
            bytesRead += dataBuffer.length;
        }

        return data;
    }
}
