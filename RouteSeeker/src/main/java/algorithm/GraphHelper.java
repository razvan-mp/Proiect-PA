package algorithm;

import com.sothawo.mapjfx.MapCircle;

public class GraphHelper {
    public static double getDistance(MapCircle firstPoint, MapCircle secondPoint) {
        double lon1 = firstPoint.getCenter().getLongitude();
        double lon2 = secondPoint.getCenter().getLongitude();
        double lat1 = firstPoint.getCenter().getLatitude();
        double lat2 = secondPoint.getCenter().getLatitude();

        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));
        double r = 6371;

        return (c * r * 1000);
    }


}
