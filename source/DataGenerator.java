package source;

public class DataGenerator {
    private static  DataGenerator instance;
    private DataGenerator() {
    }
    public static DataGenerator getInstance() {
        if(instance == null) {
            instance = new DataGenerator();
        }
        return instance;
    }

    //Methods for filling data
    public StableManager getSampleData() {
        StableManager Manager = new StableManager();

        //creating stables and getting references
        Manager.addStable("Hubertus", 15);
        Manager.addStable("Black Stallion", 8);
        Manager.addStable("Blitz", 6);

        Stable hubertus = Manager.getStable("Hubertus");
        Stable blackStallion = Manager.getStable("Black Stallion");
        Stable blitz = Manager.getStable("Blitz");

        //creating and adding horses, now adding throws exceptions so I use try-catch block
        try {
            hubertus.addHorse(new Horse("Roach", "Arabic", HorseType.HotBlood, 4, Gender.Stallion, "Brown", 480, 128000, HorseCondition.healthy));
            hubertus.addHorse(new Horse("Kelpie", "Thoroughbred", HorseType.HotBlood, 5, Gender.Mare, "Black", 510, 145000, HorseCondition.healthy));
            hubertus.addHorse(new Horse("Bucephalus", "Friesian", HorseType.ColdBlood, 8, Gender.Stallion, "Black", 650, 200000, HorseCondition.training));
            hubertus.addHorse(new Horse("Płotka", "Szlafroki", HorseType.ColdBlood, 12, Gender.Mare, "Kasztan", 550, 5000, HorseCondition.sick));
            hubertus.addHorse(new Horse("Zorro", "Andalusian", HorseType.HotBlood, 6, Gender.Gelding, "Grey", 520, 85000, HorseCondition.healthy));
            hubertus.addHorse(new Horse("Jaskier", "Kucyk", HorseType.ColdBlood, 9, Gender.Gelding, "Spotted", 350, 2500, HorseCondition.training));
            hubertus.addHorse(new Horse("Tornado", "Mustang", HorseType.HotBlood, 3, Gender.Stallion, "Black", 490, 45000, HorseCondition.recovering));
            hubertus.addHorse(new Horse("Luna", "Arab", HorseType.HotBlood, 2, Gender.Mare, "White", 410, 300000, HorseCondition.healthy));

            blackStallion.addHorse(new Horse("Shadowfax", "Andalusian", HorseType.HotBlood, 12, Gender.Stallion, "White", 550, 500000, HorseCondition.healthy));
            blackStallion.addHorse(new Horse("Spirit", "Mustang", HorseType.HotBlood, 3, Gender.Stallion, "Buckskin", 450, 60000, HorseCondition.sick));
            blackStallion.addHorse(new Horse("Black Beauty", "Thoroughbred", HorseType.HotBlood, 7, Gender.Stallion, "Black", 530, 250000, HorseCondition.training));
            blackStallion.addHorse(new Horse("Comet", "Warmblood", HorseType.HotBlood, 5, Gender.Gelding, "Bay", 580, 120000, HorseCondition.healthy));
            blackStallion.addHorse(new Horse("Pegasus", "Lipizzaner", HorseType.HotBlood, 10, Gender.Stallion, "White", 560, 80000, HorseCondition.recovering));
            blackStallion.addHorse(new Horse("Dakota", "Quarter Horse", HorseType.ColdBlood, 6, Gender.Mare, "Dun", 500, 35000, HorseCondition.healthy));

            blitz.addHorse(new Horse("Bella", "Haflinger", HorseType.ColdBlood, 6, Gender.Mare, "Chestnut", 490, 45000, HorseCondition.recovering));
            blitz.addHorse(new Horse("Titan", "Shire", HorseType.ColdBlood, 8, Gender.Stallion, "Bay", 950, 60000, HorseCondition.healthy));
            blitz.addHorse(new Horse("Herkules", "Belgian", HorseType.ColdBlood, 10, Gender.Gelding, "Roan", 920, 55000, HorseCondition.training));
            blitz.addHorse(new Horse("Goliath", "Clydesdale", HorseType.ColdBlood, 7, Gender.Stallion, "Brown", 880, 70000, HorseCondition.healthy));
            blitz.addHorse(new Horse("Fiona", "Percheron", HorseType.ColdBlood, 5, Gender.Mare, "Grey", 800, 48000, HorseCondition.sick));
        } catch (StableException e) {
            e.printStackTrace(); //log error
        }
        return Manager;
    }
}
