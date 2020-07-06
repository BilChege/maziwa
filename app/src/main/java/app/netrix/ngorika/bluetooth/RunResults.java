package app.netrix.ngorika.bluetooth;

import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;


public class RunResults {

    List<Byte> cardBcData;
    List<Byte> img1Data;
    List<Byte> imgGridData;
    List<Byte> logData;
    List<Byte> spotsData;
    List<Byte> tcnData;
    List<Byte> vmDumpData;
    List<Byte> vmLogData;

    List<Byte> weightData;
    public RunResults() {

    }


    public void saveRunData(int fileIndex, List<Byte> rawResultsData) throws DrawScriptInvalidException, UnsupportedEncodingException {

        byte[] tempDataArray;


    }


    public List<Byte> getWeightData(){
        return weightData;
    }

    private void WriteBytesToFile(int fileIndex, byte[] data)
    {
        File file = new File("storage/sdcard0/"+fileIndex);
        FileOutputStream fos = null;
        Log.d("MapResults", "MapResults:" + file.toString());
        try {

            fos = new FileOutputStream(file);

            // Writes bytes from the specified byte array to this file output stream
            fos.write(data);
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found" + e);
        }
        catch (IOException ioe)
        {
            System.out.println("Exception while writing file " + ioe);
        }
        finally
        {
            // close the streams using close method
            try
            {
                if (fos != null)
                {
                    fos.close();
                }
            }
            catch (IOException ioe)
            {
                System.out.println("Error while closing stream: " + ioe);
            }

        }

    }

}
