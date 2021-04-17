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
import java.time.LocalDate;

public class UserInteractionController extends AuthorizedController<UserInteraction,Integer> {
    private final Dao<User,Integer> userDao;
    private final UserInteractionService service;
    private final ObjectMapper objectMapper;
    private final Service<User, Integer> userService;
    String SIZE="size";
    String F_NAME="fname";
    String COOL_DOWN_SIGN="COOL DOWN";
    int TWENTY=20;
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
        LocalDate localDateNow=LocalDate.now();
        Integer size=ctx.pathParam(SIZE,Integer.class).get();
        if(size>TWENTY){
            size=TWENTY;
        }
        User user=userDao.queryBuilder().where().eq(F_NAME,login).queryForFirst();
        if(service.lastSessionTimeGetter(user.getId(),localDateNow)==null){
            ctx.result(objectMapper.writeValueAsString(service.MatchesFinder(user,size)));
        }
        else {
            LocalDate lastSession=service.lastSessionTimeGetter(user.getId(),localDateNow);
            if(localDateNow==lastSession){
                ctx.result(COOL_DOWN_SIGN);
            }
            else if(localDateNow.getYear()==lastSession.getYear()){
                if(localDateNow.getDayOfYear()-lastSession.getDayOfYear()>=COOL_DOWN){
                    ctx.result(objectMapper.writeValueAsString(service.MatchesFinder(user,size)));
                }
                else {
                    ctx.result(COOL_DOWN_SIGN);
                }
            }
            else {
                ctx.result(objectMapper.writeValueAsString(service.MatchesFinder(user,size)));
            }
        }

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
    private final int COOL_DOWN=1;
}
