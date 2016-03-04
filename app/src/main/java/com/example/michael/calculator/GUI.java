package com.example.michael.calculator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * The GUI for our calculator. Contains code for all the necessary onClick functions.
 */
public class GUI extends AppCompatActivity {

    /**
     * Enum used to tell our Text Watcher how the text was changed
     */
    private enum TextChangeCase {
        APPEND, DELETE, REPLACE, EQUALS, UNSET
    }

    //true if text area contains any message that should be deleted on next input
    int leftParenthesisCount;
    boolean modifyingDecimal;
    int offsetCurNumber;
    TextChangeCase textChangeCase;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        leftParenthesisCount = 0;
        modifyingDecimal = false;
        textView = (TextView)findViewById(R.id.textView);
        offsetCurNumber = -1;
        Button bdel = (Button) findViewById(R.id.bdel);
        bdel.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onClear(v);
                return true;
            }
        });
        textView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            /**
             *  If the reason for the text being changed is unknown, find the case
             *  and set the textChangeCase variable for use by the afterTextChanged method
             * @param s the text after it has been changed
             * @param start unused
             * @param before the length of the text before it was changed
             * @param count unused
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //If case is not UNSET, then something outside of this method has set the
                //the case since our text watcher was called, and we do not want to change it.
                if(textChangeCase != TextChangeCase.UNSET)
                    return;
                else if (s.length() == before) //Text was replaced, length is the same
                    textChangeCase = TextChangeCase.REPLACE;
                else if (s.length() > before) //A character was appended
                    textChangeCase = TextChangeCase.APPEND;
                else                          //A character was deleted
                    textChangeCase = TextChangeCase.DELETE;

            }

            /**
             * Responsible for adjust commas if necessary. It does not modify the offsetCurNumber
             * or modifyingDecimal variables; that is onDel's reponsibility.
             * Before returning, it sets textChangeCase back to UNSET.
             * @param s unused
             */
            @Override
            public void afterTextChanged(Editable s) {
                if (textChangeCase == TextChangeCase.REPLACE || modifyingDecimal || offsetCurNumber < 0)
                    ;//nothing to do
                else if (textChangeCase == TextChangeCase.APPEND) {
                    int length = textView.length();
                    StringBuilder number = new StringBuilder(textView.getText().subSequence(offsetCurNumber, length));
                    int index = number.length() - 4;
                    if (index < 0) return;
                    textView.removeTextChangedListener(this);
                    while (index > 0) {
                        number.setCharAt(index - 1, number.charAt(index));
                        number.setCharAt(index, ',');
                        index -= 4;
                    }
                    if (index == 0) {
                        number.insert(0, number.charAt(0));
                        number.setCharAt(1, ',');
                    }
                    textView.setText(textView.getText().subSequence(0, offsetCurNumber).toString() + number.toString());
                    textView.addTextChangedListener(this);
                }
                else if (textChangeCase == TextChangeCase.DELETE) {
                    int length = textView.length();
                    StringBuilder number = new StringBuilder(textView.getText().subSequence(offsetCurNumber, length));
                    int index = length - 4;
                    if (index < 0) return;
                    textView.removeTextChangedListener(this);
                    while (index > 0) {
                        number.setCharAt(index + 1, number.charAt(index));
                        number.setCharAt(index, ',');
                        index -= 4;
                    }
                    if (index == 0) {
                        number.deleteCharAt(1);
                    }
                    textView.setText(textView.getText().subSequence(0, offsetCurNumber).toString() + number.toString());
                    textView.addTextChangedListener(this);
                }
                else{ //textChangeCase == TextChangeCase.EQUALS
                    //nothing to do
                }
                //set case to unknown
                textChangeCase = TextChangeCase.UNSET;
            }
        });
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

    /**
     * onClick listener for the zero button. Does a few checks before appending to make sure
     * we avoid leading zeroes, then passes the number to the appendNumber function
     * @param view
     */
    public void onZero(View view){
        if(textView.length() == 0 || modifyingDecimal ||
                (offsetCurNumber >= 0 && textView.getText().charAt(offsetCurNumber) != '0')) {
            appendNumber('0');
        }


    }

    /**
     * The onClick listeners for digits 1-9 simply call appendNumber with the corresponding character.
     * onZero is the only one with extra logic.
     * @param view
     */
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

    /**
     * onClick listener for the addition button.
     * We do not want the + sign to be placed after the +, -, *, /, (, or . operators. If one of
     * those operators was the last character entered, we will replace that character instead of
     * simply appending the + sign.
     * Sets offsetCurNumber to -1 and modifyingDecimal to false to tell the program the user
     * is not currently inputting a number.
     * @param view
     */
    public void onAdd(View view){
        if(textView.getText().length() == 0) return;
        if("+-*/(.".indexOf(textView.getText().charAt(textView.getText().length() - 1)) >= 0)
            textView.setText(textView.getText().subSequence(0, textView.length() - 1));

        offsetCurNumber = -1;
        modifyingDecimal = false;
        textView.append("+");

    }

    /**
     * onClick listener for the subtraction button.
     * We want to allow the - symbol to be used as both a unary and binary operator(to specify
     * negative numbers), so it has far less restrictions than the other operators.
     * Replace the previous character instead of simply appending only if the last character
     * was a - or .
     * Sets offsetCurNumber to -1 and modifyingDecimal to false to tell the program the user
     * is not currently inputting a number.
     * @param view
     */
    public void onSub(View view){
        if(textView.getText().length() > 0 && "-.".indexOf(textView.getText().charAt(textView.getText().length() - 1)) >= 0)
            textView.setText(textView.getText().subSequence(0, textView.length() - 1));
        offsetCurNumber = -1;
        modifyingDecimal = false;
        textView.append("-");
    }

    /**
     * onClick listener for the multipication button.
     * We do not want the * sign to be placed after the +, -, *, /, (, or . operators. If one of
     * those operators was the last character entered, we will replace that character instead of
     * simply appending the * sign.
     * Sets offsetCurNumber to -1 and modifyingDecimal to false to tell the program the user
     * is not currently inputting a number.
     * @param view
     */
    public void onMul(View view){

        if(textView.getText().length() == 0) return;
        if("+-*/(.".indexOf(textView.getText().charAt(textView.getText().length() - 1)) >= 0)
            textView.setText(textView.getText().subSequence(0, textView.length() - 1));

        offsetCurNumber = -1;
        modifyingDecimal = false;
        textView.append("*");

    }

    /**
     * onClick listener for the division button.
     * We do not want the + sign to be placed after the +, -, *, /, (, or . operators. If one of
     * those operators was the last character entered, we will replace that character instead of
     * simply appending the / sign.
     * Sets offsetCurNumber to -1 and modifyingDecimal to false to tell the program the user
     * is not currently inputting a number.
     * @param view
     */
    public void onDiv(View view){
        if(textView.getText().length() == 0) return;
        if("+-*/(.".indexOf(textView.getText().charAt(textView.getText().length() - 1)) >= 0)
            textView.setText(textView.getText().subSequence(0, textView.length() - 1));

        offsetCurNumber = -1;
        modifyingDecimal = false;
        textView.append("/");

    }

    /**
     * onClick listener for the decimal button.
     * Only append the decimal separator if the user is in the process of inputting a number.
     * In the future, we cano consider allowing numbers less than 0 to be started
     * with a decimal point, such as ".234" instead of "0.234"
     * Sets offsetCurNumber to -1 and modifyingDecimal to false to tell the program the user
     * is not currently inputting a number.
     * @param view
     */
    public void onDec(View view){
        if(offsetCurNumber >= 0) {
            modifyingDecimal = true;
            textView.append(".");
        }
    }

    /**
     * onClick listener for the equals operator. A lot of logic needed here. If the expression
     * is empty, we just return. Otherwise, we set textChangeCase to TextChangeCase.EQUALS so that
     * the text view's text watcher knows not to do anything. Then we pass the expression, with
     * commas removed, to the Calculator class' evaluate method.
     * The evaluate method will throw an exception if our expression is not valid. We may also
     * get a result of infinity or NaN if something like division by zero occurs. If any
     * of this occurs, we display an error message and clear the text field.
     * If the expression was successfully evaluated, we display the result, set offsetCurNumber to 0
     * (which is guaranteed to be the start index of the result), and modifyingDecimal to true or
     * false depending on if the result can be represented as an integer or not.
     * @param view
     */
    public void onEq(View view){
        String text = textView.getText().toString().replace(",", "");
        //if no expression just return
        if (text.length() == 0) return;
        double result;
        textChangeCase = TextChangeCase.EQUALS;
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
            offsetCurNumber = 0;
            if(result == Math.floor(result)) {
                modifyingDecimal = true;
                String resultString = NumberFormat.getIntegerInstance().format((int) result);
                textView.setText(resultString);
            }
            else {
                modifyingDecimal = false;
                DecimalFormat formatter = new DecimalFormat();
                textView.setText(formatter.format(result));
            }
        }
    }

    /**
     * onClick listener for the delete button
     * Our TextWatcher is responsible for adjusting commas in a number that is modified due
     * to pressing the delete button. However, the onDel method is responsible for managing
     * the offsetCurNumber and modifyingDecimal variables required by the TextWatcher to make
     * the correct adjustments.
     * @param view
     */
    public void onDel(View view) {
        if (textView.getText().length() > 0) {
            if(offsetCurNumber < 0 && textView.length() >= 2 && textView.getText().charAt(textView.length() - 2) >= '0'){
                CharSequence text = textView.getText();
                int index = text.length() - 2;
                while(index >= 0 && text.charAt(index) >= '0') {
                    if (text.charAt(index) == '.') modifyingDecimal = true;
                    index--;
                }
                offsetCurNumber = index + 1;
            }
            else if(offsetCurNumber == textView.length() - 1)
                offsetCurNumber = -1;
            textView.setText(textView.getText().subSequence(0, textView.length() - 1));

        }
    }

    /**
     * onClick listener for the exponent button.
     * We do not want the + sign to be placed after the +, -, *, /, (, ^, or . operators. If one of
     * those operators was the last character entered, we will replace that character instead of
     * simply appending the ^ sign.
     * Sets offsetCurNumber to -1 and modifyingDecimal to false to tell the program the user
     * is not currently inputting a number.
     * @param view
     */
    public void onExp(View view) {
        if ("+-*/()^.".indexOf(textView.getText().charAt(textView.getText().length() - 1)) >= 0)
            textView.setText(textView.getText().subSequence(0, textView.length() - 1));

        textView.append("^");
        offsetCurNumber = -1;
        modifyingDecimal = false;
    }

    /**
     * onClick listener for the left parenthesis button.
     * There are no rules for appending a left parenthesis. We just need to make sure to
     * increase the left parenthesis counter.
     * Sets offsetCurNumber to -1 and modifyingDecimal to false to tell the program the user
     * is not currently inputting a number.
     * @param view
     */
    public void onLP(View view){
        leftParenthesisCount++;
        offsetCurNumber = -1;
        modifyingDecimal = false;
        textView.append("(");

    }

    /**
     * onClick listener for the right parenthesis button.
     * Similar to most operators, we do not want a right parenthesis following an operator
     * other than a unary '-'.
     * We must also make sure there is a matching left parenthesis by looking at the
     * leftParenthesisCount variable, and subtract the count if we are introducing
     * a new matching right parenthesis.
     * Sets offsetCurNumber to -1 and modifyingDecimal to false to tell the program the user
     * is not currently inputting a number.
     * @param view
     */
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
        offsetCurNumber = -1;
        modifyingDecimal = false;
        textView.append(")");
    }

    /**
     * onClick listener for the clear button. Also called if the delete button is held down.
     * Clears the text view.
     * Sets offsetCurNumber to -1 and modifyingDecimal to false to tell the program the user
     * is not currently inputting a number.
     * @param view
     */
    public void onClear(View view){
        offsetCurNumber = -1;
        modifyingDecimal = false;
        textView.setText("");
    }

    /**
     * Appends the given character to the text view. Called by the onClick listeners for
     * every digit button.
     * We also check if this is the beginning of a new number by checking if offsetCurNumber
     * is less than 0. If it is, we set offsetCurNumber to the index of our new character.
     * @param n the digit to append
     */
    private void appendNumber(char n){
        if (offsetCurNumber < 0) offsetCurNumber = textView.length();
        textView.append(String.valueOf(n));
    }


}
