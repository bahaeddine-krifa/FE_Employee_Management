package com.example.employeemangemen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    TextView title,logout;
    Button add;
    ListView ls;
    ProgressDialog dialog;
    JSONParser parser =new JSONParser();
    ArrayList<HashMap<String,String>> values= new ArrayList<HashMap<String, String>>();
    int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add = findViewById(R.id.ajoute);
        logout = findViewById(R.id.txt1);

        title = findViewById(R.id.txt);
        SharedPreferences sp = getApplication().getSharedPreferences("nomPref", Context.MODE_PRIVATE);
        String name = sp.getString("name","");
        title.setText("Welcome "+name);

        ls = findViewById(R.id.lst);

        new All().execute();

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView t=view.findViewById(R.id.id);

                Intent i=new Intent(MainActivity.this, DetailActivity.class);
                i.putExtra("id",t.getText().toString());
                startActivity(i);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"LogOut",Toast.LENGTH_LONG).show();
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

    }

    class All extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Patientez SVP");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            HashMap<String,String> map = new HashMap<>();
            JSONObject object=parser.makeHttpRequest("http://10.0.2.2/Employee/all.php","GET",map);
            try {
                success=object.getInt("success");
                if(success==1)
                {
                    JSONArray employees= object.getJSONArray("employees");
                    for(int i=0;i<employees.length();i++)
                    {
                        JSONObject employee=employees.getJSONObject(i);
                        HashMap<String,String> m= new HashMap<String,String>();
                        m.put("id",employee.getString("id"));
                        m.put("name",employee.getString("name"));
                        m.put("email",employee.getString("email"));
                        m.put("age", employee.getString("age"));

                        values.add(m);


                    }



                }



            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.cancel();
            SimpleAdapter adapter=new SimpleAdapter(MainActivity.this,values,R.layout.item,
                    new String[]{"id","name","email","age"}, new int[]{R.id.id, R.id.name,R.id.email,R.id.age}    );

            ls.setAdapter(adapter);
        }
    }

}