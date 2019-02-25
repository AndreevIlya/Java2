package TeamAndCourse;

class Road extends Obstacle {
    private int distance;
    Road(){
        this.distance = 50 + random.nextInt(200);
    }
    int getDistance(){return this.distance;}

    @Override
    void getObstacleInfo() {
        System.out.println("Road\'s length is " + this.getDistance());
    }
}
