/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Marko
 */
public class DBConfigReader {
    public static Properties loadProperties() throws IOException {
        Properties props = new Properties();
        try (InputStream input = DBConfigReader.class.getClassLoader().getResourceAsStream("properties/database.properties")) {
            if (input == null) {
                throw new IOException("db.properties fajl nije pronađen!");
            }
            props.load(input);
        }
        return props;
    }
}
