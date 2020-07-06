package app.netrix.ngorika.data;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import app.netrix.ngorika.R;
import app.netrix.ngorika.pojo.DataBundle;

/**
 * Created by CW on 18/01/2016.
 */
public class UnsentDataAdapter extends BaseAdapter {
    ArrayList<DataBundle> records;
    Activity _context;
    LayoutInflater inflater;
    SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    public UnsentDataAdapter(Activity _context, ArrayList<DataBundle> records) {
        super();
        this._context=_context;
        this.records=records;
        this.inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public Object getItem(int position) {
        return records.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       DataBundle dataBundle= (DataBundle) getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder=new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item, null);
            viewHolder.tv1=(TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv2=(TextView) convertView.findViewById(R.id.tv_answer);
            viewHolder.imv1=(ImageView) convertView.findViewById(R.id.imv_next);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        viewHolder.tv1.setText((position + 1) + " - " + dataBundle.getFarmerName());
        viewHolder.tv2.setText("Weight:" + dataBundle.getWeight() + "\t Date:" + dateformat.format(dataBundle.getTime()));
        return convertView;
    }

    @Override
    public int getCount() {
        return records.size();
    }
    static class ViewHolder{
        TextView tv1;
        TextView tv2;
        ImageView imv1;
    }
}
