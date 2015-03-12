package dz.alarm;

import com.example.dz.ap.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class AlarmAlert extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new AlertDialog.Builder(AlarmAlert.this)
				.setIcon(R.drawable.clock)
				.setTitle("提醒")
				.setMessage("时间到了")
				.setPositiveButton("ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								System.exit(0);
								android.os.Process
										.killProcess(android.os.Process
												.myPid());
							}
						}).show();
	}

}
