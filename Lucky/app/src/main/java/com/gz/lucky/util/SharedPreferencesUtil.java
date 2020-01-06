package com.gz.lucky.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class SharedPreferencesUtil {

    public static SharedPreferences getSharedPreferences(Context context) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences("lucky", Activity.MODE_PRIVATE);
        return mySharedPreferences;
    }

    public static SharedPreferences.Editor getSharedPreferencesEditor(Context context) {
        SharedPreferences mySharedPreferences = getSharedPreferences(context);
        return mySharedPreferences.edit();
    }

    public static void editorSharedPreferences(Context context, String key, int value) {
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
        editor.putInt(key, value);
        editor.commit();
    }

    public static void editorSharedPreferences(Context context, String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
        editor.putString(key, value);
        editor.commit();
    }

    public static void editorSharedPreferences(Context context, String key, float value) {
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
        editor.putFloat(key, value);
        editor.commit();
    }

    public static void editorSharedPreferences(Context context, String key, long value) {
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
        editor.putLong(key, value);
        editor.commit();
    }

    public static void editorSharedPreferences(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void editorSharedPreferences(Context context, String key, Set<String> value) {
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
        editor.putStringSet(key, value);
        editor.commit();
    }

    public static int getSharedPreferencesInt(Context context, String key) {
        SharedPreferences mySharedPreferences = getSharedPreferences(context);
        return mySharedPreferences.getInt(key, 0);
    }

    public static String getSharedPreferencesString(Context context, String key) {
        SharedPreferences mySharedPreferences = getSharedPreferences(context);
        return mySharedPreferences.getString(key, "");
    }

    public static float getSharedPreferencesFloat(Context context, String key) {
        SharedPreferences mySharedPreferences = getSharedPreferences(context);
        return mySharedPreferences.getFloat(key, 0.0f);
    }

    public static long getSharedPreferencesLong(Context context, String key) {
        SharedPreferences mySharedPreferences = getSharedPreferences(context);
        return mySharedPreferences.getLong(key, 0);
    }

    public static boolean getSharedPreferencesBoolean(Context context, String key) {
        SharedPreferences mySharedPreferences = getSharedPreferences(context);
        return mySharedPreferences.getBoolean(key, false);
    }

    public static Set<String> getSharedPreferencesSet(Context context, String key, Set<String> set) {
        SharedPreferences mySharedPreferences = getSharedPreferences(context);
        return mySharedPreferences.getStringSet(key, set);
    }

}
