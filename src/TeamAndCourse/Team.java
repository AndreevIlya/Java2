package TeamAndCourse;


public class Team {
    Teammate[] teammates;
    public Team(int teammatesNumber){
        teammates = new Teammate[teammatesNumber];
        for(int i = 0; i < teammates.length; i++){
            teammates[i] = new Teammate();
        }
    }
    public void teammatesInfo(){
        for(Teammate teammate : this.teammates){
            teammate.teammateInfo();
        }
    }
}
