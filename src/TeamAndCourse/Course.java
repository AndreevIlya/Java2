package TeamAndCourse;

import java.util.Random;

public class Course {
    private static final Random random = new Random();
    private Obstacle[] obstacleSequence;
    public Course(int obstaclesNumber){
        obstacleSequence = new Obstacle[obstaclesNumber];
        int randomNumber;
        for(int i = 0; i < obstacleSequence.length; i++){
            randomNumber = random.nextInt(4);
            switch(randomNumber){
                case 0:
                    obstacleSequence[i] = new Road();
                    break;
                case 1:
                    obstacleSequence[i] = new Pool();
                    break;
                case 2:
                    obstacleSequence[i] = new Pit();
                    break;
                case 3:
                    obstacleSequence[i] = new Wall();
                    break;
            }
        }
    }
    public void getObstaclesInfo(){
        for(Obstacle obstacle : this.obstacleSequence){
            obstacle.getObstacleInfo();
        }
    }
    private float passObstaclesOne(Teammate teammate){
        float time = 0;
        for(Obstacle obstacle : this.obstacleSequence){
            time += obstacle.passObstacle(teammate);
        }
        System.out.printf("%s passed the course in %.2f units of time.\n\n", teammate.name, time);
        return time;
    }
    public void passCourse(Team team){
        float timeMin = Float.POSITIVE_INFINITY, time;
        String nameWin = "";
        for(Teammate teammate : team.teammates){
            time = this.passObstaclesOne(teammate);
            if(time < timeMin){
                timeMin = time;
                nameWin = teammate.name;
            }
        }
        System.out.printf("%s won with time %.2f.\n\n\n", nameWin, timeMin);
    }
}
