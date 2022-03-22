package com.reactnativesimplejsi;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.SensorManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.JavaScriptContextHolder;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.bridge.ReactMethod;


@ReactModule(name = SimpleJsiModule.NAME)
public class SimpleJsiModule extends ReactContextBaseJavaModule {
  public static final String NAME = "SimpleJsi";
  private OrientationEventListener mOrientationListener;


  public SimpleJsiModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  private native void nativeInstall(long jsi);

  public static native int callValue(int param);

  // Installing JSI Bindings as done by
// https://github.com/mrousavy/react-native-mmkv
  @ReactMethod(isBlockingSynchronousMethod = true)
  public boolean install() {

    System.loadLibrary("cpp");
    JavaScriptContextHolder jsContext = getReactApplicationContext().getJavaScriptContextHolder();

    if (jsContext != null) {
      this.nativeInstall(
        jsContext.get()
      );
      return true;
    } else {
      Log.e("SimpleJsiModule", "JSI Runtime is not available in debug mode");
      return false;
    }

  }
  public void activateListener() {
    mOrientationListener = new OrientationEventListener(this.getReactApplicationContext(), SensorManager.SENSOR_DELAY_UI) {

      @Override
      public void onOrientationChanged(int orientation) {


        Log.i("COCO onOrientation_CB","DeviceOrientation changed to " + orientation);
        int result = callValue(orientation);
        Log.i("onOrientation_CB","Result from C++ callValue" + result);
      }

    };

    if (mOrientationListener.canDetectOrientation()) {
      Log.i("COCO: OrientationEvent", "orientation detect enabled.");
      mOrientationListener.enable();
   } else {
      Log.i("COCO: OrientationEvent", "orientation detect disabled.");
      mOrientationListener.disable();
   }
  }

  public String getModel() {
    String manufacturer = Build.MANUFACTURER;
    String model = "My Model NEW";
    if (model.startsWith(manufacturer)) {
      return model;
    } else {
      return manufacturer + " " + model;
    }
  }

  public void setItem(final String key, final String value) {

    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getReactApplicationContext());
    SharedPreferences.Editor editor = preferences.edit();
    editor.putString(key,value);
    editor.apply();
  }

  public String getItem(final String key) {
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getReactApplicationContext());
    String value = preferences.getString(key, "");
    return value;
  }

  public String getCurrentOrientation() {
    final Display display = ((WindowManager) getReactApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    Log.i("getCurrentOrientation", "Getting Current Orientation lol");

    switch (display.getRotation()) {
      case Surface.ROTATION_0:
        return "PORTRAIT";
      case Surface.ROTATION_90:
        return "LANDSCAPE-LEFT";
      case Surface.ROTATION_180:
        return "PORTRAIT-UPSIDEDOWN";
      case Surface.ROTATION_270:
        return "LANDSCAPE-RIGHT";
    }
    return "UNKNOWN";
  }

  public void lockToLandscape() {
    final Activity activity = getCurrentActivity();
    if (activity == null) return;
    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
  }

  public void lockToPortrait() {
    final Activity activity = getCurrentActivity();
    if (activity == null) return;
    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
  }
}
