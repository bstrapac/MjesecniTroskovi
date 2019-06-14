package com.example.barbara.pregledmjesecnihtroskova.YearMonthPicker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.barbara.pregledmjesecnihtroskova.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PickerDialog  implements Dialog.OnClickListener {
    private static final int MIN_YEAR = 1970;
    private static final int MAX_YEAR = 2099;
    private static final String MONTH_FORMAT = "MMMM";
    private static String[] MONTHS_LIST = null;
    private Calendar calendar;
    private OnDateSetListener mOnDateSetListener;
    private final Context mContext;
    private static Locale mCurrentLocale = Locale.getDefault();
    private AlertDialog.Builder mDialogBuilder;
    private AlertDialog mDialog;
    private int mTheme;
    private int mTextTitleColor;
    private int mYear;
    private int mMonth;
    private Picker mYearPicker;
    private TextView mYearValue;

    public PickerDialog(Context context, Calendar calendar, OnDateSetListener onDateSetListener) {
        this(context, onDateSetListener, -1, -1, calendar);
    }

    public PickerDialog(Context context, OnDateSetListener onDateSetListener, Calendar calendar) {
        this(context, onDateSetListener, -1, -1, calendar);
    }
    public PickerDialog(Context context, OnDateSetListener onDateSetListener, Calendar calendar, int theme) {
        this(context, onDateSetListener, theme, -1, calendar);
    }
    public PickerDialog(Context context, OnDateSetListener onDateSetListener, int theme,
                                 int titleTextColor, Calendar calendar) {
        mContext = context;
        mOnDateSetListener = onDateSetListener;
        mTheme = theme;
        mTextTitleColor = titleTextColor;
        this.calendar = calendar;

        //Set current locale of system
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mCurrentLocale = mContext.getResources().getConfiguration().getLocales().get(0);
        } else {
            mCurrentLocale = mContext.getResources().getConfiguration().locale;
        }

        //Builds the dialog using listed parameters.
        build();
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            //If user presses positive button
            case DialogInterface.BUTTON_POSITIVE:
                //Check if user gave us a listener
                if (mOnDateSetListener != null)
                    //Set picked year and month to the listener
                    mOnDateSetListener.onYearMonthSet(mYear, mMonth);
                break;

            //If user presses negative button
            case DialogInterface.BUTTON_NEGATIVE:
                //Exit the dialog
                dialog.cancel();
                break;
        }
    }
    private void build() {
        //Applying user's theme
        int currentTheme = mTheme;
        //If there is no custom theme, using default.
        // if (currentTheme == -1) currentTheme = R.style.MyDialogTheme;

        //Initializing dialog builder.
        mDialogBuilder = new AlertDialog.Builder(mContext, currentTheme);

        //Creating View inflater.
        final LayoutInflater layoutInflater =
            (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Inflating custom title view.
        final View titleView = layoutInflater.inflate(R.layout.view_dialog_title, null, false);
        //Inflating custom content view.
        final View contentView = layoutInflater.inflate(R.layout.view_month_year_picker, null, false);

        //Initializing year and month pickers.
        mYearPicker = (Picker) contentView.findViewById(R.id.year_picker);
        final Picker monthPicker =
            (Picker) contentView.findViewById(R.id.month_picker);

        //Initializing title text views
        final TextView monthName = (TextView) titleView.findViewById(R.id.month_name);
        mYearValue = (TextView) titleView.findViewById(R.id.year_name);

        //If there is user's title color,
        if (mTextTitleColor != -1) {
            //Then apply it.
            setTextColor(monthName);
            setTextColor(mYearValue);
        }

        //Setting custom title view and content to dialog.
        mDialogBuilder.setCustomTitle(titleView);
        mDialogBuilder.setView(contentView);

        //Setting year's picker min and max value
        mYearPicker.setMinValue(MIN_YEAR);
        mYearPicker.setMaxValue(MAX_YEAR);

        //Setting month's picker min and max value
        monthPicker.setMinValue(0);
        monthPicker.setMaxValue(monthsList().length - 1);

        //Setting month list.
        monthPicker.setDisplayedValues(monthsList());

        //Applying current date.
        setCurrentDate(mYearPicker, monthPicker, monthName, mYearValue);

        //Setting all listeners.
        setListeners(mYearPicker, monthPicker, monthName, mYearValue);

        //Setting titles and listeners for dialog buttons.
        mDialogBuilder.setPositiveButton("OK", this);
        mDialogBuilder.setNegativeButton("CANCEL", this);

        //Creating dialog.
        mDialog = mDialogBuilder.create();
    }
    private void setTextColor(TextView titleView) {
        titleView.setTextColor(ContextCompat.getColor(mContext, mTextTitleColor));
    }
    private void setCurrentDate(Picker yearPicker, Picker monthPicker, TextView monthName, TextView yearValue) {
        //Getting current date values from Calendar instance.
        ////Calendar calendar = Calendar.getInstance();
        mMonth = calendar.get(Calendar.MONTH);
        mYear = calendar.get(Calendar.YEAR);

        //Setting output format.
        SimpleDateFormat monthFormat = new SimpleDateFormat(MONTH_FORMAT, mCurrentLocale);

        //Setting current date values to dialog title views.
        monthName.setText(monthFormat.format(calendar.getTime()));
        yearValue.setText(Integer.toString(mYear));

        //Setting current date values to pickers.
        monthPicker.setValue(mMonth);
        yearPicker.setValue(mYear);
    }
    private void setListeners(final Picker yearPicker, final Picker monthPicker, final TextView monthName, final TextView yearValue) {
        //Setting listener to month name view.
        monthName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If there's no month picker visible
                if (monthPicker.getVisibility() == View.GONE) {
                    //Set it visible
                    monthPicker.setVisibility(View.VISIBLE);

                    //And hide year picker.
                    yearPicker.setVisibility(View.GONE);

                    //Change title views alpha to picking effect.
                    yearValue.setAlpha(0.39f);
                    monthName.setAlpha(1f);
                }
            }
        });

        //Setting listener to year value view.
        yearValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If there's no year picker visible
                if (yearPicker.getVisibility() == View.GONE) {
                    //Set it visible
                    yearPicker.setVisibility(View.VISIBLE);

                    //And hide year picker.
                    monthPicker.setVisibility(View.GONE);

                    //Change title views alpha to picking effect.
                    monthName.setAlpha(0.39f);
                    yearValue.setAlpha(1f);
                }
            }
        });

        //Setting listener to month picker. So it can change title text value.
        monthPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mMonth = newVal;

                //Set title month text to picked month.
                monthName.setText(monthsList()[newVal]);
            }
        });

        //Setting listener to year picker. So it can change title text value.
        yearPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mYear = newVal;

                //Set title year text to picked year.
                yearValue.setText(Integer.toString(newVal));
            }
        });
    }
    public void show() {
        mDialog.show();
    }
    public void setMinYear(int minYear) {
        if (mYearPicker != null) {
            if (mYearPicker.getValue() < minYear) {
                mYearPicker.setValue(minYear);
                mYearValue.setText(Integer.toString(minYear));
            }
            mYearPicker.setMinValue(Math.min(minYear, mYearPicker.getMaxValue()));
        }
    }
    public void setMaxYear(int maxYear) {
        if (mYearPicker != null) {
            if (mYearPicker.getValue() > maxYear) {
                mYearPicker.setValue(maxYear);
                mYearValue.setText(Integer.toString(maxYear));
            }
            mYearPicker.setMaxValue(Math.max(maxYear, mYearPicker.getMinValue()));
        }
    }
    public interface OnDateSetListener {
        void onYearMonthSet(int year, int month);
    }
    private static String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
    private static String[] monthsList() {
        if (MONTHS_LIST == null) {
            int[] months = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
            String[] stringMonths = new String[months.length];

            for (int i = 0; i < months.length; i++) {
                Calendar calendar = Calendar.getInstance();

                SimpleDateFormat monthDate = new SimpleDateFormat(MONTH_FORMAT, mCurrentLocale);

                calendar.set(Calendar.MONTH, months[i]);
                String monthName = monthDate.format(calendar.getTime());


                stringMonths[i] = capitalize(monthName);
            }
            MONTHS_LIST = stringMonths;
        }
        return MONTHS_LIST;
    }
}
