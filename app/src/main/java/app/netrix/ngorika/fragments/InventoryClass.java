package app.netrix.ngorika.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.netrix.ngorika.R;
import app.netrix.ngorika.pojo.InvClass;
import app.netrix.ngorika.pojo.InventoryItem;
import app.netrix.ngorika.retrofit.RConsumer;
import app.netrix.ngorika.utils.ActionError;
import app.netrix.ngorika.utils.ActionSuccess;
import app.netrix.ngorika.utils.Config;

public class InventoryClass extends Fragment {

    private Context context;
    private TextInputEditText search;
    private Button btnSearch, btnClear;
    private SearchableSpinner invClassesSpinner;
    private Config.ItemsListener itemsListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inventoryclass,container,false);
        search = view.findViewById(R.id.itemSearch);
        invClassesSpinner = view.findViewById(R.id.invClasses);
        btnSearch = view.findViewById(R.id.btnSearch);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        populateClassesView();
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    InvClass selectedClass = (InvClass) invClassesSpinner.getSelectedItem();
                    String invClassId = selectedClass != null ? String.valueOf(selectedClass.getId()) : null;
                    String desc = search.getText().toString();
                    ProgressDialog progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Getting items ... ");
                    progressDialog.show();
                    HashMap<String,String> qParams = new HashMap<>();
                    qParams.put("itemDesc",!desc.isEmpty() ? desc : "na");
                    qParams.put("itemClass",invClassId != null && !invClassId.isEmpty() ? invClassId : "na");
                    RConsumer.getInventoryItems(context, qParams, new ActionSuccess() {
                        @Override
                        public void action(String json) {
                            try {
                                JSONObject jsonResponse = new JSONObject(json);
                                boolean empty = jsonResponse.getBoolean("empty");
                                if (!empty){
                                    List<InventoryItem> inventoryItems = new ArrayList<>();
                                    JSONArray itemsArray = jsonResponse.getJSONArray("items");
                                    for (int i = 0; i < itemsArray.length(); i++){
                                        JSONObject obj = itemsArray.getJSONObject(i);
                                        InventoryItem inventoryItem = new InventoryItem();
                                        inventoryItem.setId(obj.getInt("id"));
                                        inventoryItem.setPrice(obj.getDouble("price"));
                                        inventoryItem.setCurrency(obj.getString("currency"));
                                        inventoryItem.setUom(obj.getString("uom"));
                                        inventoryItem.setDesc(obj.getString("desc"));
                                        inventoryItem.setVat(obj.getDouble("vat"));
                                        inventoryItem.setInternalCode(obj.getString("internalCode"));
                                        inventoryItems.add(inventoryItem);
                                    }
                                    itemsListener.itemsToShow(inventoryItems);
                                } else {
                                    new AlertDialog.Builder(context)
                                            .setTitle("No Items Found")
                                            .setMessage("There were no items found under the specified search criteria")
                                            .setPositiveButton("Ok",null).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new ActionError() {
                        @Override
                        public void action() {

                        }
                    },progressDialog);
                }
            }
        });
    }

    private boolean validate() {
        boolean validated = true;
        if (TextUtils.isEmpty(search.getText().toString()) && invClassesSpinner.getSelectedItem() == null){
            search.setError("Specify at least one search criteria");
            TextView spnView = (TextView) invClassesSpinner.getSelectedView();
            spnView.setError("Specify at leas one search criteria");
            validated = false;
        } else if (!search.getText().toString().isEmpty()){
            String desc = search.getText().toString();
            if (desc.length() < 3){
                search.setError("Input should be 3 letters or more");
                validated = false;
            }
        }
        return validated;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.itemsListener = (Config.ItemsListener) context;
    }

    private void populateClassesView(){
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Initializing ...");
        dialog.show();
        RConsumer.getInventoryClasses(context, new ActionSuccess() {
            @Override
            public void action(String json){
                try {
                    JSONObject jsonResponse = new JSONObject(json);
                    boolean empty = jsonResponse.getBoolean("empty");
                    List<InvClass> invClasses = new ArrayList<>();
                    if (!empty){
                        JSONArray jsonArray = jsonResponse.getJSONArray("invclasses");
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject invclass = jsonArray.getJSONObject(i);
                            InvClass invClass = new InvClass();
                            invClass.setId(invclass.getInt("id"));
                            invClass.setName(invclass.getString("name"));
                            invClasses.add(invClass);
                        }
                        InvClass invClass = new InvClass();
                        invClass.setName("Select Class");
                        invClasses.add(invClass);
                    } else {
                        Toast.makeText(context,"There were no inventory classes found",Toast.LENGTH_LONG).show();
                    }
                    ArrayAdapter<InvClass> classesAdapter = new ArrayAdapter<InvClass>(context,android.R.layout.simple_spinner_item,invClasses);
                    classesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    invClassesSpinner.setAdapter(classesAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new ActionError() {
            @Override
            public void action() {

            }
        },dialog);
    }

}