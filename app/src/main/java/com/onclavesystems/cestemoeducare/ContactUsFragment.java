package com.onclavesystems.cestemoeducare;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


/**
 *
 */
public class ContactUsFragment extends Fragment {

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
            Toast.makeText(getActivity().getBaseContext(), "Not null", Toast.LENGTH_LONG).show();
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

        if(savedInstanceState != null) {
            Toast.makeText(getActivity().getBaseContext(), "Not null", Toast.LENGTH_LONG).show();
            name = savedInstanceState.getString("sv_name");
            phone = savedInstanceState.getString("sv_phone");
            address = savedInstanceState.getString("sv_address");
            query = savedInstanceState.getString("sv_query");
            hear = savedInstanceState.getString("sv_hear");
            instanceSaved = savedInstanceState.getBoolean("InstanceSaved");
        }

        registerTextChangeListeners();

        if(instanceSaved) {
            Toast.makeText(getActivity().getBaseContext(), "Instance was saved", Toast.LENGTH_LONG).show();
            Toast.makeText(getActivity().getBaseContext(), name, Toast.LENGTH_LONG).show();
            Toast.makeText(getActivity().getBaseContext(), phone, Toast.LENGTH_LONG).show();
            et_name.setText(name);
            et_phoneno.setText(phone);
            et_address.setText(address);
            et_query.setText(query);
            et_hear.setText(hear);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Toast.makeText(getActivity().getBaseContext(), "saving", Toast.LENGTH_LONG).show();
        savedInstanceState.putString("sv_name", et_name.getText().toString());
        savedInstanceState.putString("sv_phone", et_phoneno.getText().toString());
        savedInstanceState.putString("sv_address", et_address.getText().toString());
        savedInstanceState.putString("sv_query", et_query.getText().toString());
        savedInstanceState.putString("sv_hear", et_hear.getText().toString());
        savedInstanceState.putBoolean("InstanceSaved", true);
    }

    private class CustomTextWatcher implements TextWatcher {
        private View view;

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

        private void validateName() {
            if(TextUtils.isEmpty(((EditText)view.findViewById(R.id.input_name)).getText().toString().trim())) {
                ((EditText)view.findViewById(R.id.input_name)).setError(getString(R.string.err_msg_name));
            }
        }

        private void validatePhone() {
            if(TextUtils.isEmpty(((EditText) view.findViewById(R.id.input_phoneno)).getText().toString().trim())) {
                ((EditText)view.findViewById(R.id.input_phoneno)).setError(getString(R.string.err_msg_phone));
            }
        }

        private void validateAddress() {
            if(TextUtils.isEmpty(((EditText) view.findViewById(R.id.input_address)).getText().toString().trim())) {
                ((EditText)view.findViewById(R.id.input_address)).setError(getString(R.string.err_msg_address));
            }
        }

        private void validateQuery() {
            if(TextUtils.isEmpty(((EditText) view.findViewById(R.id.input_query)).getText().toString().trim())) {
                ((EditText)view.findViewById(R.id.input_query)).setError(getString(R.string.err_msg_query));
            }
        }

        private void validateHear() {
            if(TextUtils.isEmpty(((EditText) view.findViewById(R.id.input_hear)).getText().toString().trim())) {
                ((EditText)view.findViewById(R.id.input_hear)).setError(getString(R.string.err_msg_hear));
            }
        }
    }
}
