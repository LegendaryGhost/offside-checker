package openCvUtils;

import java.util.List;
import java.util.stream.Collectors;

public class Analyse {

    public static PointRadius findOffsideLine(PointRadius lastDefense, PointRadius ball, int direction ){
        double checker = lastDefense.getPoint().y - ball.getPoint().y;
        if(checker > 0){ // defenseur eo ambany dia bol ambony
            if(direction == 1){ // sens midina
                return lastDefense;
            }
            else{
                return ball;
            }
        }
        else{ // defenseur eo ambony dia bol ambany
            if(direction == 1){ // sens midina
                return ball;
            }
            else{
                return lastDefense;
            }
        }
    }
    

    public static List<PointRadius> findOpponent(List<PointRadius> all, PointRadius teamMate) {
        if (all == null || teamMate == null) {
            return null;
        }

        String teamMateColor = teamMate.getColor();
        return all.stream()
                  .filter(pr -> !pr.getColor().equals(teamMateColor))
                  .collect(Collectors.toList());
    }


    public static int findDirection(PointRadius pWithBall, List<PointRadius> all){
        PointRadius top = all.getFirst();
        if(!top.getColor().equals(pWithBall.getColor())){
            double directionChecker = pWithBall.getPoint().y - top.getPoint().y;
            if(directionChecker > 0){
                return -1;
            }
            else{
                return 1;
            }
        }
        else{
            PointRadius bottom = all.getLast();
            double directionChecker = pWithBall.getPoint().y - bottom.getPoint().y;
            if(directionChecker > 0){
                return -1;
            }
            else{
                return 1;
            }
        }
    }

    public static PointRadius getPlayerClosestToBall(List<PointRadius> all,PointRadius black){
        if (all == null || all.isEmpty() || black == null) {
            return null;
        }

        PointRadius closest = null;
        double minDistance = Double.MAX_VALUE;

        for (PointRadius pr : all) {
            double distance = calculateDistance(pr, black);
            if (distance < minDistance) {
                minDistance = distance;
                closest = pr;
            }
        }

        return closest;
    }

    private static double calculateDistance(PointRadius pr1, PointRadius pr2) {
        double dx = pr1.getPoint().x - pr2.getPoint().x;
        double dy = pr1.getPoint().y - pr2.getPoint().y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    
}
