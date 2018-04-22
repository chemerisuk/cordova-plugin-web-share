package by.chemerisuk.cordova;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ComponentName;
import android.content.IntentFilter;
import android.os.Build;

public class ShareDialogPlugin extends CordovaPlugin {
    private static final int SHARE_REQUEST_CODE = 18457896;

    private CallbackContext shareCallbackContext;
    private BroadcastReceiver chosenComponentReceiver;
    private PendingIntent chosenComponentPI;
    private ComponentName lastChosenComponent;

    @Override
    protected void pluginInitialize() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Intent receiverIntent = new Intent(Intent.EXTRA_CHOSEN_COMPONENT);

            chosenComponentReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    lastChosenComponent = (ComponentName)intent.getExtras().get(Intent.EXTRA_CHOSEN_COMPONENT);
                }
            };

            cordova.getActivity().registerReceiver(chosenComponentReceiver, new IntentFilter(Intent.EXTRA_CHOSEN_COMPONENT));

            chosenComponentPI = PendingIntent.getBroadcast(cordova.getActivity(),
                SHARE_REQUEST_CODE + 1, new Intent(Intent.EXTRA_CHOSEN_COMPONENT), PendingIntent.FLAG_CANCEL_CURRENT);
        }
    }

    @Override
    public void onDestroy() {
        if (chosenComponentReceiver != null) {
            cordova.getActivity().unregisterReceiver(chosenComponentReceiver);
        }
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("share")) {
            this.share(args.getJSONObject(0), callbackContext);

            return true;
        }
        return false;
    }

    private void share(JSONObject options, CallbackContext callbackContext) throws JSONException {
        String text = options.getString("text");
        String subject = options.optString("subject");
        String title = options.optString("title");
        String url = options.optString("url");

        if (!url.isEmpty()) {
            text += "\n\n" + url;
        }

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);

        if (!subject.isEmpty()) {
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }

        if (chosenComponentPI != null) {
            sendIntent = Intent.createChooser(sendIntent, title, chosenComponentPI.getIntentSender());

            lastChosenComponent = null;
        } else {
            sendIntent = Intent.createChooser(sendIntent, title);
        }

        cordova.startActivityForResult(this, sendIntent, SHARE_REQUEST_CODE);

        this.shareCallbackContext = callbackContext;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SHARE_REQUEST_CODE && this.shareCallbackContext != null) {
            if (resultCode == Activity.RESULT_CANCELED) {
                this.shareCallbackContext.success("");
            } else if (lastChosenComponent != null) {
                this.shareCallbackContext.success(lastChosenComponent.getPackageName());
            } else {
                this.shareCallbackContext.success("undefined");
            }

            this.shareCallbackContext = null;
        }
    }
}
