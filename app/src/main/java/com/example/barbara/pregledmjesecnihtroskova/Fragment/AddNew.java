package com.example.barbara.pregledmjesecnihtroskova.Fragment;

import android.app.DatePickerDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;

import com.example.barbara.pregledmjesecnihtroskova.DBc.DabaseSingleton;
import com.example.barbara.pregledmjesecnihtroskova.Model.Zapis;
import com.example.barbara.pregledmjesecnihtroskova.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddNew extends AppCompatActivity implements View.OnClickListener {

    public static String ZAPIS_ID = "zapis_id";
    public static final String CHECKED = "value";
    String dateFormat = "dd.MM.yyyy";
    public EditText Input;
    public EditText NewDateInput;
    public Switch Details;
    public FloatingActionButton fabDate;

    public int nInput;
    public int zapisID;
    public String sDescription;
    public Long InputDate;
    public Long CalendarDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
        fabDate = findViewById(R.id.fabNewDate);
        NewDateInput = findViewById(R.id.etNewDate);
        Input = findViewById(R.id.etInput);
        Details = findViewById(R.id.description);
        fabDate.setOnClickListener(this);
        if(getIntent().getBooleanExtra(CHECKED,true))
        {
            Zapis zapis = new Zapis();
            Integer zapis_id = getIntent().getIntExtra(ZAPIS_ID, zapisID);
            zapis = DabaseSingleton.getInstance(getApplicationContext()).getDatabaseInstance().getZapisiDAO().findZapisId(zapis_id);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
            String formatedDate = simpleDateFormat.format(zapis.getCreatedDate());
            NewDateInput.setText(formatedDate);
            Input.setText(String.valueOf(zapis.getInput()));
            String opis = zapis.getDescription();
            if (opis.equals("uplata"))
            {
                Details.setChecked(true);
            }
            else
            {
                Details.setChecked(false);
            }
        }
    }
    public void change (View v)  {
        nInput = Integer.parseInt(Input.getText().toString());
        boolean isChecked = Details.isChecked();
        if(!isChecked)
        {
            sDescription =Details.getTextOff().toString();
        }
        else
        {
            sDescription =Details.getTextOn().toString();
        }
        InputDate = CalendarDate;
        if (getIntent().getBooleanExtra(CHECKED,true))
        {
            int zapis_id = getIntent().getIntExtra(ZAPIS_ID, zapisID);
            Zapis zapis =  new Zapis();
            zapis = DabaseSingleton.getInstance(getApplicationContext()).getDatabaseInstance().getZapisiDAO().findZapisId(zapis_id);
            zapis.setInput(nInput);
            zapis.setDescription(sDescription);
            DabaseSingleton.getInstance(getApplicationContext()).getDatabaseInstance().getZapisiDAO().update(zapis);
            finish();
        }
        else
        {
            Zapis novi =  new Zapis();
            novi.setInput(nInput);
            novi.setCreatedDate(InputDate);
            novi.setDescription(sDescription);
            DabaseSingleton.getInstance(getApplicationContext()).getDatabaseInstance().getZapisiDAO().AddNew(novi);
            finish();
        }
    }
    public void cancel (View v)
    {
        finish();
    }
    @Override
    public void onClick(View v)
    {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year,
                                      int month, int day) {

                    final Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.YEAR, year);
                    CalendarDate =  calendar.getTimeInMillis();

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
                    String formatedDate = simpleDateFormat.format(CalendarDate);
                    NewDateInput.setText(formatedDate);

                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),  calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
