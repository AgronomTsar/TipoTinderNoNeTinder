package com.tretonchik.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.tretonchik.models.User;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class UserSerializer extends StdSerializer<User> {
    public UserSerializer() {
        super(User.class);
    }

    @Override
    public void serialize(User user, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id",user.getId());
        gen.writeStringField("fname",user.getFname());
        gen.writeStringField("lname",user.getLname());
        DateTimeFormatter f=DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        gen.writeStringField("birthday",user.getBirthday().format(f));
        gen.writeStringField("role",user.getRole());
        gen.writeStringField("sex",user.getSex());
        gen.writeStringField("country",user.getCountry());
        gen.writeStringField("city",user.getCity());
        gen.writeStringField("phone",user.getPhone());
        gen.writeStringField("date",user.getBirthday().format(f));
        gen.writeEndObject();
    }
}
