package com.example.app2;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TransactionActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ImageView ivArrowBack;

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
    EditText etId, etNama, etHarga, etKategori, etLamaBooking, etBiayaBooking, etTanggalBooking, etJamBooking, etNamaPemesan;
    String s_id, s_nama, s_harga, s_kategori, s_lamaBooking, s_biayaBooking, s_tanggalBooking, s_jamBooking, s_namaPemesan;
    //End Pesan Kamar

    //Date Picker
    final Calendar myCalendar= Calendar.getInstance();
    //End Date Picker

    //Time Picker

    //End Time Picker

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        getSupportActionBar().hide();

        ivArrowBack = findViewById(R.id.id_arrowBack);
        ivArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent move = new Intent(TransactionActivity.this, MainActivity.class);
                startActivity(move);
            }
        });

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
//        etBiayaBooking = (EditText) viewDialog.findViewById(R.id.id_biaya_booking_kamar);
        etTanggalBooking = (EditText) viewDialog.findViewById(R.id.id_tanggal_booking_kamar);
        etJamBooking = (EditText) viewDialog.findViewById(R.id.id_jam_booking_kamar);
        etNamaPemesan = (EditText) viewDialog.findViewById(R.id.id_pemesan_booking_kamar);

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

        //Date Picker
        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);

                String myFormat="dd/MM/yy";
                SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
                etTanggalBooking.setText(dateFormat.format(myCalendar.getTime()));
            }
        };
        etTanggalBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(TransactionActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        //End Date Picker

        //Time Picker
        etJamBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = myCalendar.get(Calendar.MINUTE);
                // time picker dialog
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(TransactionActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        etJamBooking.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true); //Yes 24-hour time mode
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        //End Time Picker

        dialogForm.setPositiveButton(button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                s_id = etId.getText().toString();
                s_nama = etNama.getText().toString();
                s_harga = etHarga.getText().toString();
                s_kategori = etKategori.getText().toString();
                s_lamaBooking = etLamaBooking.getText().toString();
                s_tanggalBooking = etTanggalBooking.getText().toString();
                s_jamBooking = etJamBooking.getText().toString();
                s_namaPemesan = etNamaPemesan.getText().toString();

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
                        Intent move = new Intent(TransactionActivity.this, HistoryActivity.class);
                        startActivity(move);
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
                params.put("tanggalBooking", s_tanggalBooking);
                params.put("jamBooking", s_jamBooking);
                params.put("pemesan", s_namaPemesan);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    //End Pesan Kamar
}