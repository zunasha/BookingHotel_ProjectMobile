package com.example.app2;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().hide();

        tvTambahMenu = findViewById(R.id.buttonTambahMenu);

        tvTambahMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent move = new Intent(MenuActivity.this, AddMenuActivity.class);
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
}