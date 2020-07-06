package app.netrix.ngorika.services;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import app.netrix.ngorika.data.DatabaseHandler;
import app.netrix.ngorika.pojo.Farmer;
import app.netrix.ngorika.retrofit.Connect;
import app.netrix.ngorika.retrofit.RConsumer;
import app.netrix.ngorika.utils.ActionError;
import app.netrix.ngorika.utils.ActionSuccess;

/**
 * Created by chrissie on 12/2/15.
 */
public class GetFarmers {
    Context context;
    DatabaseHandler databaseHandler;
    int route;

    public GetFarmers(Context context, int route) {
        this.context = context;
        databaseHandler = new DatabaseHandler(context);
        this.route = route;

    }

    public ArrayList<Farmer> loadFromServer() {
        if (Connect.isNetworkAvailable(context)) {
            RConsumer.getFarmers(context,route, new ActionSuccess() {
                @Override
                public void action(String json) {
                    Log.d("routes", "farmers:Vals:" + json);
                    try {
                        if (json != null) {

                            JSONObject jsonObject = new JSONObject(json);
                            JSONArray jsonArray = jsonObject.getJSONArray("farmers");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject row = jsonArray.getJSONObject(i);
                                String name = row.getString("name");
                                int id = row.getInt("id");
                                int route = row.getInt("route");
                                databaseHandler.insertFarmer(name, id,route);
                                Log.d("Farmers", "Farmer:" + name);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new ActionError() {
                @Override
                public void action() {
                    Log.d("Routes", "Routes:Error");
                }
            });
        } else {
            //No Internet
            Log.d("Routes", "Routes:No internet");
        }

    return databaseHandler.getAllFarmers(route);
    }

}

