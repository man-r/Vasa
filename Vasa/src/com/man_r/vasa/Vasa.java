package com.man_r.vasa;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Vasa extends Activity {

	private TextView vasaTxt;
	private ImageView vasaImg;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vasa);
        
        vasaTxt = (TextView) findViewById(R.id.vasatxt);
        vasaImg = (ImageView) findViewById(R.id.vasaimg);
        
    }

    @Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("VASA Beta!").setMessage("This is a beta version." +
    			"\n\nThat means it might have bugs!" +
    			"\n\nBugs that can crawl to you at night and eat you brains out." +
    			"\n\nThis might require you to go to the hospital and ask for a new brain")
    			.setPositiveButton("Bite me!", null);
    	
    	AlertDialog alert = builder.create();
    	alert.show();
		
		
		if (getPackageManager().queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0).size() == 0)
        {
        	vasaImg.setEnabled(false);
        	vasaTxt.setText("Recognizer not present");
        }
        
        else {
        	vasaImg.setOnClickListener(new OnClickListener(){

				public void onClick(View v) {
					// TODO Auto-generated method stub
					startVoiceRecognitionActivity();
				}
        		
        	});
        	
        }
	}
    
    /**
     * Fire an intent to start the voice recognition activity.
     */
    private void startVoiceRecognitionActivity()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "VASA");
        startActivityForResult(intent, 13);
    }
    
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
    	if (requestCode == 13 && resultCode == RESULT_OK)
        {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            
            final PackageManager pm2 = getPackageManager();

    		List<PackageInfo> packages = pm2.getInstalledPackages(0);

    		
    		
    		vasaTxt.setText("Seriously you expect me to find an app called \n\"" + matches.get(0) + "\"\n\nTry again");
    		loop:
    		for (int i=0; i < matches.size(); i++) {
    			for (int j=0; j< packages.size(); j++) {

        			if( packages.get(j).applicationInfo.loadLabel(getPackageManager()).toString().contains(matches.get(i))){
        				launchApp(packages.get(j).packageName);
        				vasaTxt.setText(matches.get(i));
        				
        				break loop;
        			}
        				
        		}
    			
    		}
    		            
        }
        super.onActivityResult(requestCode, resultCode, data);
	}
    
    protected void launchApp(String packageName) {
		Intent mIntent = getPackageManager().getLaunchIntentForPackage(packageName);
		if (mIntent != null) {
			try {
				startActivity(mIntent);
			} catch (ActivityNotFoundException err) {
				Toast t = Toast.makeText(getApplicationContext(),"NOT FOUND", Toast.LENGTH_SHORT);
				t.show();
			}
		}
		//finish();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.activity_vasa, menu);
        return true;
    }
}
