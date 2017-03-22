package com.atul.notice;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

import static com.atul.notice.SessionManager.PREF_NAME;

public class Login extends AppCompatActivity {

    EditText _nameText;
    EditText _passwordText;
    Button _loginButton;
    TextView _signupLink;
    TextView _forgotLink;
    NotificationManager notificationManager;
    String username,password,role,role_local = "";
    ProgressDialog progressDialog = null;
    Spinner login_spinner;
    SessionManager session;
    SharedPreferences pref;
    SharedPreferences.Editor edit;
    private static final String LOGIN_URL = Connection.URL+"login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.from_middle, R.anim.to_middle);
        setContentView(R.layout.activity_login);
        _nameText = (EditText)findViewById(R.id.user_name);
        _passwordText = (EditText)findViewById(R.id.input_password);
        _loginButton = (Button)findViewById(R.id.btn_login);
        _signupLink = (TextView)findViewById(R.id.link_signup);
        _forgotLink = (TextView)findViewById(R.id.link_forgot);
        login_spinner = (Spinner)findViewById(R.id.spinner_login);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
        progressDialog = new ProgressDialog(Login.this, R.style.AppTheme_Dark_Dialog);
        session = new SessionManager(getApplicationContext());
        pref = getApplicationContext().getSharedPreferences(PREF_NAME, 0);
        edit = pref.edit();

        _forgotLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ForgotActivity.class));
                finish();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginCheck();
            }
        });
    }

//  --------------------------  Login check  -------------------------------------------------------

    public void loginCheck() {
          if (!validate()) {
            onLoginFailed();
            return;
        }
        else
            onLoginSuccess();
    }

    // ---------------------  Onsuccess login  ---------------------------------------------------------
    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        _nameText.setText("");
        _passwordText.setText("");
        //sending role for session management
        session.createLoginSession(role);
        userLogin();
    }
//  --------------------  OnFailed login  ----------------------------------------------------------


    public void onLoginFailed() {
        Toast toast = Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_SHORT);
        //  toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
        _nameText.setText("");
        _passwordText.setText("");
        _loginButton.setEnabled(true);
    }

//  --------------------------  Login validation ---------------------------------------------------

    public boolean validate() {
        boolean valid = true;

        username = _nameText.getText().toString();
        password = _passwordText.getText().toString();
        role = login_spinner.getSelectedItem().toString();

        if (username.isEmpty() || username.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("password length must be in between 4 and 10");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

//  --------------------------------  Login Method -------------------------------------------------

    private void userLogin() {
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if(response.trim().equals("success")){
                            if (role_local.equals("admin") || role_local.equals("Admin")) {
                                startActivity(new Intent(Login.this, Admin.class));
                                finish();
                            } else if (role_local.equals("teacher") || role_local.equals("Teacher")) {
                                startActivity(new Intent(Login.this, Teacher.class));
                                finish();
                            } else if (role_local.equals("director") || role_local.equals("Director")) {
                                startActivity(new Intent(Login.this, Director.class));
                                finish();
                            }
                            Toast.makeText(Login.this, response, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Login.this,response,Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        //Toast.makeText(Login.this,error.toString(),Toast.LENGTH_LONG ).show();
                        Toast.makeText(Login.this, "No internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("username",username);
                map.put("password",password);
                map.put("ap",role);
                role_local = role;
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
        showSnack(isNetworkAvailable(Login.this));
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
                    .make(findViewById(R.id.btn_login), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Login.this,Notice.class));
        finish();
    }
}
