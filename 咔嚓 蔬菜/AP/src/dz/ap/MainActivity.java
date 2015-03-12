package dz.ap;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.dz.ap.R;

import dz.alarm.AlarmHelper;
import dz.alarm.ObjectPool;
import dz.am.APData;
import dz.data.Record;
import dz.data.RecordDao;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView tvTitle;
	private Button btCreate;
	private final int TAKE_PICTURE  = 1;
	private static SQLiteDatabase db;
	private RecordDao recordDao;
	private String deadline = null;
	private ListView lvRecords;
	private ListAdapter setupListAdapter;
	private Date curDate = null;
	private String srcPath = null;
	private long curMillis;
	private String deadLine;
	private long deltaMillis;
	private int alarmIndex = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.titlebar_main);
		
		tvTitle = (TextView)findViewById(R.id.tvTitle);
		btCreate = (Button)findViewById(R.id.btCreate);
		lvRecords = (ListView)findViewById(R.id.lvRecords);
		
		tvTitle.setText("咔嚓蔬菜");
		
		recordDao = new RecordDao(this);
		
		ObjectPool.mAlarmHelper = new AlarmHelper(this);
		
//		ArrayList<Record> records = recordDao.query();
//		Log.d("Test","records:"+ records);
//		setAdapter(setupListAdapter, lvRecords, records);
		
		btCreate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), TAKE_PICTURE);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TAKE_PICTURE) {
			if (resultCode == RESULT_OK) {
				Bitmap bm = (Bitmap) data.getExtras().get("data");
				curMillis = System.currentTimeMillis();
				srcPath = "sdcard/"+curMillis + ".jpg";
//				img.setImageBitmap(bm);// 鎯冲浘鍍忔樉绀哄湪ImageView瑙嗗浘涓婏紝private ImageView
//										// img;
				File myCaptureFile = new File(srcPath);
				try {
					BufferedOutputStream bos = new BufferedOutputStream(
							new FileOutputStream(myCaptureFile));
					/* 閲囩敤鍘嬬缉杞。鏂规硶 */
					bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);

					/* 璋冪敤flush()鏂规硶锛屾洿鏂癇ufferStream */
					bos.flush();

					/* 缁撴潫OutputStream */
					bos.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Intent intent = new Intent(MainActivity.this, TimeActivity.class);
				startActivityForResult(intent, APData.TAG_GET_DATE_REQUEST_CODE);
			}
		}
		else if(requestCode == APData.TAG_GET_DATE_REQUEST_CODE){
			if(resultCode == APData.TAG_GET_DATE_RESULT_CODE){
				curMillis = System.currentTimeMillis();
				curDate   = new   Date(curMillis);//鑾峰彇褰撳墠鏃堕棿 
				
				SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:m:ss"); 
//				System.out.println(time.format(nowTime));
			
				String deadline = formatStrTime(data.getExtras().getString(APData.TAG_GET_DATE_RESULT_VALUE));
				
				Record newRecord = new Record();
				newRecord.setSrc_path(srcPath);
				newRecord.setCreateDate(time.format(curDate));
				newRecord.setDeadline(deadline);
				
				Log.d("log","deadline: "  + data.getExtras().getString(APData.TAG_GET_DATE_RESULT_VALUE));
				recordDao.add(newRecord);
				
				ObjectPool.mAlarmHelper.openAlarm(32, "鏂扮殑闂归挓", "鍒版湡浜�",convertStrTimeToMillis(deadline),alarmIndex);
				}
		}
	}
	
	private void setAdapter(ListAdapter adapter, ListView listView, List list) {
		adapter = new ListAdapter(this, list);
		listView.setAdapter(adapter);
	}
    
    private class ListAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private List list;

		public ListAdapter(Context context, List list) {
			this.inflater = LayoutInflater.from(context);
			this.list = list;
		}
		@Override
		public int getCount() {
			return list.size();
		}
		@Override
		public Object getItem(int position) {
			return list.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;		
			if (convertView == null) {			
				convertView = inflater.inflate(R.layout.list_item_record, null);
				holder = new ViewHolder(convertView);			
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvIndex.setText(position+1 + "");
			holder.tvCreateTime.setText(((Record)list.get(position)).getCreateDate());
			holder.tvDeadline.setText(((Record)list.get(position)).getDeadline());
			Bitmap bit = BitmapFactory.decodeFile(((Record)list.get(position)).getSrc_path());
			holder.ivThumbnail.setImageBitmap(bit);
			return convertView;
		}
		class ViewHolder {
			TextView tvCreateTime;
			TextView tvDeadline;
			TextView tvIndex;
			ImageView ivThumbnail;
			
			public ViewHolder(View v)
            {
				tvIndex = (TextView)v.findViewById(R.id.tvIndex);
				tvCreateTime     =  (TextView) v.findViewById(R.id.tvCreateTime);
				tvDeadline  =  (TextView) v.findViewById(R.id.tvDeadline);
				ivThumbnail = (ImageView)v.findViewById(R.id.ivThumbnail);
            }
		}
	}
    @Override
    public void onResume(){
    	super.onResume();
    	
    	ArrayList<Record> records = recordDao.query();
    	alarmIndex = records.size() + 1;
		Log.d("Test","records:"+ records);
		setAdapter(setupListAdapter, lvRecords, records);
    }
    
    private long convertStrTimeToMillis(String strTime){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Date date = null;
		try {
			date = sdf.parse(strTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	long millis = date.getTime();
    	return millis;
    }
    
    private String formatStrTime(String oriTime){
    	int i=0;
    	String yy=null;
    	String mm=null;
    	String dd=null;
    	String hh=null;
    	String min=null;
    	String ss=null;

    	String[]subTime = oriTime.split(" ");
    	//yy
    	yy = (Integer.valueOf(subTime[0].split("-")[0])<10?"0"+subTime[0].split("-")[0]:subTime[0].split("-")[0]);
    	//mm
    	mm = (Integer.valueOf(subTime[0].split("-")[1])<10?"0"+subTime[0].split("-")[1]:subTime[0].split("-")[1]);
    	//dd
    	dd = (Integer.valueOf(subTime[0].split("-")[2])<10?"0"+subTime[0].split("-")[2]:subTime[0].split("-")[2]);
    	//hh
    	hh = (Integer.valueOf(subTime[1].split(":")[0])<10?"0"+subTime[1].split(":")[0]:subTime[1].split(":")[0]);
    	//min
    	min = (Integer.valueOf(subTime[1].split(":")[1])<10?"0"+subTime[1].split(":")[1]:subTime[1].split(":")[1]);
    	//ss
    	ss = (Integer.valueOf(subTime[1].split(":")[2])<10?"0"+subTime[1].split(":")[2]:subTime[1].split(":")[2]);
    	
    	return yy+"-"+mm+"-"+dd+" "+hh+":"+min+":"+ss;
    }
}
