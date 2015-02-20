package ru.galakart.majordroid.widgets.control;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import ru.galakart.majordroid.R;

import static ru.galakart.majordroid.widgets.control.ScriptControlWidget.CONTROL_WIDGET_PREF;
import static ru.galakart.majordroid.widgets.control.ScriptControlWidget.WIDGET_SCRIPT_NAME_FIELD;

/**
 * Created by user on 20.02.2015.
 */
public class ControlWidgetSettingsActivity
        extends Activity
{
  // ------------------------------ View --------------------------------------------
  private EditText scriptNameTextView;
  private Button saveSettingsButton;

  // ------------------------------ Fields -----------------------------------------
  private int mWidgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
  private Intent mResultValue;
  private String mScriptName;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    initStartData();
    setResult(RESULT_CANCELED, mResultValue);

    viewInit();
  }

  private void initStartData()
  {
    Intent intent = getIntent();
    Bundle extras = intent.getExtras();
    if (extras != null) {
      mWidgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

      SharedPreferences sp = getSharedPreferences(CONTROL_WIDGET_PREF, Context.MODE_PRIVATE);

      mScriptName = sp.getString(WIDGET_SCRIPT_NAME_FIELD + mWidgetID, null);
      if (mScriptName == null)
      {
        mScriptName = "";
      }
    }

    if (mWidgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
      finish();
    }

    mResultValue = new Intent();
    mResultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mWidgetID);
  }

  private void viewInit()
  {
    setContentView(R.layout.activity_control_settings_widget);

    scriptNameTextView = (EditText) findViewById(R.id.script_name_edittext);
    scriptNameTextView.setText(mScriptName);
    saveSettingsButton = (Button) findViewById(R.id.save_settings_button);

    saveSettingsButton.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        saveSettingsButtonClick();
      }
    });
  }

  // -----------------------------------------------------------------------------------------------------------------
  private void saveSettingsButtonClick()
  {
    String scriptName = scriptNameTextView.getText().toString();

    SharedPreferences sp = getSharedPreferences(CONTROL_WIDGET_PREF, MODE_PRIVATE);
    SharedPreferences.Editor editor = sp.edit();
    editor.putString(WIDGET_SCRIPT_NAME_FIELD + mWidgetID, scriptName);

    editor.commit();

    setResult(RESULT_OK, mResultValue);
    finish();
  }
}
