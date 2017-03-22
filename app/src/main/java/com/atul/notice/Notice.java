package com.atul.notice;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
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

import static com.atul.notice.R.id.listView;

public class Notice extends AppCompatActivity {

    ImageView iv;
    ListView lv;
    EditText et;
    List<Map<String, String>> dataList;
    SimpleAdapter simpleAdapter;
    ProgressDialog progressDialog = null;
    private static final String NOTICE_URL = Connection.URL+"notice.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        iv = (ImageView)findViewById(R.id.imageView);
        lv = (ListView)findViewById(listView);
        et = (EditText)findViewById(R.id.inputSearch);
        progressDialog = new ProgressDialog(Notice.this, R.style.AppTheme_Dark_Dialog);

        et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                Notice.this.simpleAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        iv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
                return false;
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
        StringRequest stringRequest = new StringRequest(NOTICE_URL, new Response.Listener<String>() {
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
                        Toast.makeText(Notice.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response){
        String notice;
        dataList = new ArrayList<>();

        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("notice");
            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                notice = (jsonChildNode.getString("notice"));
                dataList.add(createList("details", notice));
            }
            Collections.reverse(dataList);


        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error: Data can't be fetched  ",
                    Toast.LENGTH_SHORT).show();
        }


        simpleAdapter = new SimpleAdapter(this, dataList,
                android.R.layout.simple_list_item_1,
                new String[]{"details"}, new int[]{android.R.id.text1});

        lv.setAdapter(simpleAdapter);

    }

    private HashMap<String, String> createList(String key, String value) {
        HashMap<String, String> data = new HashMap<>();
        data.put(key, value);
        return data;
    }
// --------------------------- Check Internet Connection  ------------------------------------------

    public static boolean isNetworkAvailable(Context context) {
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE,
                ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null &&
                        activeNetworkInfo.getType() == networkType)
                    return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        showSnack(isNetworkAvailable(Notice.this));
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.imageView), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }
}
