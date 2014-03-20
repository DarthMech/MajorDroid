package ru.galakart.majordroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

public class Prefs extends PreferenceActivity {
	private String currentSSID = null, tmpPrefSSID = null;
	AlertDialog.Builder ad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		String vid = prefs.getString(getString(R.string.vid), "");

		if (vid.contains("�������")) {
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}

		if (vid.contains("�������������")) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		}
		addPreferencesFromResource(R.xml.settings);

		WifiManager wifiMgr = (WifiManager) this
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
		currentSSID = wifiInfo.getSSID();
		tmpPrefSSID = prefs.getString("wifihomenet", "");

		Preference button = (Preference) findPreference("button");
		button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference arg0) {
				ad = new AlertDialog.Builder(Prefs.this);
				ad.setTitle("����� �������� Wifi-����"); // ���������
				if (tmpPrefSSID == "")
					ad.setMessage("���������� �������� ���� �� " + currentSSID
							+ " ?");
				else
					ad.setMessage("������� �������� ����: " + tmpPrefSSID
							+ "\n�������� � �� " + currentSSID + " ?");
				ad.setPositiveButton("���������", new OnClickListener() {
					public void onClick(DialogInterface dialog, int arg1) {
						Toast toast = Toast.makeText(getApplicationContext(),
								"���� " + currentSSID + " ���������",
								Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.BOTTOM, 0, 0);
						Editor editor = prefs.edit();
						editor.putString("wifihomenet", currentSSID);
						if (editor.commit())
							toast.show();
					}
				});
				ad.setNegativeButton("������", new OnClickListener() {
					public void onClick(DialogInterface dialog, int arg1) {
						// none
					}
				});
				ad.setCancelable(true);

				if (currentSSID != null) {
					if (tmpPrefSSID.equals(currentSSID)) {

						AlertDialog.Builder albuilder = new AlertDialog.Builder(
								Prefs.this);
						albuilder
								.setTitle("���������")
								.setMessage(
										"������� WiFi-���� ��������� � ��������� � ������. ��� ������ ������ �������� ����, ������������ � ���.")
								.setCancelable(false)
								.setNegativeButton("�����",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												dialog.cancel();
											}
										});
						AlertDialog alert = albuilder.create();
						alert.show();

					} else
						ad.show();
				} else {
					AlertDialog.Builder albuilder = new AlertDialog.Builder(
							Prefs.this);
					albuilder
							.setTitle("������ ���������!")
							.setMessage("WiFi �������� ��� ��� ����������")
							.setCancelable(false)
							.setNegativeButton("�����",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
										}
									});
					AlertDialog alert = albuilder.create();
					alert.show();
				}

				return true;
			}
		});

	}
}
