package ir.futurearts.esmfamil.Utils;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseInstanceIdService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseIdService";
    private static final String TOPIC_GLOBAL = "global";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("MM",s);
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_GLOBAL);
        sendRegistrationToServer(s);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }
}