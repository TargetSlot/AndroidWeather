package com.targetslot.androidweather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.targetslot.androidwauther.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.TextView;

public class NewAsync extends AsyncTask<String,Void,String>{
	private Activity activity;
	private ProgressDialog dialog;
	
	public NewAsync(Activity activity){
		this.activity=activity; 
		dialog = new ProgressDialog(activity);
		dialog.setTitle("Данные загружаются"); 
		dialog.setMessage("Пожалуйста, подождите...");
	}
	
	public String parseMethod(StringBuffer buf, String begin, String end, int position){
	     Matcher matcher = Pattern.compile(begin+"=\"(.*?)\""+end).matcher(buf.toString());;
	 	 ArrayList<String> resultArray = new ArrayList<String>();
		 while (matcher.find()){
	     	MatchResult result = matcher.toMatchResult();
			resultArray.add(result.group(1));
		 } 
	     return resultArray.get(position);
	}
	
	@Override
	protected String doInBackground(String... params){
		URL url = null;
		try {
			url = new URL(params[0]);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		StringBuffer fBuf = new StringBuffer  () ;
		try {
			InputStream in=url.openStream();
			BufferedReader dis = new BufferedReader (new InputStreamReader (in));   
			String line="";
			while ( (line = dis.readLine ()) != null) 
				fBuf.append (line + "\n");
			in.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		     
		StringBuffer fBuf2 = new StringBuffer  () ;
		String[] bufwauther1={"Воскресенье","Понедельник","Вторник","Среда","Четверг","Пятница","Суббота"};
		String[] bufwauther2={"Ясно","Малооблачно","Облачно","Пасмурно"};
		String[] bufwauther3={"Дождь","Ливень","Снег","Снег","Гроза","Нет данных","Без осадков"};
		String linebuf="";
		
		linebuf=parseMethod(fBuf, "day", " month", 1);
		fBuf2.append(linebuf+".");
		linebuf=parseMethod(fBuf, "month", " year", 1);
		fBuf2.append(linebuf+".");
		linebuf=parseMethod(fBuf, "year", " hour", 1);
		fBuf2.append(linebuf+"\n");
		linebuf=parseMethod(fBuf, "weekday", ">", 1);
		fBuf2.append(bufwauther1[Integer.parseInt(linebuf)-1]+"\n");
		
		linebuf=parseMethod(fBuf, "cloudiness"," precipitation" , 1);
		fBuf2.append(bufwauther2[Integer.parseInt(linebuf)]+"\n");
			     
		linebuf=parseMethod(fBuf, "precipitation"," rpower" , 1);
		fBuf2.append(bufwauther3[Integer.parseInt(linebuf)-4]+"\n");
			     
		linebuf=parseMethod(fBuf, "<PRESSURE max"," min" , 1);
		fBuf2.append("Атмосферное давление: "+linebuf+" мм.рт.ст.\n");
			     
		linebuf=parseMethod(fBuf, "<TEMPERATURE max"," min" , 1);
		fBuf2.append("Температура: "+linebuf+"°C\n");
			     
		linebuf=parseMethod(fBuf, "<RELWET max"," min" , 1);
		fBuf2.append("Относительная влажность воздуха: "+linebuf+"%\n");
			     
		return fBuf2.toString();
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		dialog.dismiss();
		TextView outTextView;
		outTextView=(TextView)activity.findViewById(R.id.info);
		outTextView.setText(result);
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		dialog.show();
		super.onPreExecute();
	}
}
