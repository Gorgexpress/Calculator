package com.example.michael.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.renderscript.RSInvalidStateException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class History extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = this.getIntent();
        ArrayList<String> history = intent.getStringArrayListExtra("history");
        if(history.size() != GUI.HISTORY_SIZE)
            throw new IllegalStateException("History size not equal to GUI_HISTORY_SIZE constant");

        //When a deque is converted to an arraylist, the first element in FIFO order is in
        // the last index.
        ((TextView)findViewById(R.id.textView0)).setText(history.get(9));
        ((TextView)findViewById(R.id.textView1)).setText(history.get(8));
        ((TextView)findViewById(R.id.textView2)).setText(history.get(7));
        ((TextView)findViewById(R.id.textView3)).setText(history.get(6));
        ((TextView)findViewById(R.id.textView4)).setText(history.get(5));
        ((TextView)findViewById(R.id.textView5)).setText(history.get(4));
        ((TextView)findViewById(R.id.textView6)).setText(history.get(3));
        ((TextView)findViewById(R.id.textView7)).setText(history.get(2));
        ((TextView)findViewById(R.id.textView8)).setText(history.get(1));
        ((TextView)findViewById(R.id.textView9)).setText(history.get(0));
    }

    public void onHistory(View view){
        if(((TextView)view).getText().length() > 0) {
            Intent intent = new Intent(this, GUI.class);
            String expression = ((TextView)view).getText().toString();
            expression = expression.substring(expression.indexOf('=') + 1, expression.length());
            intent.putExtra("expression",expression);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
        }
    }

}
