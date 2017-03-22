package com.atul.notice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
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

public class NoticePermission extends AppCompatActivity {

    String parsed_id = "";
    TextView show_details;
    Button allow,reject;
    ProgressDialog progressDialog = null;
    private static final String SHOW_NOTICE = Connection.URL + "director_notice_show.php";
    private static final String ALLOW_NOTICE = Connection.URL + "allow_notice.php";
    private static final String REJECT_NOTICE = Connection.URL + "reject_notice.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_permission);
        allow = (Button)findViewById(R.id.allow_notice);
        reject = (Button)findViewById(R.id.reject_notice);
        show_details = (TextView)findViewById(R.id.show_notice);
        show_details.setMovementMethod(new ScrollingMovementMethod());
        progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);

        Intent intent = getIntent();
        parsed_id = intent.getStringExtra("id");
        Log.i("parse_id",parsed_id);
        show(parsed_id);

        allow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_delete(parsed_id,ALLOW_NOTICE);
                startActivity(new Intent(NoticePermission.this,Director.class));
                finish();
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_delete(parsed_id,REJECT_NOTICE);
                startActivity(new Intent(NoticePermission.this,Director.class));
                finish();
            }
        });
    }

// -----------------------  Show Method -----------------------------------------------------------

    private void show(String id) {
        final String _id = id;
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SHOW_NOTICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray jsonMainNode = jsonResponse.optJSONArray("notice");
                            for (int i = 0; i < jsonMainNode.length(); i++) {
                                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                                // String id = (jsonChildNode.getString("id"));
                                String notice = (jsonChildNode.getString("notice"));
                                show_details.setText(notice);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(NoticePermission.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        //Toast.makeText(Login.this,error.toString(),Toast.LENGTH_LONG ).show();
                        Toast.makeText(NoticePermission.this, "No internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("id",_id);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

// -----------------------  Add and Delete Class -----------------------------------------------------------

    private void add_delete(String id,String URL) {
        final String _id = id;
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Toast.makeText(NoticePermission.this, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        //Toast.makeText(Login.this,error.toString(),Toast.LENGTH_LONG ).show();
                        Toast.makeText(NoticePermission.this, "No internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", _id);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(NoticePermission.this,Director.class));
        finish();
    }
}
