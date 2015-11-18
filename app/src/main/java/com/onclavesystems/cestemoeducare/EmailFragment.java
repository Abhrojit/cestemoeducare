package com.onclavesystems.cestemoeducare;


import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmailFragment extends Fragment implements View.OnClickListener{

    private EditText et_from, et_subject, et_message;
    private String from = "", subject = "", message = "", to="abhrojit.mukherjee1994@gmail.com";
    private boolean instanceSaved = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            from = savedInstanceState.getString("sv_from");
            subject = savedInstanceState.getString("sv_subject");
            message = savedInstanceState.getString("sv_message");
            instanceSaved = savedInstanceState.getBoolean("InstanceSaved");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_email, container, false);

        registerETs(view);
        registerTextChangeListeners();
        registerButton(view);

        if(instanceSaved) {
            et_from.setText(from);
            et_subject.setText(subject);
            et_message.setText(message);
        }

        return view;
    }


    private void registerETs(View view) {
        et_from = (EditText)view.findViewById(R.id.editTextFrom);
        et_subject = (EditText)view.findViewById(R.id.editTextSubject);
        et_message = (EditText)view.findViewById(R.id.editTextMessage);

    }

    private void registerTextChangeListeners() {
        et_from.addTextChangedListener(new CustomTextWatcher(et_from));
        et_subject.addTextChangedListener(new CustomTextWatcher(et_subject));
        et_message.addTextChangedListener(new CustomTextWatcher(et_message));
    }

    private void registerButton(View view) {
        Button button = (Button)view.findViewById(R.id.buttonSend);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.buttonSend:
                FormValidation validate = new FormValidation();

                if(validate.checkFormData(getActivity().findViewById(R.id.content_frame))) {
                    Snackbar.make(view, "Query Submitted", Snackbar.LENGTH_LONG).show();
                }
                else {
                    Snackbar.make(view, "There was a validation problem with a field", Snackbar.LENGTH_LONG).show();
                }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("sv_from", et_from.getText().toString());
        savedInstanceState.putString("sv_subject", et_subject.getText().toString());
        savedInstanceState.putString("sv_message", et_message.getText().toString());
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
                case R.id.editTextFrom:
                    validation.validateEmailid(view);
                    break;
                case R.id.editTextSubject:
                    validation.validateSubject(view);
                    break;
                case R.id.editTextMessage:
                    validation.validateMessage(view);
                    break;
            }
        }
    }

    private class FormValidation {

        private boolean checkFormData(View view) {

            return (validateEmailid(view) && validateSubject(view) && validateMessage(view));
        }

        private boolean validateEmailid(View view) {
            EditText mailid = (EditText) view.findViewById(R.id.editTextFrom);

            if (TextUtils.isEmpty((mailid).getText().toString().trim())) {
                (mailid).setError(getString(R.string.err_msg_mailid));
                return false;
            }else {
                String expression = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
                    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher((mailid).getText().toString().trim());

                    if(!matcher.matches()) {
                        (mailid).setError(getString(R.string.err_invalid_mail));
                        return false;
                    }
                    else {
                        (mailid).setError(null);
                        return true;
                    }
            }
        }

        private boolean validateSubject(View view) {
            EditText subject = (EditText) view.findViewById(R.id.editTextSubject);

            if (TextUtils.isEmpty((subject).getText().toString().trim())) {
                (subject).setError(getString(R.string.err_msg_subject));

                return false;
            } else {
                (subject).setError(null);

                return true;
            }
        }

        private boolean validateMessage(View view) {
            EditText message = (EditText) view.findViewById(R.id.editTextMessage);

            if (TextUtils.isEmpty((message).getText().toString().trim())) {
                (message).setError(getString(R.string.err_msg_message));

                return false;
            } else {
                (message).setError(null);

                return true;
            }
        }
    }
}



