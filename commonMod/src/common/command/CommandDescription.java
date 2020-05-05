package common.command;


import common.generatedClasses.Route;

import java.io.Serializable;

public class CommandDescription implements Serializable {

    private String name;
    private String arg;
    private Route route;

    public CommandDescription(String name, String arg, Route route)  {
        this.setName(name);
        this.setArg(arg);
        this.route = route;
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
}
