package com.oeitho.resource;

import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oeitho.exception.PlayerNotFoundException;
import com.oeitho.service.PlayerService;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

@Path("/player")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PlayerResource {

    @Inject
    PlayerService playerService;

    @POST
    public Response createPlayer(Map<String, Object> playerData) {
        try {
            String playerId = playerService.createPlayer(playerData);
            return Response
                .status(Response.Status.OK)
                .header("playerId", playerId)
                .build();
        } catch (JsonProcessingException exception) {
            return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .build();
        }    
    }

    @Path("/{playerId}")
    @GET
    public Response getPlayer(@PathParam String playerId) {
        try {
            Map<String, Object> player = playerService.getPlayer(playerId);
            return Response
                .status(Response.Status.OK)
                .entity(player)
                .build();
        } catch (PlayerNotFoundException exception) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .build();
        } catch (JsonProcessingException exception) {
            return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .build();
        }
    }

}