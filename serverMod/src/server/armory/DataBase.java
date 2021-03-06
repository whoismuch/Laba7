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
    String seqFromBegin = "ALTER SEQUENCE id RESTART WITH";


    public DataBase (String port, String user, String password) {
        DB_CONNECTION = "jdbc:postgresql://pg:" + port + "/studs";
        DB_USER = user;
        DB_PASSWORD = password;
        this.connection = getDBConnection( );
        while (connection == null) {
            startInizialization( );
            this.connection = getDBConnection( );
        }
    }


    public void startInizialization ( ) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите, пожалуйста, порт для подключения к БДэшечке: ");
        DB_CONNECTION = "jdbc:postgresql://pg:" + scanner.nextLine( ).trim( ) + "/studs";
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

    public String authentication (String username) {
        try {
            PreparedStatement ps = connection.prepareStatement(checkUser);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery( );
            String password = null;
            if (rs.next( )) {
                password = rs.getString(2);
            }
            return password;

        } catch (SQLException ex) {
            ex.printStackTrace( );
            return "";
        }
    }

    public String registration (String username, String expectedPassword) {
        try {
            String password = authentication(username);
            if (password != null)
                return "Пользователь с таким логином уже зарегистрирован. Может, вам стоит авторизоваться?";
            else {
                PreparedStatement ps2 = connection.prepareStatement(addUser);
                ps2.setString(1, username);
                ps2.setString(2, getHashPassword(expectedPassword));
                ps2.executeUpdate( );

                return "Вы успешно зарегистрировались";
            }
        } catch (SQLException ex) {
            ex.printStackTrace( );
            return "";
        } catch (NoSuchElementException ex) {
            ex.printStackTrace( );
            return "";
        }
    }

    public String authorization (String username, String expectedPassword) {
        String password = authentication(username);
        if (password == null) return "Пользователь с таким логином не зарегистрирован";
        if (password.equals(getHashPassword(expectedPassword))) return "Вы успешно авторизовались";
        return "Вы ввели неправильный пароль";
    }

    public String getHashPassword (String password) {
        String hashtext = "";
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-384");

            byte[] messageDigest = sha.digest(password.getBytes( ));
            BigInteger no = new BigInteger(1, messageDigest);

            hashtext = no.toString(16);
            return hashtext;
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Упс");
        }
        return hashtext;
    }

    public boolean add (Route route, String username) {
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
            if (ps.executeUpdate( ) == 0) return false;
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace( );
            return false;
        }
    }


    public Timestamp getTimestamp (Route route) {
        Timestamp timestamp = Timestamp.valueOf(route.getCreationDate( ).toLocalDateTime( ));
        return timestamp;
    }

    public String getTimeZone (Route route) {
        String timeZone = route.getCreationDate( ).getZone( ).toString( );
        return timeZone;
    }

    public Long load (LinkedHashSet<Route> routes) {
        Long finalId = new Long(0);
        try {
            Statement stmt = connection.createStatement( );
            ResultSet rs3 = stmt.executeQuery(load);

            while (rs3.next( )) {
                Long id = rs3.getLong("id");
                String name = rs3.getString("name");
                Long coordinate_X = rs3.getLong("coordinate_X");
                int coordinate_Y = rs3.getInt("coordinate_Y");
                Timestamp timestamp = rs3.getTimestamp("creationDate");
                String timeZoneStr = rs3.getString("timeZone");
                LocalDateTime localDateTime = timestamp.toLocalDateTime( );
                ZonedDateTime zdt = localDateTime.atZone(ZoneId.of(timeZoneStr));
                long from_X = rs3.getLong("from_X");
                Long from_Y = rs3.getLong("from_Y");
                String from_name = rs3.getString("from_name");
                Long to_X = rs3.getLong("to_X");
                Long to_Y = rs3.getLong("to_Y");
                String to_name = rs3.getString("to_name");
                Float distance = rs3.getFloat("distance");

                Route route = new Route(name, id, new Coordinates(coordinate_X, coordinate_Y), zdt, new Location(from_name, from_X, from_Y), new Location(to_name, to_X, to_Y), distance);
                route.setUsername(rs3.getString("username"));
                routes.add(route);

                finalId = id;
            }
        } catch (SQLException e) {
            e.printStackTrace( );
        }
        doSeqFromBegin(finalId);
        return finalId;
    }


    public boolean deleteRoutes (String username) {
        try {
            PreparedStatement ps = connection.prepareStatement(deleteRoutes);
            ps.setString(1, username);
            if (ps.executeUpdate( ) == 0) return false;
            return true;
        } catch (SQLException e) {
            e.printStackTrace( );
            return false;
        }
    }


    public boolean removeById (long id, String username) {
        try {
            PreparedStatement ps = connection.prepareStatement(deleteRouteById);
            ps.setString(1, username);
            ps.setLong(2, id);
            if (ps.executeUpdate( ) == 0) return false;
            return true;
        } catch (SQLException e) {
            e.printStackTrace( );
            return false;
        }
    }

    public boolean updateId (long id, Route route, String username) {
        if (removeById(id, username)) {
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
                if (ps.executeUpdate( ) == 0) return false;
                return true;
            } catch (SQLException e) {
                e.printStackTrace( );
            }
        }
        return false;
    }

    public void doSeqFromBegin (Long id) {
        try {
            Long newId = id + 1;
            seqFromBegin += " " + newId.toString( ) + ";";
            PreparedStatement ps = connection.prepareStatement(seqFromBegin);
            ps.executeUpdate( );
        } catch (SQLException e) {
            e.printStackTrace( );
        }

    }

    public void theEnd ( ) {
        try {
            connection.close( );
        } catch (SQLException e) {
            e.printStackTrace( );
        }
    }


}
