package com.tretonchik.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.j256.ormlite.dao.Dao;
import com.tretonchik.models.Meme;
import com.tretonchik.models.MemeReview;
import com.tretonchik.models.User;
import com.tretonchik.models.UserInteraction;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserInteractionDeserializer extends StdDeserializer<UserInteraction> {
    private final Dao<User,Integer> daoUser;
    public UserInteractionDeserializer(Dao<User,Integer> daoUser) {
        super(MemeReview.class);
        this.daoUser=daoUser;
    }
    @Override
    public UserInteraction deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode root=parser.getCodec().readTree(parser);
        int id=root.get("id").asInt();
        int source=root.get("source").asInt();
        int target=root.get("target").asInt();
        String reaction=root.get("reaction").asText();
        String date=root.get("date").asText();
        DateTimeFormatter f=DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate dateNew=LocalDate.parse(date,f);
        try {
            return new UserInteraction(daoUser.queryForId(source),daoUser.queryForId(target),reaction,dateNew,id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
}
