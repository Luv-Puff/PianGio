package com.example.scarymonstersplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddVideoActivity extends AppCompatActivity {
    private MyDB myDB;
    private ItemAdapter mAdapter;
    private EditText addname,addvid,addH,addM,addS,addnote;
    private TextView mTextViewAmount;
    private int a;
    private Button addbutton;

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
                 a = Integer.parseInt(addH.getText().toString())*3600000+
                        Integer.parseInt(addM.getText().toString())*60000+
                        Integer.parseInt(addS.getText().toString())*1000;
                addItem(a);
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
}
