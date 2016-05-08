package com.example.michael.calculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

public class Email extends AppCompatActivity {
    private final String baseUrl = getString(R.string.mailgun_key);
    private RequestQueue queue;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        Intent intent = this.getIntent();
        ArrayList<String> history = intent.getStringArrayListExtra("history");
        StringBuilder builder = new StringBuilder();
        ListIterator<String> it = history.listIterator(history.size());
        while(it.hasPrevious()){
            builder.append(it.previous());
            builder.append(System.getProperty("line.separator"));
        }
        text = builder.toString();
        queue = Volley.newRequestQueue(this);



    }

    public void onSend(View view){
        StringRequest request = new StringRequest(Request.Method.POST, baseUrl + "/messages",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        ((TextView) findViewById(R.id.response)).setText("Email sent.");
                    }
                }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                ((TextView) findViewById(R.id.response)).setText("Error");
            }
        }) {


            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("from", "Mailgun Sandbox <postmaster@sandboxea4bfc13d2734e9894d4ecd6f0e56bc9.mailgun.org>");
                params.put("to[0]", ((TextView) findViewById(R.id.email)).getText().toString());
                params.put("to[1]", "postmaster@sandboxea4bfc13d2734e9894d4ecd6f0e56bc9.mailgun.org");
                params.put("subject", "Calculator Email Test");
                params.put("text", text);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> params = new HashMap<>();
                String creds = String.format("%s:%s", "api",getString(R.string.mailgun_key) );
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);
                params.put("Authorization", auth);
                return params;

            }
        };
        queue.add(request);
    }
}
