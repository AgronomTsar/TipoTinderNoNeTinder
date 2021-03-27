package com.tretonchik.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.tretonchik.models.Meme;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
public class MemeDeserializer extends StdDeserializer<Meme> {
    public MemeDeserializer() {
        super(Meme.class);
    }
    @Override
    public Meme deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode root=parser.getCodec().readTree(parser);
        int id=root.get("id").asInt();
        String link=root.get("link").asText();
        String date=root.get("date").asText();
        DateTimeFormatter f=DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate dateNew=LocalDate.parse(date,f);
        return new Meme(id,link,dateNew);
    }
}
