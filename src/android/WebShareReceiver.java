package by.chemerisuk.cordova;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class WebShareReceiver extends BroadcastReceiver {
    private static ComponentName lastChosenComponent;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null) {
            lastChosenComponent = (ComponentName) intent.getExtras().get(Intent.EXTRA_CHOSEN_COMPONENT);
        }
    }

    public static void resetChosenComponent() {
        lastChosenComponent = null;
    }

    public static String getChosenComponentPackage() {
        if (lastChosenComponent != null) {
            return lastChosenComponent.getPackageName();
        } else {
            return "";
        }
    }
}
