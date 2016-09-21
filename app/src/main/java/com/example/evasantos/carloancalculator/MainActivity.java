package com.example.evasantos.carloancalculator;

import android.annotation.TargetApi;
import android.app.Activity;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable; // for EditText event handling
import android.text.TextWatcher; // EditText listener
import android.widget.EditText; // for bill amount input
import android.widget.SeekBar; // for changing the tip percentage
import android.widget.SeekBar.OnSeekBarChangeListener; // SeekBar listener
import android.widget.TextView; // for displaying text

@TargetApi(24)
public class MainActivity extends Activity {
    private static final NumberFormat currencyFormat =
            NumberFormat.getCurrencyInstance();
    private static final NumberFormat percentFormat =
            NumberFormat.getPercentInstance();

    private double priceAmount = 0.0; // price amount entered by the user
    private double percentInterest = 0.10; // initial interest percentage
    private double months = 60; //intial months setting
    private TextView priceTextView; // shows formatted price amount
    private TextView percentTextView; // shows interest percentage
    private TextView monthsTextView; // shows amount of months
    private TextView monthlyPaymentTextView; // shows calculated monthly payment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        priceTextView = (TextView) findViewById(R.id.fullPriceTextView);
        percentTextView = (TextView) findViewById(R.id.percentInterestTextView);
        monthsTextView = (TextView) findViewById(R.id.monthTextView);
        monthlyPaymentTextView = (TextView) findViewById(R.id.monthlyPaymentTextView);
        priceTextView.setText(currencyFormat.format(0));
        monthlyPaymentTextView.setText(currencyFormat.format(0));
    }
    private void calculate() {
        // format percent and display in percentTextView
        percentTextView.setText(percentFormat.format(percentInterest));

        // calculate the total
        double total = (priceAmount * (percentInterest/1200) * Math.pow((1 + (percentInterest/1200)),months))/(Math.pow((1 + (percentInterest/1200)),months) - 1);

        // display monthly payment formatted as currency

        monthlyPaymentTextView.setText(currencyFormat.format(total));
    }
    // listener object for the SeekBar's progress changed events
    private final OnSeekBarChangeListener seekBarListener =
            new OnSeekBarChangeListener() {
                // update percent, then call calculate
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    percentInterest = progress / 100.0; // set percent based on progress
                    calculate(); // calculate and display tip and total
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) { }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) { }
            };

    // listener object for the EditText's text-changed events
    private final TextWatcher amountEditTextWatcher = new TextWatcher() {
        // called when the user modifies the bill amount
        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {

            try { // get bill amount and display currency formatted value
                priceAmount = Double.parseDouble(s.toString()) / 100.0;
                priceTextView.setText(currencyFormat.format(priceAmount));
            }
            catch (NumberFormatException e) { // if s is empty or non-numeric
                priceTextView.setText("");
                priceAmount = 0.0;
            }

            calculate(); // update the tip and total TextViews
        }

        @Override
        public void afterTextChanged(Editable s) { }

        @Override
        public void beforeTextChanged(
                CharSequence s, int start, int count, int after) { }
    };
    // listener object for the EditText's text-changed events
    private final TextWatcher monthEditTextWatcher = new TextWatcher() {
        // called when the user modifies the bill amount
        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {

            try { // get month amount and display value
                months = Integer.parseInt(s.toString());
            }
            catch (NumberFormatException e) { // if s is empty or non-numeric
                monthsTextView.setText("");
                months = 60;
            }

            calculate(); // update the tip and total TextViews
        }

        @Override
        public void afterTextChanged(Editable s) { }

        @Override
        public void beforeTextChanged(
                CharSequence s, int start, int count, int after) { }
    };
}
