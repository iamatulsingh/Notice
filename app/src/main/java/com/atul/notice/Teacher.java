package com.atul.notice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class Teacher extends AppCompatActivity {

    ListView listView_teacher;
    private static final String TEACHER_URL = Connection.URL + "teacher_notice.php";
    ProgressDialog progressDialog = null;
    List<Map<String, String>> dataList;
    SimpleAdapter simpleAdapter;
    ImageView write_notice,logout;
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        listView_teacher = (ListView)findViewById(R.id.teacher_listView);
        write_notice = (ImageView)findViewById(R.id.write_imageview);
        logout = (ImageView)findViewById(R.id.logout_imageView);
        progressDialog = new ProgressDialog(Teacher.this, R.style.AppTheme_Dark_Dialog);
        session = new SessionManager(getApplicationContext());
        session.checkLogin();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
                finish();
            }
        });

        write_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Teacher.this,WriteNotice.class));
                finish();
            }
        });
        getData();
    }

// --------------------------------- Read data from mysql ----------------------------------------------------

    private void getData() {
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(TEACHER_URL, new Response.Listener<String>() {
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
                        Toast.makeText(Teacher.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response) {
        dataList = new ArrayList<>();
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("teacher_notice");
            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String notice = (jsonChildNode.getString("notice"));
                dataList.add(createList("details", notice));
            }
            Collections.reverse(dataList);


        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error: Sever not found -> " + e.toString(),
                    Toast.LENGTH_SHORT).show();
        }


        simpleAdapter = new SimpleAdapter(this, dataList,
                android.R.layout.simple_list_item_1,
                new String[]{"details"}, new int[]{android.R.id.text1});

        listView_teacher.setAdapter(simpleAdapter);

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
