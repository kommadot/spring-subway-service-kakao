package subway.path.caculator;

import subway.line.domain.Line;
import subway.line.domain.Section;
import subway.path.domain.ComplimentaryAge;

import java.util.Comparator;
import java.util.List;

public class FareCalculator {
    private static final int BASE_FARE = 1250;
    private static final int EXTRA_FARE_PER_DISTANCE = 100;
    private static final int DEDUCTION_FARE = 350;
    private static final int BASIC_FARE_DISTANCE = 10;
    private static final int PIVOT_OF_SECOND_DISTANCE_FOR_EXTRA_FARE = 50;
    private static final int OVER_SECOND_DISTANCE_EXTRA_FARE = 800;
    private static final double MIDDLE_DISTANCE_DENOMINATOR = 5;
    private static final double HIGH_DISTANCE_DENOMINATOR = 8;
    private static final int COMPLIMENTARY_CUSTOMER_FARE = 0;

    public static int getFinalFare(ComplimentaryAge complimentaryAge, int totalFare) {
        if (complimentaryAge.isTargetOfFree()) {
            return COMPLIMENTARY_CUSTOMER_FARE;
        }
        totalFare -= (totalFare - DEDUCTION_FARE) * complimentaryAge.getComplimentarySaleRate();
        return totalFare;
    }

    public static int getDistanceFare(int totalDistance) {
        int distanceFare = BASE_FARE;
        if (BASIC_FARE_DISTANCE < totalDistance && totalDistance <= PIVOT_OF_SECOND_DISTANCE_FOR_EXTRA_FARE) {
            distanceFare += Math.ceil((totalDistance - BASIC_FARE_DISTANCE) / MIDDLE_DISTANCE_DENOMINATOR) * EXTRA_FARE_PER_DISTANCE;
        } else if (PIVOT_OF_SECOND_DISTANCE_FOR_EXTRA_FARE < totalDistance) {
            distanceFare += OVER_SECOND_DISTANCE_EXTRA_FARE + Math.ceil((totalDistance - PIVOT_OF_SECOND_DISTANCE_FOR_EXTRA_FARE) / HIGH_DISTANCE_DENOMINATOR) * EXTRA_FARE_PER_DISTANCE;
        }
        return distanceFare;
    }

    public static int getLineExtraFare(List<Section> sections, List<Line> lines) {
        return lines.stream()
                .filter(line -> sections.stream()
                        .anyMatch(section -> line.getSections().isExist(section)))
                .max(Comparator.comparingInt(Line::getExtraFare))
                .get()
                .getExtraFare();
    }
}
