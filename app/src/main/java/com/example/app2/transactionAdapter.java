package com.example.app2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class transactionAdapter extends BaseAdapter {

    Activity activity;
    List<Data> items;
    private LayoutInflater inflater;

    public transactionAdapter(Activity activity, List<Data> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null) convertView = inflater.inflate(R.layout.activity_transaction_list, null);

        TextView id = (TextView) convertView.findViewById(R.id.id_transaksi_id);
        TextView nama = (TextView) convertView.findViewById(R.id.id_transaksi_name);
        TextView harga = (TextView) convertView.findViewById(R.id.id_transaksi_price);
        TextView kategori = (TextView) convertView.findViewById(R.id.id_transaksi_category);

        Data data = items.get(position);

        id.setText(data.getId());
        nama.setText(data.getNama());
        harga.setText(data.getHarga());
        kategori.setText(data.getKategori());

        return convertView;
    }
}
