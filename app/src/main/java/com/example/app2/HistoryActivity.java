package com.example.app2;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    //Tampil History
    public static final String URL_SELECT_HISTORY = "http://192.168.112.3/CRUDVolley/projectmobile/selectHistory.php";
    ListView list;
    SwipeRefreshLayout swipe;
    List<DataHistory> itemList = new ArrayList<DataHistory>();
    HistoryAdapter adapter;
    //End Tampil History

    private ImageView ivArrowBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().hide();

        ivArrowBack = findViewById(R.id.id_arrowBack);
        ivArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent move = new Intent(HistoryActivity.this, MainActivity.class);
                startActivity(move);
            }
        });

        //Tampil History
        swipe = (SwipeRefreshLayout) findViewById(R.id.id_swipe_history);
        list = (ListView) findViewById(R.id.id_listView_history);

        adapter = new HistoryAdapter(HistoryActivity.this, itemList);
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
        //End Tampil History
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
        JsonArrayRequest jArr = new JsonArrayRequest(URL_SELECT_HISTORY,new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response){
                //parsing json
                for (int i = 0; i < response.length(); i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        DataHistory item = new DataHistory();

                        item.setId(obj.getString("id"));
                        item.setNama(obj.getString("nama"));
                        item.setKategori(obj.getString("kategori"));
                        item.setBiaya("Rp. "+obj.getString("biaya"));
                        item.setLamaPesan(obj.getString("lamaPesan"));
                        item.setTanggalBooking(obj.getString("tanggalBooking"));
                        item.setJamBooking(obj.getString("jamBooking"));
                        item.setPemesan(obj.getString("pemesan"));

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
}