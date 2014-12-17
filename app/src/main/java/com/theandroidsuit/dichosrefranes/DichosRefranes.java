package com.theandroidsuit.dichosrefranes;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.theandroidsuit.dichosrefranes.bean.DichoRefran;
import com.theandroidsuit.dichosrefranes.util.DichosRefranesUtil;

/**
 *
 * THE ANDROID SUIT 2014
 * @author Virginia Hernandez
 * @version 1.0
 *
 */

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link DichosRefranesConfigureActivity DichosRefranesConfigureActivity}
 */


public class DichosRefranes extends AppWidgetProvider {

    private static final String TAG = "DichosRefranes";

    public static String theme = "";
    public static int color = 0;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate");

        // For each widget that needs an update, get the text that we should display:
        //   - Create a RemoteViews object for it
        //   - Set the text in the RemoteViews object
        //   - Tell the AppWidgetManager to show that views object for the widget.
        final int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            getNewDichoRefran(context, appWidgetManager, appWidgetId);
        }

    }

    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.d(TAG, "onDeleted");
        // When the user deletes the widget, delete the preference associated with it.

        final int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
            DichosRefranesConfigureActivity.deleteTitlePref(context, appWidgetIds[i]);
        }
    }


    public static void getNewDichoRefran(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Log.d(TAG, "getNewDichoRefran");
        Log.d(TAG + "." + appWidgetId, String.valueOf(appWidgetId));

        Intent intent = new Intent();
        intent.setAction(DichosRefranesUtil.WIDGET_UPDATE_ACTION);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,  appWidgetId);

        PendingIntent pending = PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // re-registering for click listener
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.dichos_refranes);

        getConfiguration(context, appWidgetId);
        setNewDichoRefran(context, appWidgetId, color, remoteViews);

        remoteViews.setOnClickPendingIntent(R.id.contentContainer, pending);


		/* button to share */
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);

        DichoRefran wisdom = DichosRefranesUtil.getCurrentDichoRefran();

        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, DichosRefranesUtil.DICHOS_Y_REFRANES_TITLE);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, wisdom.getDichoRefran());

        PendingIntent sharePending = PendingIntent.getActivity(context,
                appWidgetId,
                Intent.createChooser(sharingIntent, context.getString(R.string.shareDichosRefranes)),
                PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setOnClickPendingIntent(R.id.buttonShare, sharePending);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }


    public static void getConfiguration(Context context, int widgetID) {
        Log.d(TAG, "getConfiguration");

        color = DichosRefranesConfigureActivity.loadTitlePrefInt(context, widgetID, "color");

        Log.d(TAG + "color", String.valueOf(color));
    }

    public static void setNewDichoRefran(Context context, int widgetID, int color, RemoteViews remoteViews) {
        Log.d(TAG, "setNewDichoRefran");

        DichosRefranesUtil utils = new DichosRefranesUtil(context, color);

        DichoRefran wisdom = utils.getCurrentDichoRefran();

        String desc = wisdom.getDichoRefran();

        Log.d(TAG + ".desc", desc);

        remoteViews.setTextViewText(R.id.title, DichosRefranesUtil.DICHOS_Y_REFRANES_TITLE);
        remoteViews.setTextViewText(R.id.desc, desc);

        remoteViews.setTextColor(R.id.title, color);
        remoteViews.setTextColor(R.id.desc, color);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(TAG, "onReceive");

        int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;

        Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (intent.getAction().equals(DichosRefranesUtil.WIDGET_UPDATE_ACTION) &&
                AppWidgetManager.INVALID_APPWIDGET_ID != widgetID) {
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            getNewDichoRefran(context, manager, widgetID);
        }
    }
}


