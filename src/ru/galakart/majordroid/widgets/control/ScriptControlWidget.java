package ru.galakart.majordroid.widgets.control;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import ru.galakart.majordroid.R;
import ru.galakart.majordroid.data.script.ScriptConnectData;
import ru.galakart.majordroid.data.script.ScriptExecuteInterface;
import ru.galakart.majordroid.data.script.ScriptExecuteTask;
import ru.galakart.majordroid.data.script.ScriptPathData;
import ru.galakart.majordroid.http.ScriptExecutableThread;

/**
 * Created by user on 20.02.2015.
 */
public class ScriptControlWidget
        extends AppWidgetProvider
{
  // ----------------------------------- Constance -----------------------------------------------------------------
  public static final String WIDGET_SCRIPT_NAME_FIELD = "ru.galakart.majordroid.widgets.control.scriptname_";
  public static final String CONTROL_WIDGET_PREF = "ru.galakart.majordroid.widgets.control.pref";

  public static final String ACTION_EXECUTE_SCRIPT = "ru.galakart.majordroid.widgets.control.execute";

  // ---------------------------------------------------------------------------------------------------------------
  @Override
  public void onUpdate(Context aContext, AppWidgetManager aAppWidgetManager, int[] aAppWidgetIds) {
    super.onUpdate(aContext, aAppWidgetManager, aAppWidgetIds);

    for (int widgetId : aAppWidgetIds)
    {
      updateWidget(aContext, aAppWidgetManager, widgetId);
    }
  }

  @Override
  public void onReceive(Context aContext, Intent aIntent)
  {
    switch (aIntent.getAction())
    {
      case ACTION_EXECUTE_SCRIPT:
        executeScript(aContext, aIntent);
        break;

      default:
        super.onReceive(aContext, aIntent);
    }
  }

  private void executeScript(Context aContext, Intent aIntent)
  {
    SharedPreferences sp = aContext.getSharedPreferences(CONTROL_WIDGET_PREF, Context.MODE_PRIVATE);

    int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    Bundle extras = aIntent.getExtras();
    if (extras != null)
    {
      appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    String script = sp.getString(WIDGET_SCRIPT_NAME_FIELD + appWidgetId, null);
    if (script == null)
    {
      return;
    }

    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(aContext);
    String pathScripts = prefs.getString(aContext.getString(R.string.path_scripts), "");
    String login = prefs.getString(aContext.getString(R.string.login), "");
    String passw = prefs.getString(aContext.getString(R.string.passw), "");

    String dostup = prefs.getString(aContext.getString(R.string.dostup), "");

    boolean outAccess = false;
    String urlKey = null;
    if (dostup.contains("Локальный"))
    {
      outAccess = false;
      urlKey = aContext.getString(R.string.localUrl);
    }
    else if (dostup.contains("Глобальный"))
    {
      outAccess = true;
      urlKey = aContext.getString(R.string.globalUrl);
    }
    else if (dostup.contains("Автоматический"))
    {
      String wifiHomeNet = prefs.getString("wifihomenet", "");

      if (!wifiHomeNet.equals(""))
      {
        if (isConnectedToSSID(aContext, wifiHomeNet))
        {
          outAccess = false;
          urlKey = aContext.getString(R.string.localUrl);
        } else
        {
          outAccess = true;
          urlKey = aContext.getString(R.string.globalUrl);
        }
      }
    }

    String url = prefs.getString(urlKey, "");

    ScriptConnectData connectData = new ScriptConnectData(login, passw, outAccess);
    ScriptPathData pathData = new ScriptPathData(url, pathScripts, script);

    ScriptExecuteInterface task = new ScriptExecuteTask(connectData, pathData);

    ScriptExecutableThread execute = new ScriptExecutableThread();
    execute.execute(new ScriptExecuteInterface[]{task});
  }

  private boolean isConnectedToSSID(Context aContext, String aWifiHomeNet) {
    try {
      WifiManager wifiMgr = (WifiManager) aContext.getSystemService(Context.WIFI_SERVICE);
      WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
      if (wifiInfo.getSSID().equals(aWifiHomeNet))
        return true;
    } catch (Exception a) {
    }
    return false;
  }

  // --------------------------------------------------------------------------------------------------

  /**
   *
   * @param aContext
   * @param aAppWidgetManager
   * @param aWidgetID
   */
  static void updateWidget(Context aContext, AppWidgetManager aAppWidgetManager, int aWidgetID)
  {
    SharedPreferences sp = aContext.getSharedPreferences(CONTROL_WIDGET_PREF, Context.MODE_PRIVATE);

    String script = sp.getString(WIDGET_SCRIPT_NAME_FIELD + aWidgetID, null);
    if (script == null)
    {
      script = "";
    }

    RemoteViews widgetView = new RemoteViews(aContext.getPackageName(), R.layout.script_control_widget);
    widgetView.setTextViewText(R.id.script_name_textview, script);

    // Выполнить скрипт
    Intent onWidgetClickIntent = new Intent(aContext, ScriptControlWidget.class);
    onWidgetClickIntent.setAction(ACTION_EXECUTE_SCRIPT);
    onWidgetClickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, aWidgetID);

    PendingIntent pIntent = PendingIntent.getBroadcast(aContext, aWidgetID, onWidgetClickIntent, 0);
    widgetView.setOnClickPendingIntent(R.id.imgb_widget_script, pIntent);

    // Конфигурационный экран
    Intent configIntent = new Intent(aContext, ControlWidgetSettingsActivity.class);
    configIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
    configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, aWidgetID);

    pIntent = PendingIntent.getActivity(aContext, aWidgetID, configIntent, 0);
    widgetView.setOnClickPendingIntent(R.id.script_name_textview, pIntent);

    // Обновляем виджет
    aAppWidgetManager.updateAppWidget(aWidgetID, widgetView);
  }
}
