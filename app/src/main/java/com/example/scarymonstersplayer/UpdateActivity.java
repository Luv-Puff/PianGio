package com.example.scarymonstersplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
    private Button update,get_id;
    private Long updateID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);
        update = findViewById(R.id.add_button);
        update.setText("Update");
        myDB = new MyDB(this);
        updateID = getIntent().getLongExtra("ID",0);

        addname = findViewById(R.id.add_name);
        addvid=findViewById(R.id.add_vid);
        addH = findViewById(R.id.add_h);
        addM = findViewById(R.id.add_m);
        addS = findViewById(R.id.add_s);
        addnote = findViewById(R.id.add_note);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int h,m,s;
                h = Integer.parseInt(addH.getText().toString())*3600000;
                amount = Integer.parseInt(addH.getText().toString())*3600000+
                        Integer.parseInt(addM.getText().toString())*60000+
                        Integer.parseInt(addS.getText().toString())*1000;
                update(amount);
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
    private void update(int mAmount){
        if (addname.getText().toString().trim().length() == 0 || mAmount == 0||addvid.getText().toString().trim().length() == 0) {
            return;
        }

        DBitem item = new DBitem();
        item.setName(addname.getText().toString());
        item.setVid(addvid.getText().toString());
        item.setSecond(mAmount);
        item.setNote(addnote.getText().toString());
        myDB.updateData(updateID,item);
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
                "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ytUrl);
        if (matcher.matches()){
            vId = matcher.group(1);
        }
        return vId;
    }
    public static String getTitle(String youtubeUrl) {
        try {
            if (youtubeUrl != null) {
                URL embededURL = new URL("http://www.youtube.com/oembed?url=" + youtubeUrl + "&format=json");
                return new JSONObject(IOUtils.toString(embededURL)).getString("title");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
