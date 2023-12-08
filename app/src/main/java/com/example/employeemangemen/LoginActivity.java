package com.example.employeemangemen;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    String email_txt;
    String password_txt;
    Button login;
    TextView redirect;
    ProgressDialog dialog;
    JSONParser parser=new JSONParser();
    int success;
    String nom1;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);

        login = findViewById(R.id.login_button);
        redirect = findViewById(R.id.signupRedirectText);

        sp = getSharedPreferences("nomPref", Context.MODE_PRIVATE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_txt = email.getText().toString();
                password_txt = password.getText().toString();
                if( email_txt.equals("")||password_txt.equals("") )
                {
                    Toast.makeText(LoginActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    new Logn().execute();
                }
            }
        });

        redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    class Logn extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Patientez SVP");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> map=new HashMap<String,String>();
            map.put("email",email.getText().toString());
            map.put("password", password.getText().toString());

            JSONObject object=parser.makeHttpRequest("http://10.0.2.2/Employee/login.php","GET",map);

            try {
                success=object.getInt("success");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            try {
                nom1 = object.getString("nom");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.cancel();

            if(success==1)
            {
                SharedPreferences.Editor editor = sp.edit();

                editor.putString("name",nom1);
                editor.commit();
                Toast.makeText(LoginActivity.this,"Login Success",Toast.LENGTH_LONG).show();
                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(i);

            }
            else
            {
                Toast.makeText(LoginActivity.this,"Account not found !",Toast.LENGTH_LONG).show();
            }
        }
    }


}