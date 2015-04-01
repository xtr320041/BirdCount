package nz.govt.doc.birdcount;


//ending Emails without User Intervention (no Intents) in Android
//
//The Android SDK makes it very easy to send emails from an application, but unfortunately, that's only if you want to send them via the built-in mailing app. For most situations this works fine, but if you want to send something out and don't want any input/intervention from the user, it's not as easy.
//
//In this article I'm going to show you how to send an email in the background without the user even knowing - the application will do everything behind the scenes.
//
//Before we begin, you'll need to download a few files via the link below - this is a special version of the JavaMail API, which was written specifically for Android.
//
//http://code.google.com/p/javamail-android/downloads/list
//
//I'll be walking you through a Mail wrapper that I wrote, which makes it much easier to send emails and even add attachments if that's something you'd like to do.
//
//Here is the full wrapper class below, which I'll go through step by step - keeping in mind that you'll have to add the fore-said files if you want this to work. Add them as external libraries - they need to be accessible by the Mail class.

import java.util.Date; 
import java.util.Properties; 
import javax.activation.CommandMap; 
import javax.activation.DataHandler; 
import javax.activation.DataSource; 
import javax.activation.FileDataSource; 
import javax.activation.MailcapCommandMap; 
import javax.mail.BodyPart; 
import javax.mail.Message;
import javax.mail.Multipart; 
import javax.mail.PasswordAuthentication; 
import javax.mail.Authenticator;
import javax.mail.Session; 
import javax.mail.Transport; 
import javax.mail.internet.InternetAddress; 
import javax.mail.internet.MimeBodyPart; 
import javax.mail.internet.MimeMessage; 
import javax.mail.internet.MimeMultipart; 
 
 
public class Mail extends javax.mail.Authenticator { 
  private String _user; 
  private String _pass; 
 
  private String[] _to; 
  private String _from; 
 
  private String _port; 
  private String _sport; 
 
  private String _host; 
 
  private String _subject; 
  private String _body; 
 
  private boolean _auth; 
   
  private boolean _debuggable; 
 
  private Multipart _multipart; 
  
  public Mail() { 
    _host = "smtp.gmail.com"; // default smtp server 
    _port = "465"; // default smtp port 
    _sport = "465"; // default socketfactory port 
 
    _user = "doctest.nz"; // username 
    _pass = "doctest.nz#"; // password 
    _from = "doctest.nz@gmail.com"; // email sent from 
    _subject = "test"; // email subject 
    _body = "test"; // email body 
 
    _debuggable = false; // debug mode on or off - default off 
    _auth = true; // smtp authentication - default on 
 
    _multipart = new MimeMultipart(); 
 
    // There is something wrong with MailCap, javamail can not find a handler for the multipart/mixed part, so this bit needs to be added. 
    MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap(); 
    mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html"); 
    mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml"); 
    mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain"); 
    mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed"); 
    mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822"); 
    CommandMap.setDefaultCommandMap(mc); 
  } 
 
  public Mail(String user, String pass) { 
    this(); 
 
    _user = user; 
    _pass = pass; 
  } 
 
  public boolean send() throws Exception { 
    Properties props = _setProperties(); 
 
    if(!_user.equals("") && !_pass.equals("") && _to.length > 0 && !_from.equals("") && !_subject.equals("") && !_body.equals("")) { 
      Session session = Session.getInstance(props, this); 
 
      MimeMessage msg = new MimeMessage(session); 
 
      msg.setFrom(new InternetAddress(_from)); 
       
      InternetAddress[] addressTo = new InternetAddress[_to.length]; 
      for (int i = 0; i < _to.length; i++) { 
        addressTo[i] = new InternetAddress(_to[i]); 
      } 
        msg.setRecipients(MimeMessage.RecipientType.TO, addressTo); 
 
      msg.setSubject(_subject); 
      msg.setSentDate(new Date()); 
 
      // setup message body 
      BodyPart messageBodyPart = new MimeBodyPart(); 
      messageBodyPart.setText(_body); 
      _multipart.addBodyPart(messageBodyPart); 
 
      // Put parts in message 
      msg.setContent(_multipart); 
 
      // send email 
      Transport.send(msg); 
 
      return true; 
    } else { 
      return false; 
    } 
  } 
 
