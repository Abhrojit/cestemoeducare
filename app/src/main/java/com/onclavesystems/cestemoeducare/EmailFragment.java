package com.onclavesystems.cestemoeducare;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
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

    private EditText et_subject, et_message;
    private String subject = "", message = "", to="abhro.mukherjee093@gmail.com";
    private boolean instanceSaved = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
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
            et_subject.setText(subject);
            et_message.setText(message);
        }

        return view;
    }


    private void registerETs(View view) {
        et_subject = (EditText)view.findViewById(R.id.editTextSubject);
        et_message = (EditText)view.findViewById(R.id.editTextMessage);

    }

    private void registerTextChangeListeners() {
        et_subject.addTextChangedListener(new CustomTextWatcher(et_subject));
        et_message.addTextChangedListener(new CustomTextWatcher(et_message));
    }

    private void registerButton(View view) {
        Button button = (Button)view.findViewById(R.id.buttonSend);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(final View view) {
        switch(view.getId()) {
            case R.id.buttonSend:
                FormValidation validate = new FormValidation();

                if(validate.checkFormData(getActivity().findViewById(R.id.content_frame))) {
                    if(isInternetOn()) {
                        sendByIntent();
                    }else {
                        Snackbar.make(getView(), "Error connecting to net", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(view, "There was a validation problem with a field", Snackbar.LENGTH_LONG).show();
                }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
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

            return (validateSubject(view) && validateMessage(view));
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

    public void sendByIntent(){
        Intent email = new Intent(Intent.ACTION_SEND);
        // prompts email clients only
        email.setType("message/rfc822");
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
        email.putExtra(Intent.EXTRA_SUBJECT, et_subject.getText().toString());
        email.putExtra(Intent.EXTRA_TEXT, et_message.getText().toString());

        try {
            // the user can choose the email client
            startActivity(Intent.createChooser(email, "Choose an email client from..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Snackbar.make(getView().findViewById(R.id.linearLayout1), "No email client installed", Snackbar.LENGTH_LONG).show();
        }finally {
            et_message.setText("");
            et_subject.setText("");
        }
    }


    private boolean isInternetOn() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;

        }
    }
}




