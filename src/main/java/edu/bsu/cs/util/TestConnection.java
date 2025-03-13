package edu.bsu.cs.util;
import java.sql.*;
import java.util.Properties;

public class TestConnection {
    public static void main(String[] args) {
        try {
            System.out.println("Starting connection test...");
            Properties props = new Properties();
            props.setProperty("user", "postgres");
            props.setProperty("password", "Godisawoman0988.");
            // Disable SSL certificate validation
            props.setProperty("ssl", "true");
            props.setProperty("sslmode", "require");
            props.setProperty("sslfactory", "org.postgresql.ssl.NonValidatingFactory");
            props.setProperty("connectTimeout", "60");

            System.out.println("Attempting connection...");
            Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://db.gvaccigqarleavqzxvlv.supabase.co:5432/postgres",
                    props
            );
            System.out.println("Connected successfully!");
            conn.close();
        } catch (Exception e) {
            System.out.println("Connection failed with error:");
            e.printStackTrace();
        }
    }
}