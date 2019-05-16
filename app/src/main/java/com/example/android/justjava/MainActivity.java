package com.example.android.justjava;



import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This app displays an order form to order coffee.
 * Add toppings, select number of coffees and press order to send order as an email
 */
public class MainActivity extends AppCompatActivity {
    int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        //Checkbox for whipped cream
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();

        //Checkbox for chocolate
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        //Gets customer name entry from nameEditText
        EditText nameEditText = (EditText) findViewById(R.id.name_edit_text);
        String nameEntry = nameEditText.getText().toString();

        //Passes name and topping info to createOrderSummary()
        //Passes createOrderSummary() return value to displayMessage()

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        //intent.putExtra(Intent.EXTRA_EMAIL, "");
        intent.putExtra(Intent.EXTRA_SUBJECT, "JustJava Order for " + nameEntry);
        intent.putExtra(Intent.EXTRA_TEXT, createOrderSummary(nameEntry, hasWhippedCream, hasChocolate));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * This method displays the given quantity value on the screen.
     * Is modified by the increment and decrement methods
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    public void increment(View view) {
        if (quantity>=100){
            Toast.makeText(this, "You cannot order more than 100 coffees", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity + 1;
        display(quantity);
    }

    public void decrement(View view) {
        if (quantity<=1){
            Toast.makeText(this, "You cannot have less than 1 cup of coffee", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity - 1;
        display(quantity);
    }

    public String createOrderSummary(String nameEntry, boolean addWhippedCream, boolean addChocolate) {
        String summary = "Name: " + nameEntry;
        summary += "\nAdd Whipped Cream?" + addWhippedCream;
        summary += "\nAdd Chocolate?" + addChocolate;
        summary += "\nQuantity: " + quantity;
        summary += "\nTotal: $" + calculatePrice(addWhippedCream, addChocolate);
        summary += "\nThank you!";
        return summary;
    }

    /*
    **Returns total price based on quantity and whether the user wants whipped cream and/or chocolate
     */
    public int calculatePrice(boolean hasWhippedCream, boolean hasChocolate){
        int toppingsPrice = 0;
        if (hasWhippedCream) {
            toppingsPrice += quantity;
        }
        if (hasChocolate) {
            toppingsPrice += quantity*2;
        }
        return (quantity*5) + toppingsPrice;
    }
}