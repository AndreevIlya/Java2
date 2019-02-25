import TeamAndCourse.Course;
import TeamAndCourse.Team;

public class Main {

    public static void main(String[] args) {
        Course course = new Course(10);
        course.getObstaclesInfo();
        System.out.println();
        Team team = new Team(5);
        team.teammatesInfo();
        System.out.println();
        course.passCourse(team);
        team.teammatesInfo();
    }
}
