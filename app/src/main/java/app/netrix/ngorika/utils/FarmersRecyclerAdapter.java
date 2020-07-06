package app.netrix.ngorika.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.netrix.ngorika.R;
import app.netrix.ngorika.pojo.Farmer;

public class FarmersRecyclerAdapter extends RecyclerView.Adapter<FarmersRecyclerAdapter.ViewHolder> implements Filterable{

    private Context context;
    private List<Farmer> farmers;
    private List<Farmer> original = new ArrayList<>();
    private Config.ItemsListener listener;

    public FarmersRecyclerAdapter(Context context, List<Farmer> farmers) {
        this.context = context;
        listener = (Config.ItemsListener) context;
        this.farmers = farmers;
        original.addAll(farmers);
    }

    public void setFarmers(List<Farmer> farmers) {
        this.farmers = farmers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.farmer,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Farmer farmer = farmers.get(i);
        viewHolder.farmerName.setText(farmer.getName());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.farmerSelected(farmer);
            }
        });
    }

    @Override
    public int getItemCount() {
        return farmers.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint){
                ArrayList<Farmer> filteredList = new ArrayList<>();
                if (constraint != null && constraint.length() > 0){
                    String searchString = constraint.toString().toLowerCase().trim();
                    System.out.println("----------> SEARCH STRING: "+searchString);
                    for (Farmer farmer : original){
                        if (farmer.getName().toLowerCase().contains(searchString)){
                            filteredList.add(farmer);
                        }
                    }
                } else {
                    filteredList.clear();
                    filteredList.addAll(original);
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                filterResults.count = filteredList.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                farmers.clear();
                if (results != null && results.count > 0){
                    farmers.addAll((ArrayList<Farmer>) results.values);
                }
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView farmerName;
        ImageView iconImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            farmerName = itemView.findViewById(R.id.farmerName);
            iconImage = itemView.findViewById(R.id.imgIcon);
        }
    }

}