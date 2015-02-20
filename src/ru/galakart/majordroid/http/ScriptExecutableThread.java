package ru.galakart.majordroid.http;

import android.os.AsyncTask;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import ru.galakart.majordroid.data.script.ScriptExecuteInterface;

import java.io.IOException;

/**
 * Created by user on 20.02.2015.
 */
public class ScriptExecutableThread
        extends AsyncTask<ScriptExecuteInterface, Void, Void>
{
  @Override
  protected Void doInBackground(ScriptExecuteInterface... aExecuteTask)
  {
    try
    {
      if (aExecuteTask == null || aExecuteTask.length == 0)
      {
        return null;
      }

      HttpClient httpclient = new DefaultHttpClient();
      HttpGet httpGet = aExecuteTask[0].getHttpObject();
      httpclient.execute(httpGet);

    } catch (ClientProtocolException e) {
      //TODO Handle problems..
    } catch (IOException e) {
      //TODO Handle problems..
    }

    return null;
  }
}
