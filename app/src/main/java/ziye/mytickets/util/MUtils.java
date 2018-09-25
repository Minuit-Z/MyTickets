package ziye.mytickets.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ziye.mytickets.R;

/**
 * Created by Administrator on 2018/9/25 0025.
 */

public class MUtils {

    private static final String PREFS_NAME = "ziye.mytickets.widget.TestAppWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    public static String getRexString(String content, Pattern pattern) {
        StringBuilder sb = new StringBuilder();
        Matcher m = pattern.matcher(content);
        while (m.find()) {
            sb.append(m.group());
        }
        return sb.toString();
    }
}
