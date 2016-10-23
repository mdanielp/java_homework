package com.example.mdanielp.threads;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    private int sizeOfFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Add item to the action bar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        // Handle action bar item 
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createFile(View view) {
        new CreateFile().execute();
    }

    class LoadFile extends AsyncTask<String, String, String> {
        private ListView listView = (ListView) findViewById(R.id.list);
        private ArrayAdapter<String> adapter;
        private ProgressBar progressBar;
        private int progressStatus = 0;

        @Override
        protected String doInBackground(String... params) {

            try {
                FileInputStream inputStream = openFileInput("myFile.txt");
                InputStreamReader InputRead = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(InputRead);

                ArrayList<String> values = new ArrayList<>();

                String line;
                //read the line of the Thread
                while ((line = bufferedReader.readLine()) != null) {
                    values.add(line);
                    publishProgress();
                    //sleep function
                    Thread.sleep(250);
                }
                
                InputRead.close();

                adapter = new ArrayAdapter<>(MainActivity.this,
                        android.R.layout.simple_list_item_1, values);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(String...progress) {
            progressBar.setProgress(progressStatus);
            progressStatus += 1;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), "File loaded",
                    Toast.LENGTH_LONG).show();
            listView.setAdapter(adapter);
        }

        @Override
        protected void onPreExecute() {
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setProgress(sizeOfFile);
        }
    }

    class CreateFile extends AsyncTask<String, String, String>
    {
        private ProgressBar progressBar;
        private int progressStatus = 0;

        @Override
        protected String doInBackground(String... params) {
            String filename = "myFile.txt";
            String string;
            String string2 = "Cookies";
            FileOutputStream outputStream;

            try {
                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);

                for (int i = 0; i <= sizeOfFile; ++i) {
                    string = Integer.toString(i);
                    string += "\r\n";
                    outputStream.write(string.getBytes());
                    publishProgress();
                    Thread.sleep(250);
                }
                outputStream.write(string2.getBytes());
                outputStream.close();


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String...progress) {
            progressBar.setProgress(progressStatus);
            progressStatus += 1;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), "File created",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPreExecute() {
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setMax(sizeOfFile);

            EditText editText = (EditText) findViewById(R.id.size);

            if(editText.getText().toString().equals("")) {
                sizeOfFile = 15;
            } else {
                int i = Integer.parseInt(editText.getText().toString());
                sizeOfFile = i;
            }
        }
    }

    public void loadFile(View view) {
   new LoadFile().execute();
    }

    public void clear(View view) {
        ListView l = (ListView) findViewById(R.id.list);
        l.setAdapter(null);
        ProgressBar p = (ProgressBar) findViewById(R.id.progressBar);
        p.setProgress(0);
        Toast.makeText(getApplicationContext(), "Fields cleared",
                Toast.LENGTH_LONG).show();
    }

}
