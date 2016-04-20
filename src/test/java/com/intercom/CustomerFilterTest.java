package com.intercom;

import com.intercom.model.Customer;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CustomerFilterTest {

    private static final double MAX_RANGE = 100 * 1000; // 100 Kms
    private static final CustomerFilter CUSTOMER_FILTER = new CustomerFilter("customers.txt", MAX_RANGE, 53.3381985, -6.2592576);

    @Test
    public void customersAreSortedByDescendingOrderOfUserId() throws Exception {
        List<Customer> customers = CUSTOMER_FILTER.findCustomersInRangeSortedByIdDescending();
        for (int i = 0; i < customers.size(); i++) {
            if (i + 1 < customers.size()) {
                // There are more elements
                assertTrue("Customers are not sorted by descending userId", customers.get(i).getUserId() >= customers.get(i + 1).getUserId());
            }
        }
    }

    @Test
    public void testDistanceFromFocalPoint() throws Exception {
        // Distance from self is zero
        assertTrue("Distance from a point to itself is zero", CUSTOMER_FILTER.distanceFromFocalPoint(53.3381985, -6.2592576) == 0.0);
        // Minor changes in latitude and longitude
        assertTrue("Distance between points should be less than " + MAX_RANGE,
                CUSTOMER_FILTER.distanceFromFocalPoint(53.0381985, -6.2592576) < MAX_RANGE);
        assertTrue("Distance between points should be less than " + MAX_RANGE,
                CUSTOMER_FILTER.distanceFromFocalPoint(53.3381985, -6.1592576) < MAX_RANGE);
        // Major changes in latitude and longitude
        assertTrue("Distance between points should be greater than " + MAX_RANGE,
                CUSTOMER_FILTER.distanceFromFocalPoint(52.3381985, -6.2592576) > MAX_RANGE);
        assertTrue("Distance between points should be greater than " + MAX_RANGE,
                CUSTOMER_FILTER.distanceFromFocalPoint(53.3381985, -8.2592576) > MAX_RANGE);
    }

    @Test
    public void testReadCustomersFromFile() throws Exception {
        List<Customer> customers = new CustomerFilter("customers.txt", MAX_RANGE, 53.3381985, -6.2592576).readCustomersFromFile();
        assertFalse(customers.isEmpty());
    }

    @Test(expected = IOException.class)
    public void testReadCustomersFromNonExistingFileThrowsException() throws Exception {
        new CustomerFilter("randomFile.txt", MAX_RANGE, 53.3381985, -6.2592576).readCustomersFromFile();
    }
}