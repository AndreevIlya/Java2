package TeamAndCourse;

class Wall extends Obstacle {
    private float height;
    Wall(){
        this.height = 1 + 2 * random.nextFloat();
    }
    private float getHeight(){return this.height;}

    @Override
    void getObstacleInfo() {
        System.out.printf("Wall\'s height is %.2f.\n", this.getHeight());
    }

    @Override
    float passObstacle(Teammate teammate) {
        float time;
        System.out.printf("%s climbs over the wall.\n",teammate.name);
        if(teammate.strength < this.getHeight() * 10){
            System.out.printf("%s is too sick to climb a wall, so he runs around it.\n",teammate.name);
            teammate.agility -= 5;
            if(teammate.agility < 0) teammate.agility = 0;
            teammate.endurance -= 5;
            if (teammate.endurance < 0) teammate.endurance = 0;
            time = 100 / (1 + (teammate.agility / 200f) + (teammate.endurance / 200f));
            System.out.printf("It took %.2f time units.\n\n",time);
        }else {
            teammate.agility -= this.getHeight();
            if (teammate.agility < 0) teammate.agility = 0;
            teammate.endurance -= this.getHeight();
            if (teammate.endurance < 0) teammate.endurance = 0;
            teammate.strength -= this.getHeight();
            if (teammate.strength < 0) teammate.strength = 0;
            time = 3 * this.getHeight() / (1 + (teammate.agility / 300f) + (teammate.strength / 300f) + (teammate.endurance / 300f));
            System.out.printf("It took %.2f time units.\n\n", time);
        }
        return time;
    }
}
