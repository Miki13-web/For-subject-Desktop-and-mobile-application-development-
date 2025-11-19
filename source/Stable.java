package source;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Stable {
    private String StableName;
    private int MaxCapacity;
    List<Horse> horseList;

    Stable(String StableName, int MaxCapacity, List<Horse> horseList) {
        this.StableName = StableName;
        this.MaxCapacity = MaxCapacity;
        //collection
        this.horseList = new ArrayList<>();
    }

    //gettery
    public String getStableName() {return StableName;}
    public int getMaxCapacity() {return MaxCapacity;}
    public List<Horse> getHorseList() {return horseList;}
    public boolean isEmpty() {return horseList.isEmpty();}

    //setter
    public void setStableName(String StableName) {this.StableName = StableName;}
    public void setMaxCapacity(int MaxCapacity) {this.MaxCapacity = MaxCapacity;}

    //methods
    public void addHorse(Horse horse) {
        if(horseList.size() >= MaxCapacity) {
            System.err.println("Too many horses!");
        }

        //check if horse is already at Stable
        if(horseList.contains(horse)) {
            System.out.println("Horse" + horse.getName() + " is already in the Stable");
        }
        else{
            horseList.add(horse);
            System.out.println("Horse " + horse.getName() + " has been added to the Stable");
        }
    }

    public void removeHorse(Horse horse) {
        if(horseList.contains(horse)) {
            horseList.remove(horse);
            System.out.println("Horse " + horse.getName() + " has been removed from the Stable");
        }
    }

    public void sickHorse(Horse horse) {
        if(horseList.contains(horse)) {
            horse.setStatus(HorseCondition.sick);
            removeHorse(horse);
            System.out.println("Sick Horse " + horse.getName() + " has been removed from the Stable");
        }
    }
    public void changeStatus(Horse horse, HorseCondition status) {
        if(horseList.contains(horse)) {
            horse.setStatus(status);
            System.out.println("Horse " + horse.getName() + " has been changed to " + status);
        }
        else {
            System.out.println("Horse " + horse.getName() + " is not in the Stable");
        }
    }

    public void changeWeight(Horse horse, int weight) {
        if(horseList.contains(horse)) {
            horse.setWeight(weight);
            System.out.println("Horse " + horse.getName() + " WEIGHT has been changed to " + weight +" kg");
        }
        else {
            System.out.println("Horse " + horse.getName() + " is not in the Stable");
        }
    }

    //number of horses in particular condition
    public long countByStatus(HorseCondition status) {
        return horseList.stream().filter(horse ->horse.getStatus()==status).count();
    }

    public List<Horse> sortByName() {
        List<Horse> sorted_horses = new ArrayList<>(horseList);
        Collections.sort(sorted_horses);
        return sorted_horses;
    }

}
