package com.example.michael.calculator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class GUI extends AppCompatActivity {
    Button b1,b2,b3,b4,b5,b6,b7,b8,b9,b0,badd,bsub,bmul,bdiv,bdec,beq;
    //true if text area contains any message that should be deleted on next input
    boolean viewContainsMessage;
    int digitsUntilComma;
    boolean enteringNumber;
    boolean enteringDecimal;
    int leftParenthesisCount;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        leftParenthesisCount = 0;
        viewContainsMessage = false;
        digitsUntilComma = 3;
        textView = (TextView)findViewById(R.id.textView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gui, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onZero(View view){
        /* We don't want to allow leading zeroes (example: "0025" should be "25", so return when
        3 conditions are filled. These 3 conditions are:
        1 - The text view is not empty
        2 - enteringNumber is false. This variable is only set to true when the program detects
        that the user trying to enter a nonzero number. That is, either there is a digit greater than
        0 to the left and no operators in between, or we are entering a number less than 1 (ex: 0.4)
        3 - The last character entered was a zero.
        */
        int textLength = textView.length() - 1;
        if(textLength > 0 && !enteringNumber && textView.getText().charAt(textLength) == '0')
            return;
        textView.append("0");
        //decrease
        addCommas();
    }

    public void onOne(View view){
        appendNumber('1');
    }

    public void onTwo(View view){
        appendNumber('2');
    }

    public void onThree(View view){
        appendNumber('3');
    }

    public void onFour(View view){
        appendNumber('4');
    }

    public void onFive(View view){
        appendNumber('5');
    }

    public void onSix(View view){
        appendNumber('6');
    }

    public void onSeven(View view){
        appendNumber('7');
    }

    public void onEight(View view){
        appendNumber('8');
    }

    public void onNine(View view){
        appendNumber('9');
    }

    public void onAdd(View view){
        if(textView.getText().length() == 0) return;
        if("+-*/(.".indexOf(textView.getText().charAt(textView.getText().length() - 1)) >= 0)
            textView.setText(textView.getText().subSequence(0, textView.length() - 1));

        textView.append("+");
    }
    //subtraction is the only operator allowed if no numbers have yet to be entered
    public void onSub(View view){
        if(textView.getText().length() > 0 && "-.".indexOf(textView.getText().charAt(textView.getText().length() - 1)) >= 0)
            textView.setText(textView.getText().subSequence(0, textView.length() - 1));
        textView.append("-");
    }

    public void onMul(View view){

        if(textView.getText().length() == 0) return;
        if("+-*/(.".indexOf(textView.getText().charAt(textView.getText().length() - 1)) >= 0)
            textView.setText(textView.getText().subSequence(0, textView.length() - 1));

        textView.append("*");
    }

    public void onDiv(View view){
        if(textView.getText().length() == 0) return;
        if("+-*/(.".indexOf(textView.getText().charAt(textView.getText().length() - 1)) >= 0)
            textView.setText(textView.getText().subSequence(0, textView.length() - 1));

        textView.append("/");
    }

    public void onDec(View view){
        if(textView.getText().length() > 0 && "+-*/(.".indexOf(textView.getText().charAt(textView.getText().length() - 1)) < 0)
            textView.append(".");
    }

    public void onEq(View view){
        String text = textView.getText().toString();
        //if no expression just return
        if (text.length() == 0) return;
        double result = 0.0;
        try{
            result = Calculator.calculate(text);
        }catch(InvalidExpressionException e){
            textView.setText("");
            AlertDialog alertDialog = new AlertDialog.Builder(GUI.this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage(e.getMessage());
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return;
        }
        if(Double.isInfinite(result) || Double.isNaN(result)) {
            textView.setText("");
            AlertDialog alertDialog = new AlertDialog.Builder(GUI.this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Division by zero.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        else {
            //If the result can be represented as an integer, then do so
            //Otherwise, display the double as a string
            if(result == Math.floor(result))
                textView.setText(Integer.toString((int) result));
            else
                textView.setText(Double.toString(result));
        }
    }

    public void onDel(View view) {
        if (textView.getText().length() > 0) {
            textView.setText(textView.getText().subSequence(0, textView.length() - 1));

        }
    }

    public void onExp(View view) {
        if ("+-*/().".indexOf(textView.getText().charAt(textView.getText().length() - 1)) >= 0)
            textView.setText(textView.getText().subSequence(0, textView.length() - 1));

        textView.append("^");
    }

    public void onLP(View view){
        leftParenthesisCount++;
        textView.append("(");
    }
    //Not allowed to enter a right parenthesis if a matching left parenthesis does not exist
    public void onRP(View view){
        if(leftParenthesisCount == 0 ) return;
        if(textView.getText().length() > 0 && "+-*/(.".indexOf(textView.getText().charAt(textView.getText().length() - 1)) >= 0) {
            if (textView.getText().charAt(textView.getText().length() - 1) == '('){
                if (leftParenthesisCount > 1)
                    leftParenthesisCount--;
                else
                    return;
            }
            textView.setText(textView.getText().subSequence(0, textView.length() - 1));
        }
        leftParenthesisCount--;
        textView.append(")");
    }

    public void onClear(View view){
        textView.setText("");
    }

    private void appendNumber(char n){
        textView.append(String.valueOf(n));

        //add commas, etc here
    }

    private void addCommas(){
        if(enteringNumber && !enteringDecimal) {
            digitsUntilComma--;
        }
    }
}
