package app.netrix.ngorika.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;

import app.netrix.ngorika.R;
import app.netrix.ngorika.data.DatabaseHandler;
import app.netrix.ngorika.pojo.Farmer;
import app.netrix.ngorika.utils.AppPreferences;
import app.netrix.ngorika.utils.FarmersRecyclerAdapter;

public class ShowFarmers extends Fragment {

    private DatabaseHandler databaseHandler;
    private Context context;
    private AppPreferences appPreferences;
    private RecyclerView farmersRV;
    private EditText search;
    private FarmersRecyclerAdapter adapter;

    public ShowFarmers() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.showfarmers,container,false);
        farmersRV = view.findViewById(R.id.farmers);
        search = view.findViewById(R.id.search);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        databaseHandler = new DatabaseHandler(context);
        appPreferences = new AppPreferences(context);
        List<Farmer> farmers = databaseHandler.getAllFarmers(appPreferences.getRoute());
        adapter = new FarmersRecyclerAdapter(context,farmers);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        farmersRV.setLayoutManager(layoutManager);
        farmersRV.setAdapter(adapter);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }
}
