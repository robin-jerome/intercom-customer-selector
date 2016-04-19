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

    private final Double OFFICE_LATITUDE = Double.valueOf(53.3381985);
    private final Double OFFICE_LONGITUDE = Double.valueOf(-6.2592576);
    private final Double RADIUS = Double.valueOf(100);
    // Should be in the path
    private final String CUSTOMER_FILE_NAME = "customers.txt";

    public static void main(String[] args) {
        try {
            new CustomerFilter().readCustomersFromFile().forEach(c -> System.out.println(c.getName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Customer> readCustomersFromFile() throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(CUSTOMER_FILE_NAME), Charset.forName("utf-8"))) {
            return stream.map(jsonStr -> JSONSupport.fromJSON(jsonStr, Customer.class)).collect(Collectors.toList());
        }
    }
}
