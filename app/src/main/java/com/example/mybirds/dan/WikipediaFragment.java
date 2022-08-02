package com.example.mybirds.dan;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mybirds.R;

import java.util.ArrayList;
import java.util.Arrays;

public class WikipediaFragment extends Fragment {
    private BirdModel birdModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wikipedia, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View root = getView();

        birdModel = BirdModel.getInstance();

        ((Button)root.findViewById(R.id.addBird)).setOnClickListener(v -> birdModel.add(
                new Bird(
                        "Chouette effraie",
                        "Tyto alba",
                        37,
                        325,
                        new ArrayList<String>(Arrays.asList("Afrique","Asie du Sud","Australie","Europe")),
                        new ArrayList<String>(Arrays.asList("Petits rongeurs","Petits mamofères","Petits oiseaux")),
                        false,
                        R.drawable.chouette_effraie,
                        "L'effraie des clochers (Tyto alba) est une chouette aussi couramment appelée chouette effraie ou dame blanche. L'espèce peuple tous les continents, à l'exception de l'Antarctique et certaines îles. C'est l'espèce de Strigiformes la plus répandue au monde."
                )));
    }
}