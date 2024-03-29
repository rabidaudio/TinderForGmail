package com.rabidaudio.dev.tinderforgmail;

import android.util.Log;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.imap.IMAPSSLStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;

/**
 *
 */
public class Mailbox {
    public static final String TAG = Mailbox.class.getCanonicalName();

    public static final String GMAIL_INBOX = "INBOX";
    public static final String GMAIL_SPAM = "[Gmail]/Spam";
    public static final String GMAIL_TRASH = "[Gmail]/Trash";
    public static final String GMAIL_IMPORTANT = "[Gmail]/Important";
    public static final String GMAIL_STARRED = "[Gmail]/Starred";
    public static final String GMAIL_ALLMAIL = "[Gmail]/All Mail";

    private final String HOST = "imap.gmail.com";
    private String username;    //  = "rabidaudio@gmail.com";//todo arg object
    private String password;    //  = "JcYe0v94AQ3J";
    private static final String PROVIDER = "imaps";//"gimaps";//"imaps";
    private static final int PORT = 993;


    // create the properties for the Session
    private Properties props = new Properties();
//    private MailboxCallback callback;

    private String folderName;
    private Session session;
    private IMAPSSLStore store;

    private IMAPFolder folder;

    private IMAPFolder inbox;
    private IMAPFolder trash;
    private IMAPFolder spam;
    private IMAPFolder starred;
    private IMAPFolder important;

    public Mailbox(String folderName){
        this.folderName = folderName;

        // configure the jvm to use the jsse security.
//        java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
//        props.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        // don't fallback to normal IMAP connections on failure.
//        props.setProperty("mail.imap.socketFactory.fallback", String.valueOf(false));
        // use the simap port for imap/ssl connections.
//        props.setProperty("mail.imap.socketFactory.port", String.valueOf(PORT));
        // set this session up to use SSL for IMAP connections
        // note that you can also use the defult imap port (including the
        // port specified by mail.imap.port) for your SSL port configuration.
        // however, specifying mail.imap.socketFactory.port means that,
        // if you decide to use fallback, you can try your SSL connection
        // on the SSL port, and if it fails, you can fallback to the normal
        // IMAP port.
    }

    public void connect(String username,String password) throws MessagingException {
        if(username==null || password==null){
            Log.e(TAG, "U/P never provided!!!");
            return;
        }
        this.username = username;
        this.password = password;
        if(folderName == null){
            Log.d(TAG, "folder choice not set");
            folderName = GMAIL_ALLMAIL;
        }
        //Connect to the server
        session = Session.getInstance(props, null);
//        session.setDebug(true); //TODO remove
        store = (IMAPSSLStore) session.getStore(PROVIDER);

        store.connect(HOST, PORT, username, password);

        //open the inbox folder
        folder = (IMAPFolder) store.getFolder(folderName);
        folder.open(Folder.READ_WRITE);
        if(folderName.equals(GMAIL_INBOX)){
            inbox = folder;
        }else{
            inbox = (IMAPFolder) store.getFolder(GMAIL_INBOX);
            inbox.open(Folder.READ_WRITE);
        }
        spam = (IMAPFolder) store.getFolder(GMAIL_SPAM);
        trash = (IMAPFolder) store.getFolder(GMAIL_TRASH);
        important = (IMAPFolder) store.getFolder(GMAIL_IMPORTANT);
        starred = (IMAPFolder) store.getFolder(GMAIL_STARRED);

        spam.open(Folder.READ_WRITE);
        trash.open(Folder.READ_WRITE);
        important.open(Folder.READ_WRITE);
        starred.open(Folder.READ_WRITE);

        Log.d(TAG, "connected!");
    }

    public void disconnect() throws MessagingException {
        //close the inbox folder but do not
        //remove the messages from the server

        if(folder != null) folder.close(false);
        if(store != null) store.close();
    }

    public List<Email> getUnreadMail() throws MessagingException {
        return getUnreadMail(50);
    }


    public List<Email> getUnreadMail(int count) throws MessagingException {


        ArrayList<Email> emails = new ArrayList<Email>();

        int total = folder.getMessageCount();
        Message[] messages = folder.getMessages(total-count, total);

//        FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
//        Message messages[] = folder.search(ft);


//        FetchProfile fp = new FetchProfile();
//        fp.add(FetchProfile.Item.FLAGS);
//        fp.add(FetchProfile.Item.ENVELOPE);
        Log.d(TAG, "fetching");
//        folder.fetch(messages, fp);

        Log.d(TAG, "searching");
//        Message[] unreadMessages = folder.search(new SearchTerm() {
//            @Override
//            public boolean match(Message msg) {
//                Log.d("s", String.valueOf(msg.getMessageNumber()));
////                return (++retrieved < 50);
//                try {
//                    return !msg.isSet(Flags.Flag.SEEN);
//                } catch (MessagingException e) {
//                    return false;
//                }
//            }
//        }, messages);
        //todo search for unread

        Log.d(TAG, "building list");

        for (Message message : messages) {
            Email e = new Email((IMAPMessage) message);
//            Log.d(TAG, e.debugInfo());
            emails.add(e);
        }

        return emails;
    }

    //since these actions are based on folders, these need to be here and not as methods on email
    //  which would be much preferable. I made move and copy methods to help this a little.
    public void starEmail(Email msg) throws MessagingException{
        msg.copyMessage(starred);
    }

    public void unstarEmail(Email msg) throws MessagingException{
//        msg.moveMessage(starred, folder);
        msg.unstar();
    }

    public void markImportantEmail(Email msg) throws MessagingException{
        msg.copyMessage(important);
    }

    public void deleteEmail(Email msg) throws MessagingException{
        msg.moveMessage(folder, trash);
    }
    //TODO some bug - reimplement
//    public void archiveEmail(Email msg) throws MessagingException {
//        msg.removeMessage(inbox);
//    }
//
//    public void markUnimportantEmail(Email msg) throws MessagingException {
//        boolean unread = !msg.isRead();
////        msg.moveMessage(important, folder);
//        msg.removeMessage(important);
//        //for some reason, moving marks as \Seen so hack fix
//        if(unread) msg.markAsUnread();
//    }

    public void markReadEmail(Email msg) throws MessagingException {
        msg.markAsRead();
    }
    public void markUnreadEmail(Email msg) throws MessagingException {
        msg.markAsUnread();
    }

    public void markSpam(Email msg) throws MessagingException {
        msg.moveMessage(folder, spam);
    }

    //todo add labels (find folder by name and append)
}
