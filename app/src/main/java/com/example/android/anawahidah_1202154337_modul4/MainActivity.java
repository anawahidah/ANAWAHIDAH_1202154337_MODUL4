package com.example.android.anawahidah_1202154337_modul4;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //array for string names
    String[] items;

    ArrayAdapter<String> adapter;
    private ListView listMahasiswa;
    private Button btnAsync;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //referencing list dan button
        listMahasiswa = findViewById(R.id.listMhs);
        btnAsync = findViewById(R.id.btn_asynctask);

        //btn onclick
        btnAsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!adapter.isEmpty()) {
                    adapter.clear();
                }
                //start class async task
                new ListAsync (MainActivity.this).execute();

            }
        });

        //get resource string array
        items = getResources().getStringArray(R.array.ListNama);

        //create array adapter with empty array list
        adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                new ArrayList<String>()
        );

        //setting adapter
        listMahasiswa.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        //inflate Menu
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.actListNama:
                Intent intent1 = new Intent(this, MainActivity.class);
                startActivity(intent1);
                finish();
                return true;
            case R.id.actListGambar:
                Intent intent2 = new Intent(this, CariGambar.class);
                startActivity(intent2);
                finish();
                return true;
            default:
                return  super.onOptionsItemSelected(item);
        }
    }

    //param
    public class ListAsync extends AsyncTask<Void, String, Void>{

        ArrayAdapter<String> adapter;
        ProgressDialog dialog;

        public ListAsync(MainActivity activity){
            dialog = new ProgressDialog(activity);
        }

        @Override
        // mempersiapkan progress dialog
        protected void onPreExecute() {
            dialog.setTitle("Loading Data");
            dialog.setCancelable(false);
            dialog.setProgress(0);
            dialog.setMax(100);
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel Progress", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ListAsync.this.cancel(true);
                    adapter.clear();
                    dialog.dismiss();
                }
            });
            dialog.show();
            adapter = (ArrayAdapter<String>) listMahasiswa.getAdapter();
        }


        @Override
        //melakukan pekerjaan
        protected Void doInBackground(Void... voids) {
            for (String name : items) {
                publishProgress(name);
                try{
                    //jeda listview
                    Thread.sleep(500);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        //mengupdate list view
        protected void onProgressUpdate(String... values) {
            adapter.add(values[0]);
            int process = count++;
            dialog.setProgress(process);
            dialog.setMessage(process+"%");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
        }
    }
}
