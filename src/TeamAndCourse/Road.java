package TeamAndCourse;

class Road extends Obstacle {
    private int distance;
    Road(){
        this.distance = 50 + random.nextInt(200);
    }
    private int getDistance(){return this.distance;}

    @Override
    void getObstacleInfo() {
        System.out.println("Road\'s length is " + this.getDistance());
    }

    @Override
    float passObstacle(Teammate teammate) {
        System.out.printf("%s runs on the road.\n",teammate.name);
        teammate.agility -= this.getDistance() / 20;
        if(teammate.agility < 0) teammate.agility = 0;
        teammate.endurance -= this.getDistance() / 10;
        if (teammate.endurance < 0) teammate.endurance = 0;
        float time = this.getDistance() / (1 + (teammate.agility / 200f) + (teammate.endurance / 200f));
        System.out.printf("It took %.2f time units.\n\n",time);
        return time;
    }
}
