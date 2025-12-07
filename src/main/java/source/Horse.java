package source;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "horses")
public class Horse implements Comparable<Horse> {

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
    private Stable stable;

    @OneToMany(mappedBy = "horse", cascade = CascadeType.ALL)
    private List<Rating> ratings = new ArrayList<>();

    // --- KONSTRUKTORY ---

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

    //gettery

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getBreed() { return breed; }
    public HorseType getHorseType() { return horseType; }
    public int getAge() { return age; }
    public double getPrice() { return price; }
    public String getColor() { return color; }
    public double getWeight() { return weight; }
    public Gender getGender() { return gender; }
    public HorseCondition getStatus() { return status; }
    public Stable getStable() { return stable; }
    public List<Rating> getRatings() { return ratings; }

    // settery

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setBreed(String breed) { this.breed = breed; }
    public void setHorseType(HorseType horseType) { this.horseType = horseType; }
    public void setAge(int age) { this.age = age; }
    public void setPrice(double price) { this.price = price; }
    public void setColor(String color) { this.color = color; }
    public void setWeight(double weight) { this.weight = weight; }
    public void setGender(Gender gender) { this.gender = gender; }
    public void setStatus(HorseCondition status) { this.status = status; }
    public void setStable(Stable stable) { this.stable = stable; }
    public void setRatings(List<Rating> ratings) { this.ratings = ratings; }

    // methods

    public void printHorse(){
        System.out.println(this.toString());
    }

    public void print() {
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return String.format(
                "Horse[ID: %d | Name: %-15s | Breed: %-15s | Type: %-13s | Age: %-2d | Color: %-10s | Weight: %,.1f kg | Gender: %-10s | Price: %,.2f | Status: %-12s]",
                id, name, breed, horseType, age, color, weight, gender, price, status
        );
    }

    @Override
    public int compareTo(Horse o) {
        int nameCompare = name.compareTo(o.name);
        if(nameCompare != 0) {
            return nameCompare;
        }
        return Integer.compare(age, o.age);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Horse horse = (Horse) o;

        if (id != null && horse.id != null) {
            return Objects.equals(id, horse.id);
        }

        return age == horse.age &&
                Objects.equals(name, horse.name) &&
                Objects.equals(breed, horse.breed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, breed, age);
    }
}