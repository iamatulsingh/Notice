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

public class Admin extends AppCompatActivity {

    List<Map<String, String>> dataList;
    SimpleAdapter simpleAdapter;
    ProgressDialog progressDialog = null;
    private static final String ACCOUNT_DETAILS_URL = Connection.URL+"details.php";
    ListView admin_listView;
    ImageView iv;
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_out_left);
        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        admin_listView = (ListView)findViewById(R.id.listView_admin);
        iv = (ImageView)findViewById(R.id.logout_imageView);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
                finish();
            }
        });
        admin_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String user = String.valueOf(parent.getItemAtPosition(position));
                String username = user.substring(user.lastIndexOf("=") + 1);
                username = username.replaceAll("[\\p{Ps}\\p{Pe}]", "");
                Log.i("us",username);
                Intent intent = new Intent(Admin.this,Show.class);
                intent.putExtra("username",username);
                //based on item add info to intent
                startActivity(intent);
                finish();
            }
        });
        progressDialog = new ProgressDialog(Admin.this, R.style.AppTheme_Dark_Dialog);
        getData();
    }



// --------------------------------- Read data from mysql ----------------------------------------------------

    private void getData() {
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(ACCOUNT_DETAILS_URL, new Response.Listener<String>() {
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
                        Toast.makeText(Admin.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response) {
        dataList = new ArrayList<>();
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("details");
            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String username = (jsonChildNode.getString("username"));
             /*   String password = (jsonChildNode.getString("password"));
                String name = (jsonChildNode.getString("name"));
                String email = (jsonChildNode.getString("email"));
                String role = (jsonChildNode.getString("role"));
                String all = "\n\nName: " + name + "\n" + "Role: " + role + "\n" + "Email: " + email;*/
                dataList.add(createList("detail", username));
            }
            Collections.reverse(dataList);


        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error: Server not found",
                    Toast.LENGTH_SHORT).show();
        }


        simpleAdapter = new SimpleAdapter(this, dataList,
                android.R.layout.simple_list_item_1,
                new String[]{"detail"}, new int[]{android.R.id.text1});

        admin_listView.setAdapter(simpleAdapter);

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
