package ru.galakart.majordroid.data.script;

/**
 * Created by user on 20.02.2015.
 */
public class ScriptConnectData
{
  // ----------------------------------------- Fields ---------------------------------------------------
  private String mLogin;
  private String mPassword;
  private boolean mOutAccess;

  // ----------------------------------------------------------------------------------------------------
  public ScriptConnectData(String aLogin, String aPassword, boolean aOutAccess)
  {
    mLogin = aLogin;
    mPassword = aPassword;
    mOutAccess = aOutAccess;
  }

  public String getLogin()
  {
    return mLogin;
  }

  public String getPassword()
  {
    return mPassword;
  }

  public boolean isOutAccess()
  {
    return mOutAccess;
  }
}
