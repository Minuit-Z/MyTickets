package ziye.mytickets.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import ziye.mytickets.R;
import ziye.mytickets.service.MyWidgetService;

import static android.content.ContentValues.TAG;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link TestAppWidgetConfigureActivity TestAppWidgetConfigureActivity}
 */
public class TestAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = TestAppWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.test_app_widget);
//        views.setTextViewText(R.id.appwidget_text, widgetText);
//        views.setRemoteAdapter();

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    //当widget更新时被执行.(包含首次添加)
    // 如果在 AppWidgetProviderInfo 调用android:config,
    // 那么当用户首次添加widget时,onUpdate()不会被调用,
    // 之后更新widget时,onUpdate才会被调用.

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        // There may be multiple widgets active, so update all of them
//        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.test_app_widget);
//        Intent intent = new Intent(context, TestAppWidget.class);
//        intent.setAction("ACTION_CLICK");
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
////        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        remoteViews.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
//        for (int appWidgetId : appWidgetIds) {
//            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
//        }
        performUpdate(context, appWidgetManager, appWidgetIds, null);

//        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            TestAppWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if ("ACTION_CLICK".equals(intent.getAction())) {
            Toast.makeText(context, "hello dog!", Toast.LENGTH_SHORT).show();
        }

    }

    private void performUpdate(Context context, AppWidgetManager awm, int[] appWidgetIds, long[] changedEvents) {
        for (int appWidgetId : appWidgetIds) {
            Log.d(TAG, "appWidgetId = " + appWidgetId);
            Intent intent = new Intent(context, MyWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.test_app_widget);
            views.setRemoteAdapter(R.id.list, intent);
            awm.updateAppWidget(appWidgetId, views);
            awm.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list);
        }
    }
}

