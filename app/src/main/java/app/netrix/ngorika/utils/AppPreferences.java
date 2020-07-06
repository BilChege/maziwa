package app.netrix.ngorika.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by chrissie on 12/5/15.
 */
public class AppPreferences {
    private String username;
    private int route;
    private boolean routesConfiged;
    private final SharedPreferences sharedPreferences;
    private final Context _context;
    private SharedPreferences.Editor editor;
    private int routeCount;
    private int farmerId;
    private int userID;
    private String graderName;
    private String farmerName;
    private String routeName;
    private boolean isFarmerLoaded;
    public AppPreferences(Context context) {
        _context=context;
        sharedPreferences=_context.getSharedPreferences("NG_PREFS",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        reloadPrefs();
    }
    public void reloadPrefs(){
        username=this.sharedPreferences.getString(PrefFeys.keyUser, "");
        route=this.sharedPreferences.getInt(PrefFeys.keyRoute, 0);
        routesConfiged=this.sharedPreferences.getBoolean(PrefFeys.keyConfig, routesConfiged);
        routeCount=this.sharedPreferences.getInt(PrefFeys.keyCount, 0);
      farmerId=this.sharedPreferences.getInt(PrefFeys.keyFarmer, 0);
       userID=this.sharedPreferences.getInt(PrefFeys.keyUserID, 0);
        graderName=this.sharedPreferences.getString(PrefFeys.keyGrader, "");
        farmerName=this.sharedPreferences.getString(PrefFeys.keyFarmerName,"");
        routeName=this.sharedPreferences.getString(PrefFeys.keyROUTENAME,"");
        isFarmerLoaded=this.sharedPreferences.getBoolean(PrefFeys.keyFARMERSLOADED, false);
    }
    public void clearPrefs(){
        sharedPreferences.getAll().clear();
        editor.commit();
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        editor.putString(PrefFeys.keyUser,username);
        editor.commit();
        this.username=username;
    }

    public int getRoute() {
        return route;
    }

    public void setRoute(int route) {
        editor.putInt(PrefFeys.keyRoute,route);
        editor.commit();
        this.route = route;
    }

    public boolean isRoutesConfiged() {
        return routesConfiged;
    }

    public void setRoutesConfiged(boolean routesConfiged) {
        editor.putBoolean(PrefFeys.keyConfig, routesConfiged);
        editor.commit();
        this.routesConfiged = routesConfiged;
    }

    public int getRouteCount() {
        return routeCount;
    }

    public void setRouteCount(int routeCount) {
        editor.putInt(PrefFeys.keyCount, routeCount);
        editor.commit();
        this.routeCount = routeCount;
    }

    public int getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(int farmerId) {
        editor.putInt(PrefFeys.keyFarmer, farmerId);
        editor.commit();
        this.farmerId = farmerId;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        editor.putInt(PrefFeys.keyUserID, userID);
        editor.commit();
        this.userID = userID;
    }

    public String getGraderName() {
        return graderName;
    }

    public void setGraderName(String graderName) {
        editor.putString(PrefFeys.keyGrader, graderName);
        editor.commit();
        this.graderName = graderName;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        editor.putString(PrefFeys.keyFarmerName, farmerName);
        editor.commit();
        this.farmerName = farmerName;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        editor.putString(PrefFeys.keyROUTENAME, routeName);
        editor.commit();
        this.routeName = routeName;
    }

    public boolean isFarmerLoaded() {
        return isFarmerLoaded;
    }

    public void setIsFarmerLoaded(boolean isFarmerLoaded) {
        editor.putBoolean(PrefFeys.keyFARMERSLOADED, isFarmerLoaded);
        editor.commit();
        this.isFarmerLoaded = isFarmerLoaded;
    }
}
