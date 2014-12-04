package org.smartcampus.simulation.services;

import org.json.JSONException;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/smartcampus")
public class ServiceImpl implements Service {

    @Override
    public Response launchSimulation(String json) {
        try {
            SimulationLauncher.launchSimulation(json, false);
        } catch (JSONException e) {
            Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok().build();
    }

    @Override
    public Response launchEventSimulation(String json) {
        try {
            SimulationLauncher.launchSimulation(json, true);
        } catch (JSONException e) {
            Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok().build();
    }
}
