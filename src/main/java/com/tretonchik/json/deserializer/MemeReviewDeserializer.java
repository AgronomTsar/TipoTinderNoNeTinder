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

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MemeReviewDeserializer extends StdDeserializer<MemeReview> {
    private final Dao<User,Integer> daoUser;
    private final Dao<Meme,Integer> daoMeme;
    public MemeReviewDeserializer(Dao<User,Integer> daoUser,Dao<Meme,Integer> daoMeme) {
        super(MemeReview.class);
        this.daoUser=daoUser;
        this.daoMeme=daoMeme;
    }
    @Override
    public MemeReview deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode root=parser.getCodec().readTree(parser);
        int user=root.get("user").asInt();
        int meme=root.get("meme").asInt();
        String date=root.get("date").asText();
        DateTimeFormatter f=DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate dateNew=LocalDate.parse(date,f);
        String rate=root.get("rate").asText();
        try {
            return new MemeReview(daoMeme.queryForId(meme),daoUser.queryForId(user),dateNew,rate);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
}
