package app.netrix.ngorika.utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.ArrayList;

import app.netrix.ngorika.R;
import app.netrix.ngorika.pojo.Routes;

public class RoutesAdapter extends ArrayAdapter<Routes>
{

    private Activity context;
    ArrayList<Routes> data = null;

    public RoutesAdapter(Activity context, int resource, ArrayList<Routes> data)
    {
        super(context, resource, data);
        this.context = context;
        this.data = data;
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
       Routes item = data.get(position);
        String test = item.getRoute();
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
            name.setText(item.getRoute());
            }
 
        }

        return row;
    }
}
