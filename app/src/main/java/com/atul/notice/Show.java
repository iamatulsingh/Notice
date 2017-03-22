package com.atul.notice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Show extends AppCompatActivity {

    TextView show_details;
    Button allow,reject;
    ProgressDialog progressDialog = null;
    private static final String ALLOW_URL = Connection.URL+"add.php";
    private static final String REJECT_URL = Connection.URL+"delete.php";
    private static final String SHOW_URL = Connection.URL+"show.php";
    String username = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        show_details = (TextView) findViewById(R.id.show_notice);
        show_details.setMovementMethod(new ScrollingMovementMethod());
        allow = (Button)findViewById(R.id.allow);
        reject = (Button)findViewById(R.id.reject);
        progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        show(username);

        allow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_delete(username,ALLOW_URL);
                startActivity(new Intent(Show.this,Admin.class));
                finish();
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_delete(username,REJECT_URL);
                startActivity(new Intent(Show.this,Admin.class));
                finish();
            }
        });
    }

// -----------------------  Show Class -----------------------------------------------------------

    private void show(String username) {
        final String _username = username;
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SHOW_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray jsonMainNode = jsonResponse.optJSONArray("details");
                            for (int i = 0; i < jsonMainNode.length(); i++) {
                                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                                String name = (jsonChildNode.getString("name"));
                                String email = (jsonChildNode.getString("email"));
                                String role = (jsonChildNode.getString("role"));
                                String all = "Name: " + name + "\n" + "Email: " + email + "\n" + "Role: " + role + "\n\n";
                                show_details.setText(all);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(Show.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(Show.this, "No internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("username",_username);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

// -----------------------  Add and Delete Class -----------------------------------------------------------

    private void add_delete(String username,String URL) {
        final String _username = username;
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Toast.makeText(Show.this, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        //Toast.makeText(Login.this,error.toString(),Toast.LENGTH_LONG ).show();
                        Toast.makeText(Show.this, "No internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("username", _username);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Show.this,Admin.class));
        finish();
    }
}
