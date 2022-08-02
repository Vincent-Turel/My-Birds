package com.example.mybirds.dan;

import com.example.mybirds.R;

import java.util.ArrayList;
import java.util.Arrays;

public class ListOfBirds extends ArrayList<Bird> {
    public ListOfBirds(){
        super();
        Bird rougeGorge = new Bird(
                "Rouge-Gorge",
                "Erithacus rubecula",
                13.25,
                19,
                new ArrayList<String>(Arrays.asList("Afrique du nord","Amérique du Nord","Europe")),
                new ArrayList<String>(Arrays.asList("Invertébrés","Baies","Fruits")),
                false,
                R.drawable.rouge_gorge,
                "Le Rouge-gorge familier (Erithacus rubecula) est une espèce de passereaux. Auparavant placé dans la famille des Turdidae, il figure actuellement, avec d'autres représentants de celle-ci (particulièrement les tariers et les traquets), dans celle des Muscicapidae. La liste de la Commission de l'Avifaune française utilise l'orthographe rougegorge."
        );

        Bird aigleRoyal = new Bird(
                "Aigle royal",
                "Aquila chrysaetos",
                200,
                3500,
                new ArrayList<String>(Arrays.asList("Amérique du nord","Europe","Montagnes d'Eurasie")),
                new ArrayList<String>(Arrays.asList("Petits mamifères (lièvres...)","Petits oiseaux","Mamifères enfants")),
                true,
                R.drawable.aigle_royal,
                "L'Aigle royal (Aquila chrysaetos) est une espèce d'oiseaux de la famille des accipitridés. C'est un rapace brun foncé, avec un plumage plus brun-doré sur la tête et le cou. L'aigle royal utilise son agilité, sa vitesse et ses serres extrêmement puissantes pour attraper ses proies : des lapins, des marmottes, des écureuils, et de grands mammifères comme les renards, les chats sauvages et domestiques, de jeunes chèvres de montagne, de jeunes bouquetins, et de jeunes cervidés. Il consomme aussi des charognes, si les proies sont rares, ainsi que des reptiles. Des oiseaux, dont des espèces de grande taille comme des cygnes ou des grues, des corbeaux et des Goélands marins ont tous été notés comme proies potentielles."
        );

        add(rougeGorge);
        add(aigleRoyal);
    }



    @Override
    public String toString() {
        StringBuilder res;
        res = new StringBuilder("Liste d'oiseaux = { ");
        for (Bird bird : this) {
            res.append(bird.getCommonName());
        }
        res.append(" }");
        return res.toString();
    }
}
