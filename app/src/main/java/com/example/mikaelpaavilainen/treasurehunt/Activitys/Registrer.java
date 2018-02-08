package com.example.mikaelpaavilainen.treasurehunt.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mikaelpaavilainen.treasurehunt.HTTPREQUEST.HttpRequest;
import com.example.mikaelpaavilainen.treasurehunt.R;

import org.json.JSONObject;

public class Registrer extends AppCompatActivity {
    public String[] names;
    public String[] got_email;

    ImageView uV;
    ImageView eV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrer);
        uV = findViewById(R.id.usernameV);
        eV = findViewById(R.id.emailV);
        uV.setVisibility(View.INVISIBLE);
        eV.setVisibility(View.INVISIBLE);
        getAll();
    }
    public void getAll(){
        HttpRequest get_usernames = new HttpRequest();
        String urlParameters ="";
        get_usernames.setValues("http://192.168.1.2/TreasureHunt/get_names.php",urlParameters,this);
        get_usernames.execute();

        while(get_usernames.respons.equals("")){
            //do nothing while wait for response
        }
        //get response
        try{
            JSONObject usernames = new JSONObject(get_usernames.respons);
            Log.d("HTTPSN", "getall: " + usernames);
            names = new String[usernames.length()];
            for(int i = 0;i < usernames.length();i++){
                JSONObject temp = new JSONObject(usernames.getString(""+i));
                Log.d("HTTPSN", "getAll: " +temp);
                names[i] = temp.getString("name");
            }

        }
        catch(Exception e){

        }
        Log.d("HTTPSN", "getAll: Done " + names[0]);
        //get all emails
        HttpRequest get_emails = new HttpRequest();
        String urlParameters_e ="";
        get_emails.setValues("http://192.168.1.2/TreasureHunt/get_emails.php",urlParameters_e,this);
        get_emails.execute();

        while(get_emails.respons.equals("")){
            //do nothing while wait for response
        }
        //get response
        try{
            JSONObject emails = new JSONObject(get_emails.respons);
            got_email = new String[emails.length()];
            Log.d("HTTPSN", "getallUser: " + emails);
            for(int i = 0;i < emails.length();i++){
                JSONObject temp = new JSONObject(emails.getString(""+i));
                Log.d("HTTPSN", "getAll: " +temp);
                got_email[i] = temp.getString("email");
            }
        }
        catch(Exception e){

        }
        Log.d("HTTPSN", "getAll: " + got_email[0]);

    }

    public void register(View v){
        boolean unFailed = false;
        boolean emailFailed = false;

        TextView username = findViewById(R.id.Username);
        TextView pass1 = findViewById(R.id.Password);
        TextView pass2 = findViewById(R.id.Password2);
        TextView email = findViewById(R.id.Email);
        if(pass1.getText().toString().equals(pass2.getText().toString())){
            Log.d("HTTPSN", "register: " + username.getText() + " - " + names[0]);
            for(int i = 0;i < names.length;i++){
                if(username.getText().toString().equals(names[i])){
                    Log.d("HTTPSN", "register: Same Username");
                    uV.setVisibility(View.VISIBLE);
                    unFailed = true;
                    //error handling
                }
            }
            for(int i = 0;i < names.length;i++){
                if(email.getText().toString().equals(got_email[i])){


                    Log.d("HTTPSN", "register: Same Email");
                    eV.setVisibility(View.VISIBLE);
                    emailFailed = true;

                    //error handling
                }
            }
            if (unFailed == false && emailFailed == false){
                //send in data then goback to login
                HttpRequest add_user = new HttpRequest();
                String urlParameters ="username="+username.getText()+"&password="+pass1.getText()+"&email="+email.getText();
                Log.d("HTTPSN", "register: params" + urlParameters);
                add_user.setValues("http://192.168.1.2/TreasureHunt/add_user.php",urlParameters,this);
                add_user.execute();
                while(add_user.respons.equals("")){

                }
                Log.d("HTTPSN", "register: " + add_user.respons);

                Intent in = new Intent(Registrer.this,SplashScreen.class);
                in.putExtra("username",username.getText().toString());
                startActivity(in);
                finish();

            }
        }
    }
}
