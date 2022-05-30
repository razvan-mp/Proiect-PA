package utilities;

import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.Extent;

/**
 * Utility class for getting constant information for the predefined coordinates of points on the map
 */
public class Info {
    public static final Coordinate COORD_EXPO = new Coordinate(47.18458144737681, 27.565773142875663);
    public static final Coordinate COORD_ROND_DACIA = new Coordinate(47.16852245494382, 27.553073638333757);
    public static final Coordinate COORD_NORD_IASI = new Coordinate(47.230760, 27.583033);
    public static final Coordinate COORD_SUD_IASI = new Coordinate(47.126273, 27.596461);
    public static final Coordinate COORD_VEST_IASI = new Coordinate(47.175779, 27.499281);
    public static final Coordinate COORD_EST_IASI = new Coordinate(47.168813, 27.670672);
    public static final Extent EXTINDERE_IASI = Extent.forCoordinates(COORD_NORD_IASI, COORD_SUD_IASI, COORD_VEST_IASI, COORD_EST_IASI);
}
