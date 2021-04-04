package com.tretonchik.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.tretonchik.models.Meme;
import com.tretonchik.models.MemeReview;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class MemeReviewSerializer extends StdSerializer<MemeReview> {
    public MemeReviewSerializer() {
        super(MemeReview.class);
    }

    @Override
    public void serialize(MemeReview memeReview, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("user",memeReview.getUserId().getId());
        gen.writeNumberField("meme",memeReview.getMemeId().getId());
        DateTimeFormatter f=DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        gen.writeStringField("date",memeReview.getDate().format(f));
        gen.writeStringField("rate",memeReview.getRating().toString());
        gen.writeEndObject();
    }
}
