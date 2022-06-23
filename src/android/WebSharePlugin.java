package by.chemerisuk.cordova;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ComponentName;
import android.content.IntentFilter;
import android.os.Build;

import by.chemerisuk.cordova.support.CordovaMethod;
import by.chemerisuk.cordova.support.ReflectiveCordovaPlugin;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONObject;


public class WebSharePlugin extends ReflectiveCordovaPlugin {
    private static final int SHARE_REQUEST_CODE = 18457896;

    private CallbackContext shareCallbackContext;
    private BroadcastReceiver chosenComponentReceiver;
    private PendingIntent chosenComponentPI;
    private ComponentName lastChosenComponent;

    @Override
    protected void pluginInitialize() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            chosenComponentReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    lastChosenComponent = (ComponentName)intent.getExtras().get(Intent.EXTRA_CHOSEN_COMPONENT);
                }
            };

            cordova.getActivity().registerReceiver(chosenComponentReceiver, new IntentFilter(Intent.EXTRA_CHOSEN_COMPONENT));

            chosenComponentPI = PendingIntent.getBroadcast(cordova.getActivity(),
                SHARE_REQUEST_CODE + 1, new Intent(Intent.EXTRA_CHOSEN_COMPONENT), PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_MUTABLE);
        }
    }

    @Override
    public void onDestroy() {
        if (chosenComponentReceiver != null) {
            cordova.getActivity().unregisterReceiver(chosenComponentReceiver);
        }
    }

    @SuppressLint("NewApi")
    @CordovaMethod
    private void share(JSONObject options, CallbackContext callbackContext) {
        String text = options.optString("text");
        String title = options.optString("title");
        String url = options.optString("url");
        if (!url.isEmpty()) {
            text = text.isEmpty() ? url : text + "\n" + url;
        }

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        if (!title.isEmpty()) {
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, title);
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
        if (requestCode == SHARE_REQUEST_CODE && this.shareCallbackContext != null) {
            JSONArray packageNames = new JSONArray();
            if (resultCode == Activity.RESULT_OK) {
                packageNames.put(lastChosenComponent != null ? lastChosenComponent.getPackageName() : "");
            }

            this.shareCallbackContext.success(packageNames);
            this.shareCallbackContext = null;
        }
    }
}
