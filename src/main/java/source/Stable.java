package source;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "stables")
public class Stable implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stable_name", unique = true, nullable = false)
    private String stableName;

    @Column(name = "max_capacity")
    private int maxCapacity;

    //  @JsonIgnore
    // Dzięki temu /api/stable pokaże tylko stadniny, a nie liste koni
    @OneToMany(mappedBy = "stable", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonIgnore
    private List<Horse> horseList = new ArrayList<>();

    public Stable() {}

    public Stable(String stableName, int maxCapacity) {
        this.stableName = stableName;
        this.maxCapacity = maxCapacity;
    }

    // --- GETTERY I SETTERY ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getStableName() { return stableName; }
    public void setStableName(String stableName) { this.stableName = stableName; }
    public int getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }
    public List<Horse> getHorseList() { return horseList; }
    public void setHorseList(List<Horse> horseList) { this.horseList = horseList; }

    public boolean isEmpty() { return horseList.isEmpty(); }

    public double getLoad() {
        if (maxCapacity == 0) return 0.0;
        return (double) horseList.size() / maxCapacity;
    }

    public void addHorse(Horse horse) throws StableException {
        if (horseList.size() >= maxCapacity) {
            throw new StableException("Error: Stable: " + stableName + " is full!");
        }
        if (horseList.contains(horse)) {
            throw new StableException("Horse " + horse.getName() + " is already in the Stable");
        }
        horseList.add(horse);
        horse.setStable(this);
    }

    public void removeHorse(Horse horse) throws StableException {
        if (horseList.contains(horse)) {
            horseList.remove(horse);
            horse.setStable(null);
        } else {
            throw new StableException("Horse not found cannot remove!");
        }
    }

    public void sickHorse(Horse horse) throws StableException {
        if (horseList.contains(horse)) {
            horse.setStatus(HorseCondition.sick);
            removeHorse(horse);
        }
    }

    public void changeStatus(Horse horse, HorseCondition status) throws StableException {
        if (horseList.contains(horse)) {
            horse.setStatus(status);
        } else {
            throw new StableException("Horse not in stable");
        }
    }

    public long countByStatus(HorseCondition status) {
        return horseList.stream().filter(h -> h.getStatus() == status).count();
    }

    public List<Horse> sortByPrice() {
        List<Horse> sorted = new ArrayList<>(horseList);
        sorted.sort(Comparator.comparingDouble(Horse::getPrice));
        return sorted;
    }

    public List<Horse> searchPartial(String frag) {
        String lowerFrag = frag.toLowerCase();
        return horseList.stream()
                .filter(h -> h.getName().toLowerCase().contains(lowerFrag) ||
                        h.getBreed().toLowerCase().contains(lowerFrag))
                .collect(Collectors.toList());
    }

    public void summary() {
        System.out.println("Stable: " + stableName + " (" + horseList.size() + "/" + maxCapacity + ")");
        for (Horse h : horseList) {
            System.out.println(h);
        }
    }

    @Override
    public String toString() {
        return stableName;
    }
}