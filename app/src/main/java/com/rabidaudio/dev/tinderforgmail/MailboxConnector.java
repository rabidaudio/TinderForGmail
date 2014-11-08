package com.rabidaudio.dev.tinderforgmail;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import javax.mail.MessagingException;

/**
 * Created by charles on 11/8/14.
 */
public class MailboxConnector  {
    static final String TAG = MailboxConnector.class.getSimpleName();

    /* Activity -> Service */
    public static final String CONNECT_MESSAGE = BuildConfig.PACKAGE_NAME+".CONNECT_MESSAGE";
    public static final String GET_EMAILS_MESSAGE = BuildConfig.PACKAGE_NAME+".GET_EMAILS_MESSAGE";
    public static final String DISCONNECT_MESSAGE = BuildConfig.PACKAGE_NAME+".DISCONNECT_MESSAGE";
    public static final String STAR_MESSAGE = BuildConfig.PACKAGE_NAME+".STAR_MESSAGE";
    public static final String UNSTAR_MESSAGE = BuildConfig.PACKAGE_NAME+".UNSTAR_MESSAGE";
    public static final String READ_MESSAGE = BuildConfig.PACKAGE_NAME+".READ_MESSAGE";
    public static final String UNREAD_MESSAGE = BuildConfig.PACKAGE_NAME+".UNREAD_MESSAGE";
    public static final String IMPORTANT_MESSAGE = BuildConfig.PACKAGE_NAME+".IMPORTANT_MESSAGE";
    public static final String DELETE_MESSAGE = BuildConfig.PACKAGE_NAME+".DELETE_MESSAGE";
    public static final String SPAM_MESSAGE = BuildConfig.PACKAGE_NAME+".SPAM_MESSAGE";



    /* Service -> Activity */
    public static final String CONNECTED_MESSAGE = BuildConfig.PACKAGE_NAME+".CONNECTED_MESSAGE";
    public static final String DISCONNECTED_MESSAGE = BuildConfig.PACKAGE_NAME+".DISCONNECTED_MESSAGE";
    public static final String ERROR_MESSAGE = BuildConfig.PACKAGE_NAME+".ERROR_MESSAGE";

    /* Bundle keys */
    public static final String EXTRA_EMAIL_LIST = BuildConfig.PACKAGE_NAME+".EMAIL_LIST";
    public static final String EXTRA_EMAIL_COUNT = BuildConfig.PACKAGE_NAME+".EMAIL_COUNT";
    public static final String EXTRA_EMAIL_ID = BuildConfig.PACKAGE_NAME+".EMAIL_ID";


    public static interface MailboxListener {
//        public void onConnected();
//        public void onDisconnected();
//        public void recieveEmails(List<Email> emails);
    }

    /**
     * *********** Service Side ***********
     */
    public static class ServiceReceiver extends TwoWayBroadcastReceiver implements MailboxListener {
        public static final String FILTER = ServiceReceiver.class.getCanonicalName();

        private IMailbox callback;
        public ServiceReceiver(Context context){
            super(context, FILTER);
        }

        @Override
        public void onReceive(String message, Bundle b) {
            if(callback==null) return;
            if(CONNECT_MESSAGE.equals(message)){

//            if(DISCOVER_MESSAGE.equals(message)){
//                callback.discoverPeers();
//            }else if(CONNECT_MESSAGE.equals(message)){
//                WifiP2pDevice device = b.getParcelable(EXTRA_SELECTED_DEVICE);
//                callback.connect(device);
//            }else if(CANCEL_DISCOVERY_MESSAGE.equals(message)){
//                callback.cancelPendingPeerRequest();
//            }else if(CANCEL_CONNECT_MESSAGE.equals(message)) {
//                callback.cancelPendingConnectRequest();
//            }else if(DESTROY_MESSAGE.equals(message)){
//                callback.destroy();
            }else{
                Log.e(TAG, "Unknown message " + message);
            }
        }

//        @Override
//        public void onPeersDiscovered(WifiP2pDeviceList peers) {
//            Bundle b = new Bundle();
//            b.putParcelable(EXTRA_PEER_DEVICE_LIST, peers);
//            sendMessage(ActivityReceiver.FILTER, PEERS_DISCOVERED_MESSAGE, b);
//        }
//
//        @Override
//        public void onConnected(WifiP2pInfo info) {
//            Bundle b = new Bundle();
//            b.putParcelable(EXTRA_CONNECTED_DEVICE, info);
//            sendMessage(ActivityReceiver.FILTER, CONNECTED_MESSAGE, b);
//        }
//
//        @Override
//        public void onDisconnected() {
//            sendMessage(ActivityReceiver.FILTER, DISCONNECTED_MESSAGE, null);
//        }
//
//        @Override
//        public void onError(WifiDirectManager.WifiDirectException e) {
//            Bundle b = new Bundle();
//            b.putInt(EXTRA_ERROR_CODE, e.errorCode);
//            b.putString(EXTRA_ERROR_MESSAGE, e.getMessage());
//            b.putSerializable(EXTRA_ERROR_STACKTRACE, e.getStackTrace());
//            sendMessage(ActivityReceiver.FILTER, ERROR_MESSAGE, b);
//        }
    }


