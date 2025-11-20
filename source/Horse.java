package source;
import java.util.Objects;
public class Horse implements Comparable<Horse>{
    private final String name;
    private final String breed;
    private final HorseType HorseType;
    private HorseCondition status;
    private final int age;
    private double price;
    private final String color;
    private double weight;
    private Gender gender;

    public Horse(String Name, String Breed, HorseType horseType, int Age,  Gender gender, String Color, double Weight, double price, HorseCondition Status ){
        this.name = Name;
        this.breed = Breed;
        this.HorseType = horseType;
        this.age = Age;
        this.gender = gender;
        this.color = Color;
        this.weight = Weight;
        this.price = price;
        this.status = Status;
    }

    //gettery
    public String getName() {return name;}
    public String getBreed() {return breed;}
    public HorseType getHorseType() {return HorseType;}
    public int getAge() {return age;}
    public double getPrice() {return price;}
    public String getColor() {return color;}
    public double getWeight() {return weight;}
    public Gender getGender() {return gender;}
    public HorseCondition getStatus() {return status;}


    //settery
    public void setPrice(double price) {this.price = price;}
    public void setGender(Gender gender) {this.gender = gender;}
    public void setWeight(double weight) {this.weight = weight;}
    public void setStatus(HorseCondition status) {this.status = status;}

    public void printHorse(){
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return String.format(
                "Horse[Name: %-15s | Breed: %-15s | Type: %-13s | Age: %-2d | Color: %-10s | Weight: %,1f kg | Gender: %-10s | Price: %,2f | Status: %-12s]",
                name, breed, HorseType, age, color, weight, gender, price,  status
        );
    }

    @Override
    public int compareTo(Horse o) {
        //name comparison
        int nameCompare = name.compareTo(o.name);
        if(nameCompare != 0) {
            return nameCompare;
        }

        //breed comparison
        int  breedCompare = breed.compareTo(o.breed);
        if(breedCompare != 0) {
            return breedCompare;
        }

        //age comparison, age is a primitive so Integer.compare
        return Integer.compare(age, o.age);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Horse horse = (Horse) o;
        return age == horse.age &&
                Objects.equals(name, horse.name) &&
                Objects.equals(breed, horse.breed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, breed, age);
    }

    public void print() {
        System.out.println(this.toString());
    }
}
