package com.agricloud.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.AbstractShellComponent;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.Availability;

import com.agricloud.commands.AuthCommands;

import com.agricloud.service.PlotService;
import com.agricloud.context.UserContext;

@ShellComponent("Plot Commands")
public class PlotCommands extends AbstractShellComponent {
    
    @Autowired
    private PlotService plotService;

    @Autowired
    private UserContext userContext;

    @Autowired
    private AuthCommands authCommands;

    @ShellMethod(key = "plot create", value = "test command")
    @ShellMethodAvailability("availabilityCheck")
    public String createPlot(
        @ShellOption(value = "name", help = "Name of the plot") String plotName,
        @ShellOption(value = "desc", help = "Description of the plot") String description,
        @ShellOption(value = "grow-zone", help = "Grow Zone where the plot will be provisioned") int growZone, 
        @ShellOption(value = "plot-type", help = "Type of plot") int plotTypeID,
        @ShellOption(value = "config", help = "Config connected to the plot")  int configID
    ) {
        if (userContext.getLoggedInUser()!=null) {
            return plotService.createPlot(plotName, description, growZone, plotTypeID, configID);
        } else {
            authCommands.login();
            return "login before creating a plot";
        }
    } 

    @ShellMethod(key = "plot info", value = "test command")
    @ShellMethodAvailability("availabilityCheck")
    public String getPlotInfo(
        @ShellOption(value = "plot-id", help = "Plot ID") int plotID
    ) {
        if (userContext.getLoggedInUser()!=null) {
            return plotService.getPlotInfo(plotID);
        } else {
            authCommands.login();
            return "login before creating a plot";
        }
        
    } 

    @ShellMethod(key = "plot terminate", value = "test command")
    @ShellMethodAvailability("availabilityCheck")
    public String terminatePlot(
        @ShellOption(value = "plot-id", help = "Plot ID") int plotID
    ) {
        return plotService.terminate(plotID);
    } 

    public Availability availabilityCheck() {
        boolean connected=false;
         if (userContext.getLoggedInUser()!=null)
            connected = true;
         
        return connected
        ? Availability.available()
        : Availability.unavailable("You are not logged in, use login command to login");
    }
}
