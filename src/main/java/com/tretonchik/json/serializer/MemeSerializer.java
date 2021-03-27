package com.tretonchik.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.tretonchik.models.Meme;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class MemeSerializer extends StdSerializer<Meme> {
    public MemeSerializer() {
        super(Meme.class);
    }


    @Override
    public void serialize(Meme meme, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id",meme.getId());
        gen.writeStringField("link",meme.getLink());
        DateTimeFormatter f=DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        gen.writeStringField("date",meme.getDate().format(f));
        gen.writeEndObject();
    }
}
