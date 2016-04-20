package com.intercom;

import org.junit.Before;
import org.junit.Test;

public class CustomerFilterTest {

    @Before
    public void setUp() {

    }

    @Before
    public void tearDown() {

    }

    private CustomerFilter getValidCustomerFilter() {
        return new CustomerFilter("customers.txt", 100000, 53.3381985, -6.2592576);
    }

    @Test
    public void testFindCustomersInRangeSortedByIdDescending() throws Exception {

    }

    @Test
    public void testDistanceFromFocalPoint() throws Exception {

    }

    @Test
    public void testReadCustomersFromFile() throws Exception {

    }
}