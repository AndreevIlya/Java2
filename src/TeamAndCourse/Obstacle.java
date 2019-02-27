package TeamAndCourse;

import java.util.Random;

abstract class Obstacle {
    static final Random random = new Random();
    abstract void getObstacleInfo();
    abstract float passObstacle(Teammate teammate);
}
