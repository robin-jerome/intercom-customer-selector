package com.intercom;

import com.intercom.model.Customer;
import com.intercom.util.JSONSupport;
import lombok.Getter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class CustomerFilter {

    private static final Double EARTH_RADIUS_METRES = Double.valueOf(6371 * 1000); // 6371 Km
    private final String customerFileName;
    private final Double focalLatitude;
    private final Double focalLongitude;
    private final Double inviteRadius;
    private final Double focalLatitudeRadians;


    /**
     * @param fileName File name containing the list of customers in json format so that each line corresponds to a customer
     * @param inviteRadius Invite radius in metres from the focal point (location of the office)
     * @param latitude Latitude of the office location in degrees
     * @param longitude Longitude of the office location in degrees
     */
    public CustomerFilter(String fileName, double inviteRadius, double latitude, double longitude) {
        this.customerFileName = fileName;
        this.inviteRadius = inviteRadius;
        this.focalLatitude = latitude;
        this.focalLongitude = longitude;
        this.focalLatitudeRadians = Math.toRadians(latitude);
    }

    /**
     * This function returns the list of customers that are within the range defined by the filter.
     * The returned values are sorted by descending order of userId
     * @return List of customers
     * @throws IOException
     */
    public List<Customer> findCustomersInRangeSortedByIdDescending() throws IOException {
        return readCustomersFromFile(customerFileName).stream()
                .filter(c -> distanceFromFocalPoint(c.getLatitude(), c.getLongitude()) <= inviteRadius)
                .sorted((o1, o2) -> o2.getUserId().compareTo(o1.getUserId()))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        try {
            new CustomerFilter("customers.txt", 100000, 53.3381985, -6.2592576)
                    .findCustomersInRangeSortedByIdDescending()
                    .forEach(c -> System.out.println(c.getUserId() + " -> " + c.getName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method uses the 'Vincenty formula’ to calculate the great-circle distance between customer and the office
     * Refer - https://en.wikipedia.org/wiki/Great-circle_distance
     * @param latitude in degrees
     * @param longitude in degrees
     * @return distance of the customer from the office
     */
    double distanceFromFocalPoint(Double latitude, Double longitude) {
        // For brevity in formulas
        double la1 = Math.toRadians(latitude);
        double la2 = focalLatitudeRadians;
        double deltaLongitude = Math.toRadians(Math.abs(longitude - focalLongitude));
        double numeratorTerm1 = Math.pow(Math.cos(la2) * Math.sin(deltaLongitude), 2);
        double numeratorTerm2 = Math.pow(Math.cos(la1) * Math.sin(la2) - Math.sin(la1) * Math.cos(la2) * Math.cos(deltaLongitude), 2);
        double denominatorTerm = Math.sin(la1) * Math.sin(la2) + Math.cos(la1) * Math.cos(la2) * Math.cos(deltaLongitude);
        double centralAngle = Math.atan2(Math.sqrt(numeratorTerm1 + numeratorTerm2), denominatorTerm);

        return EARTH_RADIUS_METRES * centralAngle;
    }

    List<Customer> readCustomersFromFile(String fileName) throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(fileName), Charset.forName("utf-8"))) {
            return stream.filter(s -> !s.isEmpty())
                    .map(jsonStr -> JSONSupport.fromJSON(jsonStr, Customer.class))
                    .collect(Collectors.toList());
        }
    }
}
