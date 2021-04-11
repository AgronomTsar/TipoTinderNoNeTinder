package com.tretonchik.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.ormlite.dao.Dao;
import com.tretonchik.models.Model;
import com.tretonchik.models.User;
import com.tretonchik.service.Service;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public abstract class AuthorizedController<T extends Model<U>, U> extends AbstractController<T, U> {
    private final Dao<User,Integer> userDao;
    public AuthorizedController(Service<T, U> service, ObjectMapper objectMapper, Class<T> modelClass,
                                Dao<User,Integer> userDao ) {
        super(service, objectMapper, modelClass);
        this.userDao=userDao;
    }
    abstract public Service<User,Integer> userService();
    public User actor(Context context) throws SQLException {
        if (context.basicAuthCredentialsExist()) {
            String login = context.basicAuthCredentials().getUsername();
            String password = context.basicAuthCredentials().getPassword();
            return checkBasicAuthCredentials(login, password);
        } else {
            throw new UnauthorizedResponse();
        }
    }
    public User checkBasicAuthCredentials(String login, String password) throws SQLException {
        System.out.println(login+" "+password);
        User user = userService().findByColumnUnique(LOGIN,login);
        System.out.println(user.getId());
        if (BCrypt.checkpw(password, user.getPassword())) {
            return user;
        } else {
            throw new UnauthorizedResponse();
        }
    }
    abstract boolean isAuthorized(User user, Context context);
    public boolean isAuthorized(Context context) throws SQLException {
        User actor = actor(context);
        return isAuthorized(actor, context);
    }
    @Override
    public void getOne(Context context, U id) throws SQLException {
        if (isAuthorized(context)) {
            super.getOne(context, id);
        } else {
            System.out.println("here getOne");
            throw new ForbiddenResponse();
        }
    }

    @Override
    public void getAll(Context context) throws SQLException {
        System.out.println("here");
        if (isAuthorized(context)) {
            super.getAll(context);
        } else {
            throw new ForbiddenResponse();
        }
    }

    @Override
    public void postOne(Context context) throws SQLException {
        if (isAuthorized(context)) {
            super.postOne(context);
        } else {
            throw new ForbiddenResponse();
        }
    }

    @Override
    public void patchOne(Context context, U id) throws SQLException {
        if (isAuthorized(context)) {
            super.patchOne(context, id);
        } else {
            throw new ForbiddenResponse();
        }
    }

    @Override
    public void deleteOne(Context context, U id) throws SQLException {
        if (isAuthorized(context)) {
            super.deleteOne(context, id);
        } else {
            throw new ForbiddenResponse();
        }
    }

    String LOGIN = "fname";
}

