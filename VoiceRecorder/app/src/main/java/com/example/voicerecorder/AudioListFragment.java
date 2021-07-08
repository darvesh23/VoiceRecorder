package com.example.voicerecorder;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AudioListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AudioListFragment extends Fragment implements AudioListAdapter.onItemListClick{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ConstraintLayout playerSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView audioList;
    private AudioListAdapter audioListAdapter;
    private File[] allFiles;
    private SeekBar playerSeekbar;
    private Handler seekbarHandler;
    private Runnable updateSeekbar;
    private MediaPlayer mediaPlayer=null;
    private boolean isPlaying=false;
    private ImageView playBtn;
    private TextView playerHeader;
    private TextView playerFilename;
    private File fileToPlay;
    public AudioListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AudioListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AudioListFragment newInstance(String param1, String param2) {
        AudioListFragment fragment = new AudioListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_audio_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playerSheet = view.findViewById(R.id.player_sheet);
        bottomSheetBehavior= BottomSheetBehavior.from(playerSheet);

        playBtn = view.findViewById(R.id.player_play_btn);
        playerHeader = view.findViewById(R.id.player_header_title);
        playerFilename = view.findViewById(R.id.player_filename);

        audioList = view.findViewById(R.id.audio_list_view);

        playerSeekbar = view.findViewById(R.id.player_seekbar);


        String path = getActivity().getExternalFilesDir("/").getAbsolutePath();
        File directory = new File(path);
        allFiles = directory.listFiles();
        audioListAdapter = new AudioListAdapter(allFiles,this);

        audioList.setHasFixedSize(true);
        audioList.setLayoutManager(new LinearLayoutManager(getContext()));
        audioList.setAdapter(audioListAdapter);



        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == BottomSheetBehavior.STATE_HIDDEN){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //NOT NEEDED HERE
            }
        });


        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying){
                    pauseAudio();
                }
                else {
                    if(fileToPlay!=null){
                        resumeAudio();
                    }

                }
            }
        });

        playerSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                pauseAudio();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (fileToPlay != null) {
                    int progress = seekBar.getProgress();
                    mediaPlayer.seekTo(progress);
                    resumeAudio();


                }
                }
        });
    }

    @Override
    public void onClickListener(File file, int position) {
        fileToPlay=file;
        if (isPlaying){
            stopAudio();
            playAudio(fileToPlay);

        }
        else {
            playAudio(fileToPlay);
            }
    }

    private void pauseAudio(){
        playBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.player_play_btn,null));
        mediaPlayer.pause();
        isPlaying= false;
        seekbarHandler.removeCallbacks(updateSeekbar);

    }
    private void resumeAudio(){
        playBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.player_pause_btn,null));
        mediaPlayer.start();
        isPlaying=true;
        updateRunnable();
        seekbarHandler.postDelayed(updateSeekbar,0);


    }

    private void stopAudio() {
        playBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.player_play_btn,null));
        playerHeader.setText("Stopped");
        isPlaying=false;
        mediaPlayer.stop();
        seekbarHandler.removeCallbacks(updateSeekbar);
    }

    private void playAudio(File fileToPlay) {
        mediaPlayer = new MediaPlayer();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
       try {
           mediaPlayer.setDataSource(fileToPlay.getAbsolutePath());

           mediaPlayer.prepare();
           mediaPlayer.start();
       }
       catch (IOException e){
           e.printStackTrace();
           }
        mediaPlayer.setLooping(true);
       playBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.player_pause_btn,null));
       playerFilename.setText(fileToPlay.getName());
       playerHeader.setText("Playing");
        isPlaying =true;


        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopAudio();
                playerHeader.setText("Finished");
            }
        });

         playerSeekbar.setMax(mediaPlayer.getDuration());

         seekbarHandler = new Handler();
         updateRunnable();
         seekbarHandler.postDelayed(updateSeekbar,0);    }

    private void updateRunnable() {
        updateSeekbar = new Runnable() {
            @Override
            public void run() {
                playerSeekbar.setProgress(mediaPlayer.getCurrentPosition());
                seekbarHandler.postDelayed(this , 500);

            }
        };
    }

    @Override
    public void onStop() {
        super.onStop();
        stopAudio();
    }
}