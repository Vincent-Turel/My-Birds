package com.example.mybirds.dan;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.mybirds.R;

import java.util.Observable;
import java.util.Observer;

/**
 * A simple {@link Fragment} subclass.
 */
public class BirdListFragment extends Fragment implements IBirdAdapterListener, Observer {

    private ListView birdListView;
    private BirdAdapter birdAdapter;
    private Intent chosenBirdIntent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        BirdModel.getInstance().addObserver(this);
        return inflater.inflate(R.layout.fragment_bird_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View root = getView();
        birdListView = (ListView) root.findViewById(R.id.allBirds);
        birdAdapter = new BirdAdapter(getContext(),BirdModel.getInstance().getBirdList());
        birdListView.setAdapter(birdAdapter);
        birdAdapter.addListener(this);
        chosenBirdIntent = new Intent(this.getContext(),ChosenBirdActivity.class);
    }

    @Override
    public void onClickBird(Bird item) {
        chosenBirdIntent.putExtra("bird",item);
        startActivity(chosenBirdIntent);
    }

    @Override
    public void update(Observable o, Object arg) {
        birdAdapter.setList(BirdModel.getInstance().getBirdList());
        birdAdapter.notifyDataSetChanged();
    }
}