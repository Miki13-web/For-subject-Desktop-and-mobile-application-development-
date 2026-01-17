package source;

import com.fasterxml.jackson.annotation.JsonIgnore; // <--- DODAJ TEN IMPORT
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "horses")
public class Horse implements Comparable<Horse>, Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    private String breed;

    @Enumerated(EnumType.STRING)
    private HorseType horseType;

    @Enumerated(EnumType.STRING)
    private HorseCondition status;

    private int age;
    private double price;
    private String color;
    private double weight;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne
    @JoinColumn(name = "stable_id")
    @JsonIgnore  // <--- DODAJ TO TUTAJ (Przerywa pętlę Stadnina <-> Koń)
    private Stable stable;

    @OneToMany(mappedBy = "horse", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Rating> ratings = new ArrayList<>();

    public Horse() {}

    public Horse(String name, String breed, HorseType horseType, int age, Gender gender, String color, double weight, double price, HorseCondition status) {
        this.name = name;
        this.breed = breed;
        this.horseType = horseType;
        this.age = age;
        this.gender = gender;
        this.color = color;
        this.weight = weight;
        this.price = price;
        this.status = status;
    }

    // --- GETTERY I SETTERY ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }
    public HorseType getHorseType() { return horseType; }
    public void setHorseType(HorseType horseType) { this.horseType = horseType; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }
    public HorseCondition getStatus() { return status; }
    public void setStatus(HorseCondition status) { this.status = status; }
    public Stable getStable() { return stable; }
    public void setStable(Stable stable) { this.stable = stable; }
    public List<Rating> getRatings() { return ratings; }
    public void setRatings(List<Rating> ratings) { this.ratings = ratings; }

    // --- METODY POMOCNICZE ---

    public double getAverageRating() {
        if (ratings == null || ratings.isEmpty()) return 0.0;
        double sum = 0;
        for (Rating r : ratings) {
            sum += r.getValue();
        }
        return sum / ratings.size();
    }

    public int getRatingsCount() {
        return ratings == null ? 0 : ratings.size();
    }

    public void printHorse(){ System.out.println(this.toString()); }
    public void print() { System.out.println(this.toString()); }

    @Override
    public String toString() {
        return String.format(
                "Horse[ID: %d | Name: %s | Breed: %s | AvgRating: %.1f]",
                id, name, breed, getAverageRating()
        );
    }

    @Override
    public int compareTo(Horse o) {
        int nameCompare = name.compareTo(o.name);
        if(nameCompare != 0) return nameCompare;
        return Integer.compare(age, o.age);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Horse horse = (Horse) o;
        if (id != null && horse.id != null) return Objects.equals(id, horse.id);
        return age == horse.age && Objects.equals(name, horse.name) && Objects.equals(breed, horse.breed);
    }

    @Override
    public int hashCode() { return Objects.hash(name, breed, age); }
}