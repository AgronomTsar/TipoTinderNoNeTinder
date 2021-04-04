package com.tretonchik.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tretonchik.models.Model;
import com.tretonchik.models.User;
import com.tretonchik.service.Service;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public abstract class AuthorizedController<T extends Model<U>, U> extends AbstractController<T, U> {
    public AuthorizedController(Service<T, U> service, ObjectMapper objectMapper, Class<T> modelClass) {
        super(service, objectMapper, modelClass);
    }
    abstract public Service<User, Integer> userService();
    public User actor(Context context) {
        if (context.basicAuthCredentialsExist()) {
            String login = context.basicAuthCredentials().getUsername();
            String password = context.basicAuthCredentials().getPassword();
            return checkBasicAuthCredentials(login, password);
        } else {
            throw new UnauthorizedResponse();
        }
    }
    public User checkBasicAuthCredentials(String login, String password) {
        User user = userService().findByColumnUnique(LOGIN, login);
        if (BCrypt.checkpw(password, user.getPassword())) {
            return user;
        } else {
            throw new UnauthorizedResponse();
        }
    }
    abstract boolean isAuthorized(User user, Context context);
    public boolean isAuthorized(Context context) {
        User actor = actor(context);
        return isAuthorized(actor, context);
    }
    @Override
    public void getOne(Context context, U id) {
        if (isAuthorized(context)) {
            super.getOne(context, id);
        } else {
            throw new ForbiddenResponse();
        }
    }

    @Override
    public void getAll(Context context) throws SQLException {
        if (isAuthorized(context)) {
            super.getAll(context);
        } else {
            throw new ForbiddenResponse();
        }
    }

    @Override
    public void postOne(Context context) {
        if (isAuthorized(context)) {
            super.postOne(context);
        } else {
            throw new ForbiddenResponse();
        }
    }

    @Override
    public void patchOne(Context context, U id) {
        if (isAuthorized(context)) {
            super.patchOne(context, id);
        } else {
            throw new ForbiddenResponse();
        }
    }

    @Override
    public void deleteOne(Context context, U id) {
        if (isAuthorized(context)) {
            super.deleteOne(context, id);
        } else {
            throw new ForbiddenResponse();
        }
    }

    String LOGIN = "fname";
}

