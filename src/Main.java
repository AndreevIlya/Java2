import TeamAndCourse.Course;
import TeamAndCourse.Team;

public class Main {

    public static void main(String[] args) {
        Course course = new Course(15);
        course.getObstaclesInfo();
        System.out.println();
        Team team = new Team(10);
        team.teammatesInfo();
        System.out.println();
        course.passCourse(team);
        team.teammatesInfo();
    }
}
