package server.armory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class DataBase {

    private String DB_DRIVER = "org.postgresql.Driver";
    private String DB_CONNECTION;
    private String DB_USER;
    private String DB_PASSWORD;
    private Connection connection;

    String checkUser = "SELECT * FROM USERS WHERE username = ?";
    String adduser = "INSERT INTO USERS (username, password) VALUES (?, ?)";


    public DataBase ( ) {
        while (connection == null) {
            startInizialization( );
            this.connection = getDBConnection( );
        }
    }


    public void startInizialization ( ) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите, пожалуйста, порт для подключения к БДэшечке: ");
        DB_CONNECTION = "jdbc:postgresql://localhost:" + scanner.nextLine( ).trim( ) + "/studs";
        System.out.print("Введите, пожалуйста, имя пользователя: ");
        DB_USER = scanner.nextLine( ).trim( );
        System.out.print("Введите, пожалуйста, пароль: ");
        DB_PASSWORD = scanner.nextLine( );
    }

    public Connection getDBConnection ( ) {
        Connection dbConnection = null;
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage( ));
        }
        try {
            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            return dbConnection;
        } catch (SQLException e) {
            System.out.println(e.getMessage( ));
        }
        return dbConnection;
    }

    public String authentication (String username, String expectedPassword) {
        try {
            PreparedStatement ps = connection.prepareStatement(checkUser);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery( );
            String password = null;
            if (rs.next( )) {
                password = rs.getString(2);
            }
            if (password == null) {

                PreparedStatement ps2 = connection.prepareStatement(adduser);
                ps2.setString(1, username);
                ps2.setString(2, getHashPassword(expectedPassword));

                ps2.executeUpdate( );

                return "Вы успешно зарегистрировались";
            } else {
                if (password.equals(getHashPassword(expectedPassword))) return "Вы успешно авторизовались";
                return "Упс...Если вы ранее регистрировались под этим логином, то указанный вами пароль неверен:( \n Если же вы регистрируетесь впервые, вам стотит выбрать другой логин";
            }
        } catch (SQLException ex) {
            ex.printStackTrace( );
            return "";
        } catch (NoSuchElementException ex) {
            ex.printStackTrace( );
            return "";
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace( );
            return "";
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace( );
            return "";
        }
    }

    public String getHashPassword (String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance("SHA-384");
        byte[] data = password.getBytes("UTF-8");
        byte[] hashbytes = sha.digest(data);
        String expPass = new String(hashbytes, "UTF-8");
        return expPass;
    }


}
