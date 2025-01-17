package openCvUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ListUtils {

    public static void orderedAsc(List<Circle> all) {
	all.sort(Comparator.comparingDouble(pr -> pr.getPoint().y));
    }

    public static List<Circle> gatherAll(List<Circle> blue, List<Circle> red) {
	List<Circle> all = new ArrayList<>();
	all.addAll(blue);
	all.addAll(red);
	return all;
    }

}
