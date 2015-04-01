package nz.govt.doc.birdcount;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

public class DataUpload extends ActionBarActivity {
    private static final String username = "steven.li@slingshot.co.nz";
    private static final String password = "EassyPassw0rd#1";
    private EditText emailEdit;
    private EditText subjectEdit;
    private EditText messageEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_upload);

        emailEdit = (EditText) findViewById(R.id.emailAddress);
        subjectEdit = (EditText) findViewById(R.id.emailSubject);
        messageEdit = (EditText) findViewById(R.id.emailBody);
        Button sendButton = (Button) findViewById(R.id.emailSend);

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
        message.setFrom(new InternetAddress("stevem.li@slingshot.co.nz", "Steven Li"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email, email));
        message.setSubject(subject);
        message.setText(messageBody);
        
        MimeBodyPart messageBodyPart = new MimeBodyPart();

        Multipart multipart = new MimeMultipart();

        messageBodyPart = new MimeBodyPart();
        String filePath = ((BirdCountApp)this.getApplication()).getBirdCountDataFile();
        String fileName = "BirdCountData.csv";
        DataSource source = new FileDataSource(filePath);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(fileName);
        multipart.addBodyPart(messageBodyPart);

        message.setContent(multipart);
        return message;
    }

    private Session createSessionObject() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.slingshot.co.nz");
        properties.put("mail.smtp.port", "465"); //465 587, 25
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

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
            progressDialog = ProgressDialog.show(DataUpload.this, "Please wait", "Sending mail", true, false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
		@Override
		protected Void doInBackground(Message... messages) {
	           try {
	        	   System.out.println("Start sending..."); 
	        	   Transport.send(messages[0]);
	        	   System.out.println("Done");
	            	//testSend(email, subject, messageBody);
	            } catch (Exception e) {
	            	System.out.println(e.getMessage());
	                e.printStackTrace();
	            }
	            return null;
		}
    }
    
}
