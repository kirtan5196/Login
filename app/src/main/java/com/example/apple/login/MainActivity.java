package com.example.apple.login;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

public class MainActivity extends AppCompatActivity {
    private Button button;
    TextView tvIsConnected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);

        button= (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netwrorkInfo =  manager.getActiveNetworkInfo();

                if(netwrorkInfo != null && netwrorkInfo.isConnected()){
                    Toast.makeText(MainActivity.this,"Device is connected to the Inernet",Toast.LENGTH_LONG).show();
                    tvIsConnected.setBackgroundColor(0xFF00CC00);

                }
                else{
                    Toast.makeText(MainActivity.this,"Device is not connected to the Inernet",Toast.LENGTH_LONG).show();
                    tvIsConnected.setBackgroundColor(0xFF0000);

                }
                new HttpAsyncTask().execute("https://ikidzcare.com/JSON/LoginService.svc/ValidateUser?UserName=admin@testschool&Password=adminpw@testschool&SchoolId=5ea2354f-ddf3-4706-b68d-3976f76b69b6");
            }

            public String GET(String url){
                InputStream inputStream = null;
                String result = "";
                try {

                    // create HttpClient
                    HttpClient httpclient = new DefaultHttpClient();

                    // make GET request to the given URL
                    HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

                    // receive response as inputStream
                    inputStream = httpResponse.getEntity().getContent();

                    // convert inputstream to string
                    if(inputStream != null)
                        result = convertInputStreamToString(inputStream);
                    else
                        result = "Did not work!";

                } catch (Exception e) {
                    Log.d("InputStream", e.getLocalizedMessage());
                }

                return result;
            }

            private String convertInputStreamToString(InputStream inputStream) throws IOException{
                BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
                String line = "";
                String result = "";
                while((line = bufferedReader.readLine()) != null)
                    result += line;

                inputStream.close();
                return result;

            }

            public boolean isConnected(){
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected())
                    return true;
                else
                    return false;
            }
            class HttpAsyncTask extends AsyncTask<String, Void, String> {
                @Override
                protected String doInBackground(String... urls) {

                    return GET(urls[0]);
                }
                // onPostExecute displays the results of the AsyncTask.
                @Override
                protected void onPostExecute(String result) {
                    Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
                    tvIsConnected.setText(result);
                }
            }
        });

            }

    }
