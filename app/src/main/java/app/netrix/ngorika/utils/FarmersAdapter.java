package app.netrix.ngorika.utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;


import java.util.ArrayList;

import app.netrix.ngorika.R;
import app.netrix.ngorika.pojo.Farmer;


public class FarmersAdapter extends ArrayAdapter<Farmer>{

    private Activity context;
    ArrayList<Farmer> data = null;
    private ArrayList<Farmer> suggestions;
    private ArrayList<Farmer> itemsAll;
    private ArrayList<Farmer> items;
    public FarmersAdapter(Activity context, int resource, ArrayList<Farmer> data)
    {
        super(context, resource, data);
        this.context = context;
        this.data = data;
        itemsAll=(ArrayList<Farmer>) data.clone();
        this.suggestions = new ArrayList<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {   // Ordinary view in Spinner, we use android.R.layout.simple_spinner_item

        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {   // This view starts when we click the spinner.
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if(row == null)
        {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_item, parent, false);
        }
        Farmer item = data.get(position);
        String test = item.getName();
//        Log.d("test ", test);

        if(item != null)
        {   // Parse the data from each object and set it.
            TextView uuid  = (TextView) row.findViewById(R.id.item_id);
            TextView name = (TextView) row.findViewById(R.id.item_value);
            if(uuid != null)
            {
                uuid.setText(""+item.getId());
            }
            if(name != null){
                name.setText(item.getName());
            }

        }

        return row;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((Farmer)(resultValue)).getName();
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null) {
                ArrayList<Farmer> suggestions = new ArrayList<Farmer>();
                for (Farmer customer : itemsAll) {
                    if (customer.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(customer);
                    }
                }

                results.values = suggestions;
                results.count = suggestions.size();
            }

            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            if (results != null && results.count > 0) {
                // we have filtered results
                addAll((ArrayList<Farmer>) results.values);
            } else {
                // no filter, add entire original list back in
                addAll(itemsAll);
            }
            notifyDataSetChanged();
        }




    };
}
