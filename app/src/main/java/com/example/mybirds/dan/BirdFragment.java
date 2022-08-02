package com.example.mybirds.dan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mybirds.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BirdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BirdFragment extends Fragment {

    private Bird bird;
    private ImageView birdPicture;
    private TextView commonName;
    private TextView latinName;
    private TextView size;
    private TextView weight;
    private ListView areas;
    private ArrayList<String> areaList;
    private ArrayAdapter<String> areaAdapter;
    private ListView food;
    private ArrayList<String> foodList;
    private ArrayAdapter<String> foodAdapter;
    private TextView endangered;
    private TextView description;

    public BirdFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param aBird Parameter 1.
     * @return A new instance of fragment BirdFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BirdFragment newInstance(Bird aBird) {
        BirdFragment birdFragment = new BirdFragment();
        birdFragment.bird = aBird;
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return birdFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_bird, container, false);
        initialiseAllView(root);
        return root;
    }
    private void initialiseAllView(View root){
        instanceOfPicture(root);
        instanceOfLatinName(root);
        instanceOfCommonName(root);
        instanceOfSize(root);
        instanceOfWeight(root);
        areaList = new ArrayList<>();
        createAreaList(root);

        foodList = new ArrayList<>();
        createFoodList(root);

        instanceOfEndangered(root);
        instanceOfDescription(root);
        updateFragment();
    }
    private void instanceOfPicture(View root){
        birdPicture = (ImageView)root.findViewById(R.id.birdPicture);
        if(birdPicture == null){
            throw new RuntimeException("Une image nulle");
        }
    }
    private void instanceOfLatinName(View root){
        latinName = (TextView) root.findViewById(R.id.latinName);
        if(latinName == null){
            throw new RuntimeException("Un nom latin nul");
        }
    }
    private void instanceOfCommonName(View root){
        commonName = (TextView) root.findViewById(R.id.commonName);
        if(commonName == null){
            throw new RuntimeException("Un nom usuel nul");
        }
    }
    private void instanceOfSize(View root){
        size = (TextView) root.findViewById(R.id.size);
        if(size == null){
            throw new RuntimeException("Une taille nulle");
        }
    }
    private void instanceOfWeight(View root){
        weight = (TextView) root.findViewById(R.id.weight);
        if(weight == null){
            throw new RuntimeException("Un poids nul");
        }
    }
    private void instanceOfEndangered(View root){
        endangered = (TextView) root.findViewById(R.id.endangered);
        if(endangered == null){
            throw new RuntimeException("Pas de danger ? nul");
        }
    }
    private void instanceOfDescription(View root){
        description = (TextView) root.findViewById(R.id.description);
        if(description == null){
            throw new RuntimeException("Pas de description ? nulle");
        }
    }
    private void createAreaList(View root){
        areas = (ListView) root.findViewById(R.id.areas);
        areaAdapter = new ArrayAdapter<>(root.getContext(), android.R.layout.simple_list_item_1 , areaList);
        areas.setAdapter(areaAdapter);
    }

    private void createFoodList(View root){
        food = (ListView) root.findViewById(R.id.food);
        foodAdapter = new ArrayAdapter<>(root.getContext(), android.R.layout.simple_list_item_1 , foodList);
        food.setAdapter(foodAdapter);
    }


    public void updateFragment(){
        StringBuilder stringBuilder = new StringBuilder();
        birdPicture.setImageResource(bird.getPhoto());

        fillStringBuilder(stringBuilder,"Nom latin : ",bird.getLatinName());
        latinName.setText(stringBuilder.toString());

        fillStringBuilder(stringBuilder,"Nom usuel : ",bird.getCommonName());
        commonName.setText(stringBuilder.toString());

        fillStringBuilder(stringBuilder,"Taille moyenne : ",String.valueOf(bird.getHeight()));
        stringBuilder.append(" cm");
        size.setText(stringBuilder.toString());

        fillStringBuilder(stringBuilder,"Poids moyen : ",String.valueOf(bird.getWeight()));
        stringBuilder.append(" g");
        weight.setText(stringBuilder);

        for (String area: bird.getAreas()){
            if(!areaList.contains(area)) {
                areaList.add(area);
            }
        }

        for (String aFood: bird.getFood()){
            if(!foodList.contains(aFood)) {
                foodList.add(aFood);
            }
        }

        fillStringBuilder(stringBuilder,"En danger : ",bird.isEndangered() ? "Oui" : "Non");
        endangered.setText(stringBuilder.toString());

        fillStringBuilder(stringBuilder,"Description : ",bird.getDescription());
        description.setText(stringBuilder.toString());
    }

    private void fillStringBuilder(StringBuilder stringBuilder, String description, String value){
        stringBuilder.delete(0,stringBuilder.length());
        stringBuilder.append(description);
        stringBuilder.append(value);
    }
}