//package com.rabidaudio.dev.tinderforgmail;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.Binder;
//import android.os.IBinder;
//
//import java.util.List;
//
//import javax.mail.MessagingException;
//
///**
// * Created by charles on 11/8/14.
// */
//public class MailService extends Service implements IMailbox {
//
//    public class LocalBinder extends Binder {
//        public MailService getService() {
//            // Return this instance of Service so clients can call public methods
//            return MailService.this;
//        }
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return mBinder;
//    }
//
//    @Override
//    public void onCreate(){
//        mailbox = new Mailbox(Mailbox.GMAIL_ALLMAIL);
//    }
//
//    private final IBinder mBinder = new LocalBinder();
//
//    private Mailbox mailbox = null;
//
//    @Override
//    public void connect() throws MessagingException {
//        mailbox.connect();
//    }
//
//    @Override
//    public void disconnect() throws MessagingException {
//        mailbox.disconnect();
//    }
//
//    @Override
//    public List<Email> getUnreadMail() throws MessagingException {
//        return mailbox.getUnreadMail();
//    }
//
//    @Override
//    public List<Email> getUnreadMail(int count) throws MessagingException {
//        return mailbox.getUnreadMail(count);
//    }
//
//    @Override
//    public void starEmail(Email msg) throws MessagingException {
//
//    }
//
//    @Override
//    public void unstarEmail(Email msg) throws MessagingException {
//
//    }
//
//    @Override
//    public void markImportantEmail(Email msg) throws MessagingException {
//
//    }
//
//    @Override
//    public void deleteEmail(Email msg) throws MessagingException {
//
//    }
//
//    @Override
//    public void markReadEmail(Email msg) throws MessagingException {
//
//    }
//
//    @Override
//    public void markUnreadEmail(Email msg) throws MessagingException {
//
//    }
//
//    @Override
//    public void markSpam(Email msg) throws MessagingException {
//
//    }
//}
