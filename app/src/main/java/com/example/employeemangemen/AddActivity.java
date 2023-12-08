package com.example.employeemangemen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AddActivity extends AppCompatActivity {
    TextView back,logout;
    EditText name,email,age;
    String name_txt,email_txt,age_txt;
    Button add;
    ProgressDialog dialog;

    JSONParser parser=new JSONParser();
    int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        back = findViewById(R.id.txt2);
        logout = findViewById(R.id.txt3);

        name = findViewById(R.id.ajouteName);
        email = findViewById(R.id.ajouteEmail);
        age = findViewById(R.id.ajouteAge);
        add = findViewById(R.id.ajouter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_txt = name.getText().toString();
                email_txt = email.getText().toString();
                age_txt = age.getText().toString();

                if( name_txt.equals("")||email_txt.equals("")||age_txt.equals(""))
                {
                    Toast.makeText(AddActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    new Add().execute();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddActivity.this,"LogOut",Toast.LENGTH_LONG).show();
                Intent i = new Intent(AddActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }


    class Add extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(AddActivity.this);
            dialog.setMessage("Patientez SVP");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> map=new HashMap<String,String>();
            map.put("name",name.getText().toString());
            map.put("email",email.getText().toString());
            map.put("age", age.getText().toString());

            JSONObject object=parser.makeHttpRequest("http://10.0.2.2/Employee/add.php","GET",map);

            try {
                success=object.getInt("success");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.cancel();

            if(success==1)
            {
                Toast.makeText(AddActivity.this,"Add Successfully",Toast.LENGTH_LONG).show();
                Intent i = new Intent(AddActivity.this,MainActivity.class);
                startActivity(i);
            }
            else
            {
                Toast.makeText(AddActivity.this,"Echec !!!!!",Toast.LENGTH_LONG).show();
            }
        }
    }
}