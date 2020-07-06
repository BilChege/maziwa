package app.netrix.ngorika.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import app.netrix.ngorika.R;
import app.netrix.ngorika.pojo.InventoryItem;
import app.netrix.ngorika.pojo.MaterialRequisitionDetail;
import app.netrix.ngorika.retrofit.RConsumer;
import app.netrix.ngorika.utils.ActionError;
import app.netrix.ngorika.utils.ActionSuccess;
import app.netrix.ngorika.utils.AppPreferences;
import app.netrix.ngorika.utils.Config;

public class ItemDetails extends Fragment {

    private Context context;
    private InventoryItem inventoryItem;
    private Config.ItemsListener listener;
    private TextView itmDesc, itmPrice, itmQty;
    private TextInputEditText quantity, total;
    private TextInputLayout tlSpQty, tlTotal;
    private Button addItem;
    private AppPreferences appPreferences;
    private double currentQuantity,totalAmount,specifiedQuantity;

    public static ItemDetails init(InventoryItem inventoryItem){
        ItemDetails itemDetails = new ItemDetails();
        itemDetails.setInventoryItem(inventoryItem);
        return itemDetails;
    }

    public ItemDetails() {
    }

    public void setInventoryItem(InventoryItem inventoryItem) {
        this.inventoryItem = inventoryItem;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.itemdetails,container,false);
        itmDesc = view.findViewById(R.id.txtItemDescription);
        itmPrice = view.findViewById(R.id.txtItemPrice);
        itmQty = view.findViewById(R.id.txtItemQuantity);
        quantity = view.findViewById(R.id.itmQty);
        tlSpQty = view.findViewById(R.id.tlSpQty);
        tlTotal = view.findViewById(R.id.tlTotal);
        total = view.findViewById(R.id.total);
        addItem = view.findViewById(R.id.btnAddItem);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        appPreferences = new AppPreferences(context);
        itmDesc.append(inventoryItem.getDesc());
        itmPrice.append(String.valueOf(inventoryItem.getPrice())+" "+inventoryItem.getCurrency());
        getItemCurrentQty();
        String hint = tlSpQty.getHint().toString().concat(" ("+inventoryItem.getUom()+")");;
        tlSpQty.setHint(hint);
        String ttHint = tlTotal.getHint().toString().concat(" ("+inventoryItem.getCurrency()+")");
        tlTotal.setHint(ttHint);
        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString();
                if (!input.isEmpty()){
                    double qty = Double.parseDouble(input);
                    if (currentQuantity >= qty){
                        specifiedQuantity = qty;
                        double price = inventoryItem.getPrice();
                        totalAmount = price * qty;
                        total.setText(String.valueOf(totalAmount));
                    } else {
                        quantity.setError("Amount specified is greater than current quantity");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    MaterialRequisitionDetail materialRequisitionDetail = new MaterialRequisitionDetail();
                    materialRequisitionDetail.setInv(inventoryItem.getId());
                    materialRequisitionDetail.setPrice(inventoryItem.getPrice());
                    materialRequisitionDetail.setQty(specifiedQuantity);
                    materialRequisitionDetail.setTotal(totalAmount);
                    materialRequisitionDetail.setDesc(inventoryItem.getDesc());
                    materialRequisitionDetail.setUom(inventoryItem.getUom());
                    materialRequisitionDetail.setCurr(inventoryItem.getCurrency());
                    listener.itemAddedToCart(materialRequisitionDetail);
                    Toast.makeText(context,"Item has been added",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateTotal(double totalAmount) {

    }

    private boolean validate() {
        boolean validated = true;
        if (TextUtils.isEmpty(quantity.getText().toString())){
            quantity.setError("Specify quantity");
            validated = false;
        } else {
            double qty = Double.parseDouble(quantity.getText().toString());
            if (currentQuantity < qty){
                validated = false;
            }
        }
        return validated;
    }

    private void getItemCurrentQty() {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Loading ... ");
        dialog.show();
        RConsumer.getItemQuantity(context, inventoryItem.getId(), new ActionSuccess() {
            @Override
            public void action(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    currentQuantity = jsonObject.getDouble("currqty");
                    itmQty.append(String.valueOf(currentQuantity)+" "+inventoryItem.getUom());
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

    @Override
    public void onAttach(Context context) {
        this.context = context;
        this.listener = (Config.ItemsListener) context;
        super.onAttach(context);
    }
}
