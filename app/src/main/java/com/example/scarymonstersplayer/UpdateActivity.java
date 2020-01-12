package com.example.scarymonstersplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateActivity extends AppCompatActivity {
    private MyDB myDB;
    //private ItemAdapter mAdapter;
    private EditText addname,addvid,addH,addM,addS,addnote;
    private int amount;
    private Button update;
    private Long updateID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);
        update = findViewById(R.id.add_button);
        update.setText("Update");
        myDB = new MyDB(this);
        updateID = getIntent().getLongExtra("ID",0);

        Cursor cursor = myDB.getSingleItem(updateID);
        cursor.moveToPosition(0);
        int h = parsehour(cursor.getInt(cursor.getColumnIndex(DBitem.KEY_SECOND)));
        int m = parsemin(cursor.getInt(cursor.getColumnIndex(DBitem.KEY_SECOND)));
        int s = parsesec(cursor.getInt(cursor.getColumnIndex(DBitem.KEY_SECOND)));
        String mName = cursor.getString(cursor.getColumnIndex(DBitem.KEY_NAME));
        String mVid = cursor.getString(cursor.getColumnIndex(DBitem.KEY_VID));
        String mNote = cursor.getString(cursor.getColumnIndex(DBitem.KEY_NOTE));

        addname = findViewById(R.id.add_name);
        addname.setText(mName);
        addvid=findViewById(R.id.add_vid);
        addvid.setText(mVid);
        addH = findViewById(R.id.add_h);
        addH.setText(String.valueOf(h));
        addM = findViewById(R.id.add_m);
        addM.setText(String.valueOf(m));
        addS = findViewById(R.id.add_s);
        addS.setText(String.valueOf(s));
        addnote = findViewById(R.id.add_note);
        addnote.setText(mNote);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_vid = extractYTId(addvid.getText().toString());
                if (!new_vid.equals("")){
                    addvid.setText(new_vid);
                }else{
                    Toast.makeText(UpdateActivity.this, "Invalid String!", Toast.LENGTH_LONG).show();
                    return;
                }

                amount = Integer.parseInt(addH.getText().toString())*3600000+
                        Integer.parseInt(addM.getText().toString())*60000+
                        Integer.parseInt(addS.getText().toString())*1000;
                update(amount);
            }
        });

    }
    private void update(int mAmount){
        if (addname.getText().toString().trim().length() == 0 ||addvid.getText().toString().trim().length() == 0) {
            return;
        }

        DBitem item = new DBitem();
        item.setName(addname.getText().toString());
        item.setVid(addvid.getText().toString());
        item.setSecond(mAmount);
        item.setNote(addnote.getText().toString());
        myDB.updateData(updateID,item);
        Toast.makeText(this, "Video wad updated!", Toast.LENGTH_LONG).show();
        returntomain();

    }
    private void returntomain(){
        Intent intent =new Intent(UpdateActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    public static String extractYTId(String ytUrl) {
        String vId = null;
        Pattern pattern = Pattern.compile(
                "https?://(?:[0-9A-Z-]+\\.)?(?:youtu\\.be/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|</a>))[?=&+%\\w]*",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ytUrl);
        if (matcher.matches()){
            vId = matcher.group(1);
            if (vId.length()==11){
                return vId;
            }else {
                return "";
            }
        }else if (ytUrl.length()==11){
            return  ytUrl;
        }else{
            return "";
        }
    }
    private int parsesec (int a){
        return  (a%60000)/1000;


    }
    private int parsemin (int a){
        int min;
        if (a>=3600000){
            min = (a%3600000)/60000;
            return  min;
        }else if (a >=60000){
            min = a/60000;
            return  min;
        }else{
            return  0;
        }
    }
    private int parsehour (int a){
        int hour;
        if (a>=3600000){
            hour = a/3600000;
            return  hour;
        }else{
            return 0;
        }
    }

}