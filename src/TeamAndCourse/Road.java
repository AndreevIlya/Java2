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
        float time;
        System.out.printf("%s runs on the road.\n",teammate.name);
        if(teammate.agility < 10){
            System.out.printf("%s\'s gone limp and can not run, so he walks.\n",teammate.name);
            teammate.agility = 0;
            teammate.endurance += this.getDistance() / 10;
            teammate.strength += this.getDistance() / 25;
            time = this.getDistance();
            System.out.printf("It took %.2f time units.\n\n", time);
        }else {
            teammate.agility -= this.getDistance() / 20;
            if (teammate.agility < 0) teammate.agility = 0;
            teammate.endurance -= this.getDistance() / 10;
            if (teammate.endurance < 0) teammate.endurance = 0;
            time = this.getDistance() / (1 + (teammate.agility / 200f) + (teammate.endurance / 200f));
            System.out.printf("It took %.2f time units.\n\n", time);
        }
        return time;
    }
}
