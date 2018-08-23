package com.mprimavera.pearform.model.fields;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;

import com.mprimavera.pearform.R;
import com.mprimavera.pearform.contracts.IValidator;
import com.mprimavera.pearform.model.FieldWidget;
import com.mprimavera.pearform.tools.DrawableTools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateInput extends FieldWidget {
    protected TextInputLayout mInputLayout;
    protected TextInputEditText mInputText;
    protected String mError;

    private String current = "";
    private String mFormatHint = "dd/MM/yyyy";
    private String mFormat = "dd/MM/yyyy";
    private String ddmmyyyy = "ddMMyyyy";
    private Calendar cal = Calendar.getInstance();

    boolean isValid = false;

    public DateInput(Context context) {
        super(context);
        this.init();
    }

    public DateInput(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public DateInput(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    public DateInput(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init();
    }

    public void init() {
        inflate(getContext(), R.layout.form_input_date_edit_field, this);
        mInputText = findViewById(R.id.input_text);
        mInputLayout = findViewById(R.id.layout);
    }

    public void setTextColor(int color) {
        mInputText.setTextColor(color);
    }

    public DateInput setInputType(int type) {
        mInputText.setInputType(type);
        return this;
    }

    public DateInput setFormat(String format) {
        mFormat = format;
        return this;
    }

    public DateInput setFormatHint(String format) {
        mFormatHint = format;
        ddmmyyyy = format.replaceAll("/", "");
        return this;
    }

    public void addTextChangedListener() {

        mInputText.setOnTouchListener((v, event) -> {

            if (mInputText.getText().length() == 0)
                new Handler().postDelayed(() -> {
                    mInputText.setText(String.format("%s/%s/%s", ddmmyyyy.substring(0, 2),
                            ddmmyyyy.substring(2, 4),
                            ddmmyyyy.substring(4, 8)));
                }, 500);

            return false;
        });

        mInputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals(current)) {
                    return;
                }

                String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                Log.d("TEST_BIRTHDATE", "clean before " + clean);

                if (clean.length() != 8) isValid = false;
                else isValid = true;


                int cl = clean.length();
                int sel = cl;
                for (int i = 2; i <= cl && i < 6; i += 2) {
                    sel++;
                }
                //Fix for pressing delete next to a forward slash
                if (clean.equals(cleanC)) sel--;

                if (clean.length() < 8) {
                    clean = clean + ddmmyyyy.substring(clean.length());
                    hideError();

                } else {
                    //This part makes sure that when we finish entering numbers
                    //the date is correct, fixing it otherwise

                    int xx = Integer.parseInt(clean.substring(0, 2));
                    int yy = Integer.parseInt(clean.substring(2, 4));
                    int zzzz = Integer.parseInt(clean.substring(4, 8));

                    String dateStr = String.format("%s/%s/%s", xx,yy,zzzz);
                    DateFormat formatter = new SimpleDateFormat(mFormat);
                    Date date;
                    try {
                        date = formatter.parse(dateStr);
                    } catch (ParseException e) {
                        date = new Date();
                    }

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    int year = cal.get(Calendar.YEAR);
                    int mon = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    boolean valid = true;
                    if (mon < 1 || mon > 12) valid = false;
                    if (year < 1900 || year > 2100) valid = false;
                    if (day > cal.getActualMaximum(Calendar.DATE)) valid = false;


                    /*
                    mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                    cal.set(Calendar.MONTH, mon - 1);
                    year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                    cal.set(Calendar.YEAR, year);

                     // ^ first set year for the line below to work correctly
                    //with leap years - otherwise, date e.g. 29/02/2012
                    //would be automatically corrected to 28/02/2012

                    day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                    clean = String.format("%02d%02d%02d", day, mon, year);
                    */


                    if (!valid) {
                        showError();
                        isValid = false;
                    }
                    else {
                        hideError();
                        isValid=true;
                    }
                }

                clean = String.format("%s/%s/%s", clean.substring(0, 2),
                        clean.substring(2, 4),
                        clean.substring(4, 8));

                sel = sel < 0 ? 0 : sel;
                current = clean;

                mInputText.setText(current);
                mInputText.setSelection(sel < current.length() ? sel : current.length());


                Log.d("TEST_BIRTHDATE", "clean after " + clean);
                Log.d("TEST_BIRTHDATE", "CharSequence " + s);
                Log.d("TEST_BIRTHDATE", "start " + start);
                Log.d("TEST_BIRTHDATE", "before " + before);
                Log.d("TEST_BIRTHDATE", "count " + count);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void hideError() {
        mInputLayout.setErrorEnabled(false);
        mInputLayout.setError(null);
    }

    public void showError() {
        mInputLayout.setErrorEnabled(true);
        mInputLayout.setError(mError);
    }


    public DateInput error(String text) {
        mError = text;
        return this;
    }

    public void setText(String text) {
        mInputText.setText(text);
    }


    public DateInput hint(String text) {
        mInputLayout.setHintEnabled(true);
        mInputLayout.setHintAnimationEnabled(true);
        mInputLayout.setHint(text);
        return this;
    }

    public DateInput text(String text) {
        mInputText.setText(text);
        return this;
    }

    public DateInput icon(int resource) {
        setIconResource(resource);
        return this;
    }

    public void setIconResource(int resource) {
        Drawable drawable = DrawableTools.getDrawable(mContext, resource);
        mInputText.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
    }

    @Override
    public boolean validate() {
        if (!isValid) showError();
        else hideError();
        return isValid;
    }

    @Override
    public void enable() {
        this.mInputText.setClickable(true);
    }

    @Override
    public void disable() {
        mInputText.setClickable(false);
    }

    @Override
    public Bundle getValue() {
        //return a timestamp
        Bundle bundle = new Bundle();

        DateFormat formatter = new SimpleDateFormat(mFormat);
        Date date;
        try {
            date = formatter.parse(String.valueOf(mInputText.getText()));
        } catch (ParseException e) {
            //date = new Date();
            return bundle;
        }
        bundle.putString(mResultKey, String.valueOf(date.getTime() / 1000));
        return bundle;
    }

    @Override
    public void prefill(Bundle bundle) {
        if (bundle != null) {
            Long text = bundle.getLong(mResultKey, 0);
            if (text != 0) {
                Log.d("TEST_DATEP", String.valueOf(text));
                long timestamp = text * 1000L;
                mInputText.setText(getDate(timestamp));
            }

        }
    }

    private String getDate(long timeStamp) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(mFormat);
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "";
        }
    }


    @Override
    public void reset() {
        mInputText.setText(null);
    }

    @Override
    public void setValidator(IValidator validator) {

    }
}