  public void Send0(){
	  String  d_email = "doctest.nz@gmail.com",
	            d_uname = "doctest.nz",
	            d_password = "doctest.nz#",
	            d_host = "smtp.gmail.com",
	            d_port  = "465",
	            m_to = "sli@doc.govt.nz",
	            m_subject = "Indoors Readable File",
	            m_text = "This message is from Indoor Positioning App. Required file(s) are attached.";
	    Properties props = new Properties();
	    props.put("mail.smtp.user", d_email);
	    props.put("mail.smtp.host", d_host);
	    props.put("mail.smtp.port", d_port);
	    props.put("mail.smtp.starttls.enable","true");
	    props.put("mail.smtp.debug", "true");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.socketFactory.port", d_port);
	    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	    props.put("mail.smtp.socketFactory.fallback", "false");

	    SMTPAuthenticator auth = new SMTPAuthenticator();
	    Session session = Session.getInstance(props, auth);
	    //session.setDebug(true);

	    MimeMessage msg = new MimeMessage(session);
	    try {
	        msg.setSubject(m_subject);
	        msg.setFrom(new InternetAddress(d_email));
	        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(m_to));

	Transport transport = session.getTransport("smtps");
	            transport.connect(d_host, Integer.valueOf(d_port), d_uname, d_password);
	            transport.sendMessage(msg, msg.getAllRecipients());
	            transport.close();

	        } catch (Exception e) {
	            e.printStackTrace();
	            //return false;
	        } 
  }
  
  private class SMTPAuthenticator extends javax.mail.Authenticator {
      public PasswordAuthentication getPasswordAuthentication() {
         String username = "doctest.nz";
         String password = "doctest.nz#";
         return new PasswordAuthentication(username, password);
      }
  }  
  
  public void addAttachment(String filename) throws Exception { 
    BodyPart messageBodyPart = new MimeBodyPart(); 
    DataSource source = new FileDataSource(filename); 
    messageBodyPart.setDataHandler(new DataHandler(source)); 
    messageBodyPart.setFileName(filename); 
 
    _multipart.addBodyPart(messageBodyPart); 
  } 
 
  @Override 
  public PasswordAuthentication getPasswordAuthentication() { 
    return new PasswordAuthentication(_user, _pass); 
  } 
 
  private Properties _setProperties() { 
    Properties props = new Properties(); 
 
    props.put("mail.smtp.host", _host); 
 
    if(_debuggable) { 
      props.put("mail.debug", "true"); 
    } 
 
    if(_auth) { 
      props.put("mail.smtp.auth", "true"); 
    } 
 
    props.put("mail.smtp.port", _port); 
    props.put("mail.smtp.socketFactory.port", _sport); 
    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); 
    props.put("mail.smtp.socketFactory.fallback", "false"); 
 
    return props; 
  } 
 
  // the getters and setters 
  public String getBody() { 
    return _body; 
  } 
 
  public void setBody(String _body) { 
    this._body = _body; 
  } 
  public String[] getTo() {
	return _to;
  }

public void setTo(String[] _to) {
	this._to = _to;
	}  
 
  // more of the getters and setters ….. 
} 

//And now I'm going to go through each bit of code

//public Mail() { 
//  _host = "smtp.gmail.com"; // default smtp server 
//  _port = "465"; // default smtp port 
//  _sport = "465"; // default socketfactory port 
// 
//  _user = ""; // username 
//  _pass = ""; // password 
//  _from = ""; // email sent from 
//  _subject = ""; // email subject 
//  _body = ""; // email body 
// 
//  _debuggable = false; // debug mode on or off - default off 
//  _auth = true; // smtp authentication - default on 
// 
//  _multipart = new MimeMultipart(); 
// 
//  // There is something wrong with MailCap, javamail can not find a handler for the multipart/mixed part, so this bit needs to be added. 
//  MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap(); 
//  mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html"); 
//  mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml"); 
//  mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain"); 
//  mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed"); 
//  mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822"); 
//  CommandMap.setDefaultCommandMap(mc); 
//} 
// 
//public Mail(String user, String pass) { 
//  this(); 
// 
//  _user = user; 
//  _pass = pass; 
//} 

