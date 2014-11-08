package com.rabidaudio.dev.tinderforgmail;

import android.util.Log;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.imap.IMAPSSLStore;
import com.sun.mail.imap.IMAPStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;
import javax.mail.search.SearchTerm;

/**
 *
 */
public class Mailbox {
    String host      = "imap.gmail.com";
    String username  = "rabidaudio@gmail.com";//todo arg object
    String password  = "JcYe0v94AQ3J";
    String provider  = "imaps";
    int    PORT      = 993;
    

    // create the properties for the Session
    Properties props = new Properties();

    String folderName;
    Session session;
    IMAPStore store;

    IMAPFolder folder;

    IMAPFolder inbox;
    IMAPFolder trash;
    IMAPFolder spam;
    IMAPFolder starred;
    IMAPFolder important;

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

    public void connect() throws MessagingException {
        //Connect to the server
        session = Session.getInstance(props, null);
        session.setDebug(true); //TODO remove
        store = (IMAPSSLStore) session.getStore(provider);

        store.connect(host, PORT, username, password);

        //open the inbox folder
        folder = (IMAPFolder) store.getFolder(folderName);
        if(folderName.equals("INBOX")){
            inbox = folder;
        }else{
            inbox = (IMAPFolder) store.getFolder("INBOX");
        }
        spam = (IMAPFolder) store.getFolder("[Gmail]/Spam");
        trash = (IMAPFolder) store.getFolder("[Gmail]/Trash");
        important = (IMAPFolder) store.getFolder("[Gmail]/Important");
        starred = (IMAPFolder) store.getFolder("[Gmail]/Starred");

        folder.open(Folder.READ_ONLY); //READ_WRITE);
    }

    public void disconnect() throws MessagingException {
        //close the inbox folder but do not
        //remove the messages from the server
        folder.close(false);
        store.close();
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
        Log.d("z", "fetching");
//        folder.fetch(messages, fp);

        Log.d("z", "searching");
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


        Log.d("z", "building list");

        for (Message message : messages) {
            Email e = new Email((IMAPMessage) message);
            Log.d("z", e.debugInfo());
            emails.add(e);
        }

        return emails;
    }

    public void starEmail(){

    }
}
