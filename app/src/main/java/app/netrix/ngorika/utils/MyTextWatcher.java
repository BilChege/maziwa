package app.netrix.ngorika.utils;

/**
 * Created by chrissie on 12/11/15.
 */

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import app.netrix.ngorika.MainActivity;

public class MyTextWatcher implements TextWatcher {

AppPreferences preferences;
    public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
    Context context;

    public MyTextWatcher(Context context){
        this.context = context;
        preferences=new AppPreferences(context);
    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence userInput, int start, int before, int count) {



        MainActivity mainActivity = ((MainActivity) context);

        /*// query the database based on the user input
        mainActivity.item = mainActivity.getItemsFromDb(userInput.toString());

        // update the adapater
        mainActivity.myAdapter.notifyDataSetChanged();
        mainActivity.myAdapter = new ArrayAdapter<String>(mainActivity, android.R.layout.simple_dropdown_item_1line, mainActivity.item);
        mainActivity.myAutoComplete.setAdapter(mainActivity.myAdapter);*/

        mainActivity.farmerArrayList =mainActivity.loadFarmers(preferences.getRoute());
        mainActivity.farmersAdapter.notifyDataSetChanged();
        mainActivity.farmersAdapter.notifyDataSetChanged();
        mainActivity.farmersAdapter = new FarmersAdapter(mainActivity, android.R.layout.simple_dropdown_item_1line, mainActivity.farmerArrayList);
        mainActivity.autoText.setAdapter(mainActivity.farmersAdapter);


      /*
       mainActivity.myAdapter.notifyDataSetChanged();
       mainActivity.myAdapter = new ArrayAdapter(mainActivity, android.R.layout.simple_spinner_item, mainActivity.farmersArrayList);
        mainActivity.autoText.setAdapter(mainActivity.myAdapter);*/


    }

}