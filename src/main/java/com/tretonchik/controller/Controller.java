package com.tretonchik.controller;

import com.tretonchik.models.Model;
import io.javalin.http.Context;

import java.sql.SQLException;

public interface Controller<T extends Model<U>, U>  {
    void getOne(Context context, U id);
    void getAll(Context context) throws SQLException;
    void postOne(Context context);
    void patchOne(Context context, U id);
    void deleteOne(Context context, U id);
}
