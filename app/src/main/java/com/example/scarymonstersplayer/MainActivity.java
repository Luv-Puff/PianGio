package com.example.scarymonstersplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.Collections;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.ListPopupWindow.MATCH_PARENT;
import static com.example.scarymonstersplayer.apikey.key;


public class MainActivity extends YouTubeFailureRecoveryActivity implements YouTubePlayer.OnFullscreenListener {

    //RetainedFragment retainedFragment ;
    private MyDB myDB;
    private ItemAdapter mAdapter;
    private Button addbutton,shareButton;
    private LinearLayout baseLayout ;
    private View otherViews ;
    private boolean fullscreen;
    private YouTubePlayerView youTubePlayerView;
    public YouTubePlayer player;
    //private YouTubePlayer.OnInitializedListener MonInitializedListener;
    private RecyclerView recyclerView;
    public String videoId = "V79zSSDweUA";// I, Giorno Giovanna, have a dream.
    public int second = 47000;// *piano sound*

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();

        if(mNetworkInfo != null)
        {

        }else {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("警告:");
            alertDialog.setMessage("沒有連線，八成是國家機器又開始動了");
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.setButton(Dialog.BUTTON_POSITIVE,"重新確認連線", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();

                }
            });

            alertDialog.show();
        }

        myDB = new MyDB(this);

        youTubePlayerView =  (YouTubePlayerView) findViewById(R.id.YoutubeView);
        baseLayout = findViewById(R.id.layout);
        otherViews =  findViewById(R.id.other_views);
        addbutton = findViewById(R.id.add_video);
        youTubePlayerView.initialize(key, (YouTubePlayer.OnInitializedListener) this);

        doLayout();

        recyclerView = findViewById(R.id.otherRecycler);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ItemAdapter(this, myDB.getData());
        recyclerView.setAdapter(mAdapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP| ItemTouchHelper.DOWN|ItemTouchHelper.START|ItemTouchHelper.END,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    int pos_d = viewHolder.getAdapterPosition();
                    int pos_t = target.getAdapterPosition();
                    swap(new Long((int)viewHolder.itemView.getTag()),new Long((int)target.itemView.getTag()));
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem(new Long((int)viewHolder.itemView.getTag()));
            }
        }).attachToRecyclerView(recyclerView);

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this,AddVideoActivity.class);
                startActivity(intent);
            }
        });
        shareButton = findViewById(R.id.share_video);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String extraLink = "https://youtu.be/"+videoId+"?t="+second/1000;
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, extraLink);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });

    }


    @Override
    public void onResume(){
        super.onResume();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            otherViews.setVisibility(View.GONE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            LinearLayout.LayoutParams playerParams =
                    (LinearLayout.LayoutParams) youTubePlayerView.getLayoutParams();
            playerParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            playerParams.height = LinearLayout.LayoutParams.MATCH_PARENT;

        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        this.player = player;
        // Specify that we want to handle fullscreen behavior ourselves.
        player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
        player.setOnFullscreenListener(this);
        if (!wasRestored) {
            Cursor newcursor = myDB.getData();
            if (newcursor.getCount()!=0){
                newcursor.moveToPosition(0);
                videoId = newcursor.getString(newcursor.getColumnIndex(DBitem.KEY_VID));
                second = newcursor.getInt(newcursor.getColumnIndex(DBitem.KEY_SECOND));
            }
            player.cueVideo(videoId,second);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubePlayerView;
    }


    private void doLayout() {
        LinearLayout.LayoutParams playerParams =
                (LinearLayout.LayoutParams) youTubePlayerView.getLayoutParams();
        if (fullscreen) {
            // When in fullscreen, the visibility of all other views than the player should be set to
            // GONE and the player should be laid out across the whole screen.
            playerParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            playerParams.height = LinearLayout.LayoutParams.MATCH_PARENT;

            otherViews.setVisibility(View.GONE);
        } else {
            // This layout is up to you - this is just a simple example (vertically stacked boxes in
            // portrait, horizontally stacked in landscape).
            otherViews.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams otherViewsParams = otherViews.getLayoutParams();
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                playerParams.width = otherViewsParams.width = 0;
                playerParams.height = WRAP_CONTENT;
                otherViewsParams.height = MATCH_PARENT;
                playerParams.weight = 1;
                baseLayout.setOrientation(LinearLayout.HORIZONTAL);
            } else {
                playerParams.width = otherViewsParams.width = MATCH_PARENT;
                playerParams.height = WRAP_CONTENT;
                playerParams.weight = 0;
                otherViewsParams.height = 0;
                baseLayout.setOrientation(LinearLayout.VERTICAL);
            }
        }
    }



    @Override
    public void onFullscreen(boolean isFullscreen) {
        fullscreen = isFullscreen;
        doLayout();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // 階段變更時
        super.onConfigurationChanged(newConfig);
        doLayout();
    }

    private void removeItem(long id) {
        myDB.deleteData(id);
        mAdapter.swapCursor(myDB.getData());
    }

    private void swap(long d,long t){
        myDB.swapData(d,t);
        mAdapter.swapCursor(myDB.getData());
    }




}
