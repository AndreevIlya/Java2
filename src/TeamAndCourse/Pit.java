package TeamAndCourse;

class Pit extends Obstacle{
    private float distance;
    private float depth;
    Pit(){
        this.distance = 2 + 2 * random.nextFloat();
        this.depth = 1 + 2 * random.nextFloat();
    }
    float getDistance(){return this.distance;}
    float getDepth(){return this.depth;}

    @Override
    void getObstacleInfo() {
        System.out.printf("Pit\'s length is %.2f and its depth is %.2f.\n", this.getDistance(), this.getDepth());
    }
}
