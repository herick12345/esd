package app;

/**
 * Armazena informações de localização de uma cidade
 */
public class LocationInfo {
    private final int geonameId;
    private final String countryCode;
    private final String cityName;
    
    public LocationInfo(int geonameId, String countryCode, String cityName) {
        this.geonameId = geonameId;
        this.countryCode = countryCode != null ? countryCode : "";
        this.cityName = cityName != null ? cityName : "";
    }
    
    public int getGeonameId() {
        return geonameId;
    }
    
    public String getCountryCode() {
        return countryCode;
    }
    
    public String getCityName() {
        return cityName;
    }
    
    @Override
    public String toString() {
        return String.format("LocationInfo[id=%d, country=%s, city=%s]", 
                           geonameId, countryCode, cityName);
    }
}