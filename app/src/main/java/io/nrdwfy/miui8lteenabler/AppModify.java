package io.nrdwfy.miui8lteenabler;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceCategory;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class AppModify implements IXposedHookLoadPackage{
    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpParam) throws Throwable {

        if (lpParam.packageName.equals("com.android.phone")) {
            findAndHookMethod("com.android.phone.settings.MobileNetworkSettings", lpParam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    final PreferenceCategory mPrefGeneral = (PreferenceCategory) XposedHelpers.getObjectField(param.thisObject, "mPrefGeneral");
                    CheckBoxPreference mButton4glte = (CheckBoxPreference) XposedHelpers.getObjectField(param.thisObject, "mButton4glte");
                    mPrefGeneral.addPreference(mButton4glte);
                    String e = mPrefGeneral.getTitle().toString() + " (Xposed Mod Enabled)";
                    mPrefGeneral.setTitle(e);
                }
            });
        }else{
            return;
        }


    }
    }
