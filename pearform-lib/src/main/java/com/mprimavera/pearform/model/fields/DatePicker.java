package com.mprimavera.pearform.model.fields;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.mprimavera.pearform.R;
import com.mprimavera.pearform.contracts.IValidator;
import com.mprimavera.pearform.model.FieldWidget;
import com.mprimavera.pearform.tools.DrawableTools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatePicker extends FieldWidget {
    private IFieldValidator mValidator;
    private EditText mEditText;
    boolean isDisabled = false;


    public DatePicker(Context context) {
        super(context);
        init(context);
    }

    public DatePicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DatePicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public DatePicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public DatePicker init(Context context) {
        mContext = context;
        mLayout = (LinearLayout) inflate(getContext(), R.layout.form_input_date_picker_field, this);
        mEditText = findViewById(R.id.editText);
        return this;
    }

    public DatePicker prefillWhen(boolean condition, Bundle bundle) {
        if (condition) prefill(bundle);
        return this;
    }

    public DatePicker hint(String text) {
        mEditText.setHint(text);
        return this;
    }

    public DatePicker text(String text) {
        mEditText.setText(text);
        return this;
    }

    public void setText(String text) {
        mEditText.setText(text);
    }


    public DatePicker resultKey(String key) {
        mResultKey = key;
        return this;
    }

    public DatePicker icon(int resource) {
        setIconResource(resource);
        return this;
    }

    public void setIconResource(int resource) {
        Drawable drawable = DrawableTools.getDrawable(mContext, resource);
        mEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
    }

    public void setImageDrawable(Drawable drawable) {
        mLeftIcon.setImageDrawable(drawable);
    }

    public void setImageBitmap(Bitmap bitmap) {
        mLeftIcon.setImageBitmap(bitmap);
    }

    @Override
    public boolean validate() {
        if (!isDisabled)
            return mEditText.getText() != null && !String.valueOf(mEditText.getText()).equals("");
        else return true;
    }


    @Override
    public Bundle getValue() {
        //return a timestamp
        Bundle bundle = new Bundle();

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date;
        try {
            date = formatter.parse(String.valueOf(mEditText.getText()));
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
                mEditText.setText(getDate(timestamp));
            }

        }
    }

    private String getDate(long timeStamp) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "";
        }
    }

    @Override
    public boolean hasFieldBeenUpdated(Bundle initialBundle) {
        if (initialBundle != null) {
            String oldValue = initialBundle.getString(mResultKey);
            return oldValue.equals(String.valueOf(mEditText.getText()));
        } else return false;
    }

    public void setOnTouchListener(OnTouchListener listener) {
        mEditText.setOnTouchListener(listener);
    }

    public void setVisibility(int visibility) {
        mEditText.setVisibility(visibility);
    }

    @Override
    public void disable() {
        isDisabled = true;
        mEditText.setClickable(false);
    }

    @Override
    public void enable() {
        isDisabled = false;
        mEditText.setClickable(true);
    }

    @Override
    public void reset() {
        mEditText.setText("");
    }

    @Override
    public void setValidator(IValidator validator) {
        mValidator = (IFieldValidator) validator;
    }

    public static interface IFieldValidator extends IValidator {
    }
}
