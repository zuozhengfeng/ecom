package com.yabobaozb.ecom;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest
public class H2ConnectionTest {
    @Autowired
    DataSource dataSource;

    @Test
    public void testDatabaseConnection() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            assertNotNull(connection);
            System.out.println("Connected to H2 in-memory database");
        }
    }
}
