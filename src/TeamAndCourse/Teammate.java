package TeamAndCourse;

import java.util.Random;

class Teammate{
    private static Random random = new Random();
    private static String[] teammateNames = {"Sam", "Joe", "Robert", "Cuthbert", "Andy","John","Patrick","Antony"};
    int strength;
    int agility;
    int endurance;
    String name;

    Teammate(){
        this.name = teammateNames[random.nextInt(teammateNames.length)];
        this.strength = 60 + random.nextInt(40);
        this.agility = 60 + random.nextInt(40);
        this.endurance = 60 + random.nextInt(40);
    }
    void teammateInfo(){
        System.out.printf("Here is %s, his strength is %d, his agility - %d, his endurance - %d.\n",this.name, this.strength, this.agility, this.endurance);
    }
}
