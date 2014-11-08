package com.rabidaudio.dev.tinderforgmail;

import com.sun.mail.gimap.GmailFolder;
import com.sun.mail.gimap.GmailMessage;
import com.sun.mail.iap.ProtocolException;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.protocol.IMAPProtocol;

import java.io.Serializable;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;

/**
 * Created by charles on 11/8/14.
 */
public class VEmail implements Serializable {

        public boolean isRead() {
            return false;
        }

        public VEmail(GmailMessage message){
        }

        public void markAsRead() throws MessagingException {
        }

        public void markAsUnread() throws MessagingException {
        }

        public void unstar() throws MessagingException {
        }

        //These must be done by moving folders in gmail
//    public void delete() throws MessagingException {
//    }
//
//    public void archive() throws MessagingException {
//    }

        public String getSubject() throws MessagingException {
            return "Hello from the South Pacific!";
        }

        public String getSenderName() {
            return "Bob Bole";
        }

        public String getSenderEmail() {
            return "bob@boleho.le";
        }

//    public BufferedReader getBody() throws MessagingException {
//        //TODO maybe convert HTML to raw text //message.getContentType();
//        try {
//            return new BufferedReader(new InputStreamReader(message.getDataHandler().getInputStream()));
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

        public long getGMID() throws MessagingException{
            return 100;
        }
        public String getID() throws MessagingException{
            return "100";
        }

        public String debugInfo() throws MessagingException{
            return "dummy email";
        }

        public String[] getLabels() throws MessagingException{
            return new String[]{ "[Gmail]/Travel", "[Gmail]/Friends" };
        }

        public boolean hasLabel(String label) throws MessagingException{
            for(String l : getLabels()){
                if(label.equals(label)){
                    return true;
                }
            }
            return false;
        }
        public boolean isStarred() throws MessagingException{
            return hasLabel("[Gmail]/Starred");
        }


        public void moveMessage(GmailFolder src, GmailFolder dest) throws MessagingException {
        }

        public void removeMessage(GmailFolder src) throws MessagingException {
        }

        private void setLabel(IMAPFolder src) throws MessagingException{
        }

        public void copyMessage(IMAPFolder dest) throws MessagingException{
        }

}
