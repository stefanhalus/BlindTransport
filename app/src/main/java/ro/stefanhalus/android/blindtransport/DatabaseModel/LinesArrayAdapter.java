package ro.stefanhalus.android.blindtransport.DatabaseModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
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
import ro.stefanhalus.android.blindtransport.R;

public class LinesArrayAdapter extends ArrayAdapter<LinesModel> {
    private final Context context;
    private final ArrayList<LinesModel> values;

    public LinesArrayAdapter(Context context, ArrayList<LinesModel> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(final int position, View convertView, @NonNull final ViewGroup parent) {
        final LinesModel currentLine = values.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") final View rowView = inflater.inflate(R.layout.util_list_checkbox, parent, false);
        TextView textView = rowView.findViewById(R.id.rowTextView);
        ImageView imageView = rowView.findViewById(R.id.rowImageView);
        final CheckBox checkBox = rowView.findViewById(R.id.rowCheckBox);
        if(BusesActivity.selectedCheckboxItems.containsKey(position)){
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
        textView.setText(currentLine.getName());
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToast = "";
                if (checkBox.isChecked()) {
                    BusesActivity.selectedCheckboxItems.put(position, currentLine);
                    textToast += context.getString(R.string.list_checkbox_checked, currentLine.getName());
                } else {
                    BusesActivity.selectedCheckboxItems.remove(position);
                    textToast += context.getString(R.string.list_checkbox_unchecked, currentLine.getName());
                }
                Toast toast = Toast.makeText(context, textToast, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
        return rowView;
    }
}