package com.example.msrhbaseapp;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
//import com.example.msrhbaseapp.SendFile;


public class MainActivity extends ActionBarActivity {

	
	
	Button button4;
	Button button5;
	Button button6;
	Button button7;
	WebView webView;
//	private LocationManager locationManager;
  //  private LocationListener locationListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/// Collecting Time Data
		 DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		 Date date = new Date();

		 /// Collecting GPS Data
		LocationManager locationManager =
			        (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			 
			String mlocProvider;
			Criteria hdCrit = new Criteria();
			System.out.println("HIIIIIIIII   1");
			hdCrit.setAccuracy(Criteria.ACCURACY_COARSE);
			 
			mlocProvider = locationManager.getBestProvider(hdCrit, true);
			System.out.println("HIIIIIIIII   2");
			 
			Location currentLocation = locationManager.getLastKnownLocation(mlocProvider);
			System.out.println("HIIIIIIIII   3");
			 System.out.println("loc"+currentLocation.getLongitude());
			 
			double currentLatitude = currentLocation.getLatitude();
			double currentLongitude = currentLocation.getLongitude();
			System.out.println("currentLatitude"+currentLongitude);
			System.out.println("loc"+currentLongitude);
		 
		 //// Write both Data to File
		writeFile(dateFormat.format(date)+"\t"+Double.toString(currentLatitude)+"\t"+Double.toString(currentLongitude));

	//	 new SendFile().execute(""); --srikar

		
		webView = (WebView) findViewById(R.id.webView1);
		
	
 
			
		
	
		Button button2 =(Button) findViewById(R.id.button2);
		button2.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
			
				webView.setWebViewClient(new WebViewClient());
				webView.loadUrl("http://134.193.136.114:8181/HDFSWS/jaxrs/generic/hadoopRun/-home-cloudera-WordCountExercise.jar");
			}
 
		});
		
		
		Button button3 =(Button) findViewById(R.id.button3);
		button3.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				
				webView.setWebViewClient(new WebViewClient());
				webView.loadUrl("http://134.193.136.114:8181/HDFSWS/jaxrs/generic/viewResult/outputDir");
			}
 
		});
	

		Button button4 =(Button) findViewById(R.id.button4);
		button4.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				
				webView.setWebViewClient(new WebViewClient());
				webView.loadUrl("http://134.193.136.114:8181/HBaseWS/jaxrs/generic/hbaseCreate/Tablemsr10/Date:Acc:GPS");
			}
 
		});
		

		Button button5 =(Button) findViewById(R.id.button5);
		button5.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				
				webView.setWebViewClient(new WebViewClient());
				webView.loadUrl("http://134.193.136.114:8181/HBaseWS/jaxrs/generic/hbaseInsert/Tablemsr10/-home-group4-GPS.txt/Date:Acc:GPS");
			}
 
		});
		
		Button button6 =(Button) findViewById(R.id.button6);
		button6.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				
				webView.setWebViewClient(new WebViewClient());
				webView.loadUrl("http://134.193.136.114:8181/HBaseWS/jaxrs/generic/hbaseRetrieveAll/Tablemse10");
			}
 
		});
		
		Button button7 =(Button) findViewById(R.id.button7);
		button7.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				
				webView.setWebViewClient(new WebViewClient());
				webView.loadUrl("http://134.193.136.114:8181/HBaseWS/jaxrs/generic/hbasedeletel/Tablemsr10");
			}
 
		});
	}
	
	  public void writeFile(String time)
      {
		 
		  String root = Environment.getExternalStorageDirectory().toString();
			File myDir = new File(root + "/Data2");
			String fname = "GPS.txt";
			myDir.mkdirs();
		    File file = new File (myDir, fname);
		   
		    try {
		           FileOutputStream out = new FileOutputStream(file,true);
		           out.write(time.getBytes());
		           out.write("\n".getBytes());
		           out.flush();
		           out.close();

		    } catch (Exception e) {
		           e.printStackTrace();
		    }

      }
	
	
	 
}

class SendFile extends AsyncTask<String, Void, String> {
	

    private Exception exception;

    protected String doInBackground(String... urls) {
        try {
           
        	
        	
        	JSch ssh = new JSch();
		        JSch.setConfig("StrictHostKeyChecking", "no");
		        Session session;
				try {
					session = ssh.getSession("cloudera", "134.193.136.114", 22);
				
		        session.setPassword("your_password");
		        session.connect();
		        Channel channel = session.openChannel("sftp");
		        channel.connect();
		        ChannelSftp sftp = (ChannelSftp) channel;
		        
		        File sdCard = Environment.getExternalStorageDirectory(); 
				File directory = new File (sdCard.getAbsolutePath() + "/Data2");
			    
				Log.i(null,directory+"/GPS.txt");
				
		        
		        sftp.put(directory+"/GPS.txt", "/home/cloudera/");
		    	
				} catch (JSchException e) {
					// TODO Auto-generated catch block
					Log.i(null,e.toString());
					e.printStackTrace();
					
				} catch (SftpException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}

        	
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
		return null;
    }

    protected void onPostExecute() {
        // TODO: check this.exception 
        // TODO: do something with the feed
    }
/* -- Srikar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    } --Srikar */
}
