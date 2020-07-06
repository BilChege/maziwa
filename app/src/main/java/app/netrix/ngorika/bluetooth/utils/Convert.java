package app.netrix.ngorika.bluetooth.utils;


import java.util.ArrayList;
import java.util.List;

public class Convert {

    public static List<Byte> toList(byte[] dataArray) {
        List<Byte> dataList = new ArrayList<Byte>(dataArray.length);
        for (int i = 0; i < dataArray.length; i++)
            dataList.add(dataArray[i]);
        return dataList;
    }

    public static byte[] toArray(List<Byte> dataList) {
        byte[] dataArray = new byte[dataList.size()];
        for (int i = 0; i < dataList.size(); i++)
            dataArray[i] = dataList.get(i);
        return dataArray;
    }

    public static int buildInteger(List<Byte> integerBytes) {
        if (integerBytes.size() != 4)
            throw new IllegalArgumentException("A 32 bit integer must have exactly 4 bytes to assemble it.");

        return buildInteger(integerBytes.get(0),
                            integerBytes.get(1),
                            integerBytes.get(2),
                            integerBytes.get(3));
    }

    public static int buildInteger(byte msb, byte nextMsb, byte prevLsb, byte lsb) {
        int msbUnsigned     = msb     & 0xff;
        int nextMsbUnsigned = nextMsb & 0xff;
        int prevLsbUnsigned = prevLsb & 0xff;
        int lsbUnsigned     = lsb     & 0xff;

        int fullInteger = msbUnsigned     << 24
                        | nextMsbUnsigned << 16
                        | prevLsbUnsigned <<  8
                        | lsbUnsigned;

        return fullInteger;
    }

}
