package com.example.mybirds.dan;

import java.util.ArrayList;
import java.util.Observable;

public class BirdModel extends Observable {

    private final double MAXSMALL = 30;
    private final double MAXMEDIUM = 150;
    private static ListOfBirds birdList;
    private ArrayList<Integer> smallBirds;
    private ArrayList<Integer> mediumBirds;
    private ArrayList<Integer> largeBirds;
    private static BirdModel instance = null;

    public static BirdModel getInstance() {
        if (instance==null )
            instance = new BirdModel();
        return instance;
    }

    private BirdModel(){
        smallBirds = new ArrayList<>();
        mediumBirds = new ArrayList<>();
        largeBirds = new ArrayList<>();
        birdList = new ListOfBirds();
        for (int i = 0; i < birdList.size(); i++) {
            classTheBird(i,birdList.get(i).getHeight());
        }
    }

    private void classTheBird(Integer indexInList, double birdHeight){
        if(birdHeight <= MAXSMALL){
            smallBirds.add(indexInList);
        }
        else if(birdHeight <= MAXMEDIUM){
            mediumBirds.add(indexInList);
        }
        else{
            largeBirds.add(indexInList);
        }
    }

    public Bird getBird(int index){
        return birdList.get(index);
    }

    public void add(String name, String latinName, double height, double weight, ArrayList<String> areas, ArrayList<String> food, boolean endangered, int photo, String description){
        classTheBird(birdList.size(),height);
        birdList.add(new Bird(name, latinName, height, weight, areas, food, endangered, photo, description));
        setChanged();
        notifyObservers();
    }

    public void add(Bird bird){
        classTheBird(birdList.size(),bird.getHeight());
        birdList.add(bird);
        setChanged();
        notifyObservers();
    }
    public ListOfBirds getBirdList(){
        return birdList;
    }

    public int howManySmallBird(){
        return smallBirds.size();
    }

    public int howManyMediumBird(){
        return mediumBirds.size();
    }

    public int howManyLargeBird(){
        return largeBirds.size();
    }

    public int size(){
        return birdList.size();
    }
}
