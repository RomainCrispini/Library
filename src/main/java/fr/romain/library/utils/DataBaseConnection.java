package fr.romain.library.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataBaseConnection implements Constants {

    public Connection databaseLink;

    public Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connecté !!!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur de Connection");
        }
        return databaseLink;
    }
}
