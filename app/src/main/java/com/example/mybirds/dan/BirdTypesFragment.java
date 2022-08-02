package com.example.mybirds.dan;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mybirds.R;

import org.w3c.dom.Text;

import java.util.Observable;
import java.util.Observer;

/**
 * A simple {@link Fragment} subclass.
 */
public class BirdTypesFragment extends Fragment implements Observer {
    private TextView viewSmallBirds;
    private TextView viewMediumBirds;
    private TextView viewLargeBirds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        BirdModel.getInstance().addObserver(this);
        return inflater.inflate(R.layout.fragment_bird_types, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View root = getView();
        viewSmallBirds = (TextView) root.findViewById(R.id.smallBirds);
        viewMediumBirds = (TextView) root.findViewById(R.id.mediumBirds);
        viewLargeBirds = (TextView) root.findViewById(R.id.largeBirds);
        updateCountTypes();

    }

    @Override
    public void update(Observable o, Object arg) {
       updateCountTypes();
    }

    private void updateCountTypes(){
        viewSmallBirds.setText("Petits oiseaux : "+Double.toString(BirdModel.getInstance().howManySmallBird()));
        viewMediumBirds.setText("Moyens oiseaux : "+Double.toString(BirdModel.getInstance().howManyMediumBird()));
        viewLargeBirds.setText("Grands oiseaux : "+Double.toString(BirdModel.getInstance().howManyLargeBird()));
    }
}