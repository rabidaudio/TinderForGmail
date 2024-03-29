package com.rabidaudio.dev.tinderforgmail;

import com.sun.mail.iap.ProtocolException;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.imap.protocol.IMAPProtocol;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;

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
public class Email implements Serializable {

    private IMAPMessage message;

    String body;

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
        body = "";
        try {
//            message.getContent()
            BufferedReader b =  new BufferedReader(new InputStreamReader(message.getInputStream()));
            while(b.ready()){
                body += b.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void markAsRead() throws MessagingException {
        message.setFlag(Flags.Flag.SEEN, true);
    }

    public void markAsUnread() throws MessagingException {
        message.setFlag(Flags.Flag.SEEN, false);
    }

    public void unstar() throws MessagingException {
        message.setFlag(Flags.Flag.FLAGGED, false);
    }

    //These must be done by moving folders in gmail
//    public void delete() throws MessagingException {
//    }
//
//    public void archive() throws MessagingException {
//    }

    public Address getSenderEmail() throws MessagingException{
        Address s = message.getSender();
        return s;
    }
    public Address getSenderName() throws MessagingException{
        Address s = message.getSender();
        return s;
    }

    public String getSubject() throws MessagingException {
        return message.getSubject();
    }

    public String getBody() throws MessagingException {
        //TODO maybe convert HTML to raw text //message.getContentType();
        return body;
    }

//    public long getGMID() throws MessagingException{
//        return message.getMsgId(); //google's id
//    }
    public String getID() throws MessagingException{
        return message.getMessageID();
    }

//    public String debugInfo() throws MessagingException{
//        String labels = "";
//        for(String l : getLabels()){
//            labels+="\t"+l+"\n";
//        }
//        try {
//            return "Message: " + message.getSubject() + "\n" + message.getSender() + "\n"
//                    + "star? "+isStarred()+"\n"
//                    + "read? "+isRead() + "\n"
//                    + "LABELS"+labels;
//        }catch (Exception e){
//            return "error:"+e.getMessage();
//        }
//    }

//    public String[] getLabels() throws MessagingException{
//        return message.getLabels();
//    }

//    public boolean hasLabel(String label) throws MessagingException{
//        for(String l : getLabels()){
//            if(label.equals(label)){
//                return true;
//            }
//        }
//        return false;
//    }

//    public boolean isStarred() throws MessagingException{
//        return hasLabel("[Gmail]/Starred");
//    }


    public void moveMessage(IMAPFolder src, IMAPFolder dest) throws MessagingException {
        copyMessage(dest);
        removeMessage(src);
    }

    public void removeMessage(IMAPFolder src) throws MessagingException {
        src.setFlags(_messageArray(), new Flags(Flags.Flag.DELETED), true);
        src.expunge();
    }

    private void setLabel(IMAPFolder src) throws MessagingException{
        src.doCommand(new IMAPFolder.ProtocolCommand() {
            @Override
            public Object doCommand(IMAPProtocol protocol) throws ProtocolException {
//                p.comm
                return null;
            }
        });
    }

    //dumbass methods want an array
    private Message[] _messageArray(){
        return new Message[]{ message };
    }

    public void copyMessage(IMAPFolder dest) throws MessagingException{
        dest.appendMessages(_messageArray());
    }
}
