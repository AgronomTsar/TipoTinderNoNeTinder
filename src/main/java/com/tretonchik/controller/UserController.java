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
import java.time.LocalDate;
public class UserController extends AuthorizedController<User,Integer>{

    private final Dao<Meme,Integer> memeDao;
    private final Dao<User,Integer> userDao;
    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final ReactionOperations reactionOperations;
    public UserController(Service<User, Integer> service, ObjectMapper objectMapper,
                          Dao<Meme,Integer> memeDao,Dao<User,Integer> userDao,ReactionOperations reactionOperations) {
        super(service,objectMapper, User.class,userDao);
        this.memeDao=memeDao;
        this.userDao=userDao;
        this.userService= (UserService) service;
        this.objectMapper=objectMapper;
        this.reactionOperations=reactionOperations;
    }

    @Override
    public Service<User, Integer> userService() {
        return getService();
    }
    @Override
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
        User user=userDao.queryBuilder().where().eq(F_NAME,login).queryForFirst();
        Integer size=ctx.pathParam(SIZE,Integer.class).get();
        LocalDate localDateNow=LocalDate.now();
        if(userService.lastSessionTimeGetter(user.getId(),localDateNow)==null){
            ctx.result(objectMapper.writeValueAsString(userService.UserMemeGetter(size,user.getId())));
        }
        else {
            LocalDate lastSession=userService.lastSessionTimeGetter(user.getId(),localDateNow);
            if(localDateNow==lastSession){
                ctx.result(COOL_DOWN_SIGN);
            }
            else if(localDateNow.getYear()==lastSession.getYear()){
                if(localDateNow.getDayOfYear()-lastSession.getDayOfYear()>=COOL_DOWN){
                    ctx.result(objectMapper.writeValueAsString(userService.UserMemeGetter(size,user.getId())));
                }
                else {
                    ctx.result(COOL_DOWN_SIGN);
                }
            }
            else {
                ctx.result(objectMapper.writeValueAsString(userService.UserMemeGetter(size,user.getId())));
            }
        }

    }// function for getting memes depending on chosen size
    public void MemeReactionSetter(Context ctx) throws SQLException, JsonProcessingException {
        String login=ctx.basicAuthCredentials().getUsername();
        String password=ctx.basicAuthCredentials().getPassword();
        User user=userDao.queryBuilder().where().eq(F_NAME,login).queryForFirst();
        String rate=ctx.pathParam(REACTION,String.class).get();
        Reactions reaction=reactionOperations.reactionConverter(rate);
        Integer memeId=ctx.pathParam(MEME_ID,Integer.class).get();
        ctx.result(objectMapper.writeValueAsString(userService.MemeReviewer(reaction.toString(),memeId,user)));
    }
    private final int COOL_DOWN=1;
    String F_NAME="fname";
    String REACTION="reaction";
    String MEME_ID="memeId";
    String COOL_DOWN_SIGN="COOL DOWN";
    String SIZE="size";
}