//In this piece of code we initialise the properties, and setup the default values.
//
//Also, we're setting up the mime types for javamail. I've also added a comment which describes why we need this.
//
//And you've probably noticed that there are 2 constructors - one overrides the other, just incase the you want to pass the username and password when instantiating the class.

//public boolean send() throws Exception { 
//  Properties props = _setProperties(); 
// 
//  if(!_user.equals("") && !_pass.equals("") && _to.length > 0 && !_from.equals("") && !_subject.equals("") && !_body.equals("")) { 
//    Session session = Session.getInstance(props, this); 
// 
//    MimeMessage msg = new MimeMessage(session); 
// 
//    msg.setFrom(new InternetAddress(_from)); 
//       
//    InternetAddress[] addressTo = new InternetAddress[_to.length]; 
//    for (int i = 0; i < _to.length; i++) { 
//      addressTo[i] = new InternetAddress(_to[i]); 
//    } 
//    msg.setRecipients(MimeMessage.RecipientType.TO, addressTo); 
// 
//    msg.setSubject(_subject); 
//    msg.setSentDate(new Date()); 
// 
//    // setup message body 
//    BodyPart messageBodyPart = new MimeBodyPart(); 
//    messageBodyPart.setText(_body); 
//    _multipart.addBodyPart(messageBodyPart); 
// 
//    // Put parts in message 
//    msg.setContent(_multipart); 
// 
//    // send email 
//    Transport.send(msg); 
// 
//    return true; 
//  } else { 
//    return false; 
//  } 
//} 

//This is the most important method - here we're putting all the data from the properties and sending the mail.

//public void addAttachment(String filename) throws Exception { 
//  BodyPart messageBodyPart = new MimeBodyPart(); 
//  DataSource source = new FileDataSource(filename); 
//  messageBodyPart.setDataHandler(new DataHandler(source)); 
//  messageBodyPart.setFileName(filename); 
// 
//  _multipart.addBodyPart(messageBodyPart); 
//} 

//You can call this method at any time if you want to add an attachment, but make sure you call it before the send method.

//private Properties _setProperties() { 
//  Properties props = new Properties(); 
// 
//  props.put("mail.smtp.host", _host); 
// 
//  if(_debuggable) { 
//    props.put("mail.debug", "true"); 
//  } 
// 
//  if(_auth) { 
//    props.put("mail.smtp.auth", "true"); 
//  } 
// 
//  props.put("mail.smtp.port", _port); 
//  props.put("mail.smtp.socketFactory.port", _sport); 
//  props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); 
//  props.put("mail.smtp.socketFactory.fallback", "false"); 
// 
//  return props; 
//} 

//Here we're setting up the properties for the mail retrieval - defaulting to SMTP authentication.
//
//Also keep in mind that this is all defaulted to connect to the Gmail (Google) SMTP server.
//
//Below is an example of how to use the Mail wrapper, in an Android activity.

//@Override 
//public void onCreate(Bundle icicle) { 
//  super.onCreate(icicle); 
//  setContentView(R.layout.main); 
// 
//  Button addImage = (Button) findViewById(R.id.send_email); 
//  addImage.setOnClickListener(new View.OnClickListener() { 
//    public void onClick(View view) { 
//      Mail m = new Mail("gmailusername@gmail.com", "password"); 
// 
//      String[] toArr = {"bla@bla.com", "lala@lala.com"}; 
//      m.setTo(toArr); 
//      m.setFrom("wooo@wooo.com"); 
//      m.setSubject("This is an email sent using my Mail JavaMail wrapper from an Android device."); 
//      m.setBody("Email body."); 
// 
//      try { 
//        m.addAttachment("/sdcard/filelocation"); 
// 
//        if(m.send()) { 
//          Toast.makeText(MailApp.this, "Email was sent successfully.", Toast.LENGTH_LONG).show(); 
//        } else { 
//          Toast.makeText(MailApp.this, "Email was not sent.", Toast.LENGTH_LONG).show(); 
//        } 
//      } catch(Exception e) { 
//        //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show(); 
//        Log.e("MailApp", "Could not send email", e); 
//      } 
//    } 
//  }); 
//}