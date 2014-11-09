//package com.rabidaudio.dev.tinderforgmail;
//
//import android.os.Build;
//
//import com.sun.mail.gimap.GmailFolder;
//import com.sun.mail.gimap.GmailMessage;
//import com.sun.mail.imap.IMAPFolder;
//
//import java.io.Serializable;
//
//import javax.mail.MessagingException;
//
///**
// * Created by charles on 11/8/14.
// */
//public class VEmail {
//
//    private String subject;
//    private String body;
//    private String senderEmail;
//    private String senderName;
//
//    public VEmail(String subject, String body, String senderEmail, String senderName){
//        this.subject = subject;
//        this.body = body;
//        this.senderEmail = senderEmail;
//        this.senderName = senderName;
//    }
//
//    public boolean isRead() {
//        return false;
//    }
//
//    public void markAsRead() throws MessagingException {
//    }
//
//    public void markAsUnread() throws MessagingException {
//    }
//
//    public void unstar() throws MessagingException {
//    }
//
//    //These must be done by moving folders in gmail
////    public void delete() throws MessagingException {
////    }
////
////    public void archive() throws MessagingException {
////    }
//
//    public String getSubject() throws MessagingException {
//        return subject;
//    }
//
//    public String getSenderName() throws MessagingException{
//        return senderName;
//    }
//
//    public String getSenderEmail() throws MessagingException{
//        return senderEmail;
//    }
//
////    public BufferedReader getBody() throws MessagingException {
////        //TODO maybe convert HTML to raw text //message.getContentType();
////        try {
////            return new BufferedReader(new InputStreamReader(message.getDataHandler().getInputStream()));
////        } catch (IOException e) {
////            e.printStackTrace();
////            return null;
////        }
////    }
//
//public String getBody(){
//    return body.replaceAll("/\\s+/"," ");
////    return " \t\n" +
////            "CharlesJulianKnight,\n" +
////            "Your Tweet got favorited!\t \tCharlesJulianKnight\t\n" +
////            " \t \t\t \t\n" +
////            " CharlesJulianKnight\t\tCharlesJulianKnight\n" +
////            "@charlesjuliank\n" +
////            "The 6 pitches I've heard over and over: 1. EHR 2. Social Fitness 3. Social Events 4. Social Fashion 5. Career Placement 6. Course Management\n" +
////            "09:19 PM - 07 Nov 14\n" +
////            "\t\tFavorited by\n" +
////            " Wanda-Your Friend\t\tWanda-Your Friend @WandaExplores\t\n" +
////            "music evangelist || creator: stageHuddle · Music:Tech Atl · Music Hack Atl\n" +
////            "See what else @WandaExplores is favoriting.\t\n" +
////            "View their profile\t\n" +
////            "Forgot your Twitter password? Get instructions on how to reset it.\n" +
////            "You can also unsubscribe from these emails or change your notification settings. Need help?\n" +
////            "If you received this message in error and did not sign up for Twitter, click not my account.\n" +
////            "Twitter, Inc. 1355 Market St., Suite 900 San Francisco, CA 94103\n";
//}
//
//    public long getGMID() throws MessagingException{
//        return 100;
//    }
//    public String getID() throws MessagingException{
//        return "100";
//    }
//
//
//    public String[] getLabels() throws MessagingException{
//        return new String[]{ "[Gmail]/Travel", "[Gmail]/Friends" };
//    }
//
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
//
//
//    public void moveMessage(GmailFolder src, GmailFolder dest) throws MessagingException {
//    }
//
//    public void removeMessage(GmailFolder src) throws MessagingException {
//    }
//
//    private void setLabel(IMAPFolder src) throws MessagingException{
//    }
//
//    public void copyMessage(IMAPFolder dest) throws MessagingException{
//    }
//
//}
