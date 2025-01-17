package openCvUtils;

import java.util.List;
import java.util.stream.Collectors;

public class Analyse {

    public static Circle findOffsideLine(Circle lastDefense, Circle ball, int direction ){
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
    

    public static List<Circle> findOpponent(List<Circle> all, Circle teamMate) {
        if (all == null || teamMate == null) {
            return null;
        }

        String teamMateColor = teamMate.getColor();
        return all.stream()
                  .filter(pr -> !pr.getColor().equals(teamMateColor))
                  .collect(Collectors.toList());
    }


    public static int findDirection(Circle pWithBall, List<Circle> all){
        Circle top = all.getFirst();
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
            Circle bottom = all.getLast();
            double directionChecker = pWithBall.getPoint().y - bottom.getPoint().y;
            if(directionChecker > 0){
                return -1;
            }
            else{
                return 1;
            }
        }
    }

    public static Circle getPlayerClosestToBall(List<Circle> all, Circle black){
        if (all == null || all.isEmpty() || black == null) {
            return null;
        }

        Circle closest = null;
        double minDistance = Double.MAX_VALUE;

        for (Circle pr : all) {
            double distance = calculateDistance(pr, black);
            if (distance < minDistance) {
                minDistance = distance;
                closest = pr;
            }
        }

        return closest;
    }

    private static double calculateDistance(Circle pr1, Circle pr2) {
        double dx = pr1.getPoint().x - pr2.getPoint().x;
        double dy = pr1.getPoint().y - pr2.getPoint().y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    
}
