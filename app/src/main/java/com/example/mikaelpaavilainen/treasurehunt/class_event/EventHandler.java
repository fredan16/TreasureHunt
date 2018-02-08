package com.example.mikaelpaavilainen.treasurehunt.class_event;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mikaelpaavilainen.treasurehunt.Activitys.Maps;
import com.example.mikaelpaavilainen.treasurehunt.R;

public class EventHandler extends AppCompatActivity {

    TextView name;
    TextView timer;
    TextView theProblem;
    EditText et;
    String answer;

    int userscore;
    int worth;

    String Username;
    String Userhash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_handler);


        Intent in = getIntent();
        Bundle b = in.getExtras();
        name = findViewById(R.id.textView);
        timer = findViewById(R.id.textView2);
        theProblem = findViewById(R.id.textView4);
        et = findViewById(R.id.editText);
        String newName= b.getString("name");
        String newLevel= b.getString("level");
        String newTimer= b.getString("timer");
        answer = b.getString("answer");
        theProblem.setText(b.getString("problem"));

        userscore = b.getInt("score");
        worth = b.getInt("worth");

        //user stuff
        this.Username = b.getString("username");
        this.Userhash = b.getString("userhash");


        name.setText(newName + " - Level:" + newLevel);
        timer.setText("Timer: " + newTimer);



    }
    public void checkAnswer(View view){
        Log.d("GMAP3", "checkAnswer: Try " + answer + " | " + '"'+et.getText().toString()+'"');
        String temp1 = answer;
        String temp2 = et.getText().toString();

        if(temp1.compareToIgnoreCase(temp2) == 0){
            Log.d("GMAP3", "checkAnswer: Working");
            name.setText("Correct");
            Intent in = new Intent(EventHandler.this,Maps.class);
            in.putExtra("newScore",worth);
            in.putExtra("name",Username);
            in.putExtra("hash",Userhash);
            setResult(Activity.RESULT_OK,in);
            finish();
        }
    }
}
