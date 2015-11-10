package com.onclavesystems.cestemoeducare;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 */
public class ContactUsFragment extends Fragment implements View.OnClickListener {

    private EditText et_name, et_phoneno, et_address, et_query, et_hear;
    private String name = "", phone = "", address = "", query = "", hear = "";
    private boolean instanceSaved = false;

    public ContactUsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            name = savedInstanceState.getString("sv_name");
            phone = savedInstanceState.getString("sv_phone");
            address = savedInstanceState.getString("sv_address");
            query = savedInstanceState.getString("sv_query");
            hear = savedInstanceState.getString("sv_hear");
            instanceSaved = savedInstanceState.getBoolean("InstanceSaved");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);

        registerETs(view);
        registerTextChangeListeners();
        registerButton(view);

        if(instanceSaved) {
            et_name.setText(name);
            et_phoneno.setText(phone);
            et_address.setText(address);
            et_query.setText(query);
            et_hear.setText(hear);
        }

        return view;
    }

    private void registerETs(View view) {
        et_name = (EditText)view.findViewById(R.id.input_name);
        et_phoneno = (EditText)view.findViewById(R.id.input_phoneno);
        et_address = (EditText)view.findViewById(R.id.input_address);
        et_query = (EditText)view.findViewById(R.id.input_query);
        et_hear = (EditText)view.findViewById(R.id.input_hear);
    }

    private void registerTextChangeListeners() {
        et_name.addTextChangedListener(new CustomTextWatcher(et_name));
        et_phoneno.addTextChangedListener(new CustomTextWatcher(et_phoneno));
        et_address.addTextChangedListener(new CustomTextWatcher(et_address));
        et_query.addTextChangedListener(new CustomTextWatcher(et_query));
        et_hear.addTextChangedListener(new CustomTextWatcher(et_hear));
    }

    private void registerButton(View view) {
        Button button = (Button)view.findViewById(R.id.btn_submitQuery);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_submitQuery:
                FormValidation validate = new FormValidation();

                if(validate.checkFormData(getActivity().findViewById(R.id.content_frame))) {
                    Snackbar.make(view, "QUERY SUBMITTED", Snackbar.LENGTH_LONG).show();
                }
                else {
                    Snackbar.make(view, "There was a validation problem with a field", Snackbar.LENGTH_LONG).show();
                }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("sv_name", et_name.getText().toString());
        savedInstanceState.putString("sv_phone", et_phoneno.getText().toString());
        savedInstanceState.putString("sv_address", et_address.getText().toString());
        savedInstanceState.putString("sv_query", et_query.getText().toString());
        savedInstanceState.putString("sv_hear", et_hear.getText().toString());
        savedInstanceState.putBoolean("InstanceSaved", true);
    }

    private class CustomTextWatcher implements TextWatcher {
        private View view;
        private FormValidation validation = new FormValidation();

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
                    validation.validateName(view);
                    break;
                case R.id.input_phoneno:
                    validation.validatePhone(view);
                    break;
                case R.id.input_address:
                    validation.validateAddress(view);
                    break;
                case R.id.input_query:
                    validation.validateQuery(view);
                    break;
                case R.id.input_hear:
                    validation.validateHear(view);
                    break;
            }
        }
    }

    private class FormValidation {

        private boolean checkFormData(View view) {

            return (validateName(view) && validatePhone(view) && validateAddress(view) && validateQuery(view) && validateHear(view));
        }

        private boolean validateName(View view) {
            EditText nameText = (EditText)view.findViewById(R.id.input_name);

            if(TextUtils.isEmpty((nameText).getText().toString().trim())) {
                (nameText).setError(getString(R.string.err_msg_name));

                return false;
            }
            else {
                (nameText).setError(null);

                return true;
            }
        }

        private boolean validatePhone(View view) {
            EditText phoneText = (EditText)view.findViewById(R.id.input_phoneno);
            String expression = "^[1-9][0-9]{9}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher((phoneText).getText().toString().trim());

            if(TextUtils.isEmpty((phoneText).getText().toString().trim())) {
                (phoneText).setError(getString(R.string.err_msg_phone));

                return false;
            }
            if (!matcher.matches()) {
                (phoneText).setError(getString(R.string.err_msg_phone1));

                return false;
            }

            (phoneText).setError(null);
            return true;

        }

        private boolean validateAddress(View view) {
            EditText addressText = (EditText)view.findViewById(R.id.input_address);

            if(TextUtils.isEmpty((addressText).getText().toString().trim())) {
                (addressText).setError(getString(R.string.err_msg_address));

                return false;
            }
            else {
                (addressText).setError(null);

                return true;
            }
        }

        private boolean validateQuery(View view) {
            EditText queryText = (EditText)view.findViewById(R.id.input_query);

            if(TextUtils.isEmpty((queryText).getText().toString().trim())) {
                (queryText).setError(getString(R.string.err_msg_query));

                return false;
            }
            else {
                (queryText).setError(null);

                return true;
            }
        }

        private boolean validateHear(View view) {
            EditText hearText = (EditText)view.findViewById(R.id.input_hear);

            if(TextUtils.isEmpty((hearText).getText().toString().trim())) {
                (hearText).setError(getString(R.string.err_msg_hear));

                return false;
            }
            else {
                (hearText).setError(null);

                return true;
            }
        }
    }
}
