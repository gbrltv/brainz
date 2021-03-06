package com.compscitutorials.basigarcia.navigationdrawervideotutorial;

/**
 * Created by Matheus on 07/10/2016.
 */
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import jp.co.recruit_lifestyle.android.widget.PlayPauseButton;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {
    MediaPlayer mediaplayer;
    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<String>> expandableListDetail;
    private HashMap<String, Data> hashData;
    Bitmap albumimage;
    public ImageView img;


    public CustomExpandableListAdapter(Context context, List<String> expandableListTitle,
                                       HashMap<String, List<String>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
        DBHandler db = new DBHandler(context);
        hashData = new HashMap<String, Data>();
        for (Track t : db.getTracks(1)){
            Data d = new Data(t.getAlbumImage(), t.getTrackName(), t.getArtistName(), t.getPreviewUrl(), t.getId());
            hashData.put(t.getId(), d);
        }


    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }
        TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.expandedListItem);
        expandedListTextView.setText(expandedListText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        final String listTitle = (String) getGroup(listPosition);
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) this.context.
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.list_group, null);
            }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        listTitleTextView.setText(hashData.get(listTitle).getDescription());

        TextView listAuthorTextView = (TextView) convertView
                .findViewById(R.id.listAuthor);
        listAuthorTextView.setTypeface(Typeface.SANS_SERIF);
        listAuthorTextView.setText(hashData.get(listTitle).getArtist());

        img = (ImageView) convertView.findViewById(R.id.matchImage);
        try {
            albumimage = new LoadImageFromUrl().execute(hashData.get(listTitle).getImagePath()).get();
            img.setImageBitmap(albumimage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        final PlayPauseButton playPauseButton =
                (PlayPauseButton) convertView.findViewById(R.id.play);
        playPauseButton.setColor(Color.WHITE);
        playPauseButton.setOnControlStatusChangeListener(

                new PlayPauseButton.OnControlStatusChangeListener() {
                    @Override public void onStatusChange(View view, boolean status) {
                        if (status) {
                            mediaplayer = new MediaPlayer();
                            mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                            try {
                                mediaplayer.setDataSource(hashData.get(listTitle).getPreviewPath());
                                mediaplayer.prepare();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            mediaplayer.start();
                        } else {
                            mediaplayer.pause();
                        }
                    }
                });

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

}
