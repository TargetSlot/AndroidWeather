package com.targetslot.androidweather;

import com.targetslot.androidwauther.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {
	private String[] city={"Гомель","Минск","Москва"};
	private Button btnRef;
	private TextView outText;
	private Spinner spin;
	
	public boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {  
	        super.onConfigurationChanged(newConfig);  
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    
        outText =(TextView)findViewById(R.id.info);   
        btnRef = (Button)findViewById(R.id.refresh);   
    	spin=(Spinner)findViewById(R.id.spinner);
        
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, city);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
	    
	    Spinner spinner = (Spinner) findViewById(R.id.spinner);
	    spinner.setAdapter(adapter);
	    //Заголовок
	    spinner.setPrompt("Выберите город");
	    spinner.setSelection(0);
    
	    android.view.View.OnClickListener oclbtnRef = new android.view.View.OnClickListener() {
	    	@Override
	    	public void onClick(View v){
	    		if (isNetworkAvailable()==false) outText.setText("Нет подключения к сети");
	    		else{
	    			NewAsync async = new NewAsync(MainActivity.this);
					switch(spin.getSelectedItemPosition()){
						case 0: async.execute("http://informer.gismeteo.ru/xml/33041_1.xml");break;
						case 1: async.execute("http://informer.gismeteo.ru/xml/26850_1.xml"); break;
						case 2: async.execute("http://informer.gismeteo.ru/xml/27612_1.xml"); break;
						default: async.execute("http://informer.gismeteo.ru/xml/33041_1.xml"); break;
					}
	    		}
	    	}
	    };
	    btnRef.setOnClickListener(oclbtnRef);
    }    
}



