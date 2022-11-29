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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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

public class MenuActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private TextView tvTambahMenu;
    private ImageView ivArrowBack;

    //Tampil Menu
    public static final String URL_SELECT = "http://192.168.112.3/CRUDVolley/projectmobile/select.php";
    ListView list;
    SwipeRefreshLayout swipe;
    List<Data> itemList = new ArrayList<Data>();
    menuAdapter adapter;
    //End Tampil Menu

    //Delete Menu
    public static final String URL_DELETE = "http://192.168.112.3/CRUDVolley/projectmobile/delete.php";
    //End Delete Menu

    //Edit Menu
    public static final String URL_EDIT = "http://192.168.112.3/CRUDVolley/projectmobile/edit.php";
    public static final String URL_INSERT = "http://192.168.112.3/CRUDVolley/projectmobile/insert.php";
    LayoutInflater inflater;
    EditText etId, etNama, etHarga, etKategori;
    String s_id, s_nama, s_harga, s_kategori;
    //End Edit Menu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().hide();

        tvTambahMenu = findViewById(R.id.buttonTambahMenu);
        ivArrowBack = findViewById(R.id.id_arrowBack);

        tvTambahMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent move = new Intent(MenuActivity.this, AddMenuActivity.class);
                startActivity(move);
            }
        });

        ivArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent move = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(move);
            }
        });

        //Tampil Menu
        swipe = (SwipeRefreshLayout) findViewById(R.id.id_swipe);
        list = (ListView) findViewById(R.id.id_listView);

        adapter = new menuAdapter(MenuActivity.this, itemList);
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

        //Delete Menu
       list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                final String idx = itemList.get(position).getId();
                final CharSequence[] pilihanAksi = {"Hapus","Edit"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(MenuActivity.this);
                dialog.setItems(pilihanAksi, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        switch (which) {
                            case 0:
                                //jika dipilih hapus
                                hapusData(idx);
                                callVolley();
                                break;
                            case 1:
                                //jika dipilih edit
                                editData(idx);
                                break;
                        }
                    }
                }).show();
                return false;
            }
        });
        //End Delete Menu
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

    //Delete Menu
    public void hapusData(String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MenuActivity.this, response, Toast.LENGTH_LONG).show();
                        callVolley();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MenuActivity.this, "Gagal koneksi ke server", Toast.LENGTH_LONG).show();
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
    //End Delete Menu

    //Edit Menu
    public void editData(String id){
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

                            dialogForm(idx,namax,hargax,kategorix,"UPDATE");

                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            //JSON error
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MenuActivity.this, "Gagal koneksi ke server", Toast.LENGTH_LONG).show();
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
        AlertDialog.Builder dialogForm = new AlertDialog.Builder(MenuActivity.this);
        inflater = getLayoutInflater();
        View viewDialog = inflater.inflate(R.layout.form_edit_menu, null);
        dialogForm.setView(viewDialog);
        dialogForm.setCancelable(true);
        dialogForm.setTitle("Edit Menu");

        etId = (EditText) viewDialog.findViewById(R.id.id_editId);
        etNama = (EditText) viewDialog.findViewById(R.id.id_editNama);
        etHarga = (EditText) viewDialog.findViewById(R.id.id_editHarga);
        etKategori = (EditText) viewDialog.findViewById(R.id.id_editKategori);

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

                simpan();
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

    public void simpan() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_INSERT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callVolley();
                        Toast.makeText(MenuActivity.this, response, Toast.LENGTH_LONG);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MenuActivity.this, "Gagal koneksi ke server", Toast.LENGTH_LONG);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //posting parameters ke post url
                Map<String, String> params = new HashMap<String,String>();

                if (s_id.isEmpty()){
                    params.put("nama", s_nama);
                    params.put("harga",s_harga);
                    params.put("kategori", s_kategori);
                    return params;
                } else {
                    params.put("id", s_id);
                    params.put("nama", s_nama);
                    params.put("harga",s_harga);
                    params.put("kategori", s_kategori);
                    return params;
                }
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
    //End Edit Menu
}