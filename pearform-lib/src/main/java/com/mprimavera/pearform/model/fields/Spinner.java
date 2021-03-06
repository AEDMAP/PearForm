package com.mprimavera.pearform.model.fields;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import com.mprimavera.pearform.contracts.IValidator;
import com.mprimavera.pearform.R;
import com.mprimavera.pearform.model.FieldWidget;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import java.io.Serializable;
import java.util.HashMap;

public class Spinner<T extends Serializable> extends FieldWidget {
    protected MaterialBetterSpinner mSpinner;
    protected HashMap<Integer, T> mElements;
    protected String[] mLabels;
    private IFieldValidator mValidator;
    private SpinnerListener mListener;
    protected boolean mItemSelected;
    protected int mSelectedIndex;
    private boolean mMandatory;
    private String mError;

    public interface SpinnerListener<T> {
        void onItemSelected(T item);
    }

    public Spinner(Context context) {
        super(context);
        this.init();
    }

    public Spinner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public Spinner(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    public Spinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init();
    }

    public void init() {
        inflate(getContext(), R.layout.form_input_spinner_field, this);
        mElements = new HashMap<>();
        mItemSelected = false;
        mListener = null;
        mLabels = null;
        mResultKey = null;
        mMandatory = false;
        mSpinner = findViewById(R.id.materialSpinner);
    }

    public Spinner hintColor(int color) {
        mSpinner.setHintTextColor(color); return this;
    }

    public Spinner hint(int hint) {
        mSpinner.setHint(hint);
        return this;
    }

    public Spinner hints(String hint) {
        this.hint(hint);
        this.floatingHint(hint);
        return this;
    }

    public Spinner hint(String hint) {
        mSpinner.setHint(hint);
        return this;
    }

    public Spinner floatingHint(String hint) {
        mSpinner.setFloatingLabelText(hint);
        return this;
    }

    public Spinner floatingHint(String hint, int color) {
        mSpinner.setFloatingLabelText(hint);
        mSpinner.setFloatingLabelTextColor(color);
        return this;
    }

    public Spinner validateWith(IValidator validator) {
        mValidator = (IFieldValidator) validator;
        return this;
    }

    public Spinner resultKey(String key) {
        mResultKey = key;
        return this;
    }

    public Spinner elements(String[] labels, Object[] elements) {
        if(labels.length != elements.length) return null;

        for(int i = 0; i < elements.length; i++)
            mElements.put(i, (T) elements[i]);

        mLabels = labels;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, labels);
        mSpinner.setAdapter(arrayAdapter);
        return this;
    }

    public Spinner listener(SpinnerListener listener) {
        mListener = listener;
        this.setupListener();
        return this;
    }

    public Spinner build() {
        this.setupListener();
        return this;
    }

    private void setupListener() {
        mSpinner.setOnItemClickListener((parent, view, position, id) -> {
            mItemSelected = true;
            mSelectedIndex = position;
            if (mListener != null)
                mListener.onItemSelected(mElements.get(position));
        });
    }

    public Spinner mandatory(boolean mandatory) {
        mMandatory = mandatory;
        return this;
    }

    @Override public boolean validate() {
        if(mItemSelected || !mMandatory) return true; // Requires selection by default
        else{
            if(mError != null)
                showError(mError);
            return false;
        }
    }

    @Override
    public Bundle getValue() {
        Bundle bundle = new Bundle();

        bundle.remove(mResultKey);
        bundle.putSerializable(mResultKey, mElements.get(mSelectedIndex));
        return bundle;
    }

    public Spinner prefillWhen(boolean condition, Bundle bundle) {
        if(condition) this.prefill(bundle);
        return this;
    }

    @Override
    public void prefill(Bundle bundle) {
        if(mResultKey != null && bundle != null) {
            int value = bundle.getInt(mResultKey); // TODO restore this line, remove two above
          //  if(value != 0) {
                if(value >= 0 && value <= (mLabels.length - 1)) {
                    mSpinner.setText(mLabels[value ]); // IDs start from 1, arrays from 0
                    mSelectedIndex = value ;
                    mItemSelected = true;
                }
         //   }
        }
    }

    protected void setText(String text){
        mSpinner.setText(text);
    }

    @Override public void disable() {
        mSpinner.setFocusable(false);
        mSpinner.setOnTouchListener((view, motionEvent) -> true);
    }

    @Override public void enable() {
        mSpinner.setActivated(true);
        mSpinner.setEnabled(true);
        mSpinner.setClickable(true);
        mSpinner.setFocusable(true);
        mSpinner.setOnTouchListener((view, motionEvent) -> false);
    }

    @Override
    public void reset() {
        mItemSelected = false;
        mSpinner.setText(null);
    }

    public Spinner error(String error) {
        mError = error;
        return this;
    }

    public void showError(String error) {
        mSpinner.setError(error);
    }

    @Override
    public void setValidator(IValidator validator) {
        mValidator = (IFieldValidator) validator;
    }

    public static interface IFieldValidator extends IValidator {}
}
