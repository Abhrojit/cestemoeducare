package com.onclavesystems.cestemoeducare;

/**
 * Created by Abhrojit on 11/19/2015.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail extends Activity {
    private String from = "", subject = "", message = "", to="", password="";
    Message msg;
    public SendMail(String to, String from, String subject, String message, String password){
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.message = message;
        this.password = password;

    }

    public Message createSession() {
        Session session = createSessionObject();

        try {
            msg = createMessage(session);

        } catch (AddressException e) {
            Log.e("AddressException", e.getMessage(), e);
        } catch (MessagingException e) {
            Log.e("MessagingException", e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            Log.e("UnsupportedException", e.getMessage(), e);
        }
        return msg;
    }

    private Message createMessage(Session session) throws MessagingException, UnsupportedEncodingException {
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from, from));
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to, to));
        msg.setSubject(subject);
        msg.setText(message);
        return msg;
    }

    private Session createSessionObject() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        return Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });
    }

}
