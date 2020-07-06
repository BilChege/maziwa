package app.netrix.ngorika.bluetooth.utils;


import java.util.List;

public class Math {

    public static int clamp(int min, int max, int value) {
        if (value < min)        return min;
        else if (value > max)   return max;
        else                    return value;
    }

    public static <T> int clampIndex(List<T> list, int index) {
        return clamp(0, list.size()-1, index);
    }
}
