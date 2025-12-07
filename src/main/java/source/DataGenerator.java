package source;

public class DataGenerator {
    private static DataGenerator instance;

    private DataGenerator() {}

    public static DataGenerator getInstance() {
        if(instance == null) {
            instance = new DataGenerator();
        }
        return instance;
    }

    public void generateSampleData(StableManager manager) {
        try {
            System.out.println("Generating sample data...");

            //creating stables
            manager.addStable("Hubertus", 15);
            manager.addStable("Black Stallion", 8);
            manager.addStable("Blitz", 6);

            //adding horses
            manager.addHorseToStable("Hubertus", new Horse("Roach", "Arabic", HorseType.HotBlood, 4, Gender.Stallion, "Brown", 480, 128000, HorseCondition.healthy));
            manager.addHorseToStable("Hubertus", new Horse("Kelpie", "Thoroughbred", HorseType.HotBlood, 5, Gender.Mare, "Black", 510, 145000, HorseCondition.healthy));
            manager.addHorseToStable("Hubertus", new Horse("Bucephalus", "Friesian", HorseType.ColdBlood, 8, Gender.Stallion, "Black", 650, 200000, HorseCondition.training));
            manager.addHorseToStable("Hubertus", new Horse("Płotka", "Szlafroki", HorseType.ColdBlood, 12, Gender.Mare, "Kasztan", 550, 5000, HorseCondition.sick));
            manager.addHorseToStable("Hubertus", new Horse("Zorro", "Andalusian", HorseType.HotBlood, 6, Gender.Gelding, "Grey", 520, 85000, HorseCondition.healthy));
            manager.addHorseToStable("Hubertus", new Horse("Jaskier", "Kucyk", HorseType.ColdBlood, 9, Gender.Gelding, "Spotted", 350, 2500, HorseCondition.training));
            manager.addHorseToStable("Hubertus", new Horse("Tornado", "Mustang", HorseType.HotBlood, 3, Gender.Stallion, "Black", 490, 45000, HorseCondition.recovering));
            manager.addHorseToStable("Hubertus", new Horse("Luna", "Arab", HorseType.HotBlood, 2, Gender.Mare, "White", 410, 300000, HorseCondition.healthy));

            // "Black Stallion"
            manager.addHorseToStable("Black Stallion", new Horse("Shadowfax", "Andalusian", HorseType.HotBlood, 12, Gender.Stallion, "White", 550, 500000, HorseCondition.healthy));
            manager.addHorseToStable("Black Stallion", new Horse("Spirit", "Mustang", HorseType.HotBlood, 3, Gender.Stallion, "Buckskin", 450, 60000, HorseCondition.sick));
            manager.addHorseToStable("Black Stallion", new Horse("Black Beauty", "Thoroughbred", HorseType.HotBlood, 7, Gender.Stallion, "Black", 530, 250000, HorseCondition.training));
            manager.addHorseToStable("Black Stallion", new Horse("Comet", "Warmblood", HorseType.HotBlood, 5, Gender.Gelding, "Bay", 580, 120000, HorseCondition.healthy));
            manager.addHorseToStable("Black Stallion", new Horse("Pegasus", "Lipizzaner", HorseType.HotBlood, 10, Gender.Stallion, "White", 560, 80000, HorseCondition.recovering));
            manager.addHorseToStable("Black Stallion", new Horse("Dakota", "Quarter Horse", HorseType.ColdBlood, 6, Gender.Mare, "Dun", 500, 35000, HorseCondition.healthy));

            // "Blitz"
            manager.addHorseToStable("Blitz", new Horse("Bella", "Haflinger", HorseType.ColdBlood, 6, Gender.Mare, "Chestnut", 490, 45000, HorseCondition.recovering));
            manager.addHorseToStable("Blitz", new Horse("Titan", "Shire", HorseType.ColdBlood, 8, Gender.Stallion, "Bay", 950, 60000, HorseCondition.healthy));
            manager.addHorseToStable("Blitz", new Horse("Herkules", "Belgian", HorseType.ColdBlood, 10, Gender.Gelding, "Roan", 920, 55000, HorseCondition.training));
            manager.addHorseToStable("Blitz", new Horse("Goliath", "Clydesdale", HorseType.ColdBlood, 7, Gender.Stallion, "Brown", 880, 70000, HorseCondition.healthy));
            manager.addHorseToStable("Blitz", new Horse("Fiona", "Percheron", HorseType.ColdBlood, 5, Gender.Mare, "Grey", 800, 48000, HorseCondition.sick));

            System.out.println("Data generation completed!");

        } catch (StableException e) {
            System.err.println("Error generating data: " + e.getMessage());
        }
    }
}