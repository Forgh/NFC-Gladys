package com.coffee.nfc_gladys;

import com.coffee.nfc_gladys.PartieMetier.ModuleSerializable;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by s-setsuna-f on 19/04/16.
 */

public class ItemCreateAmbianceAdapter extends ArrayAdapter<ModuleSerializable> {
    private List<ModuleSerializable> items;
    private int layoutResourceId;
    private Context context;

    public ItemCreateAmbianceAdapter(Context context, int layoutResourceId, List<ModuleSerializable> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ModuleHolder holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new ModuleHolder();
        holder.atomModulet = items.get(position);
        holder.removeButton = (ImageButton) row.findViewById(R.id.item_remove_img);
        holder.removeButton.setTag(holder.atomModulet);

        holder.name = (TextView) row.findViewById(R.id.item_name_module);
        setNameTextChangeListener(holder);

        row.setTag(holder);

        setupItem(holder);
        notifyDataSetChanged();

        return row;
    }

    private void setupItem(ModuleHolder holder) {
        holder.name.setText(holder.atomModulet.getCode());
    }

    private void setNameTextChangeListener(final ModuleHolder holder) {
        holder.name.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                holder.atomModulet.setCode(s.toString());}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public static class ModuleHolder {
        ModuleSerializable atomModulet;
        TextView name;
        ImageButton removeButton;
    }
}









