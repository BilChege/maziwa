package app.netrix.ngorika;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import app.netrix.ngorika.data.SessionPreferences;
import app.netrix.ngorika.fragments.Checkout;
import app.netrix.ngorika.fragments.InventoryClass;
import app.netrix.ngorika.fragments.InventoryItems;
import app.netrix.ngorika.fragments.ItemDetails;
import app.netrix.ngorika.fragments.ShowFarmers;
import app.netrix.ngorika.pojo.Farmer;
import app.netrix.ngorika.pojo.InvClass;
import app.netrix.ngorika.pojo.InventoryItem;
import app.netrix.ngorika.pojo.MaterialRequisitionDetail;
import app.netrix.ngorika.utils.Config;

public class MaterialRequisition extends AppCompatActivity implements Config.ItemsListener{

    private List<MaterialRequisitionDetail> materialRequisitionDetails = new ArrayList<>();
    private SessionPreferences sessionPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_requisition);
        getSupportActionBar().setIcon(R.mipmap.milkicon);
        Fragment current = getSupportFragmentManager().findFragmentById(R.id.frame);
        if (current == null){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Fragment showFarmers = new ShowFarmers();
            transaction.add(R.id.frame,showFarmers);
            transaction.commit();
        }
    }

    @Override
    public void itemSelected(InventoryItem inventoryItem) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment itemDetails = ItemDetails.init(inventoryItem);
        transaction.replace(R.id.frame,itemDetails);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void itemAddedToCart(MaterialRequisitionDetail materialRequisitionDetail) {
        materialRequisitionDetails.add(materialRequisitionDetail);
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void itemsToShow(List<InventoryItem> items) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment inventoryItems = InventoryItems.init(items);
        transaction.replace(R.id.frame,inventoryItems);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void checkOut() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment checkOut = Checkout.init(materialRequisitionDetails);
        transaction.replace(R.id.frame,checkOut);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void cancelRequisition() {
        if (!materialRequisitionDetails.isEmpty()){
            new AlertDialog.Builder(MaterialRequisition.this)
                    .setTitle("Items pending")
                    .setMessage("Are you sure you want to exit? You will lose all data associated with the ongoing transaction.")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).setNegativeButton("No",null).show();
        } else {
            finish();
        }
    }

    @Override
    public void farmerSelected(Farmer farmer){
        sessionPreferences = new SessionPreferences(MaterialRequisition.this);
        sessionPreferences.setSelectedFarmer(farmer);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment invClass = new InventoryClass();
        fragmentTransaction.replace(R.id.frame,invClass);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        Fragment current = getSupportFragmentManager().findFragmentById(R.id.frame);
        if (current instanceof InventoryClass){
            if (!materialRequisitionDetails.isEmpty()){
                new AlertDialog.Builder(MaterialRequisition.this)
                        .setTitle("Items pending")
                        .setMessage("Are you sure you want to exit? You will lose all data associated with the ongoing transaction.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getSupportFragmentManager().popBackStack();
                            }
                        }).setNegativeButton("No",null).show();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        } else if (current instanceof ShowFarmers) {
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
}