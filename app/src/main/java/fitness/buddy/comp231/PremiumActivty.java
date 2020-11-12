package fitness.buddy.comp231;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class
PremiumActivty extends AppCompatActivity {

    private EditText name, cardNumber, security, cardDate, cardCVV;
    private RadioGroup rgPayType;
    private String payType;
    private SharedPreferences spFinished;
    private Calendar myCalendar = Calendar.getInstance();
    private Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("PREMIUM");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PremiumActivty.this,ProfileActivity.class));
            }
        });

        findAllViews();
        spFinished = PreferenceManager.getDefaultSharedPreferences(this);

        rgPayType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton b = findViewById(checkedId);

                payType = b.getText().toString().trim();
                if (b.getText().toString().trim().equals("Cash")) {
                    cardNumber.setEnabled(false);
                    cardDate.setEnabled(false);
                    cardCVV.setEnabled(false);

                } else {
                    cardNumber.setEnabled(true);
                    cardDate.setEnabled(true);
                    cardCVV.setEnabled(true);
                }
            }
        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        cardDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(PremiumActivty.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        cardDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void findAllViews() {
        name = findViewById(R.id.etName);
        cardNumber = findViewById(R.id.etCard);
        security = findViewById(R.id.etSecurityQuestion);
        cardDate = findViewById(R.id.etExDate);
        cardCVV = findViewById(R.id.etCVV);
        rgPayType = findViewById(R.id.paymentRg);
    }

    public void onClickPayment(View view) {
        showAlertDialog();
    }

    public void showAlertDialog() {

        String msg = "Name: " + name.getText().toString() + "\n" +
                "Card Number: " + cardNumber.getText().toString() + "\n" +
                "Payment Type: " + payType + "\n" +
                "Expiry Date: " + cardDate.getText().toString() + "\n" +
                "Security Code: " + cardCVV.getText().toString() + "\n" +
                "Security Question: " + security.getText().toString();

        String msg1 = "Name: " + name.getText().toString() + "\n" +
                "Card Number: " + cardNumber.getText().toString() + "\n" +
                "Payment Type: " + payType + "\n" +
                "Security Question: " + security.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Review Payment");
        if (!(payType.equals("Cash"))) {
            builder.setMessage(msg);
        } else {
            builder.setMessage(msg1);
        }

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(PremiumActivty.this, "Your Subscription is Complete! \nThank you.", Toast.LENGTH_LONG).show();
                SharedPreferences.Editor editor = spFinished.edit();
                editor.clear().apply();

                startActivity(new Intent(getApplicationContext(), StartActivity.class));
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
