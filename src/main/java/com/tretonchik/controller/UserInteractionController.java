package com.tretonchik.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.ormlite.dao.Dao;
import com.tretonchik.models.User;
import com.tretonchik.models.UserInteraction;
import com.tretonchik.service.Service;
import com.tretonchik.service.UserInteractionService;
import io.javalin.http.Context;

import java.sql.SQLException;

public class UserInteractionController extends AuthorizedController<UserInteraction,Integer> {
    private final Dao<User,Integer> userDao;
    private final UserInteractionService service;
    private final ObjectMapper objectMapper;
    public UserInteractionController(Service<UserInteraction, Integer> service, ObjectMapper objectMapper,
                                     Dao<User,Integer> userDao) {
        super(service, objectMapper, UserInteraction.class,userDao);
        this.userDao=userDao;
        this.service= (UserInteractionService) service;
        this.objectMapper=objectMapper;
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
        return null;
    }

    @Override
    boolean isAuthorized(User user, Context context) {
        return false;
    }
}
