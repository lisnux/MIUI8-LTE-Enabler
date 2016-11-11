package io.nrdwfy.miui8lteenabler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.XResources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.getParameterTypes;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class miui8lteenabler implements IXposedHookLoadPackage{

    @Override
    public void handleLoadPackage(final LoadPackageParam lpParam) throws Throwable {

        //Moving to Top to allow 4G on Boot
        findAndHookMethod("miui.telephony.TelephonyManager", lpParam.classLoader, "isDisableLte", "boolean", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(false);
            }
        });

           if(lpParam.packageName.equals("com.android.phone")){
            XposedBridge.log("[nrdwfy] : com.android.phone Detected. Loaded.");
            findAndHookMethod("com.android.phone.settings.MobileNetworkSettings", lpParam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    // Enable VoLTE Button
                    final PreferenceCategory mPrefGeneral = (PreferenceCategory)XposedHelpers.getObjectField(param.thisObject,"mPrefGeneral");
                    CheckBoxPreference mButton4glte = (CheckBoxPreference)XposedHelpers.getObjectField(param.thisObject,"mButton4glte");
                    mPrefGeneral.addPreference(mButton4glte);
                    String e = mPrefGeneral.getTitle().toString() + " (Xposed Mod Enabled)";
                    mPrefGeneral.setTitle(e);
                }
            });

            findAndHookMethod("com.android.phone.settings.PreferredNetworkTypeListPreference", lpParam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                }
            });

        }else if(lpParam.packageName.equals("com.android.settings")){

            XposedBridge.log("[nrdwfy] : com.android.settings Detected. Loaded.");

                //Tricking Telephony Manager to make RadioInfo Know the LTE doesn't disabled.
                //v1.1 : Deprecated due to Unflexibility of using TelephonyManager
                /*findAndHookMethod("miui.telephony.TelephonyManager", lpParam.classLoader, "isDisableLte", "boolean", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(false);
                    }
                });*/
        }else{
            return;
        }



    }
}
