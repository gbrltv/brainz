package com.compscitutorials.basigarcia.navigationdrawervideotutorial;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.co.recruit_lifestyle.android.widget.PlayPauseButton;

/**
 * Created by Matheus on 15/12/2016.
 */
public class DiscoverArtistFragment extends Fragment{

        Button buttonStop,buttonStart ;
        MediaPlayer mediaplayer;
        Boolean v = true;
        String AudioURL;
        DBHandler db;

        public DiscoverArtistFragment() {
            // Required empty public constructor
        }

        public static MyAppAdapter myAppAdapter;
        public static ViewHolder viewHolder;
        private ArrayList<Track> al;
        private List<Track> ltr;
        private SwipeFlingAdapterView flingContainer;
        View rootView;
        JSONObject Tracks;
        String playlist_id;
        LayoutInflater inflater2;
        public static void removeBackground() {

            viewHolder.background.setVisibility(View.GONE);
            myAppAdapter.notifyDataSetChanged();

        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            inflater2 = inflater;
            rootView = inflater.inflate(R.layout.fragment_main, container, false);

            flingContainer = (SwipeFlingAdapterView) rootView.findViewById(R.id.frame);
            final String token = getArguments().getString("token");
            final String artistID = getArguments().getString("artid");
            String user_id = getArguments().getString("user_id");

            db = new DBHandler(rootView.getContext());

            ltr = db.getTracks(0);

            al = new ArrayList<>();
            callRecommendations(token,artistID);
          /*  for (Track l : ltr){
                al.add(new Data(l.getAlbumImage(),l.getTrackName(),l.getArtistName(), l.getPreviewUrl(),l.getId()));
            }
               */

            myAppAdapter = new MyAppAdapter(al, rootView.getContext());
            flingContainer.setAdapter(myAppAdapter);
            flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
                @Override
                public void removeFirstObjectInAdapter() {

                }

                @Override
                public void onLeftCardExit(Object dataObject) {
                    //db.updateStatus(al.get(0).getId(), 2);
                    al.remove(0);

                    mediaplayer.stop();

                    //mediaplayer.release();
                    mediaplayer.reset();

                    mediaplayer = new MediaPlayer();
                    mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                    myAppAdapter.notifyDataSetChanged();
                }

                @Override
                public void onRightCardExit(Object dataObject) {
                   // db.updateStatus(al.get(0).getId(), 1);
                    al.remove(0);
                    mediaplayer.stop();
                    //mediaplayer.release();
                    mediaplayer.reset();
                    mediaplayer = new MediaPlayer();
                    mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                    myAppAdapter.notifyDataSetChanged();
                }

                @Override
                public void onAdapterAboutToEmpty(int itemsInAdapter) {
                    Log.d("itemsInAdapter", "" + itemsInAdapter);
                    callRecommendations(token,artistID);
                    //viewHolder.notify();
                    //ltr = db.getTracks(0);

                    for (Track l : ltr){
                        al.add(l);
                    }
                    Collections.shuffle(al);
                    myAppAdapter.notifyDataSetChanged();
                }

                @Override
                public void onScroll(float scrollProgressPercent) {

                    View view = flingContainer.getSelectedView();
                    view.findViewById(R.id.background).setAlpha(0);
                    view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0); view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
                    //Log.d("myAppAdapter>>",""+myAppAdapter.getCount());
                }
            });

            // Inflate the layout for this fragment
            return rootView;
        }

        public void callRecommendations(String token, String artid){
            try {
                //DEBUG

                //Alterar Thread
                JSONObject Recommendations = new threadrecommendationsArtist().execute(token,artid).get();
                for (int i=0 ; i < 30; i++){
                    //DEBUG
                    JSONObject track = (JSONObject) Recommendations.getJSONArray("items").get(i);
                    Log.d("TRACK","--"+track);
                    Track tr = new Track();

                    tr.setId(track.getString("id_da_musica"));
                    tr.setTrackName(track.getString("nome_da_musica"));
                    tr.setAlbumName(track.getString("nome_do_album"));
                    tr.setArtistName(track.getString("nome_do_artista"));
                    tr.setAlbumImage(track.getString("imagem_do_album"));
                    tr.setPreviewUrl(track.getString("preview"));
                    tr.setArtistID(track.getString("id_do_artista"));
                    tr.setStatus(0);

                    al.add(tr);
                }

            } catch(Exception e){
                e.printStackTrace();
            }
        }

        public static class ViewHolder {
            public static FrameLayout background;
            public TextView DataText;
            public TextView Artist;
            public ImageView cardImage;
        }


        public class MyAppAdapter extends BaseAdapter {
            public List<Track> parkingList;
            public Context context;

            private MyAppAdapter(List<Track> apps, Context context) {
                this.parkingList = apps;
                this.context = context;
            }

            @Override
            public int getCount() {
                return parkingList.size();
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                View rowView = convertView;

                if (rowView == null) {

                    rowView = inflater2.inflate(R.layout.item, parent, false);
                    // configure view holder
                    viewHolder = new ViewHolder();
                    viewHolder.DataText = (TextView) rowView.findViewById(R.id.bookText);
                    viewHolder.Artist = (TextView) rowView.findViewById(R.id.bookText2);
                    viewHolder.background = (FrameLayout) rowView.findViewById(R.id.background);
                    viewHolder.cardImage = (ImageView) rowView.findViewById(R.id.cardImage);

                    viewHolder.DataText.setText(parkingList.get(position).getTrackName() + "");
                    viewHolder.Artist.setText(parkingList.get(position).getArtistName()+"");

                    AudioURL = parkingList.get(position).getPreviewUrl();
                    Glide.with(rootView.getContext()).load(parkingList.get(position).getAlbumImage()).into(viewHolder.cardImage);

                    mediaplayer = new MediaPlayer();
                    mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                    try {

                        mediaplayer.setDataSource(AudioURL);
                        mediaplayer.prepare();

                    } catch (IllegalArgumentException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (SecurityException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    final PlayPauseButton playPauseButton =
                            (PlayPauseButton) rowView.findViewById(R.id.button1);
                    playPauseButton.setColor(Color.WHITE);
                    playPauseButton.setOnControlStatusChangeListener(
                            new PlayPauseButton.OnControlStatusChangeListener() {
                                @Override public void onStatusChange(View view, boolean status) {
                                    if (status) {
                                        mediaplayer.start();
                                    } else {
                                        mediaplayer.pause();
                                    }
                                }
                            });

                    rowView.setTag(viewHolder);

                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                return rowView;
            }
        }

    }