    /**
     * *********** Activity Side ***********
     *
     * The reason for implementing WifiDirectCommands is so this acts identical to WifiDirectManager.
     * That way, the Activity doesn't know the difference between making a WifiDirectManager and
     * spawning a WifiDirectService to do it instead. TODO put service spawning and destruction here
     */
    public static class ActivityReceiver extends TwoWayBroadcastReceiver implements IMailbox {
        private static final String FILTER = ActivityReceiver.class.getCanonicalName();

        private MailboxListener callback;
        public ActivityReceiver(Context context, MailboxListener callback){
            super(context, FILTER);
            this.callback = callback;
        }

        @Override
        public void onReceive(String message, Bundle b) {
            if(CONNECTED_MESSAGE.equals(message)) {

            }else{
                Log.e(TAG, "Unknown message received "+message);
            }
        }

        @Override
        public void connect() throws MessagingException {
            sendMessage(ServiceReceiver.FILTER, CONNECT_MESSAGE, null);
        }

        @Override
        public void disconnect() throws MessagingException {
            sendMessage(ServiceReceiver.FILTER, DISCONNECT_MESSAGE, null);
        }

        @Override
        public void getUnreadMail() throws MessagingException {
            getUnreadMail(10);
        }

        @Override
        public void getUnreadMail(int count) throws MessagingException {
            Bundle b = new Bundle();
            b.putInt(EXTRA_EMAIL_COUNT, count);
            sendMessage(ServiceReceiver.FILTER, GET_EMAILS_MESSAGE, b);
        }

        @Override
        public void starEmail(Email msg) throws MessagingException {
            Bundle b = new Bundle();
//            b.putString(msg.getID());

        }

        @Override
        public void unstarEmail(Email msg) throws MessagingException {

        }

        @Override
        public void markImportantEmail(Email msg) throws MessagingException {

        }

        @Override
        public void deleteEmail(Email msg) throws MessagingException {

        }

        @Override
        public void markReadEmail(Email msg) throws MessagingException {

        }

        @Override
        public void markUnreadEmail(Email msg) throws MessagingException {

        }

        @Override
        public void markSpam(Email msg) throws MessagingException {

        }

//        public void cancelPendingPeerRequest(){
//            sendMessage(ServiceReceiver.FILTER, CANCEL_DISCOVERY_MESSAGE, null);
//        }
//
//        public void cancelPendingConnectRequest(){
//            sendMessage(ServiceReceiver.FILTER, CANCEL_CONNECT_MESSAGE, null);
//        }
//
//        public void connect(WifiP2pDevice device){
//            Bundle b = new Bundle();
//            b.putParcelable(EXTRA_SELECTED_DEVICE, device);
//            sendMessage(ServiceReceiver.FILTER, CONNECT_MESSAGE, b);
//        }
//
//        public void destroy(){
//            sendMessage(ServiceReceiver.FILTER, DESTROY_MESSAGE, null);
//        }
//
//        public void discoverPeers(){
//            sendMessage(ServiceReceiver.FILTER, DISCOVER_MESSAGE, null);
//        }
    }
}
