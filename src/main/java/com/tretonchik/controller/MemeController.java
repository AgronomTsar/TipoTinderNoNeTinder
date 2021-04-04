package com.tretonchik.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tretonchik.models.Meme;
import com.tretonchik.models.User;
import com.tretonchik.service.Service;
import io.javalin.http.Context;

public class MemeController extends AuthorizedController<Meme, Integer>{

    public MemeController(Service<Meme, Integer> service, ObjectMapper objectMapper) {
        super(service, objectMapper, Meme.class);
    }

    @Override
    public Service<User, Integer> userService() {
        return null;
    }

    @Override
    boolean isAuthorized(User user, Context context) {
        return false;
    }
}
