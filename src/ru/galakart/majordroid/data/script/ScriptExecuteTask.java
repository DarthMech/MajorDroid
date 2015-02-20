package ru.galakart.majordroid.data.script;

import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;

/**
 * Created by user on 20.02.2015.
 */
public class ScriptExecuteTask
  implements ScriptExecuteInterface
{
  // ----------------------------------------------- Fields -------------------------------------------------------
  private ScriptConnectData mConnectData;
  private ScriptPathData mPathData;

  public ScriptExecuteTask(ScriptConnectData aConnectData, ScriptPathData aPathData)
  {
    mConnectData = aConnectData;
    mPathData = aPathData;
  }

  @Override
  public HttpGet getHttpObject()
  {
    HttpGet httpGet = new HttpGet(mPathData.getUrl());

    if (mConnectData.isOutAccess())
    {
      httpGet.addHeader(BasicScheme.authenticate(
              new UsernamePasswordCredentials(mConnectData.getLogin(), mConnectData.getPassword()), "UTF-8", false));
    }

    return httpGet;
  }
}
