package server.armory;

import common.generatedClasses.Coordinates;
import common.generatedClasses.Location;
import common.generatedClasses.Route;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class DataBase {

    private String DB_DRIVER = "org.postgresql.Driver";
    private String DB_CONNECTION;
    private String DB_USER;
    private String DB_PASSWORD;
    private Connection connection;

    String checkUser = "SELECT * FROM USERS WHERE username = ?";
    String addUser = "INSERT INTO USERS (username, password) VALUES (?, ?)";
    String addRoute = "INSERT INTO ROUTES (username, name, coordinate_X, coordinate_Y, creationDate, timeZone, from_X, from_Y, from_name, to_X, to_Y, to_name, distance) VALUES (?, ?, ?, ?, ?, ? ,?,?,?, ?, ?, ?, ?);";
    String addRouteWithId = "INSERT INTO ROUTES (username, id, name, coordinate_X, coordinate_Y, creationDate, timeZone, from_X, from_Y, from_name, to_X, to_Y, to_name, distance) VALUES (?, ?, ?, ?, ?, ?, ? ,?,?,?, ?, ?, ?, ?);";
    String load = "SELECT * FROM ROUTES";
    String deleteRoutes = "DELETE FROM ROUTES WHERE username = ?;";
    String deleteRouteById = "DELETE FROM ROUTES WHERE username = ? AND id = ?;";
    String seqFromBegin ="ALTER SEQUENCE id RESTART WITH 1;";


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
            System.out.println(username + username.length());
            System.out.println(expectedPassword + expectedPassword.length());
            PreparedStatement ps = connection.prepareStatement(checkUser);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery( );
            String password = null;
            if (rs.next( )) {
                password = rs.getString(2);
            }
            if (password == null) {

                PreparedStatement ps2 = connection.prepareStatement(addUser);
                ps2.setString(1, username);
                ps2.setString(2, getHashPassword(expectedPassword));

                System.out.println(username + " " + getHashPassword(expectedPassword));

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

        byte[] messageDigest = sha.digest(password.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);

        String hashtext = no.toString(16);
        return hashtext;
    }

    public boolean add (Route route, String username){
        try {
            PreparedStatement ps = connection.prepareStatement(addRoute);
            ps.setString(1, username);
            ps.setString(2, route.getName( ));
            ps.setLong(3, route.getCoordinates( ).getX( ));
            ps.setInt(4, route.getCoordinates( ).getY( ));
            ps.setTimestamp(5, getTimestamp(route));
            ps.setString(6, getTimeZone(route));
            ps.setLong(7, route.getFrom( ).getX( ));
            ps.setLong(8, route.getFrom( ).getY( ));
            ps.setString(9, route.getFrom( ).getName( ));
            ps.setLong(10, route.getTo( ).getX( ));
            ps.setLong(11, route.getTo( ).getY( ));
            ps.setString(12, route.getTo( ).getName( ));
            ps.setFloat(13, route.getDistance( ));
            ps.executeUpdate( );
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace( );
            return false;
        }
    }


    public Timestamp getTimestamp (Route route) {
        Timestamp timestamp = Timestamp.valueOf(route.getCreationDate().toLocalDateTime());
        return timestamp;
    }

    public String getTimeZone (Route route) {
        String timeZone = route.getCreationDate().getZone().toString();
        return timeZone;
    }

    public Long load(LinkedHashSet<Route> routes) {
        Long finalId = new Long(0);
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs3 = stmt.executeQuery(load);

            while (rs3.next()) {
                Long id = rs3.getLong("id");
                String name = rs3.getString("name");
                Long coordinate_X = rs3.getLong("coordinate_X");
                int coordinate_Y = rs3.getInt("coordinate_Y");
                Timestamp timestamp = rs3.getTimestamp("creationDate");
                String timeZoneStr = rs3.getString("timeZone");
                LocalDateTime localDateTime = timestamp.toLocalDateTime();
                ZonedDateTime zdt = localDateTime.atZone(ZoneId.of(timeZoneStr));
                long from_X = rs3.getLong("from_X");
                Long from_Y = rs3.getLong("from_Y");
                String from_name = rs3.getString("from_name");
                Long to_X = rs3.getLong("to_X");
                Long to_Y = rs3.getLong("to_Y");
                String to_name = rs3.getString("to_name");
                Float distance = rs3.getFloat("distance");

                Route route = new Route(name, id, new Coordinates(coordinate_X, coordinate_Y), zdt, new Location(from_name, from_X, from_Y), new Location(to_name, to_X, to_Y), distance);
                routes.add(route);

                finalId = id;
            }
        } catch (SQLException e) {
            e.printStackTrace( );
        }
        if (finalId.equals(0L)) doSeqFromBegin();
        return finalId;
    }



    public int deleteRoutes (String username) {
        int count = 0;
        try {
            PreparedStatement ps = connection.prepareStatement(deleteRoutes);
            ps.setString(1, username);
            count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace( );
        }
        return count;
    }


    public int removeById (long id, String username) {
        System.out.println(id);
        int count = 0;
        try {
            PreparedStatement ps = connection.prepareStatement(deleteRouteById);
            ps.setString(1, username);
            ps.setLong(2, id);
            count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace( );
        }
        System.out.println(count);
        return count;

    }

    public boolean updateId (long id, Route route, String username) {
        int a = removeById(id, username);
        System.out.println("remove " + a );
        if (a > 0 ) {
            try {
                PreparedStatement ps = connection.prepareStatement(addRouteWithId);
                ps.setString(1, username);
                ps.setLong(2, id);
                ps.setString(3, route.getName( ));
                ps.setLong(4, route.getCoordinates( ).getX( ));
                ps.setInt(5, route.getCoordinates( ).getY( ));
                ps.setTimestamp(6, getTimestamp(route));
                ps.setString(7, getTimeZone(route));
                ps.setLong(8, route.getFrom( ).getX( ));
                ps.setLong(9, route.getFrom( ).getY( ));
                ps.setString(10, route.getFrom( ).getName( ));
                ps.setLong(11, route.getTo( ).getX( ));
                ps.setLong(12, route.getTo( ).getY( ));
                ps.setString(13, route.getTo( ).getName( ));
                ps.setFloat(14, route.getDistance( ));
                ps.executeUpdate( );
                return true;
            } catch (SQLException e) {
                e.printStackTrace( );
            }
        }
        return false;
    }

    public void doSeqFromBegin() {
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(seqFromBegin);
        } catch (SQLException e) {
            e.printStackTrace( );
        }

    }


}
