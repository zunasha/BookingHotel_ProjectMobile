package com.example.app2;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    //Tampil Menu
    public static final String URL_SELECT = "http://192.168.112.3/CRUDVolley/projectmobile/select.php";
    ListView list;
    SwipeRefreshLayout swipe;
    List<Data> itemList = new ArrayList<Data>();
    transactionAdapter adapter;
    //End Tampil Menu

    //Pesan Kamar
    public static final String URL_EDIT = "http://192.168.112.3/CRUDVolley/projectmobile/edit.php";
    public static final String URL_PESAN = "http://192.168.112.3/CRUDVolley/projectmobile/pesan.php";
    LayoutInflater inflater;
    EditText etId, etNama, etHarga, etKategori, etLamaBooking, etBiayaBooking;
    String s_id, s_nama, s_harga, s_kategori, s_lamaBooking, s_biayaBooking;
    //End Pesan Kamar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        //Tampil Menu
        swipe = (SwipeRefreshLayout) findViewById(R.id.id_swipe_transaksi);
        list = (ListView) findViewById(R.id.id_listView_transaksi);

        adapter = new transactionAdapter(TransactionActivity.this, itemList);
        list.setAdapter(adapter);

        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(true);
                itemList.clear();
                adapter.notifyDataSetChanged();
                callVolley();
            }
        });
        //End Tampil Menu

        //Pesan Kamar
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                final String idx = itemList.get(position).getId();
                final CharSequence[] pilihanAksi = {"pesan"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(TransactionActivity.this);
                dialog.setItems(pilihanAksi, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        switch (which) {
                            case 0:
                                //jika dipilih pesan
                                pesanKamar(idx);
//                                callVolley();
                                break;
                        }
                    }
                }).show();
                return false;
            }
        });
        //End Pesan Kamar
    }

    //Tampil Menu
    @Override
    public void onRefresh(){
        itemList.clear();
        adapter.notifyDataSetChanged();
        callVolley();
    }
    private void callVolley(){
        itemList.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

        //membuat request JSON
        JsonArrayRequest jArr = new JsonArrayRequest(URL_SELECT,new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response){
                //parsing json
                for (int i = 0; i < response.length(); i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        Data item = new Data();

                        item.setId(obj.getString("id"));
                        item.setNama(obj.getString("nama"));
                        item.setHarga(obj.getString("harga"));
                        item.setKategori(obj.getString("kategori"));

                        //menambah item ke array
                        itemList.add(item);
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }

                //notifikasi adanya perubahan data pada adapter
                adapter.notifyDataSetChanged();

                swipe.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error : " + error.getMessage());
                swipe.setRefreshing(false);
            }
        });

        //menambah request ke request queue
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mRequestQueue.add(jArr);

    }
    //End Tampil Menu

    //Pesan Kamar

    public void pesanKamar(String id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);

                            String idx = jObj.getString("id");
                            String namax = jObj.getString("nama");
                            String hargax = jObj.getString("harga");
                            String kategorix = jObj.getString("kategori");

                            dialogForm(idx,namax,hargax,kategorix,"PESAN");

                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            //JSON error
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TransactionActivity.this, "Gagal koneksi ke server", Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //posting parameters ke post url
                Map<String, String> params = new HashMap<String,String>();

                params.put("id", id);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    public void dialogForm(String id, String nama, String harga, String kategori, String button){
        AlertDialog.Builder dialogForm = new AlertDialog.Builder(TransactionActivity.this);
        inflater = getLayoutInflater();
        View viewDialog = inflater.inflate(R.layout.form_pemesanan, null);
        dialogForm.setView(viewDialog);
        dialogForm.setCancelable(true);
        dialogForm.setTitle("Pesan Kamar");

        etId = (EditText) viewDialog.findViewById(R.id.id_id_kamar);
        etNama = (EditText) viewDialog.findViewById(R.id.id_nama_kamar);
        etHarga = (EditText) viewDialog.findViewById(R.id.id_harga_kamar);
        etKategori = (EditText) viewDialog.findViewById(R.id.id_kategori_kamar);
        etLamaBooking = (EditText) viewDialog.findViewById(R.id.id_lama_booking_kamar);
        etBiayaBooking = (EditText) viewDialog.findViewById(R.id.id_biaya_booking_kamar);

        if (id.isEmpty()) {
            etId.setText(null);
            etNama.setText(null);
            etHarga.setText(null);
            etKategori.setText(null);
        } else {
            etId.setText(id);
            etNama.setText(nama);
            etHarga.setText(harga);
            etKategori.setText(kategori);
        }

        dialogForm.setPositiveButton(button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                s_id = etId.getText().toString();
                s_nama = etNama.getText().toString();
                s_harga = etHarga.getText().toString();
                s_kategori = etKategori.getText().toString();
                s_lamaBooking = etLamaBooking.getText().toString();

                int a = Integer.parseInt(s_harga);
                int b = Integer.parseInt(s_lamaBooking);
                int biaya = a * b;
                s_biayaBooking = String.valueOf(biaya);

                pesan();
                dialog.dismiss();
            }
        });

        dialogForm.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
                etId.setText(null);
                etNama.setText(null);
                etHarga.setText(null);
                etKategori.setText(null);
            }
        });
        dialogForm.show();
    }

    public void pesan(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PESAN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(TransactionActivity.this, response, Toast.LENGTH_LONG);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TransactionActivity.this, "Gagal koneksi ke server", Toast.LENGTH_LONG);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();

                params.put("nama", s_nama);
                params.put("harga", s_harga);
                params.put("kategori", s_kategori);
                params.put("lamaPesan", s_lamaBooking);
                params.put("biaya", s_biayaBooking);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }



    //End Pesan Kamar
}