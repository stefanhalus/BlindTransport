package ro.stefanhalus.android.blindtransport.DatabaseModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ro.stefanhalus.android.blindtransport.BusesActivity;
import ro.stefanhalus.android.blindtransport.Models.LinesModel;
import ro.stefanhalus.android.blindtransport.Models.StationsModel;
import ro.stefanhalus.android.blindtransport.R;

public class StationsArrayAdapter extends ArrayAdapter<StationsModel> {
    private final Context context;
    private final ArrayList<StationsModel> values;

    public StationsArrayAdapter(Context context, ArrayList<StationsModel> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final StationsModel currentLine = values.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") final View rowView = inflater.inflate(R.layout.util_list_item_simple, parent, false);
        TextView textView = rowView.findViewById(R.id.rowTextView);
        ImageView imageView = rowView.findViewById(R.id.rowImageView);
        final CheckBox checkBox = rowView.findViewById(R.id.rowCheckBox);
        textView.setText(currentLine.getName());
        return rowView;
    }
}