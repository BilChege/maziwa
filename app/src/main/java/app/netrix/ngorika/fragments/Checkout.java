package app.netrix.ngorika.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import app.netrix.ngorika.MaterialRequisition;
import app.netrix.ngorika.R;
import app.netrix.ngorika.data.SessionPreferences;
import app.netrix.ngorika.pojo.Farmer;
import app.netrix.ngorika.pojo.MaterialRequisitionDetail;
import app.netrix.ngorika.pojo.MaterialRequisitionObj;
import app.netrix.ngorika.pojo.MrResponse;
import app.netrix.ngorika.retrofit.ConnectService;
import app.netrix.ngorika.retrofit.PostService2;
import app.netrix.ngorika.retrofit.RConsumer;
import app.netrix.ngorika.utils.ActionError;
import app.netrix.ngorika.utils.ActionSuccess;
import app.netrix.ngorika.utils.AppPreferences;
import app.netrix.ngorika.utils.Config;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class Checkout extends Fragment {

    private Context context;
    private List<MaterialRequisitionDetail> materialRequisitionDetails;
    private Config.ItemsListener listener;
    private LinearLayout itemsContainer;
    private TextInputEditText totalView;
    private AppPreferences appPreferences;
    private Button postRequisition;
    private SessionPreferences sessionPreferences;
    private Farmer selectedFarmer;

    public Checkout() {
    }

    public static Checkout init(List<MaterialRequisitionDetail> details){
        Checkout checkout = new Checkout();
        checkout.setMaterialRequisitionDetails(details);
        return checkout;
    }

    public void setMaterialRequisitionDetails(List<MaterialRequisitionDetail> materialRequisitionDetails) {
        this.materialRequisitionDetails = materialRequisitionDetails;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.checkout,container,false);
        itemsContainer = view.findViewById(R.id.requisitionDetails);
        totalView = view.findViewById(R.id.requisitionTotal);
        postRequisition = view.findViewById(R.id.postRequisition);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        appPreferences = new AppPreferences(context);
        sessionPreferences = new SessionPreferences(context);
        final Farmer farmer = sessionPreferences.getSelectedFarmer();
        this.selectedFarmer = farmer;
        double total = 0;
        for (final MaterialRequisitionDetail materialRequisitionDetail: materialRequisitionDetails){
            final View reqItemView = LayoutInflater.from(context).inflate(R.layout.mrdetail,itemsContainer,false);
            TextView itmDesc = reqItemView.findViewById(R.id.txtItemDescription);
            TextView itmQty = reqItemView.findViewById(R.id.txtItemQuantity);
            TextView itmTotal = reqItemView.findViewById(R.id.txtItemTotal);
            itmDesc.append(materialRequisitionDetail.getDesc());
            itmQty.append(String.valueOf(materialRequisitionDetail.getQty())+" "+materialRequisitionDetail.getUom());
            itmTotal.append(String.valueOf(materialRequisitionDetail.getTotal())+" "+materialRequisitionDetail.getCurr());
            total += materialRequisitionDetail.getTotal();
            Button remove = reqItemView.findViewById(R.id.btnRemove);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context)
                            .setTitle("Remove item")
                            .setMessage("Are you sure you want to remove this item")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    itemsContainer.removeView(reqItemView);
                                    materialRequisitionDetails.remove(materialRequisitionDetail);
                                    double currTotal = Double.parseDouble(totalView.getText().toString());
                                    currTotal -= materialRequisitionDetail.getTotal();
                                    totalView.setText(String.valueOf(currTotal));
                                }
                            }).setNegativeButton("No",null).show();
                }
            });
            itemsContainer.addView(reqItemView);
        }
        totalView.setText(String.valueOf(total));
        postRequisition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MaterialRequisitionObj materialRequisitionObj = new MaterialRequisitionObj();
                materialRequisitionObj.setMaterialRequisitionDetails(materialRequisitionDetails);
                materialRequisitionObj.setMrType("FARMER");
                materialRequisitionObj.setRef("MR");
                materialRequisitionObj.setFarmer(farmer.getId());
                materialRequisitionObj.setTotal(Double.parseDouble(totalView.getText().toString()));
                materialRequisitionObj.setUser(appPreferences.getUserID());
                new AlertDialog.Builder(context)
                        .setTitle("Post requisition?")
                        .setMessage("Are you sure you want to post this request?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ProgressDialog dialog1 = new ProgressDialog(context);
                                dialog1.setMessage("Making requisition ... ");
                                dialog1.show();
                                postData(materialRequisitionObj,dialog1);
                            }
                        }).setNegativeButton("No",null).show();
            }
        });
    }

    private void postData(MaterialRequisitionObj materialRequisitionObj, final ProgressDialog dialog1) {
        System.out.println("---------> REQUEST OBJECT "+materialRequisitionObj.toString());
        PostService2 postService2 = ConnectService.getNetClient();
        Call<MrResponse> call = postService2.makeRequisition(materialRequisitionObj);
        System.out.println(call.request().url());
        System.out.println(call.request().body());
        call.enqueue(new Callback<MrResponse>() {
            @Override
            public void onResponse(Call<MrResponse> call, Response<MrResponse> response) {
                dialog1.dismiss();
                if (response != null){
                    int statusCode = response.code();
                    if (statusCode == 200){
                        MrResponse mrResponse = response.body();
                        if (mrResponse.getResultcode() == 0){
                            Toast.makeText(context,"Material requisition made successfully",Toast.LENGTH_SHORT).show();
                            AppCompatActivity activity = (AppCompatActivity) context;
                            activity.finish();
                        } else {
                            Toast.makeText(context,"Could not make requisition",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context,"Error "+statusCode+" occurred",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context,"No response from the Server",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MrResponse> call, Throwable t) {
                dialog1.dismiss();
                Toast.makeText(context,"Error occurred : "+t.getMessage(),Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        listener = (Config.ItemsListener) context;
        super.onAttach(context);
    }
}