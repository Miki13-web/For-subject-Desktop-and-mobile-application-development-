package source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StableManager {
    private Map<String, Stable> stables;

    public StableManager(){
        this .stables = new HashMap<>();
    }

    //get
    public Stable getStable(String name){
        return stables.get(name);
    }
    //methods
    public void addStable(String name, int capacity){
        //check if stable this name already exists
        if(stables.containsKey(name)){
            System.err.println("Stable already exists!");
        }
        else{
           Stable newStable = new Stable(name, capacity);
           stables.put(name, newStable);
           System.out.println("Stable: " + name +" with capacity: "+ capacity + " has been added!");
        }
    }

    public void removeStable(String name){
        Stable reamovedStable =  stables.remove(name);
        if(reamovedStable != null){System.out.println("Stable: " + name + " has been removed!");}
        else{System.out.println("Stable: " + name + " not found! CANNOT REMOVE!");}
    }

    public List<Stable> findEmpty(){
        List<Stable> empty = new ArrayList<>();
        //iterate after only values of the map
        for(Stable stable : stables.values()){
            if(stable.isEmpty()){empty.add(stable);}
        }
        return empty;
    }

    public void summaryManager(){
        System.out.println("=======Summary Stable Manager=======");
        if(stables.isEmpty()){System.out.println("No Stables in system!");}
        else {
            for(Map.Entry<String, Stable> entry : stables.entrySet()){
                String name = entry.getKey();
                Stable stable = entry.getValue();

                double occupancy = 0.0;
                if(stable.getMaxCapacity() > 0){
                    occupancy = (double) stable.getHorseList().size() /stable.getMaxCapacity()*100;
                }

                //printing summary
                System.out.println("Stable: " + name + " | Occupancy: " + occupancy + "%");
                //System.out.println(stable.toString());
                System.out.println("==================================\nEnd of summary");
            }
        }
    }


    public List<Stable> getAll(){
        List<Stable> all = new ArrayList<>();
        for(Stable stable : stables.values()){
            all.add(stable);
        }
        return all;
    }
}
