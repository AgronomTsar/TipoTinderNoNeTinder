package com.tretonchik.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.ormlite.dao.Dao;
import com.tretonchik.models.User;
import com.tretonchik.models.UserInteraction;
import com.tretonchik.models.UserRole;
import com.tretonchik.service.Service;
import com.tretonchik.service.UserInteractionService;
import io.javalin.http.Context;

import java.sql.SQLException;

public class UserInteractionController extends AuthorizedController<UserInteraction,Integer> {
    private final Dao<User,Integer> userDao;
    private final UserInteractionService service;
    private final ObjectMapper objectMapper;
    private final Service<User, Integer> userService;
    public UserInteractionController(Service<UserInteraction, Integer> service, ObjectMapper objectMapper,
                                     Dao<User,Integer> userDao,Service<User, Integer> userService) {
        super(service, objectMapper, UserInteraction.class,userDao);
        this.userDao=userDao;
        this.service= (UserInteractionService) service;
        this.objectMapper=objectMapper;
        this.userService=userService;
    }
    public void MatchesFinder(Context ctx) throws SQLException, JsonProcessingException {
        String login=ctx.basicAuthCredentials().getUsername();
        String password=ctx.basicAuthCredentials().getPassword();
        User user=userDao.queryBuilder().where().eq("fname",login).queryForFirst();
        Integer size=ctx.pathParam("size",Integer.class).get();
        ctx.result(objectMapper.writeValueAsString(service.MatchesFinder(user,size)));
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
