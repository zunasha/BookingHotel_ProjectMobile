package com.example.app2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class HistoryAdapter extends BaseAdapter {
    Activity activity;
    List<DataHistory> items;
    private LayoutInflater inflater;

    public HistoryAdapter(Activity activity, List<DataHistory> items) {
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

        if(convertView == null) convertView = inflater.inflate(R.layout.activity_history_list, null);

        TextView id = (TextView) convertView.findViewById(R.id.id_order_id);
        TextView nama = (TextView) convertView.findViewById(R.id.id_kamar);
        TextView tanggal = (TextView) convertView.findViewById(R.id.id_tanggal_order);
        TextView jam = (TextView) convertView.findViewById(R.id.id_jam_order);
        TextView bayar = (TextView) convertView.findViewById(R.id.id_total_pembayaran);
        TextView pemesan = (TextView) convertView.findViewById(R.id.id_pemesan);

        DataHistory data = items.get(position);

        id.setText(data.getId());
        nama.setText(data.getNama()+" ("+data.getKategori()+")");
        tanggal.setText(data.getTanggalBooking());
        jam.setText(data.getJamBooking());
        bayar.setText(data.getBiaya());
        pemesan.setText(data.getPemesan());

        return convertView;
    }
}
