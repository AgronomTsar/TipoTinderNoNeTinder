package com.tretonchik.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.ormlite.dao.Dao;
import com.tretonchik.models.MemeReview;
import com.tretonchik.models.User;
import com.tretonchik.models.UserInteraction;
import com.tretonchik.models.UserRole;
import com.tretonchik.service.Service;
import io.javalin.http.Context;

public class MemeReviewController extends AuthorizedController<MemeReview,Integer> {
    private final Service<User, Integer> userService;
    public MemeReviewController(Service<MemeReview, Integer> service, ObjectMapper objectMapper,Dao<User,Integer> userDao,
                                Service<User, Integer> userService) {
        super(service, objectMapper, MemeReview.class,userDao);
        this.userService=userService;
    }

    @Override
    public Service<User, Integer> userService() {
        return userService;
    }

    @Override
    boolean isAuthorized(User user, Context context) {
        if (user.getRole() == UserRole.ADMIN) {
            return true;
        }
        switch (context.method()) {
            case "GET":
                return isAuthorizedGet(user, context);
            case "POST":
                return true;
            case "PATCH":
                return isAuthorizedPatch(user, context);
            case "DELETE":
                return isAuthorizedDelete(user, context);
            default:
                throw new RuntimeException();
        }
    }
    private boolean isAuthorizedGet(User user, Context context) {
        Integer id = context.pathParam("id", Integer.class).getOrNull();
        if (id == null) {
            return false;
        } else {
            return id.equals(user.getId());
        }
    }
    private boolean isAuthorizedPatch(User user, Context context) {
        Integer id = context.pathParam("id", Integer.class).get();
        if (id == null) {
            return false;
        } else {
            return id.equals(user.getId());
        }
    }
    private boolean isAuthorizedDelete(User user, Context context) {
        Integer id = context.pathParam("id", Integer.class).get();
        if (id == null) {
            return false;
        } else {
            return id.equals(user.getId());
        }
    }
}
