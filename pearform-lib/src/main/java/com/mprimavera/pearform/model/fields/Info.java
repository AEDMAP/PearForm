package com.mprimavera.pearform.model.fields;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mprimavera.pearform.R;
import com.mprimavera.pearform.contracts.IValidator;
import com.mprimavera.pearform.model.FieldWidget;

public class Info extends FieldWidget {
    private LinearLayout mLayout;
    private TextView mText;

    public Info(Context context) {
        super(context);
        init(context);
    }

    public Info(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Info(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public Info(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context) {
        mContext = context;
        inflate(context, R.layout.form_info_field, this);
        mLayout = findViewById(R.id.layout);
        mText = mLayout.findViewById(R.id.text);
    }

    public Info resultKey(String key) {
        mResultKey = key;
        return this;
    }

    public void setText(String text) {
        mText.setText(text);
    }

    public Info text(String text) {
        setText(text);
        return this;
    }


    public Info textSize(float size) {
        mText.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        return this;
    }

    public Info bold(boolean condition) {
        if(condition) mText.setTypeface(null, Typeface.BOLD);
        return this;
    }

    public Info prefillWhen(boolean condition, Bundle bundle) {
        if (condition && bundle != null) prefill(bundle);
        return this;
    }

    @Override
    public void prefill(Bundle bundle) {
        if (bundle != null) {
            String text = bundle.getString(mResultKey);
            if (text != null) mText.setText(text);
        }
    }

    @Override
    public void reset() {
    }

    @Override
    public void setValidator(IValidator validator) {
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public Bundle getValue() {
        String text = mText.getText().toString();
        Bundle result = new Bundle();

        if (text != null) {
            result.putString(mResultKey, text);
            return result;
        } else return null;
    }
}
