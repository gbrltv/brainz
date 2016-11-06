package com.compscitutorials.basigarcia.navigationdrawervideotutorial;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import jp.co.recruit_lifestyle.android.widget.PlayPauseButton;

/**
 * Created by Matheus on 08/10/2016.
 */
public class Match_Adapter_Expand extends RecyclerView.Adapter<Match_Adapter_Expand.MyViewHolder>  {
    MediaPlayer mediaplayer;
    private List<Match> matchesList;
    Bitmap albumimage;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView artist;
        public TextView preview;
        public ImageView img;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            img = (ImageView) view.findViewById(R.id.matchImage);
            artist = (TextView) view.findViewById(R.id.artist);
            preview = (TextView) view.findViewById(R.id.preview);


            final PlayPauseButton playPauseButton =
                    (PlayPauseButton) view.findViewById(R.id.play);
            playPauseButton.setColor(Color.WHITE);
            playPauseButton.setOnControlStatusChangeListener(
                    new PlayPauseButton.OnControlStatusChangeListener() {
                        @Override public void onStatusChange(View view, boolean status) {
                            if (status) {
                                mediaplayer = new MediaPlayer();
                                mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                                try {
                                    mediaplayer.setDataSource((String)preview.getText());
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
        }
    }

    public Match_Adapter_Expand(List<Match> moviesList) {
        this.matchesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expandable_list, parent, false);
        //xml do elemento
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Match match = matchesList.get(position);
        holder.title.setText(match.getTitle());
        holder.artist.setText(match.getArtist());
        holder.preview.setText(match.getPreview());

        try {
            albumimage = new LoadImageFromUrl().execute(match.getImgUri()).get();
            holder.img.setImageBitmap(albumimage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
    @Override
    public int getItemCount() {
        return matchesList.size();
    }
}
