package com.mprimavera.pearform.model.fields;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;

import com.mprimavera.pearform.R;
import com.mprimavera.pearform.contracts.IValidator;
import com.mprimavera.pearform.model.FieldWidget;

public class RadioGroup extends FieldWidget {

    private android.widget.RadioGroup mRadioGroup;
    protected IFieldValidator mValidator;
    protected String mError;
    protected String[] mLabels;
    protected int[] mIds;


    public RadioGroup(Context context) {
        super(context);
        init();
    }

    public RadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RadioGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    @Override
    public Bundle getValue() {
        Bundle bundle = new Bundle();
        bundle.putInt(mResultKey, this.mRadioGroup.getCheckedRadioButtonId());
        return bundle;
    }

    public RadioGroup resultKey(String key) {
        this.setResultKey(key);
        return this;
    }

    private void init() {
        inflate(getContext(), R.layout.form_radiogroup_field, this);
        mRadioGroup = findViewById(R.id.radioGroup);
    }

    public RadioGroup addButton(String[] labels, int[] ids) {

        for (int i = 0; i < labels.length; i++) {
            android.widget.RadioButton radioButton = new android.widget.RadioButton(mContext);
            radioButton.setText(labels[i]);
            radioButton.setId(ids[i]);
            this.mRadioGroup.addView(radioButton);
        }
        mLabels = labels;
        mIds = ids;
        return this;
    }

    public RadioGroup validator(IValidator validator) {
        this.setValidator(validator);
        return this;
    }


    @Override
    public void enable() {
        this.mRadioGroup.setClickable(true);
    }

    @Override
    public void disable() {
        this.mRadioGroup.setClickable(false);
    }


    @Override
    public void prefill(Bundle bundle) {
        boolean enabled = bundle.getBoolean(mResultKey);
        this.mRadioGroup.setEnabled(enabled);
    }

    public void showError(String error) {
        mError = error;
       /* mRadioGroup.setErrorEnabled(true);
        mRadioGroup.setError(error);
        */
    }

    public void hideError() {
     /*   mRadioGroup.setErrorEnabled(false);
        mRadioGroup.setError(null);
        */
    }

    @Override
    public boolean validate() {
        if (mValidator != null) {
            boolean valid = mValidator.validate(mRadioGroup);
            if (!valid) {
                if (mError != null)
                    showError(mError);
            } else hideError();
            return valid;
        } else return true; // Default to NOT_REQUIRED
    }

    @Override
    public void reset() {
        this.mRadioGroup.setEnabled(false);
    }

    @Override
    public void setValidator(IValidator validator) {
        mValidator = (IFieldValidator) validator;
    }


    public interface IFieldValidator extends IValidator {
        boolean validate(android.widget.RadioGroup radioGroup);
    }
}