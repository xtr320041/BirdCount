package nz.govt.doc.birdcount;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class TestMail extends Activity {
    private static final String username = "doctest.nz@gmail.com";
    private static final String password = "doctest.nz#";
    private EditText emailEdit;
    private EditText subjectEdit;
    private EditText messageEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_mail);

        emailEdit = (EditText) findViewById(R.id.email);
        subjectEdit = (EditText) findViewById(R.id.subject);
        messageEdit = (EditText) findViewById(R.id.message);
        Button sendButton = (Button) findViewById(R.id.send);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEdit.getText().toString();
                String subject = subjectEdit.getText().toString();
                String message = messageEdit.getText().toString();
                sendMail(email, subject, message);
            }
        });
    }

    private void sendMail(String email, String subject, String messageBody) {
        Session session = createSessionObject();

        try {
            Message message = createMessage(email, subject, messageBody, session);
            new SendMailTask().execute(message);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch(Exception e){
        	e.printStackTrace();
        }
    }

    private Message createMessage(String email, String subject, String messageBody, Session session) throws MessagingException, UnsupportedEncodingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("tutorials@tiemenschut.com", "Tiemen Schut"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email, email));
        message.setSubject(subject);
        message.setText(messageBody);
        return message;
    }

    private Session createSessionObject() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465"); //587, 25

        return Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    private class SendMailTask extends AsyncTask<Message, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(TestMail.this, "Please wait", "Sending mail", true, false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Message... messages) {
            try {
                //Transport.send(messages[0]);
            	testSend();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    
    public void testSend(){
    	Properties props = new Properties();
    	props.put("mail.smtp.auth", "true");
    	props.put("mail.smtp.starttls.enable", "true");
    	props.put("mail.smtp.host", "smtp.gmail.com");
    	props.put("mail.smtp.port", "465"); //587, 25
    	props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	    //props.put("mail.smtp.socketFactory.fallback", "false");    	

    	Session session = Session.getInstance(props,
    	  new javax.mail.Authenticator() {
    	    protected PasswordAuthentication getPasswordAuthentication() {
    	        return new PasswordAuthentication(username, password);
    	    }
    	  });
    	    try {
    	        Message message = new MimeMessage(session);
    	        message.setFrom(new InternetAddress("doctest.nz@gmail.com"));
    	        message.setRecipients(Message.RecipientType.TO,
    	            InternetAddress.parse("xtr320041@gmail.com"));
    	        message.setSubject("Testing Subject");
    	        message.setText("Dear Mail Crawler,"
    	            + "\n\n No spam to my email, please!");

    	        MimeBodyPart messageBodyPart = new MimeBodyPart();

    	        Multipart multipart = new MimeMultipart();

    	        messageBodyPart = new MimeBodyPart();
    	        String file = "path of file to be attached";
    	        String fileName = ((BirdCountApp)this.getApplication()).getBirdCountDataFile();
    	        DataSource source = new FileDataSource(file);
    	        messageBodyPart.setDataHandler(new DataHandler(source));
    	        messageBodyPart.setFileName(fileName);
    	        multipart.addBodyPart(messageBodyPart);

    	        message.setContent(multipart);

    	        Transport.send(message);

    	        System.out.println("Done");

    	    } catch (MessagingException e) {
    	        throw new RuntimeException(e);
    	    }    	
    }
}
