package com.rabidaudio.dev.tinderforgmail;

import android.util.Log;

import com.sun.mail.gimap.GmailFolder;
import com.sun.mail.gimap.GmailMessage;
import com.sun.mail.gimap.GmailSSLStore;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;

/**
 * Created by charles on 11/8/14.
 *
 * this is so i can be sure MailService implements all of Mailbox's methods
 */
public interface IMailbox {

    public void connect()throws MessagingException;
    public void disconnect()throws MessagingException;

    public List<Email> getUnreadMail()throws MessagingException;
    public List<Email> getUnreadMail(int count)throws MessagingException;


    public void starEmail(Email msg)throws MessagingException;
    public void unstarEmail(Email msg)throws MessagingException;
    public void markImportantEmail(Email msg)throws MessagingException;
    public void deleteEmail(Email msg)throws MessagingException;

    //TODO some bug - reimplement
//    public void archiveEmail(Email msg)throws MessagingException;
//    public void markUnimportantEmail(Email msg)throws MessagingException;

    public void markReadEmail(Email msg) throws MessagingException;
    public void markUnreadEmail(Email msg) throws MessagingException;
    public void markSpam(Email msg) throws MessagingException;
}
