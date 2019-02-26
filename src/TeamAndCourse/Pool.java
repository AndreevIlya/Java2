package TeamAndCourse;

class Pool extends Obstacle{
    private int distance;
    Pool(){
        this.distance = 25 + 25 * random.nextInt(3);
    }
    private int getDistance(){return this.distance;}

    @Override
    void getObstacleInfo() {
        System.out.println("Pool\'s length is " + this.getDistance());
    }

    @Override
    float passObstacle(Teammate teammate) {
        System.out.printf("%s swims in the pool.\n",teammate.name);
        teammate.strength -= this.getDistance() / 6;
        if(teammate.strength < 0) teammate.strength = 0;
        teammate.endurance -= this.getDistance() / 10;
        if (teammate.endurance < 0) teammate.endurance = 0;
        float time = this.getDistance() / (1 + (teammate.strength / 200f) + (teammate.endurance / 200f));
        System.out.printf("It took %.2f time units.\n\n",time);
        return time;
    }
}
