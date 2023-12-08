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

public class SignupActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    EditText confirmPassword;
    String email_txt;
    String password_txt;
    String confirmPassword_txt;
    Button signup;
    TextView redirect;
    ProgressDialog dialog;
    JSONParser parser=new JSONParser();
    int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);
        confirmPassword = findViewById(R.id.signup_confirm);

        signup = findViewById(R.id.signup_button);
        redirect = findViewById(R.id.loginRedirectText);



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_txt = email.getText().toString();
                password_txt = password.getText().toString();
                confirmPassword_txt = confirmPassword.getText().toString();

                if( email_txt.equals("")||password_txt.equals("")||confirmPassword_txt.equals("") )
                {
                    Toast.makeText(SignupActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if( password_txt.equals(confirmPassword_txt) == false)
                    {
                        Toast.makeText(SignupActivity.this, "Invalid Password!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        new Add().execute();
                    }
                }
            }
        });

        redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    class Add extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(SignupActivity.this);
            dialog.setMessage("Patientez SVP");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> map=new HashMap<String,String>();
            map.put("email",email.getText().toString());
            map.put("password", password.getText().toString());

            JSONObject object=parser.makeHttpRequest("http://10.0.2.2/Employee/register.php","GET",map);

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
                Toast.makeText(SignupActivity.this,"SingUp Success",Toast.LENGTH_LONG).show();
                Intent i = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(i);
            }
            else
            {
                Toast.makeText(SignupActivity.this,"Echec !!!!",Toast.LENGTH_LONG).show();
            }
        }
    }

}