package com.intercom;

import com.intercom.model.Customer;
import com.intercom.util.JSONSupport;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomerFilter {
    private static final Double OFFICE_LATITUDE_DEGREES = Double.valueOf(53.3381985);
    private static final Double OFFICE_LONGITUDE_DEGREES = Double.valueOf(-6.2592576);
    private static final Double OFFICE_LATITUDE_RADIANS = Math.toRadians(OFFICE_LATITUDE_DEGREES);
    private static final Double INVITE_RADIUS_METRES = Double.valueOf(100 * 1000); // 100 Km
    private static final Double EARTH_RADIUS_METRES = Double.valueOf(6371 * 1000); // 6371 Km
    // Should be in the path
    private final String CUSTOMER_FILE_NAME = "customers.txt";

    public static void main(String[] args) {
        try {
            new CustomerFilter()
                    .readCustomersFromFile()
                    .stream().filter(c -> distanceFromOffice(c) <= INVITE_RADIUS_METRES)
                    .forEach(c -> System.out.println(c.getName() + " -> " + distanceFromOffice(c)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method uses the 'Vincenty formula’ to calculate the great-circle distance between customer and the office
     * Refer - https://en.wikipedia.org/wiki/Great-circle_distance
     * @param customer
     * @return distance of the customer from the office
     */
    private static double distanceFromOffice(Customer customer) {
        // For brevity in formulas
        double la1 = Math.toRadians(customer.getLatitude());
        double la2 = OFFICE_LATITUDE_RADIANS;
        double deltaLo = Math.toRadians(Math.abs(customer.getLongitude() - OFFICE_LONGITUDE_DEGREES));
        double numeratorTerm1 = Math.pow(Math.cos(la2) * Math.sin(deltaLo), 2);
        double numeratorTerm2 = Math.pow(Math.cos(la1) * Math.sin(la2) - Math.sin(la1) * Math.cos(la2) * Math.cos(deltaLo), 2);
        double denominatorTerm = Math.sin(la1) * Math.sin(la2) + Math.cos(la1) * Math.cos(la2) * Math.cos(deltaLo);
        double centralAngle = Math.atan2(Math.sqrt(numeratorTerm1 + numeratorTerm2), denominatorTerm);

        return EARTH_RADIUS_METRES * centralAngle;
    }

    private List<Customer> readCustomersFromFile() throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(CUSTOMER_FILE_NAME), Charset.forName("utf-8"))) {
            return stream.filter(s -> !s.isEmpty())
                    .map(jsonStr -> JSONSupport.fromJSON(jsonStr, Customer.class))
                    .collect(Collectors.toList());
        }
    }
}
