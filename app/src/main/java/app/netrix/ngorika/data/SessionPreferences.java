package app.netrix.ngorika.data;

import android.content.Context;
import android.content.SharedPreferences;

import app.netrix.ngorika.pojo.Farmer;

public class SessionPreferences {

    private Context context;

    private String farmerName = "farmerName";
    private String farmerId = "farmerId";
    private String selectedFarmer = "selectedFarmer";

    public SessionPreferences(Context context) {
        this.context = context;
    }

    public SharedPreferences getSharedPreferences(String fileName){
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName,context.MODE_PRIVATE);
        return sharedPreferences;
    }

    public SharedPreferences.Editor getEditor(String fileName){
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName,context.MODE_PRIVATE);
        return sharedPreferences.edit();
    }

    public void setSelectedFarmer(Farmer farmer){
        SharedPreferences.Editor editor = getEditor(selectedFarmer);
        editor.putInt(farmerId,farmer.getId());
        editor.putString(farmerName,farmer.getName());
        editor.commit();
    }

    public Farmer getSelectedFarmer(){
        SharedPreferences sharedPreferences = getSharedPreferences(selectedFarmer);
        Farmer farmer = new Farmer();
        farmer.setId(sharedPreferences.getInt(farmerId,0));
        farmer.setName(sharedPreferences.getString(farmerName,null));
        return farmer;
    }

}