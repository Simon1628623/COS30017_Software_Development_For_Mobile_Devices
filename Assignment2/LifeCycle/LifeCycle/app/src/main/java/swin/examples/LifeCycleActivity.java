package swin.examples;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/** This app will display messages on screen, and in the Log 
 * indicating the life cycle states
 * @author rvasa
 */
public class LifeCycleActivity extends Activity
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		appendState("onCreate");
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		appendState("onStart");
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		appendState("onResume");
	}
	
	@Override
	protected void onRestart()
	{
		super.onRestart();
		appendState("onRestart");
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		appendState("onPause");
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		appendState("onStop");
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		appendState("onDestroy");
	}
	
	private void appendState(String s)
	{
		TextView stateTextView = (TextView) findViewById(R.id.stateTextView);
		String prevStates = stateTextView.getText().toString();
		stateTextView.setText(prevStates+"\n"+s);
		Log.i("LIFE-CYCLE-ACTIVITY-CURRENT-STATE", s);
	}
}