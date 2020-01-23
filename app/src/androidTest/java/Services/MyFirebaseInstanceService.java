package Services;

public class MyFirebaseInstanceService extends FirebaseInstanceIdService{

    private static final String TAG="MyFirebaseInstanceServi";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken =           FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic("all")

                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override

                    public void onComplete(@NonNull Task<Void> task) {

                        String msg = getString(R.string.msg_subscribed);

                        if (!task.isSuccessful()) {

                            msg = getString(R.string.msg_subscribe_failed);

                        }

                        Log.d(TAG, msg);

                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();

                    }

                });
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        /* If you want to send messages to this application instance or manage this apps subscriptions on the server side, send the Instance ID token to your app server.*/

        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        Log.d("TOKEN ", refreshedToken.toString());
    }
}