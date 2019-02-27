package TeamAndCourse;

class Pit extends Obstacle{
    private float distance;
    private float depth;
    Pit(){
        this.distance = 2 + 2 * random.nextFloat();
        this.depth = 1 + 2 * random.nextFloat();
    }
    private float getDistance(){return this.distance;}
    private float getDepth(){return this.depth;}

    @Override
    void getObstacleInfo() {
        System.out.printf("Pit\'s length is %.2f and its depth is %.2f.\n", this.getDistance(), this.getDepth());
    }

    @Override
    float passObstacle(Teammate teammate) {
        float time;
        System.out.printf("%s jumps over the pit.\n",teammate.name);
        if (teammate.agility < this.getDistance() * 10 || teammate.strength < this.getDistance() * 10 || teammate.endurance < this.getDistance() * 10) {
            System.out.printf("%s dropped into the pit and climbs out.\n",teammate.name);
            teammate.agility -= this.getDepth()* 4;
            if (teammate.agility < 0) teammate.agility = 0;
            teammate.strength -= this.getDepth() * 4;
            if (teammate.strength < 0) teammate.strength = 0;
            teammate.endurance -= this.getDepth() * 4;
            if (teammate.endurance < 0) teammate.endurance = 0;
            time = 30 * this.getDepth() / (1 + (teammate.agility / 300f)+ (teammate.strength / 300f) + (teammate.endurance / 300f));
            System.out.printf("It took %.2f time units.\n\n",time);
        }else {
            teammate.agility -= this.getDistance();
            if (teammate.agility < 0) teammate.agility = 0;
            teammate.strength -= this.getDistance() * 2;
            if (teammate.strength < 0) teammate.strength = 0;
            time = this.getDistance() / (1 + (teammate.agility / 200f) + (teammate.strength / 200f));
            System.out.printf("It took %.2f time units.\n\n", time);
        }
        return time;
    }
}
