package swin.examples;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;

public class ClockDisplayActivity extends Activity
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initializeUI();
	}

	/** Initialise the User Interface and show the current time */
	private void initializeUI()
	{
		long sysTime = System.currentTimeMillis();
		String timeStr = DateFormat.format("kk:mm:ss", sysTime).toString();
		TextView ctv = (TextView) findViewById(R.id.timeTextView);
		ctv.setText(timeStr);
	}

}