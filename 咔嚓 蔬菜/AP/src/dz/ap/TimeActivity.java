package dz.ap;

import java.util.Calendar;

import com.example.dz.ap.R;


import dz.am.APData;
import dz.timewidget.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

public class TimeActivity extends Activity {
	// Time changed flag
	private boolean timeChanged = false;
	//public static String EXTRA_DATE = "finish_date";
	//
	private boolean timeScrolled = false;
	Button set;
	Button cancel;
	TextView inputTX;
	
	private TextView tvYear;
	private TextView tvMonth;
	private TextView tvDay;
	private TextView tvHour;
	private TextView tvMin;
	
	String setMonth="";
	String setDay="";
	String setHour="";
	String setMin="";
	String setYear="";
	WheelView months;
	WheelView days;
	WheelView hours;
	WheelView mins;
	WheelView years;
	final int sp=15;//the sp to px

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_time);
		setResult(Activity.RESULT_CANCELED);		 
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();         
        /*
         * dm:exchange px to sp 
         */        
        DisplayMetrics dm = new DisplayMetrics();
        TimeActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);

        getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);   
        getWindow().setGravity(Gravity.CENTER);      

		set=(Button)findViewById(R.id.btn_set);
		cancel=(Button)findViewById(R.id.btn_cancel);
		inputTX = (TextView)findViewById(R.id.inputTX);
		tvYear = (TextView)findViewById(R.id.yearTX);
		tvMonth = (TextView)findViewById(R.id.monthTX);
		tvDay = (TextView)findViewById(R.id.dayTX);
		tvMin = (TextView)findViewById(R.id.minsTX);
		tvHour = (TextView)findViewById(R.id.hourTX);
		
		tvYear.setText("年");
		tvMonth.setText("月");
		tvDay.setText("日");
		tvHour.setText("时");
		tvMin.setText("分");
		
		set.setText("确     定");
		cancel.setText("取消");
		inputTX.setText("设置到期时间");
		
		Calendar c = Calendar.getInstance();
		final int curDays=c.get(Calendar.DAY_OF_MONTH);
		final int curMonths=c.get(Calendar.MONTH);
		final int curHours = c.get(Calendar.HOUR_OF_DAY);
		final int curMinutes = c.get(Calendar.MINUTE);
		final int curYears=c.get(Calendar.YEAR);
		 /*
         * set
         *
         */
       set.setOnClickListener(new Button.OnClickListener(){


 			public void onClick(View v) {
 				Intent intent = new Intent();
 				String strDate = "";
 	            // Create the result Intent and include the MAC address

 				setYear=years.getCurrentItem()+curYears+"";
 				setMonth=months.getCurrentItem()+1+"";
 				setDay=days.getCurrentItem()+1+"";
 				setHour=hours.getCurrentItem()+"";
 				setMin=mins.getCurrentItem()+"";
 				strDate = setYear+"-"+setMonth+"-"+setDay+" "+setHour+":"+setMin+":0";
 				Log.i("year",setYear);
 				Log.i("month",setMonth);
 				Log.i("day",setDay);
 				Log.i("hour",setHour);
 				Log.i("min",setMin);


 	            intent.putExtra(APData.TAG_GET_DATE_RESULT_VALUE, strDate);
 	            // Set result and finish this Activity
 	            setResult(APData.TAG_GET_DATE_RESULT_CODE, intent);
 	            finish();
 				//startActivity(i);

 			}});
       /*
        * cancel
        */
      cancel.setOnClickListener(new Button.OnClickListener(){


			public void onClick(View v) {
		       TimeActivity.this.finish();

			}});

        years = (WheelView) findViewById(R.id.year);
        Log.i("scale",dm.scaledDensity+"");
        float a=sptopx(sp,dm.scaledDensity);
        Log.i("a",a+"");
        years.TEXT_SIZE=a;
        
       
       
        int i=c.get(Calendar.YEAR);//锟斤拷玫锟角帮拷锟斤拷之锟斤拷锟斤拷锟斤拷锟角�锟斤拷锟斤拷锟斤拷锟斤拷锟街拷锟�
        Log.i("year",i+"");
        
		years.setAdapter(new NumericWheelAdapter(i, i+5));
	
		years.setCyclic(true);

		months = (WheelView) findViewById(R.id.month);
		months.TEXT_SIZE=a;
		
		months.setAdapter(new NumericWheelAdapter(1, 12));
		
		
		months.setCyclic(true);

		days = (WheelView) findViewById(R.id.day);
		 days.TEXT_SIZE=a;
		/*
		 * 锟铰份诧拷同锟斤拷锟斤拷每锟斤拷锟铰碉拷锟斤拷锟斤拷同
		 */
		if(curMonths==1||curMonths==3||curMonths==5||curMonths==10||curMonths==12||curMonths==7||curMonths==8)
		{
			days.setAdapter(new NumericWheelAdapter(1, 31));
		}else if(curMonths==2)//锟斤拷锟斤拷
		{
			if(curYears%4==0)
			{
			days.setAdapter(new NumericWheelAdapter(1, 29));
			}
			else
			{
			days.setAdapter(new NumericWheelAdapter(1, 28));	
			}
		}
		
		else
		{
			days.setAdapter(new NumericWheelAdapter(1, 30));
		}
		
		
		days.setCyclic(true);

		hours = (WheelView) findViewById(R.id.hour);
		hours.TEXT_SIZE=a;
		  
		hours.setAdapter(new NumericWheelAdapter(0, 23));
	
		hours.setCyclic(true);

		mins = (WheelView) findViewById(R.id.mins);
		
		mins.TEXT_SIZE=a;
		mins.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
		
		mins.setCyclic(true);


		// set current time



		months.setCurrentItem(curMonths);
		days.setCurrentItem(curDays-1);
		hours.setCurrentItem(curHours);
		mins.setCurrentItem(curMinutes);
		years.setCurrentItem(curYears-1);



		OnWheelChangedListener wheelListener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if (!timeScrolled) {
					timeChanged = true;
					timeChanged = false;
				}
			}
		};

		OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
			public void onScrollingStarted(WheelView wheel) {
				timeScrolled = true;
			}
			public void onScrollingFinished(WheelView wheel) {
				timeScrolled = false;
				timeChanged = true;
				//picker.setCurrentHour(hours.getCurrentItem());
				//picker.setCurrentMinute(mins.getCurrentItem());
				int m=months.getCurrentItem()+1;
				if(m==1||m==3||m==5||m==7||m==8||m==10||m==12)
				{
					days.setAdapter(new NumericWheelAdapter(1, 31));
				}else if(m==2)//锟斤拷锟斤拷
				{
					if(years.getCurrentItem()%4==0)
					{
						days.setAdapter(new NumericWheelAdapter(1, 29));
					}
					else
					{
						days.setAdapter(new NumericWheelAdapter(1, 28));
					}
				}else
				{
					days.setAdapter(new NumericWheelAdapter(1, 30));
				}
				timeChanged = false;
			}
		};

		months.addScrollingListener(scrollListener);

	}
/*
 * sp to px
 */
	private int sptopx(float spValue, float fontScale) {
		return (int) (spValue*fontScale + 0.5f);

	}

}
