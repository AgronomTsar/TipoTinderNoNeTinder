package com.tretonchik.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.tretonchik.models.MemeReview;
import com.tretonchik.models.UserInteraction;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class UserInteractionSerializer extends StdSerializer<UserInteraction> {
    public UserInteractionSerializer() {
        super(UserInteraction.class);
    }
    @Override
    public void serialize(UserInteraction userInteraction, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("source",userInteraction.getSource().getId());
        gen.writeNumberField("target",userInteraction.getTarget().getId());
        gen.writeStringField("reaction",userInteraction.getReaction());
        DateTimeFormatter f=DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        gen.writeStringField("date",userInteraction.getDate().format(f));
        gen.writeNumberField("id",userInteraction.getId());
        gen.writeEndObject();
    }
}
