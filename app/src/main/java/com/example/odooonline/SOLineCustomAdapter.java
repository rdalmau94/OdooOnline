package com.example.odooonline;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SOLineCustomAdapter extends BaseAdapter {
    private static ArrayList<SOLine> searchArrayList;
    private LayoutInflater mInflater;
    public SOLineCustomAdapter(Context context, ArrayList<SOLine> results)
    {
        searchArrayList = results;
        mInflater = LayoutInflater.from(context);
    }
    public int getCount() {
        return searchArrayList.size();
    }
    public Object getItem(int position) {
        return searchArrayList.get(position);
    }
    public long getItemId(int position) {
        return position;
    }
    @SuppressLint("SetTextI18n")
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.custom_row_soline_view, null);
            holder = new ViewHolder();
            holder.txtName = (TextView)
                    convertView.findViewById(R.id.txtName);
            holder.txtProduct = (TextView)
                    convertView.findViewById(R.id.txtProduct);
            holder.txtQuantity= (TextView)
                    convertView.findViewById(R.id.txtQuantity);
            holder.txtPriceUnit = (TextView)
                    convertView.findViewById(R.id.txtPriceUnit);
            holder.txtPriceSubtotal = (TextView)
                    convertView.findViewById(R.id.txtPriceSubtotal);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtName.setText(searchArrayList.get(position)
                .getName());
        holder.txtProduct.setText(searchArrayList.get(position)
                .getProduct());
        holder.txtQuantity.setText(searchArrayList.get(position)
                .getProductUomQty().toString());
        holder.txtPriceUnit.setText(searchArrayList.get(position)
                .getPriceUnit().toString());
        holder.txtPriceSubtotal.setText(searchArrayList.get(position)
                .getPriceSubtotal().toString());
        return convertView;
    }
    static class ViewHolder {
        TextView txtName;
        TextView txtProduct;
        TextView txtQuantity;
        TextView txtPriceUnit;
        TextView txtPriceSubtotal;
    }
}