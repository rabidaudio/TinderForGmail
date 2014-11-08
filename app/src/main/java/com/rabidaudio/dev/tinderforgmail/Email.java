package com.rabidaudio.dev.tinderforgmail;

import com.sun.mail.imap.IMAPMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

    public void delete() throws MessagingException {
        message.setFlag(Flags.Flag.DELETED, true);
    }

    public void archive() throws MessagingException {
        //TODO
    }

    public String getSubject() throws MessagingException {
        return message.getSubject();
    }

    public BufferedReader getBody() throws MessagingException {
        //TODO maybe convert HTML to raw text
            //message.getContentType();
        try {
            return new BufferedReader(new InputStreamReader(message.getDataHandler().getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String debugInfo(){
//        String flags = "";
//        int flagcount = 0;
        try {
//            Flags fs = message.getFlags();
//            if(fs.contains(Flags.Flag.SEEN)){
//                flags+= "\nseen";
//            }
//            if(fs.contains(Flags.Flag.FLAGGED)){
//                flags+="\nflagged";
//            }

//            for(Flags.Flag f : message.getFlags().getSystemFlags()){
//                flagcount++;
//                flags+= "\n"+f.toString();
//            }
//            for(String f : message.getFlags().getUserFlags()){
//                flagcount++;
//                flags+= "\n"+f;
//            }

            return "Message: " + message.getSubject() + "\n" + message.getFrom() + "\n"
                    + "read? "+isRead() + "\n";

//                   + "FLAGS: " + flagcount + flags;
        }catch (Exception e){
            return "error:"+e.getMessage();
        }
    }
}
