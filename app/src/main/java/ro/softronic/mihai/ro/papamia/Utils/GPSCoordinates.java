package ro.softronic.mihai.ro.papamia.Utils;

public class GPSCoordinates {
    public float longitude = -1;
    public float latitude = -1;

    public GPSCoordinates(float theLatitude, float theLongitude) {
        longitude = theLongitude;
        latitude = theLatitude;
    }

    public GPSCoordinates(double theLatitude, double theLongitude) {
        longitude = (float) theLongitude;
        latitude = (float) theLatitude;
    }
}