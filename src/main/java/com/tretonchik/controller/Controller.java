package com.tretonchik.controller;

import com.tretonchik.models.Model;
import io.javalin.http.Context;

import java.sql.SQLException;

public interface Controller<T extends Model<U>, U>  {
    void getOne(Context context, U id) throws SQLException;
    void getAll(Context context) throws SQLException;
    void postOne(Context context) throws SQLException;
    void patchOne(Context context, U id) throws SQLException;
    void deleteOne(Context context, U id) throws SQLException;
}
