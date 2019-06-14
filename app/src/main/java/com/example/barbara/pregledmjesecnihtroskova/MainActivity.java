package com.example.barbara.pregledmjesecnihtroskova;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.barbara.pregledmjesecnihtroskova.Adapter.ZapisRecyclerAdapter;
import com.example.barbara.pregledmjesecnihtroskova.DBc.DabaseSingleton;
import com.example.barbara.pregledmjesecnihtroskova.Fragment.AddNew;
import com.example.barbara.pregledmjesecnihtroskova.Model.Zapis;
import com.example.barbara.pregledmjesecnihtroskova.YearMonthPicker.PickerDialog;
import com.example.barbara.pregledmjesecnihtroskova.listeners.ZapisListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;


import static com.example.barbara.pregledmjesecnihtroskova.Fragment.AddNew.CHECKED;
import static com.example.barbara.pregledmjesecnihtroskova.Fragment.AddNew.ZAPIS_ID;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener {
    public RecyclerView recyclerView;
    public ZapisRecyclerAdapter recyclerAdapter;
    public EditText etDate;
    public FloatingActionButton myFabSearch;
    public List<Zapis> listZapisi;
    public List<Zapis> listOnResume;
    public List<Zapis> listByDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listZapisi = DabaseSingleton.getInstance(getApplicationContext()).getDatabaseInstance().getZapisiDAO().getZapisi();

        recyclerView= findViewById(R.id.rvZapis);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        recyclerAdapter = new ZapisRecyclerAdapter(listZapisi, mClick);
        recyclerView.setAdapter(recyclerAdapter);

        FloatingActionButton myFab = findViewById(R.id.fabNew);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, AddNew.class);
                myIntent.putExtra(CHECKED, false);
                startActivity(myIntent);
            }
        });

        etDate = findViewById(R.id.etDate);
        myFabSearch =findViewById(R.id.fabSearchDate);
        myFabSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2019,05,01);

        PickerDialog yearMonthPickerDialog = new PickerDialog(this,
            calendar,
            new PickerDialog.OnDateSetListener() {
                @Override
                public void onYearMonthSet(int year, int month) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
                    etDate.setText(dateFormat.format(calendar.getTime()));
                    Integer monthT = calendar.get(Calendar.MONTH);
                    Integer yearT =  calendar.get(Calendar.YEAR);
                    search(monthT, yearT);
                }
            });
        yearMonthPickerDialog.setMinYear(2000);
        yearMonthPickerDialog.setMaxYear(2020);
        yearMonthPickerDialog.show();
    }
    static public long getFirstOfMonth(int year, int month, TimeZone tz) {
        GregorianCalendar cal = new GregorianCalendar(year, (month), 1);
        cal.setTimeZone(tz);
        return cal.getTime().getTime();
    }
    static public long getFirstMiliOfMonth(Integer year, Integer month)
    {
        return getFirstOfMonth(year, month, TimeZone.getDefault());
    }
    static public long getLastOfMonth(int year, int month, TimeZone tz) {
        GregorianCalendar cal = new GregorianCalendar(year, (month), 1);
        cal.setTimeZone(tz);

        int lastday = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        cal.set(GregorianCalendar.DAY_OF_MONTH, lastday);

        cal.set(GregorianCalendar.HOUR_OF_DAY, 23);
        cal.set(GregorianCalendar.MINUTE, 59);
        cal.set(GregorianCalendar.SECOND, 59);
        cal.set(GregorianCalendar.MILLISECOND, 999);

        long time = cal.getTime().getTime();
        return time;
    }
    static public long getLastMilliOfMonth(Integer year, Integer month) {
        return getLastOfMonth(year, month, TimeZone.getDefault());
    }
    public void search(Integer month, Integer year){
        Long startdate = getFirstMiliOfMonth(year, month);
        Long enddate = getLastMilliOfMonth(year, month);
        listByDate = DabaseSingleton.getInstance(getApplicationContext()).getDatabaseInstance().getZapisiDAO().getZapisiByDate(startdate, enddate);
        recyclerView= findViewById(R.id.rvZapis);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        recyclerAdapter = new ZapisRecyclerAdapter(listByDate, mClick);
        recyclerView.setAdapter(recyclerAdapter);
    }

    ZapisListener mClick = new ZapisListener() {
        @Override
        public void onClick(final Zapis zapis) {
            final  Intent intent = new Intent(MainActivity.this, AddNew.class );
            final int id_zapisa = zapis.getId();
            intent.putExtra(ZAPIS_ID, id_zapisa);
            intent.putExtra(CHECKED, true);
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setMessage(R.string.edit_message).setTitle(R.string.dialog_edit);
            builder.setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    startActivity(intent);
                }
            });
            builder.setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(R.string.dialog_title);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog_confirm, int id) {
                            if(getIntent().getBooleanExtra(CHECKED,true))
                            {
                                Zapis zapis = new Zapis();
                                zapis = DabaseSingleton.getInstance(getApplicationContext()).getDatabaseInstance().getZapisiDAO().findZapisId(id_zapisa);
                                DabaseSingleton.getInstance(getApplicationContext()).getDatabaseInstance().getZapisiDAO().Delete(zapis);
                                onResume();
                                Toast.makeText(getApplicationContext(),R.string.confirmation,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog_confirm, int id) {
                            dialog_confirm.dismiss();
                        }
                    });
                    AlertDialog dialog_confirm = builder.create();
                    dialog_confirm.show();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    };
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    public void onResume()
    {
        super.onResume();
        listOnResume = DabaseSingleton.getInstance(getApplicationContext()).getDatabaseInstance().getZapisiDAO().getZapisi();
        recyclerAdapter = new ZapisRecyclerAdapter(listOnResume, mClick);
        recyclerView.setAdapter(recyclerAdapter);
    }
}

