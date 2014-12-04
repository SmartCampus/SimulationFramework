package org.smartcampus.simulation.services;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

public interface Service {

    @Path("simulations/simulation")
    @POST
    public Response launchSimulation(@QueryParam("simulationParams") String jsonParams);

    @Path("simulations/eventsimulation")
    @POST
    public Response launchEventSimulation(@QueryParam("simulationParams") String jsonParams);

}
