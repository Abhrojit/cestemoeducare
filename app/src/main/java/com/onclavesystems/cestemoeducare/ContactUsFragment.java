package com.onclavesystems.cestemoeducare;

import android.app.Activity;
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
import android.support.design.widget.TextInputLayout;
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
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;


/**
 *
 */
public class ContactUsFragment extends Fragment implements View.OnClickListener {

    private EditText et_name, et_phoneno, et_address, et_from, et_query, et_hear;
    private String name = "", phone = "", address = "", from = "", query = "", hear = "", subject = "Contact Us details", message = "", to="", password = "";
    private boolean instanceSaved = false, result = false;

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
            from = savedInstanceState.getString("sv_from");
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
            et_from.setText(from);
            et_query.setText(query);
            et_hear.setText(hear);
        }

        return view;
    }

    private void registerETs(View view) {
        et_name = (EditText)view.findViewById(R.id.input_name);
        et_phoneno = (EditText)view.findViewById(R.id.input_phoneno);
        et_address = (EditText)view.findViewById(R.id.input_address);
        et_from = (EditText)view.findViewById(R.id.input_mailid);
        et_query = (EditText)view.findViewById(R.id.input_query);
        et_hear = (EditText)view.findViewById(R.id.input_hear);
    }

    private void registerTextChangeListeners() {
        et_name.addTextChangedListener(new CustomTextWatcher(et_name));
        et_phoneno.addTextChangedListener(new CustomTextWatcher(et_phoneno));
        et_address.addTextChangedListener(new CustomTextWatcher(et_address));
        et_from.addTextChangedListener(new CustomTextWatcher(et_from));
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
                    message = ("Name:" + et_name.getText().toString() +
                            "\nAddress:" + et_address.getText().toString() +
                            "\nPhone:" + et_phoneno.getText().toString() +
                            "\nQuery:" + et_query.getText().toString() +
                            "\nWhere did you hear about us:" + et_hear.getText().toString()) ;
                    if(isInternetOn()) {
                        checkSend();
                    }else {
                        Snackbar.make(getView(), "Error connecting to net", Snackbar.LENGTH_SHORT).show();
                    }
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
        savedInstanceState.putString("sv_from", et_from.getText().toString());
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
                case R.id.input_mailid:
                    validation.validateEmailid(view);
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

            return (validateName(view) && validatePhone(view) && validateAddress(view) && validateEmailid(view) && validateQuery(view) && validateHear(view));
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

            if(TextUtils.isEmpty((phoneText).getText().toString().trim())) {
                (phoneText).setError(getString(R.string.err_msg_phone));

                return false;
            }
            else {
                String expression = "^[1-9][0-9]{9}$";
                Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher((phoneText).getText().toString().trim());

                if(!matcher.matches()) {
                    (phoneText).setError(getString(R.string.err_wrong_phone));

                    return false;
                }
                else {
                    (phoneText).setError(null);

                    return true;
                }
            }
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

        private boolean validateEmailid(View view) {
            EditText mailid = (EditText) view.findViewById(R.id.input_mailid);

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
                        SendMail sendmail = new SendMail(to, et_from.getText().toString(), subject, message, password);
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
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, message);
        setFieldNull();
        try {
            // the user can choose the email client
            startActivity(Intent.createChooser(email, "Choose an email client from..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Snackbar.make(getView().findViewById(R.id.content_frame), "No email client installed", Snackbar.LENGTH_LONG).show();
        }
    }

    public void showSnackbar(){
        Snackbar.make(getView().findViewById(R.id.content_frame), "Mail sent successfully", Snackbar.LENGTH_LONG).show();
        setFieldNull();

    }

    private boolean isInternetOn() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;

        }
    }

    private void setFieldNull(){
        et_name.setText("");
        et_address.setText("");
        et_phoneno.setText("");
        et_from.setText("");
        et_query.setText("");
        et_hear.setText("");
    }
}
