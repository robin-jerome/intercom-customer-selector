package com.intercom.util;

import com.intercom.model.Customer;
import com.intercom.model.JSONParsingException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JSONSupportTest {

    @Test
    public void successfulParsingOfWellFormattedData() throws Exception {
        Customer customer = JSONSupport.fromJSON("{\"latitude\": \"52.833502\", " +
                "\"user_id\": 25, " +
                "\"name\": \"David Behan\", " +
                "\"longitude\": \"-8.522366\"}", Customer.class);
        assertEquals("David Behan", customer.getName());
        assertEquals(Double.valueOf(52.833502), customer.getLatitude());
        assertEquals(Double.valueOf(-8.522366), customer.getLongitude());
        assertEquals(Integer.valueOf(25), customer.getUserId());
    }

    @Test(expected = JSONParsingException.class)
    public void parsingFailsOnUnSupportedFields() throws Exception {
        JSONSupport.fromJSON("{\"randomString\": \"12345\"}", Customer.class);
    }

}