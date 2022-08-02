package com.example.mybirds.dan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mybirds.R;

public class BirdAdapter extends BaseAdapter {
        private ListOfBirds birds;
        private IBirdAdapterListener listener;
        //Le contexte dans lequel est présent notre adapter
        private Context context;

        //Un mécanisme pour gérer l'affichage graphique depuis un layout XML
        private LayoutInflater mInflater;

        public BirdAdapter(Context context, ListOfBirds birds) {
            this.context = context;
            this.birds = birds;
            mInflater = LayoutInflater.from(this.context);
        }

        public int getCount() { return birds.size(); }

        public Object getItem(int position) { return birds.get(position); }

        public long getItemId(int position) { return position; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View layoutItem;
//(1) : Réutilisation des layouts
            layoutItem = (convertView == null ? mInflater.inflate(R.layout.bird_widget, parent, false) : convertView);

//(2) : Récupération des TextView de notre layout
            ImageView birdPicture = layoutItem.findViewById(R.id.birdPhoto);
            TextView birdCommonName = layoutItem.findViewById(R.id.birdCommonName);


//(3) : Renseignement des valeurs
            birdPicture.setImageResource(birds.get(position).getPhoto());
            birdCommonName.setText(birds.get(position).getCommonName());

            layoutItem.setOnClickListener(click -> {
                listener.onClickBird(birds.get(position));
            });
            return layoutItem;
        }

        public void addListener(IBirdAdapterListener listener){
            this.listener = listener;
        }

        public void setList(ListOfBirds listOfBirds){
            birds = listOfBirds;
        }
}

