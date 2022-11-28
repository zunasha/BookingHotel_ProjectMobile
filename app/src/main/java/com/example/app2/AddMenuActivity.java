package com.example.app2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AddMenuActivity extends AppCompatActivity {

    TextView tvTambah, tvStatus;
    EditText etAddNama, etAddHarga, etAddKategori;

    public static final String url = "http://192.168.112.3/CRUDVolley/projectmobile/insert.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);
        getSupportActionBar().hide();

        tvTambah = (TextView) findViewById(R.id.id_buttonTambah);
        tvStatus = (TextView) findViewById(R.id.id_status);
        etAddNama = (EditText) findViewById(R.id.id_addNama);
        etAddHarga = (EditText) findViewById(R.id.id_addHarga);
        etAddKategori = (EditText) findViewById(R.id.id_addKategori);

        tvTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputData();
            }
        });
    }

    void inputData(){
        String nama = etAddNama.getText().toString();
        String harga = etAddHarga.getText().toString();
        String kategori = etAddKategori.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        tvStatus.setText(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvStatus.setText("error tidak dapat di proses");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();

                params.put("nama", nama);
                params.put("harga", harga);
                params.put("kategori", kategori);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }
}