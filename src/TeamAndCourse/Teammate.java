package TeamAndCourse;

import java.util.Random;

public class Teammate implements Actions{
    private static Random random = new Random();
    private static String[] teammateNames = {"Sam", "Joe", "Robert", "Cuthbert", "Andy","John","Patrick","Antony"};
    private int strength;
    private int agility;
    private int endurance;
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

    @Override
    public float run(Road road) {
        System.out.printf("%s runs on the road.\n",this.name);
        this.agility -= road.getDistance() / 20;
        if(this.agility < 0) this.agility = 0;
        this.endurance -= road.getDistance() / 10;
        if (this.endurance < 0) this.endurance = 0;
        float time = road.getDistance() / (1 + (this.agility / 200f) + (this.endurance / 200f));
        System.out.printf("It took %.2f time units.\n\n",time);
        return time;
    }

    @Override
    public float jump(Pit pit) {
        float time;
        System.out.printf("%s jumps over the pit.\n",this.name);
        if (this.agility < 30 || this.strength < 30 || this.endurance < 30) {
            System.out.printf("%s dropped into the pit and climbs out.\n",this.name);
            this.agility -= pit.getDepth()* 2;
            if (this.agility < 0) this.agility = 0;
            this.strength -= pit.getDepth() * 2;
            if (this.strength < 0) this.strength = 0;
            this.endurance -= pit.getDepth() * 2;
            if (this.endurance < 0) this.endurance = 0;
            time = climb(pit);
        }else {
            this.agility -= pit.getDistance();
            if (this.agility < 0) this.agility = 0;
            this.strength -= pit.getDistance() * 2;
            if (this.strength < 0) this.strength = 0;
            time = pit.getDistance() / (1 + (this.agility / 200f) + (this.strength / 200f));
            System.out.printf("It took %.2f time units.\n\n", time);
        }
        return time;
    }

    @Override
    public float swim(Pool pool) {
        System.out.printf("%s swims in the pool.\n",this.name);
        this.strength -= pool.getDistance() / 6;
        if(this.strength < 0) this.strength = 0;
        this.endurance -= pool.getDistance() / 10;
        if (this.endurance < 0) this.endurance = 0;
        float time = pool.getDistance() / (1 + (this.agility / 200f) + (this.endurance / 200f));
        System.out.printf("It took %.2f time units.\n\n",time);
        return time;
    }

    @Override
    public float climb(Pit pit) {
        this.agility -= pit.getDepth() * 2;
        if(this.agility < 0) this.agility = 0;
        this.endurance -= pit.getDepth() * 2 ;
        if (this.endurance < 0) this.endurance = 0;
        this.strength -= pit.getDepth() * 4;
        if (this.strength < 0) this.strength = 0;
        float time = 30 * pit.getDepth() / (1 + (this.agility / 200f) + (this.endurance / 200f));
        System.out.printf("It took %.2f time units.\n\n",time);
        return time;
    }

    @Override
    public float climb(Wall wall) {
        System.out.printf("%s climbs over the wall.\n",this.name);
        this.agility -= wall.getHeight();
        if(this.agility < 0) this.agility = 0;
        this.endurance -= wall.getHeight();
        if (this.endurance < 0) this.endurance = 0;
        this.strength -= wall.getHeight();
        if (this.strength < 0) this.strength = 0;
        float time = 3 * wall.getHeight() / (1 + (this.agility / 200f) + (this.endurance / 200f));
        System.out.printf("It took %.2f time units.\n\n",time);
        return time;
    }
}
