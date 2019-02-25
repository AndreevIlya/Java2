package TeamAndCourse;

class Pool extends Obstacle{
    private int distance;
    Pool(){
        this.distance = 25 + 25 * random.nextInt(3);
    }
    int getDistance(){return this.distance;}

    @Override
    void getObstacleInfo() {
        System.out.println("Pool\'s length is " + this.getDistance());
    }
}
