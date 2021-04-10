package com.tretonchik.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.ormlite.dao.Dao;
import com.tretonchik.models.MemeReview;
import com.tretonchik.models.User;
import com.tretonchik.models.UserInteraction;
import com.tretonchik.service.Service;
import io.javalin.http.Context;

public class MemeReviewController extends AuthorizedController<MemeReview,Integer> {

    public MemeReviewController(Service<MemeReview, Integer> service, ObjectMapper objectMapper,Dao<User,Integer> userDao) {
        super(service, objectMapper, MemeReview.class,userDao);
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
