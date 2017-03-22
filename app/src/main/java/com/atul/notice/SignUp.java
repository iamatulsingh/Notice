package com.atul.notice;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

/*
 * Created by ATUL on 11/19/2016.
 */

public class SignUp extends AppCompatActivity {

    EditText _nameText;
    EditText _emailText;
    EditText _passwordText;
    Button _signupButton;
    EditText _signupName;
    Spinner spinner;
    ProgressDialog progressDialog = null;
    String username,email,password,role,name="";

    private static final String REGISTER_URL = Connection.URL+"register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        overridePendingTransition(R.anim.from_middle, R.anim.to_middle);
        _nameText = (EditText)findViewById(R.id.user_name);
        _emailText = (EditText)findViewById(R.id.input_email);
        _passwordText = (EditText)findViewById(R.id.input_password);
        _signupName = (EditText)findViewById(R.id.name_signup);
        _signupButton = (Button)findViewById(R.id.btn_signup);
        spinner = (Spinner) findViewById(R.id.spinner1);
        progressDialog = new ProgressDialog(SignUp.this, R.style.AppTheme_Dark_Dialog);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

// -----------------------------  SignUp  ----------------------------------------------------------

    public void signup() {
        if (!validate()) {
            onSignupFailed();
            return;
        }
        else{
            onSignupSuccess();
        }
    }

// -----------------------------  onSuccess situation  ---------------------------------------------

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        _nameText.setText("");
        _signupName.setText("");
        _emailText.setText("");
        _passwordText.setText("");
        registerUser();
    }

// -----------------------------  onFailed situation  ----------------------------------------------

    public void onSignupFailed() {
        Toast toast = Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_SHORT);
        // toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
        _nameText.setText("");
        _signupName.setText("");
        _emailText.setText("");
        _passwordText.setText("");
        // progressDialog.dismiss();
        _signupButton.setEnabled(true);
    }

// -----------------------------  Validation  ------------------------------------------------------

    public boolean validate() {
        boolean valid = true;

        name = _signupName.getText().toString();
        username = _nameText.getText().toString();
        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();
        role = spinner.getSelectedItem().toString();

        if (name.isEmpty() || name.length() < 3) {
            _signupName.setError("at least 3 characters");
            valid = false;
        } else {
            _signupName.setError(null);
        }

        if (username.isEmpty() || username.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }


//  ----------------------------   Validation and Connection class ---------------------------------

    private void registerUser(){
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Toast.makeText(SignUp.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(SignUp.this,"No internet Connection",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("name",name);
                params.put("username",username);
                params.put("password",password);
                params.put("email", email);
                params.put("role",role);
                return params;
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
        showSnack(isNetworkAvailable(SignUp.this));
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
                    .make(findViewById(R.id.btn_signup), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SignUp.this,Login.class));
        finish();
    }
}
