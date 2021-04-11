package com.tretonchik.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.ormlite.dao.Dao;
import com.tretonchik.models.Meme;
import com.tretonchik.models.User;
import com.tretonchik.models.UserRole;
import com.tretonchik.service.Service;
import com.tretonchik.service.UserService;
import io.javalin.http.Context;
import org.omg.CORBA.portable.ApplicationException;

public class MemeController extends AuthorizedController<Meme, Integer>{

    private final Service<User, Integer> userService;


    public MemeController(Service<Meme, Integer> service, ObjectMapper objectMapper, Dao<User,Integer> userDao,
                          Service<User, Integer> userService ) {
        super(service, objectMapper, Meme.class,userDao);
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
                System.out.println("dont work");
                throw new RuntimeException();
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
    private boolean isAuthorizedGet(User user, Context context) {
        Integer id = context.pathParam("id", Integer.class).getOrNull();
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
