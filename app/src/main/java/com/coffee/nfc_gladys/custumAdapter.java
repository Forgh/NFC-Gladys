package com.coffee.nfc_gladys;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by s-setsuna-f on 26/04/16.
 */
public class custumAdapter extends BaseAdapter {
    private Context context;



    private String[] titles = {"Ligne 1",
                "Ligne 2", "Ligne 3", "Ligne 4", "Ligne 5"};


    private String[] details = { "Description de la première ligne",
            "Description de la 2nde ligne",
            "Description de la 3ème ligne",
            "Description de la 4ème ligne",
            "Description de la 5ème ligne"};

    private boolean[] expanded = { false,
        false,
        false,
        false,
        false};


    public custumAdapter(Context context){ this.context = context; }
    @Override
    public int getCount() { return titles.length; }

    @Override
    public Object getItem(int position) { return position; }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        convertView = inflater.inflate(R.layout.collapse_layout, parent, false);

        final LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.collapse_root);
        final TextView title = (TextView) convertView.findViewById(R.id.collapse_title);
        //final TextView detail = (TextView) convertView.findViewById(R.id.collapse_detail);

        title.setText(titles[position]);
        //detail.setText(details[position]);

        //layout.setVisibility(expanded[position]?VISIBLE:GONE);
        layout.setVisibility(expanded[position]?View.VISIBLE:View.GONE);

        return convertView;
    }

    public void toggle(int position) {
        expanded[position] = !expanded[position];
        notifyDataSetChanged();
    }
}
