package com.example.mikaelpaavilainen.treasurehunt.Activitys;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mikaelpaavilainen.treasurehunt.HTTPREQUEST.HttpRequest;
import com.example.mikaelpaavilainen.treasurehunt.R;
import com.example.mikaelpaavilainen.treasurehunt.database.Repository;
import com.example.mikaelpaavilainen.treasurehunt.database.eventList;

import org.json.JSONObject;

import java.util.List;


public class SplashScreen extends AppCompatActivity {
    public int count;
    EditText username;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        username = findViewById(R.id.editText2);
        Intent tIntent = getIntent();
        Bundle b = tIntent.getExtras();
        if(b != null){
            username.setText(b.getString("username"));
        }





    }
    public void changeIntent(String username,String hash){
        Intent myIntent = new Intent(SplashScreen.this, Maps.class);
        myIntent.putExtra("name",username);
        myIntent.putExtra("hash",hash);
        startActivity(myIntent);

    }
    public void start_reg(View view){
        Intent newInt = new Intent(SplashScreen.this,Registrer.class);
        startActivity(newInt);
    }
    public void login_check(View v){

        EditText password = findViewById(R.id.editText3);

        TextView t = findViewById(R.id.result);


        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //check if databse is empty
        if(cm.getActiveNetworkInfo() != null) {
            HttpRequest test = new HttpRequest();
            String urlParameters = "username=" + username.getText() + "&password=" + password.getText();
            test.setValues("http://192.168.216.119/TreasureHunt/login_user.php", urlParameters, this);
            test.execute();


            Log.d("HTTPS", "login_check: ");
            while (test.respons.equals("")) { //Checks if respons
                //Log.d("HTTPS_S", "login_check: 123 - " + count);
                count++;
            }
            t.setText(test.respons);
            try {
                JSONObject json = new JSONObject(test.respons);
                Log.d("HTTPS1", "login_check: " + json.getString("name"));

                changeIntent(json.getString("name"), json.getString("hash"));
            } catch (Exception e) {
                Log.d("HTTPS1", "login_check: " + e);
            }
        }
        else{
            Toast.makeText(this,"Cant Download Events, Please Connect To Internet" ,Toast.LENGTH_LONG).show();
            changeIntent("Unknown","???");
        }
    }
}
