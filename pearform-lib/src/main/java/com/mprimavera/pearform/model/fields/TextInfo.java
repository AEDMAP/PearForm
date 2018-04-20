package com.mprimavera.pearform.model.fields;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mprimavera.pearform.R;
import com.mprimavera.pearform.contracts.IValidator;
import com.mprimavera.pearform.model.FieldWidget;

import java.util.FormatFlagsConversionMismatchException;

public class TextInfo extends FieldWidget {
    private LinearLayout mLayout;
    private TextView mLabel, mText;

    public TextInfo(Context context) {
        super(context);
        init(context);
    }

    public TextInfo(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextInfo(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public TextInfo(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context) {
        mContext = context;
        inflate(context, R.layout.form_text_info_field, this);
        mLayout = findViewById(R.id.layout);
        mLabel = mLayout.findViewById(R.id.label);
        mText = mLayout.findViewById(R.id.text);
    }

    public TextInfo resultKey(String key) {
        mResultKey = key;
        return this;
    }

    public void setText(String text) {
        mText.setText(text);
    }

    public void setLabel(String label) {
        if (label != null) {
            mLabel.setText(label);
            mLabel.setVisibility(VISIBLE);
        } else {
            mLabel.setVisibility(GONE);
        }
    }

    public TextInfo text(String text) {
        setText(text);
        return this;
    }

    public TextInfo label(String label) {
        setLabel(label);
        return this;
    }

    public TextInfo labelSize(float size) {
        mLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        return this;
    }

    public TextInfo textSize(float size) {
        mText.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        return this;
    }

    public TextInfo prefillWhen(boolean condition, Bundle bundle) {
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
