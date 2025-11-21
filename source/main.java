import source.*;

void main() throws StableException {
    System.out.println("Stable Simulator Starting ...");

    //new object StableManager test
    StableManager manager1 = new StableManager();

    //adding stables test
    System.out.println("Adding Stables");
    manager1.addStable("Hubertus", 12);
    manager1.addStable("Black Stallion", 2);
    manager1.addStable("Blitz", 7);

    //test for duplicate
    //manager1.addStable("Hubertus", 12);

    //getting satbles
    Stable Hubertus = manager1.getStable("Hubertus");
    Stable BlackStallion = manager1.getStable("Black Stallion");
    Stable Blitz = manager1.getStable("Blitz");

    //creating horses
    Horse h1 = new Horse("Roach", "Arabic", HorseType.HotBlood, 4, Gender.Stallion, "Brown", 480, 128000, HorseCondition.healthy);
    Horse h2 = new Horse("Kelpie", "Thoroughbred", HorseType.HotBlood, 5, Gender.Mare, "Black", 510, 145000, HorseCondition.healthy);
    Horse h3 = new Horse("Bucephalus", "Friesian", HorseType.ColdBlood, 8, Gender.Stallion, "Black", 650, 200000, HorseCondition.training);
    Horse h4 = new Horse("Shadowfax", "Andalusian", HorseType.HotBlood, 12, Gender.Stallion, "White", 550, 500000, HorseCondition.healthy);
    Horse h5 = new Horse("Spirit", "Mustang", HorseType.HotBlood, 3, Gender.Stallion, "Buckskin", 450, 60000, HorseCondition.sick);
    Horse h6 = new Horse("Bella", "Haflinger", HorseType.ColdBlood, 6, Gender.Mare, "Chestnut", 490, 45000, HorseCondition.recovering); // Jeśli masz status 'recovering' lub inny

    //Stable methods test
    //adding horses
    System.out.println("Adding horses");
    Hubertus.addHorse(h1);
    Hubertus.addHorse(h2);
    Hubertus.addHorse(h3);
    BlackStallion.addHorse(h4);
    BlackStallion.addHorse(h5);
    Blitz.addHorse(h6);

    //testing adding the same horse
    Hubertus.addHorse(h1);

    //capacity test, commented if you want to test uncomment
    System.out.println("Capacity test");
    //BlackStallion.addHorse(new Horse("Wyrwij","Mustang", HorseType.HotBlood, 3, Gender.Stallion, "Buckskin", 450, 60000, HorseCondition.healthy));

    //summary stable
    System.out.println("\nSummary Stables");
    Hubertus.summary();
    BlackStallion.summary();
    Blitz.summary();

    // removeHorse
    System.out.println("\nRemoving horse 'Bucephalus' from Hubertus");
    Hubertus.removeHorse(h3);
    Hubertus.removeHorse(h3); // Test removing nonexisting horse

    // sickHorse
    System.out.println("\nHorse Roach SICK in Hubertus");
    Hubertus.sickHorse(h1);
    Hubertus.sickHorse(h3); // Test on removed horse

    // changeCondition
    System.out.println("\nChange status 'Kelpie' to SOLD in Hubertus");
    Hubertus.changeStatus(h2, HorseCondition.sold);

    // Summary after changes
    System.out.println("\nStables summary after changes");
    Hubertus.summary();
    BlackStallion.summary();
    Blitz.summary();

    // changeWeight
    System.out.println("\nChange weight 'Shadowfax' in Black Stallion to 490.5 ");
    BlackStallion.changeWeight(h4, 490.5F);

    // countByStatus
    System.out.println("\nCounting by status in stable Hubertus");
    System.out.println("Sick horses: " + Hubertus.countByStatus(HorseCondition.sick)); // h1
    System.out.println("Sold horses: " + Hubertus.countByStatus(HorseCondition.sold)); // h2
    System.out.println("Healthy horses: " + Hubertus.countByStatus(HorseCondition.healthy));

    System.out.println("\nCounting by status in stable Black Stallion");
    System.out.println("Sick horses: " + BlackStallion.countByStatus(HorseCondition.sick)); // h1
    System.out.println("Sold horses: " + BlackStallion.countByStatus(HorseCondition.sold)); // h2
    System.out.println("Healthy horses: " + BlackStallion.countByStatus(HorseCondition.healthy));

    // sortByName
    System.out.println("\nSorting by name (Hubertus)");
    List<Horse> sortedByName = Hubertus.sortByName();
    sortedByName.forEach(Horse::print);

    // sortByPrice
    System.out.println("\nSorting by price (Hubertus)");
    List<Horse> sortedByPrice = Hubertus.sortByPrice();
    sortedByPrice.forEach(Horse::print);

    // search (by name)
    System.out.println("\nSearching by name ('Kelpie') ");
    Horse foundHorse = Hubertus.search("Kelpie");
    System.out.println("Search 'Kelpie' in Hubertus: " + (foundHorse != null ? foundHorse.getName() : "NOT FOUND"));

    Horse notFoundHorse = BlackStallion.search("Kelpie");
    System.out.println("Search 'Kelpie' in Black Stallion: " + (notFoundHorse != null ? notFoundHorse.getName() : "NOT FOUND"));

    // searchPartial
    System.out.println("\nSearching frag 'Must' (in Black Stallion) ");
    List<Horse> partialMatches = BlackStallion.searchPartial("Must"); // h5 (Spirit)
    partialMatches.forEach(Horse::print);

    // max
    System.out.println("\n--- Method max (natural order = name) in 'Black Stallion' ---");
    System.out.println("Max horse: " + BlackStallion.max());

    // Test print() Horse
    System.out.println("\nTest print Horse");
    h1.print();

    //Test StableManager continue

    // removeStable
    System.out.println("\nRemoving stable ('Blitz')");
    manager1.removeStable("Blitz");

    // findEmpty
    System.out.println("\nFind empty stables");
    manager1.addStable("BambiBambi", 5); // adding empty for tests
    List<Stable> emptyStables = manager1.findEmpty();
    System.out.println("Empty stables: " + emptyStables.size());
    emptyStables.forEach(s -> System.out.println("- " + s.getStableName()));

    // summary (Manager)
    System.out.println("\nManager Summary");
    manager1.summaryManager();

    System.out.println("\nThe end of Simulator");
}