package com.mprimavera.pearform.model.fields;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import com.mprimavera.pearform.contracts.IValidator;
import com.mprimavera.pearform.R;
import com.mprimavera.pearform.model.FieldWidget;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import java.io.Serializable;
import java.util.HashMap;

public class Spinner<T extends Serializable> extends FieldWidget {
    private MaterialBetterSpinner mSpinner;
    private HashMap<Integer, T> mElements;
    private String[] mLabels;
    private IFieldValidator mValidator;
    private SpinnerListener mListener;
    private boolean mItemSelected;
    private int mSelectedIndex;
    private boolean mMandatory;

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
        mSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mItemSelected = true;
            mSelectedIndex = position;
            if (mListener != null) mListener.onItemSelected(mElements.get(position));
            }
        });
    }

    public Spinner mandatory(boolean mandatory) {
        mMandatory = mandatory;
        return this;
    }

    @Override public boolean validate() {
        if(mItemSelected || !mMandatory) return true; // Requires selection by default
        else return false;
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
            int value = bundle.getInt(mResultKey);
            if(value != 0) {
                if(value >= 0 && value <= (mLabels.length - 1)) {
                    mSpinner.setText(mLabels[value]);
                    mSelectedIndex = value;
                    mItemSelected = true;
                }
            }
        }
    }

    @Override public void disable() {
        mSpinner.setFocusable(false);
        mSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }

    @Override public void enable() {
        mSpinner.setActivated(true);
        mSpinner.setEnabled(true);
        mSpinner.setClickable(true);
        mSpinner.setFocusable(true);
        mSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
    }

    @Override
    public void reset() {
        mItemSelected = false;
        mSpinner.setText(null);
    }

    @Override
    public void setValidator(IValidator validator) {
        mValidator = (IFieldValidator) validator;
    }

    public static interface IFieldValidator extends IValidator {}
}
