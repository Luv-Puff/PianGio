package com.example.scarymonstersplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddVideoActivity extends AppCompatActivity {
    private MyDB myDB;
    //private ItemAdapter mAdapter;
    private EditText addname,addvid,addH,addM,addS,addnote;
    private int amount;
    private Button addbutton,get_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);

        myDB = new MyDB(this);

        addname = findViewById(R.id.add_name);
        addvid=findViewById(R.id.add_vid);
        addH = findViewById(R.id.add_h);
        addM = findViewById(R.id.add_m);
        addS = findViewById(R.id.add_s);
        addnote = findViewById(R.id.add_note);

        addbutton = findViewById(R.id.add_button);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 amount = Integer.parseInt(addH.getText().toString())*3600000+
                        Integer.parseInt(addM.getText().toString())*60000+
                        Integer.parseInt(addS.getText().toString())*1000;
                addItem(amount);
            }
        });
        get_id=findViewById(R.id.get_ID);
        get_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_vid = extractYTId(addvid.getText().toString());
                addvid.setText(new_vid);

            }
        });

    }

    private void addItem(int mAmount) {
        if (addname.getText().toString().trim().length() == 0 || mAmount == 0||addvid.getText().toString().trim().length() == 0) {
            return;
        }

        DBitem item = new DBitem();
        item.setName(addname.getText().toString());
        item.setVid(addvid.getText().toString());
        item.setSecond(mAmount);
        item.setNote(addnote.getText().toString());

        boolean insert = myDB.addItem(item);
        if (insert){
            Toast.makeText(this, "Video wad added!", Toast.LENGTH_LONG).show();
            returntomain();
        }else{
            Toast.makeText(this, "Error...", Toast.LENGTH_LONG).show();
        }

    }


    private void returntomain(){
        Intent intent =new Intent(AddVideoActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public static String extractYTId(String ytUrl) {
        String vId = null;
        Pattern pattern = Pattern.compile(
                "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ytUrl);
        if (matcher.matches()){
            vId = matcher.group(1);
        }
        return vId;
    }

}
