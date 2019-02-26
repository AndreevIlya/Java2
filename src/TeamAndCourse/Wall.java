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
        System.out.printf("%s climbs over the wall.\n",teammate.name);
        teammate.agility -= this.getHeight();
        if(teammate.agility < 0) teammate.agility = 0;
        teammate.endurance -= this.getHeight();
        if (teammate.endurance < 0) teammate.endurance = 0;
        teammate.strength -= this.getHeight();
        if (teammate.strength < 0) teammate.strength = 0;
        float time = 3 * this.getHeight() / (1 + (teammate.agility / 300f)+ (teammate.strength / 300f) + (teammate.endurance / 300f));
        System.out.printf("It took %.2f time units.\n\n",time);
        return time;
    }
}
