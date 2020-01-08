package com.example.scarymonstersplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubePlayer;

import static androidx.core.content.ContextCompat.startActivity;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private Context mContext;
    private Cursor mCursor;
    private int amount = 0;

    public ItemAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView nameText , noteText, timeText,vidText,realsec;

        public ItemViewHolder(View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.Title);
            noteText = itemView.findViewById(R.id.Note);
            timeText = itemView.findViewById(R.id.Time);
            vidText = itemView.findViewById(R.id.vid);
            realsec = itemView.findViewById(R.id.realsec);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            MainActivity m = (MainActivity) mContext;
            String vid = vidText.getText().toString();
            int Realsec = Integer.parseInt(realsec.getText().toString());
            //Toast.makeText(mContext,vid + amount,Toast.LENGTH_LONG).show();
            m.player.cueVideo(vid,Realsec);

        }

        @Override
        public boolean onLongClick(View v) {

            Intent intent = new Intent(mContext,UpdateActivity.class);
            intent.putExtra("ID",new Long((int)itemView.getTag()));
            mContext.startActivity(intent);
            return true;
        }

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        String name = mCursor.getString(mCursor.getColumnIndex(DBitem.KEY_NAME));
        amount = mCursor.getInt(mCursor.getColumnIndex(DBitem.KEY_SECOND));
        String note = mCursor.getString(mCursor.getColumnIndex(DBitem.KEY_NOTE));
        String VID = mCursor.getString(mCursor.getColumnIndex(DBitem.KEY_VID));
        int id= mCursor.getInt(mCursor.getColumnIndex(DBitem.KEY_ID));

       holder.nameText.setText(name);
       holder.noteText.setText(note);
       holder.timeText.setText(parseTime(amount));
       holder.vidText.setText(VID);
       holder.realsec.setText(String.valueOf(amount));
       holder.itemView.setTag(id);

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }

    private String parseTime(int a){
        int hour,min;
        int sec = (a%60000)/1000;
        if (a>=3600000){
            hour = a/3600000;
            min = (a%3600000)/60000;
            return  hour +"h"+min+"m"+sec+"s";
        }else if (a >=60000){
            min = a/60000;
            return  min+"m"+sec+"s";
        }else{
            return  sec+"s";
        }


    }


}
