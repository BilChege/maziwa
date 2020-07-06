package app.netrix.ngorika.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.netrix.ngorika.R;
import app.netrix.ngorika.pojo.InventoryItem;
import app.netrix.ngorika.retrofit.RConsumer;
import app.netrix.ngorika.utils.ActionError;
import app.netrix.ngorika.utils.ActionSuccess;
import app.netrix.ngorika.utils.Config;
import app.netrix.ngorika.utils.InventoryItemsAdapter;

public class InventoryItems extends Fragment {

    private RecyclerView recyclerView;
    private InventoryItemsAdapter adapter;
    private Config.ItemsListener itemsListener;
    private Button checkOutButton, cancelReqButton;
    private Context context;
    private RecyclerView.LayoutManager layoutManager;
    List<InventoryItem> inventoryItems = new ArrayList<>();

    public InventoryItems() {

    }

    public static InventoryItems init(List<InventoryItem> items){
        InventoryItems inventoryItems = new InventoryItems();
        inventoryItems.setInventoryItems(items);
        return inventoryItems;
    }

    public void setInventoryItems(List<InventoryItem> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inventoryitems,container,false);
        recyclerView = view.findViewById(R.id.invitems);
        cancelReqButton = view.findViewById(R.id.cancelRequisition);
        checkOutButton = view.findViewById(R.id.checkOut);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        adapter = new InventoryItemsAdapter(inventoryItems,context);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        checkOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemsListener.checkOut();
            }
        });
        cancelReqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemsListener.cancelRequisition();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        itemsListener = (Config.ItemsListener) context;
        super.onAttach(context);
    }

}