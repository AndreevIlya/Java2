package TeamAndCourse;

public interface Actions {
    float run(Road road);
    float jump(Pit pit);
    float swim(Pool pool);
    float climb(Pit pit);
    float climb(Wall wall);
}
