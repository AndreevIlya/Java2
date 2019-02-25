package TeamAndCourse;

class Wall extends Obstacle {
    private float height;
    Wall(){
        this.height = 1 + 2 * random.nextFloat();
    }
    float getHeight(){return this.height;}

    @Override
    void getObstacleInfo() {
        System.out.printf("Wall\'s height is %.2f.\n", this.getHeight());
    }
}
