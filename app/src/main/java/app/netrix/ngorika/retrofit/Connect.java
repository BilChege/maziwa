

package app.netrix.ngorika.retrofit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public  class Connect {

//    public final static String SERVER_LOCAL = "http://10.0.2.2/task_manager/v1";
//    public final static String SERVER_LOCAL= "http://159.203.139.54/task_manager/v1";
    public final static String SERVER_LOCAL="http://192.168.43.89:8080/MaziwaAPI/";
    public final static String URL_LOGIN = "/login";
    public final static String URL_GETDETAILS = "request.php";
    public final static String URL_UPLOAD = "group.php";

//http://172.104.242.19:8282/manager/html/upload?org.apache.catalina.filters.CSRF_NONCE=6251EF2732863848861CDCA42E221C15


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
