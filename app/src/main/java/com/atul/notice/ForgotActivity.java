package com.atul.notice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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

public class ForgotActivity extends AppCompatActivity {

    EditText _nameText;
    EditText _emailText;
    Button _forgotButton;
    ProgressDialog progressDialog = null;
    String username,email,role = "";
    Spinner spinner_forgot;
    private static final String FORGOT_URL = Connection.URL+"forgot.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        overridePendingTransition(R.anim.from_middle, R.anim.to_middle);
        spinner_forgot = (Spinner)findViewById(R.id.spinner_forgot);
        _nameText = (EditText)findViewById(R.id.user_name);
        _emailText = (EditText)findViewById(R.id.input_email);
        _forgotButton = (Button)findViewById(R.id.btn_forgot);
        progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);

        _forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotCheck();
            }
        });
    }

// ------------------------- Forgot Check ----------------------------------------------------------
    public void forgotCheck(){
         if (!validate()) {
            onForgotFailed();
            return;
        }
        else
            onForgotSuccess();
    }

// -------------- Onforgot  Success ----------------------------------------------------------------

    public void onForgotSuccess() {
        _forgotButton.setEnabled(true);
        _nameText.setText("");
        _emailText.setText("");
        forgot();
        //Notify("Mail Sent","Your password has been sent to your registered email. Login with your password here.");
    }

// -------------- Onforgot  Failed -----------------------------------------------------------------

    public void onForgotFailed() {
        Toast toast = Toast.makeText(getBaseContext(), "Failed", Toast.LENGTH_SHORT);
        toast.show();
        _nameText.setText("");
        _emailText.setText("");
        _forgotButton.setEnabled(true);
    }

// -------------- Validate -------------------------------------------------------------------------

    public boolean validate() {
        boolean valid = true;

        username = _nameText.getText().toString();
        email = _emailText.getText().toString();
        role = spinner_forgot.getSelectedItem().toString();

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
        return valid;
    }

// -------------------------  Notification ---------------------------------------------------------

    private void Notify(String notificationTitle, String notificationMessage){
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
        NotificationManager notificationManager = getNotificationManager();
        PendingIntent pi = getPendingIntent();
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(notificationTitle)
                .setContentText(notificationMessage)
                .setSmallIcon(R.drawable.notice_logo)
                .setAutoCancel(true)
                //HEADSUP Notification  --- priority and setvibrate
        .setPriority(Notification.PRIORITY_HIGH);
        if (Build.VERSION.SDK_INT >= 21) builder.setVibrate(new long[0])
                .addAction(R.drawable.ic_account_circle_black_24dp, "Login", pi);
        Notification notification = new Notification.BigTextStyle(builder)
                .bigText(notificationMessage).build();

        builder.build().flags |= Notification.FLAG_AUTO_CANCEL;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notification);

    }

// ---------------------  pending intent for notification click ------------------------------------

    public PendingIntent getPendingIntent() {
        return PendingIntent.getActivity(this, (int)System.currentTimeMillis(), new Intent(getApplicationContext(),
                Login.class), 0);
    }

// -----------------------  Start notification -----------------------------------------------------

    public NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

// -----------------------  Forgot Class -----------------------------------------------------------

    private void forgot() {
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FORGOT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if(response.trim().equals("success")){
                            Notify("Mail Sent", "Your password has been sent to your registered email. Login with your password here.");
                            Toast.makeText(ForgotActivity.this, response, Toast.LENGTH_SHORT).show();
                        }else if(response.trim().equals("failed")){
                            Notify("Mail Sent", "Your password has been sent to your registered email. Login with your password here.");
                            Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        }
                        else
                            Notify("Internet","Wrong Email or Username");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(ForgotActivity.this,"No internet Connection",Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("username",username);
                map.put("email",email);
                map.put("role",role);
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
        showSnack(isNetworkAvailable(ForgotActivity.this));
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
                    .make(findViewById(R.id.btn_forgot), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
}
