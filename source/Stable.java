package source;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Stable {
    private String StableName;
    private int MaxCapacity;
    private List<Horse> horseList;

    public Stable(String StableName, int MaxCapacity) {
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
    public void addHorse(Horse horse) throws StableException {
        if(horseList.size() >= MaxCapacity) {
           //throw exception
            throw new StableException("Error: Stable: "+StableName+" is full!");
        }

        //check if horse is already at Stable
        if(horseList.contains(horse)) {
            throw new StableException("Horse" + horse.getName() + " is already in the Stable");
        }
        else{
            horseList.add(horse);
            System.out.println("Horse " + horse.getName() + " has been added to the stable: "+ StableName);
        }
    }

    public void removeHorse(Horse horse) throws StableException {
        if(horseList.contains(horse)) {
            horseList.remove(horse);
            System.out.println("Horse " + horse.getName() + " has been removed from the Stable");
        }
        else{
            throw new StableException("there is no horse:" + horse.getName() + " in the stable: "+StableName+" CANNOT remove!");
        }
    }

    public void sickHorse(Horse horse) throws StableException {
        if(horseList.contains(horse)) {
            horse.setStatus(HorseCondition.sick);
            removeHorse(horse);
            System.out.println("Sick Horse " + horse.getName() + " has been removed from the Stable");
        }
    }
    public void changeStatus(Horse horse, HorseCondition status) throws StableException {
        if(horseList.contains(horse)) {
            horse.setStatus(status);
            System.out.println("Horse " + horse.getName() + " has been changed to " + status);
        }
        else {
            throw new StableException("Horse " + horse.getName() + " is not in the Stable");
        }
    }

    public void changeWeight(Horse horse, float weight) throws StableException {
        if(horseList.contains(horse)) {
            horse.setWeight(weight);
            System.out.println("Horse " + horse.getName() + " WEIGHT has been changed to " + weight +" kg");
        }
        else {
            throw new StableException("Horse " + horse.getName() + " is not in the Stable");
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

    public List<Horse> sortByPrice() {
        List<Horse> sorted_horses = new ArrayList<>(horseList);
        //option 1
        sorted_horses.sort(Comparator.comparingDouble(Horse::getPrice));
        //option 2 Lambda
        //sorted_horses.sort((h1,h2)->Double.compare(h1.getPrice(),h2.getPrice()));
        return sorted_horses;
    }

    public Horse search(String name){
        //if horse not found this stay null and we return null
        Horse horse = null;
        //in the instruction it was required to create a Comparator
        Comparator<String> nameComparator = Comparator.naturalOrder();
        for(Horse horse1 : horseList){
            if(nameComparator.compare(horse1.getName(),name)==0){
                horse = horse1;
            }
        }

        //different option shorter with equals
        //for(Horse horse1 : horseList){
        //    if(horse1.getName().equals(name)){horse = horse1}
        //}

        return horse;
    }

    public List<Horse> searchPartial(String frag){
        String lowerFrag = frag.toLowerCase();

        //version 1 simple for
        //List<Horse> result = new ArrayList<>();
        //for(Horse horse1 : horseList){
        //    if(horse1.getName().toLowerCase()==lowerFrag || horse1.getBreed().toLowerCase() == lowerFrag){
        //        result.add(horse1);
        //    }
        //}
        //return result;

        //version 2 with stream
        return horseList.stream().filter(horse->horse.getName().toLowerCase().contains(lowerFrag) ||
                horse.getBreed().toLowerCase().contains(lowerFrag)).collect(Collectors.toList());
    }

    public void summary(){
        System.out.println("Stable Summary: "+StableName);
        System.out.println("Capacity: "+horseList.size()+" / "+MaxCapacity);
        if(horseList.isEmpty()){System.out.println("Empty Stable");}
        else{
            System.out.println("Horses: ");
            String horsePrint;
            for(Horse horse : horseList){
                horsePrint = horse.toString();
                System.out.println(horsePrint);
            }
        }
        System.out.println("The End of summary");
    }

    //Collections.max uses comparable and return max value from horse list
    public Horse max(){
        if(horseList.isEmpty()){return null;}

        return Collections.max(horseList);
    }

}
