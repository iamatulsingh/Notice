package com.atul.notice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class WriteNotice extends AppCompatActivity {

    EditText notice_head,notice_msg;
    CheckBox teacher,student;
    Button post;
    String notice,notice_teacher,notice_student = "";
    ProgressDialog progressDialog = null;
    private static final String WRITE_NOTICE = Connection.URL + "write_notice.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_notice);

        notice_head = (EditText)findViewById(R.id.notice_head);
        notice_msg = (EditText)findViewById(R.id.notice_text);
        teacher = (CheckBox)findViewById(R.id.teacher_checkBox);
        student = (CheckBox)findViewById(R.id.student_checkBox);
        post = (Button)findViewById(R.id.post_button);
        progressDialog = new ProgressDialog(WriteNotice.this, R.style.AppTheme_Dark_Dialog);

        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(teacher.isChecked())
                    notice_teacher = "teacher";
                else
                    notice_teacher = "";
            }
        });

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(student.isChecked())
                    notice_student = "student";
                else
                    notice_student = "";
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post();
            }
        });
    }

// ---------------------------------- Check method -------------------------------------------------

    public boolean check(){
        String noticehead = notice_head.getText().toString();
        String noticemsg = notice_msg.getText().toString();
        notice = "Topic: " + noticehead + "\n\n" + noticemsg + "\n";

        if(noticehead.equals("")&&noticemsg.equals("")){
            notice_head.setError("Enter Notice Head");
            notice_msg.setError("Enter Notice body");
            return false;
        }
        else{
            notice_head.setError(null);
            notice_msg.setError(null);
        }
        if(noticehead.equals("")) {
            notice_head.setError("Enter Notice Head");
            return false;
        }
        else
            notice_head.setError(null);
        if(noticemsg.equals("")) {
            notice_msg.setError("Enter Notice body");
            return false;
        }
        else
            notice_msg.setError(null);
        if(!teacher.isChecked()&&!student.isChecked()){
            Toast.makeText(WriteNotice.this, "Choose at least one category from student and teacher", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

// ----------------------------------- Post notice method ------------------------------------------

    public void post(){
        boolean check = check();
        if(check){
            notice_head.setText("");
            notice_msg.setText("");

            writeNotice(notice,notice_teacher,notice_student);
        }
        else
            return;
    }

// -----------------------------  Write Notice class  ----------------------------------------------

    private void writeNotice(String notice, String teacher, String student) {
        final String _notice = notice;
        final String _teacher = teacher;
        final String _student = student;
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WRITE_NOTICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                            Toast.makeText(WriteNotice.this, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        //Toast.makeText(Login.this,error.toString(),Toast.LENGTH_LONG ).show();
                        Toast.makeText(WriteNotice.this, "No internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("notice",_notice);
                map.put("teacher",_teacher);
                map.put("student",_student);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(WriteNotice.this,Teacher.class));
        finish();
    }
}
