package com.udacity.popular_movies_2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TrailerFragment extends Fragment {

    public TrailerFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_trailer, container, false);

        TextView trailerTitleView = rootView.findViewById(R.id.trailer_title);

        trailerTitleView.setText(getArguments().getString("trailerTitle"));

        return rootView;
    }
}
