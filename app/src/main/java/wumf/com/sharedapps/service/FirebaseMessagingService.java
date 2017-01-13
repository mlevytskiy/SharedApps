package wumf.com.sharedapps.service;

import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

import wumf.com.sharedapps.eventbus.ReceivedPushEvent;

/**
 * Created by filipp on 5/23/2016.
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String uid = remoteMessage.getData().get("body");
        EventBus.getDefault().post(new ReceivedPushEvent(uid));
    }

}
