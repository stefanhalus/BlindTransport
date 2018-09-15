package ro.stefanhalus.android.blindtransport.DatabaseModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ro.stefanhalus.android.blindtransport.Models.LinesFoundModel;
import ro.stefanhalus.android.blindtransport.R;

public class CardsArrayAdapter  extends BaseAdapter {
    Context c;
    ArrayList<LinesFoundModel> list;

    public CardsArrayAdapter(Context c, ArrayList<LinesFoundModel> list) {
        this.c = c;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null)
        {
            view = LayoutInflater.from(c).inflate(R.layout.util_list_cards,viewGroup,false);
        }

        final LinesFoundModel s = (LinesFoundModel) this.getItem(i);

        TextView nameTxt = (TextView) view.findViewById(R.id.line_name);
        TextView positionTxt = view.findViewById(R.id.line_position);
        TextView distBackTxt = view.findViewById(R.id.line_distance_back);
        TextView distFrontTxt = view.findViewById(R.id.line_distance_front);

        nameTxt.setText(s.getLineName());
//        if(s.getLineDistanceFront() != 0.00 && s.getLineDistanceBack() != 0.00) {
            positionTxt.setText(positionFromDistances(s.getLineDistanceBack(), s.getLineDistanceFront()));
//        }
        distBackTxt.setText(c.getResources().getString(R.string.bus_distance_back, String.format("%.2f", s.getLineDistanceBack()).toString()));
        distFrontTxt.setText(c.getResources().getString(R.string.bus_distance_front, String.format("%.2f", s.getLineDistanceFront()).toString()));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(c, s.getLineName(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private String positionFromDistances(Double distanceBack, Double distanceFront) {
        String position = "";
        int distance = Math.round(distanceFront.intValue() - distanceBack.intValue());
        if (distance > 5) {
            position = c.getResources().getString(R.string.bus_position_left);
        }
        if (distance < 5 && distance > -5) {
            position = c.getResources().getString(R.string.bus_position_front);
        }
        if (distance < -5) {
            position = c.getResources().getString(R.string.bus_position_right);
        }
        return position;
    }
}
