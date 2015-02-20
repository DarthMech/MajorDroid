package ru.galakart.majordroid.data.script;

/**
 * Created by user on 20.02.2015.
 */
public class ScriptPathData
{
  // ----------------------------------------------- Fields -------------------------------------------------------
  private String mServerURL;
  private String mPathScripts;
  private String mScriptName;

  // ------------------------------------------------------------------------------------------------------------
  public ScriptPathData(String aServerURL, String aPathScripts, String aScriptName)
  {
    mServerURL = aServerURL;
    mPathScripts = aPathScripts;
    mScriptName = aScriptName;
  }

  public String getUrl()
  {
    return "http://" + mServerURL + mPathScripts + mScriptName.trim().replace(" ", "%20");
  }
}
