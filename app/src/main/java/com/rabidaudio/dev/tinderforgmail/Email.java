package com.rabidaudio.dev.tinderforgmail;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;

/**
 *
 * Wraps the JavaMail Message class with only the stuff useful for this app.
 *
 * https://support.google.com/mail/answer/77657?hl=en
 */
public class Email {

    private IMAPMessage message;

    public boolean isRead() {
        try {
            return message.isSet(Flags.Flag.SEEN);
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Email(IMAPMessage message){
        this.message = message;
    }

    public void markAsRead() throws MessagingException {
        message.setFlag(Flags.Flag.SEEN, true);
    }

    public void markAsUnread() throws MessagingException {
        message.setFlag(Flags.Flag.SEEN, false);
    }

    public void star() throws MessagingException {
        message.setFlag(Flags.Flag.FLAGGED, true);
    }

    //These must be done by moving folders in gmail
//    public void delete() throws MessagingException {
//    }
//
//    public void archive() throws MessagingException {
//    }

    public String getSubject() throws MessagingException {
        return message.getSubject();
    }

    public BufferedReader getBody() throws MessagingException {
        //TODO maybe convert HTML to raw text //message.getContentType();
        try {
            return new BufferedReader(new InputStreamReader(message.getDataHandler().getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getID() throws MessagingException{
        return message.getMessageID();
    }

    public String debugInfo(){
        try {
            return "Message: " + message.getSubject() + "\n" + message.getSender() + "\n"
                    + "read? "+isRead() + "\n";
        }catch (Exception e){
            return "error:"+e.getMessage();
        }
    }

    public void moveMessage(IMAPFolder src, IMAPFolder dest) throws MessagingException {
        copyMessage(dest);

    }

    public void copyMessage(IMAPFolder dest) throws MessagingException{
        dest.appendMessages(new Message[]{ message });
    }
}
