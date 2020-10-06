package com.oeitho.service;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oeitho.exception.PlayerNotFoundException;
import com.oeitho.utils.RandomString;

import org.infinispan.client.hotrod.RemoteCache;

import io.quarkus.infinispan.client.Remote;

@ApplicationScoped
public class PlayerService {

    RandomString randomString = new RandomString();
    ObjectMapper mapper = new ObjectMapper();

    @Inject
    @Remote("PlayerCache")
    RemoteCache<String, String> playerCache;

    public String createPlayer(final Map<String, Object> playerData) throws JsonProcessingException {
        final String playerId = randomString.randomString(RandomString.UPPER_CASE_ALPHANUMERIC, 9);
        final String json = mapper.writeValueAsString(playerData);
        playerCache.put(playerId, json);
        return playerId;
    }

    public Map<String, Object> getPlayer(final String playerId) throws PlayerNotFoundException, JsonProcessingException {
        final String json = playerCache.get(playerId);
        if (json == null) {
            throw new PlayerNotFoundException("Game with id " + playerId + " not found!");
        }
        final Map<String, Object> player = mapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        return player;
    }

}