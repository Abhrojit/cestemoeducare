package com.onclavesystems.cestemoeducare;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmailFragment extends Fragment implements View.OnClickListener{

    private EditText et_from, et_subject, et_message;
    private String from = "", subject = "", message = "", to="", password = "";
    private boolean instanceSaved = false, result = false;

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
    public void onClick(final View view) {
        switch(view.getId()) {
            case R.id.buttonSend:
                FormValidation validate = new FormValidation();

                if(validate.checkFormData(getActivity().findViewById(R.id.content_frame))) {
                    if(isInternetOn()) {
                        checkSend();
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


    private class SendMailTask extends AsyncTask<Message, Void, Void> {
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "Please wait", "Sending mail", true, false);
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

        }
        @Override
        protected Void doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
                result = true;
            } catch (MessagingException e) {
                result = false;
                Log.e("SendMail", e.getMessage(), e);
            }
            finally {
                if(result)
                    showSnackbar();
                else
                    sendByIntent();
            }
            return null;
        }
    }

    public void checkSend(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        alertDialog.setTitle("Choose");

        alertDialog.setMessage("Do You want to send your mail via intent?");

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sendByIntent();
                    }
                });
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getPassword();
                    }
                });

        alertDialog.show();
    }

    public void getPassword(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        alertDialog.setTitle("PASSWORD");

        alertDialog.setMessage("Enter your gmail Password");
        final EditText input = new EditText(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setHint("Your gmail password");
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setLayoutParams(lp);

        alertDialog.setView(input);


        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        password = input.getText().toString();
                        SendMail sendmail = new SendMail(to, et_from.getText().toString(), et_subject.getText().toString(), et_message.getText().toString(), password);
                        new SendMailTask().execute(sendmail.createSession());
                    }
                });
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
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
            et_from.setText("");
        }
    }

    public void showSnackbar(){
        Snackbar.make(getView().findViewById(R.id.linearLayout1), "Mail sent successfully", Snackbar.LENGTH_LONG).show();
        et_message.setText("");
        et_subject.setText("");
        et_from.setText("");
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




