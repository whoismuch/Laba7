package common.command;


import common.generatedClasses.Route;

import java.io.Serializable;

public class CommandDescription implements Serializable {

    private String name;
    private String arg;
    private Route route;
    private String username;
    private String password;
    private String choice;


    public CommandDescription(String name, String arg, Route route, String username, String password, String choice)  {
        this.setName(name);
        this.setArg(arg);
        this.route = route;
        this.username = username;
        this.password = password;
        this.choice = choice;
    }

    @Override
    public String toString() {
        return "CommandDescription{" +
                "name='" + name + '\'' +
                ", arg='" + arg + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getArg() {
        return arg;
    }

    public Route getRoute() {return route;}

    public void setName(String name) {
        this.name = name;
    }

    public void setArg(String arg) {
        this.arg = arg;
    }

    public void setRoute(Route route) {this.route = route;}

    public String getUsername ( ) {
        return username;
    }

    public String getPassword ( ) {
        return password;
    }

    public String getChoice ( ) {
        return choice;
    }
}
