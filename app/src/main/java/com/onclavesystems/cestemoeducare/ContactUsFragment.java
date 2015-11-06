package com.onclavesystems.cestemoeducare;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


/**
 *
 */
public class ContactUsFragment extends Fragment {

    private EditText et_name, et_phoneno, et_address, et_query, et_hear;
    private TextInputLayout til_name, til_phoneno, til_address, til_query, til_hear;

    public ContactUsFragment() {
        // Required empty public constructor
    }

    public ContactUsFragment getThisClassReference() {
        return ContactUsFragment.this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        registerTILs(view);
        registerETs(view);
        registerTextChangeListeners();

        return view;
    }

    public void registerTILs(View view) {
        til_name = (TextInputLayout)view.findViewById(R.id.input_layout_name);
        til_phoneno = (TextInputLayout)view.findViewById(R.id.input_layout_phoneno);
        til_address = (TextInputLayout)view.findViewById(R.id.input_layout_address);
        til_query = (TextInputLayout)view.findViewById(R.id.input_layout_query);
        til_hear = (TextInputLayout)view.findViewById(R.id.input_layout_hear);
    }

    public void registerETs(View view) {
        et_name = (EditText)view.findViewById(R.id.input_name);
        et_phoneno = (EditText)view.findViewById(R.id.input_phoneno);
        et_address = (EditText)view.findViewById(R.id.input_address);
        et_query = (EditText)view.findViewById(R.id.input_query);
        et_hear = (EditText)view.findViewById(R.id.input_hear);
    }

    public void registerTextChangeListeners() {
        et_name.addTextChangedListener(new CustomTextWatcher(et_name));
        et_phoneno.addTextChangedListener(new CustomTextWatcher(et_phoneno));
        et_address.addTextChangedListener(new CustomTextWatcher(et_address));
        et_query.addTextChangedListener(new CustomTextWatcher(et_query));
        et_hear.addTextChangedListener(new CustomTextWatcher(et_hear));
    }

    public static class CustomTextWatcher implements TextWatcher {
        public View view;
        public Context context;

        public CustomTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence cs, int i1, int i2, int i3) {

        }

        public void onTextChanged(CharSequence cs, int i1, int i2, int i3) {

        }

        public void afterTextChanged(Editable editable) {
            switch(view.getId()) {
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_phoneno:
                    validatePhone();
                    break;
                case R.id.input_address:
                    validateAddress();
                    break;
                case R.id.input_query:
                    validateQuery();
                    break;
                case R.id.input_hear:
                    validateHear();
                    break;
            }
        }

        public boolean checkFormData() {
            if(validateName() && validatePhone() && validateAddress() && validateQuery() && validateHear()) {
                return true;
            }
            else {
                return false;
            }
        }

        private boolean validateName() {
            if(((EditText)view.findViewById(R.id.input_name)).getText().toString().trim().isEmpty()) {
                ((TextInputLayout)view.findViewById(R.id.input_layout_name)).setError(context.getString(R.string.err_msg_name));
                return false;
            }
            else {
                return true;
            }
        }

        private boolean validatePhone() {
            if(((EditText)view.findViewById(R.id.input_phoneno)).getText().toString().trim().isEmpty()) {
                ((TextInputLayout)view.findViewById(R.id.input_layout_phoneno)).setError(context.getString(R.string.err_msg_phone));
                return false;
            }
            else {
                return true;
            }
        }

        private boolean validateAddress() {
            if(((EditText)view.findViewById(R.id.input_address)).getText().toString().trim().isEmpty()) {
                ((TextInputLayout)view.findViewById(R.id.input_layout_address)).setError(context.getString(R.string.err_msg_address));
                return false;
            }
            else {
                return true;
            }
        }

        private boolean validateQuery() {
            if(((EditText)view.findViewById(R.id.input_query)).getText().toString().trim().isEmpty()) {
                ((TextInputLayout)view.findViewById(R.id.input_layout_query)).setError(context.getString(R.string.err_msg_query));
                return false;
            }
            else {
                return true;
            }
        }

        private boolean validateHear() {
            if(((EditText)view.findViewById(R.id.input_hear)).getText().toString().trim().isEmpty()) {
                ((TextInputLayout)view.findViewById(R.id.input_layout_hear)).setError(context.getString(R.string.err_msg_hear));
                return false;
            }
            else {
                return true;
            }
        }
    }
}
