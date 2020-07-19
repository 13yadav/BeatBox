package com.learning.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.learning.android.databinding.FragmentBeatBoxBinding;
import com.learning.android.databinding.ListItemSoundBinding;

import java.util.List;

public class BeatBoxFragment extends Fragment {

    private BeatBox beatBox;

    public static BeatBoxFragment newInstance(){
        return new BeatBoxFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        beatBox = new BeatBox(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentBeatBoxBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_beat_box, container, false);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        binding.recyclerView.setAdapter(new SoundAdapter(beatBox.getSounds()));
        binding.seekBarPlaybackSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                BeatBox.onProgressChanged(seekBar, progress, fromUser);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        beatBox.release();
    }

    private class SoundHolder extends RecyclerView.ViewHolder {
        private ListItemSoundBinding binding;

        public SoundHolder(ListItemSoundBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.setViewModel(new SoundViewModel(beatBox));
        }

        public void bind(Sound sound){
            binding.getViewModel().setSound(sound);
            binding.executePendingBindings();
        }
    }


    private class SoundAdapter extends RecyclerView.Adapter<SoundHolder> {
        private List<Sound> sounds;

        public SoundAdapter(List<Sound> sounds){
            this.sounds = sounds;
        }

        @NonNull
        @Override
        public SoundHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            ListItemSoundBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_item_sound, parent, false);

            return new SoundHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull SoundHolder holder, int position) {
            Sound sound = sounds.get(position);
            holder.bind(sound);
        }

        @Override
        public int getItemCount() {
            return sounds.size();
        }
    }
}
