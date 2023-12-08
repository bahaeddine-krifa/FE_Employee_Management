package com.example.employeemangemen;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {
    TextView back,logout;
    EditText name;
    EditText email;
    EditText age;
    Button update;
    Button delete;
    String id;
    ProgressDialog dialog;
    JSONParser parser=new JSONParser();
    int success, success_update,success_delete;
    String name_db,email_db,age_db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        back = findViewById(R.id.txt4);
        logout = findViewById(R.id.txt5);

        name = findViewById(R.id.editName);
        email = findViewById(R.id.editEmail);
        age = findViewById(R.id.editAge);
        update = findViewById(R.id.modifier);
        delete = findViewById(R.id.supprimer);

        Bundle extras=getIntent().getExtras();
        if(extras!=null)
        {
            id=extras.getString("id");

            new Select().execute();

        }

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Update().execute();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Delete().execute();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DetailActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailActivity.this,"LogOut",Toast.LENGTH_LONG).show();
                Intent intent1=new Intent(DetailActivity.this,LoginActivity.class);
                startActivity(intent1);
            }
        });
    }

    class Select extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(DetailActivity.this);
            dialog.setMessage("Patientez SVP");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> map=new HashMap<String,String>();
            map.put("id",id);

            JSONObject object=parser.makeHttpRequest("http://10.0.2.2/Employee/select_one.php","GET",map);

            try {
                success=object.getInt("success");
                if(success==1)
                {
                    JSONArray user=object.getJSONArray("employee");
                    JSONObject o=user.getJSONObject(0);
                    name_db=o.getString("name");
                    email_db=o.getString("email");
                    age_db=o.getString("age");

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
            name.setText(name_db);
            email.setText(email_db);
            age.setText(age_db);
        }
    }


    class Update extends AsyncTask<String,String,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> map=new HashMap<>();
            map.put("id",id);
            map.put("name",name.getText().toString());
            map.put("email",email.getText().toString());
            map.put("age",age.getText().toString());

            JSONObject ob= parser.makeHttpRequest("http://10.0.2.2/Employee/update.php","GET",map);
            try {
                success_update=ob.getInt("success");
                if(success_update==1)
                {
                    Intent i=new Intent(DetailActivity.this,MainActivity.class);
                    startActivity(i);
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(success_update==0)
            {
                Toast.makeText(DetailActivity.this,"Edit Error!",Toast.LENGTH_LONG).show();
            }
        }
    }

    class Delete extends AsyncTask<String,String,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> map=new HashMap<>();
            map.put("id",id);


            JSONObject ob= parser.makeHttpRequest("http://10.0.2.2/Employee/delete.php","GET",map);
            try {
                success_delete=ob.getInt("success");
                if(success_delete==1)
                {
                    Intent i=new Intent(DetailActivity.this,MainActivity.class);
                    startActivity(i);
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(success_delete==0)
            {
                Toast.makeText(DetailActivity.this,"Deletion Error!",Toast.LENGTH_LONG).show();
            }
        }
    }

}