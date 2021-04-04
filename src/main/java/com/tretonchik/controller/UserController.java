package com.tretonchik.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.ormlite.dao.Dao;
import com.tretonchik.models.Meme;
import com.tretonchik.models.User;
import com.tretonchik.models.UserRole;
import com.tretonchik.service.ReactionOperations;
import com.tretonchik.service.Reactions;
import com.tretonchik.service.Service;
import com.tretonchik.service.UserService;
import io.javalin.http.Context;
import org.omg.CORBA.portable.ApplicationException;

import java.sql.SQLException;

public class UserController extends AbstractController<User,Integer>{
    private final Dao<Meme,Integer> memeDao;
    private final Dao<User,Integer> userDao;
    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final ReactionOperations reactionOperations;
    public UserController(Service<User, Integer> service, ObjectMapper objectMapper,
                          Dao<Meme,Integer> memeDao,Dao<User,Integer> userDao,ReactionOperations reactionOperations) {
        super(service, objectMapper, User.class);
        this.memeDao=memeDao;
        this.userDao=userDao;
        this.userService= (UserService) service;
        this.objectMapper=objectMapper;
        this.reactionOperations=reactionOperations;
    }
    public Service<User, Integer> userService() {
        return getService();
    }
    boolean isAuthorized(User user, Context context) {
        if (user.getRole() ==UserRole.ADMIN) {
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
    public void UserMemeGetter(Context ctx) throws SQLException, JsonProcessingException {
        String login=ctx.basicAuthCredentials().getUsername();
        String password=ctx.basicAuthCredentials().getPassword();
        User user=userDao.queryBuilder().where().eq("fname",login).queryForFirst();
        Integer size=ctx.pathParam("size",Integer.class).get();

        ctx.result(objectMapper.writeValueAsString(userService.UserMemeGetter(size,user.getId())));
    }// function for getting memes depending on chosen size
    public void MemeReactionSetter(Context ctx) throws SQLException, JsonProcessingException {
        String login=ctx.basicAuthCredentials().getUsername();
        String password=ctx.basicAuthCredentials().getPassword();
        User user=userDao.queryBuilder().where().eq("fname",login).queryForFirst();
        String rate=ctx.pathParam("reaction",String.class).get();
        Reactions reaction=reactionOperations.reactionConverter(rate);
        Integer memeId=ctx.pathParam("memeId",Integer.class).get();
        ctx.result(objectMapper.writeValueAsString(userService.MemeReviewer(reaction.toString(),memeId,user)));
    }


}
