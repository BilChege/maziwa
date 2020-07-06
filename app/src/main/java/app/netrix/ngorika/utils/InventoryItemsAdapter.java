package app.netrix.ngorika.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.netrix.ngorika.R;
import app.netrix.ngorika.pojo.InventoryItem;

public class InventoryItemsAdapter extends RecyclerView.Adapter<InventoryItemsAdapter.ViewHolder> {

    private List<InventoryItem> inventoryItems;
    private Context context;
    private Config.ItemsListener itemsListener;

    public InventoryItemsAdapter(List<InventoryItem> inventoryItems, Context context) {
        this.inventoryItems = inventoryItems;
        this.context = context;
        itemsListener = (Config.ItemsListener) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.inventory_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final InventoryItem inventoryItem = inventoryItems.get(i);
        viewHolder.desc.append(inventoryItem.getDesc());
        viewHolder.invCode.append(inventoryItem.getInternalCode());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemsListener.itemSelected(inventoryItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return inventoryItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView desc, invCode;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            desc = itemView.findViewById(R.id.itemDesc);
            invCode = itemView.findViewById(R.id.invCode);
        }
    }

}
