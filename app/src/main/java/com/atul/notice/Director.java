package com.atul.notice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Director extends AppCompatActivity {

    private static final String DIRECTOR_URL = Connection.URL+"director_notice.php";
    ListView directorListView;
    ProgressDialog progressDialog = null;
    SimpleAdapter simpleAdapter;
    List<Map<String, String>> dataList;
    ImageView iv;
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_director);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        directorListView = (ListView)findViewById(R.id.director_ListView);
        progressDialog = new ProgressDialog(Director.this, R.style.AppTheme_Dark_Dialog);
        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        iv = (ImageView)findViewById(R.id.logout_imageView);
        getData();
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
                finish();
            }
        });
        directorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String _id = String.valueOf(parent.getItemAtPosition(position));
                String parsed_id = _id.substring(_id.lastIndexOf("=") + 1);
                parsed_id = parsed_id.replaceAll("[\\p{Ps}\\p{Pe}]", "");
                Log.i("us",parsed_id);
                Intent intent = new Intent(Director.this,NoticePermission.class);
                intent.putExtra("id",parsed_id);
                startActivity(intent);
                finish();
            }
        });

    }

// --------------------------------- Read data from mysql ----------------------------------------------------
private void getData() {
    progressDialog.setIndeterminate(true);
    progressDialog.setMessage("Authenticating...");
    progressDialog.setCanceledOnTouchOutside(false);
    progressDialog.show();
    StringRequest stringRequest = new StringRequest(DIRECTOR_URL, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            progressDialog.dismiss();
            showJSON(response);
        }
    },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(Director.this,error.toString(),Toast.LENGTH_LONG).show();
                }
            });

    RequestQueue requestQueue = Volley.newRequestQueue(this);
    requestQueue.add(stringRequest);
}

    private void showJSON(String response) {
        dataList = new ArrayList<>();
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("director_notice");
            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String id = (jsonChildNode.getString("id"));
                //  String notice = (jsonChildNode.getString("notice"));
                dataList.add(createList("detail", id));
            }
            Collections.reverse(dataList);


        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error: Server not found",
                    Toast.LENGTH_SHORT).show();
        }


        simpleAdapter = new SimpleAdapter(this, dataList,
                android.R.layout.simple_list_item_1,
                new String[]{"detail"}, new int[]{android.R.id.text1});

        directorListView.setAdapter(simpleAdapter);
    }

    private HashMap<String, String> createList(String key, String value) {
        HashMap<String, String> data = new HashMap<>();
        data.put(key, value);
        return data;
    }
       @Override
    public void onBackPressed() {
       moveTaskToBack(true);
    }
}
