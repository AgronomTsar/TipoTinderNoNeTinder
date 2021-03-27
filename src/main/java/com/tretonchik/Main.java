package com.tretonchik;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.tretonchik.configuration.DatabaseConfiguration;
import com.tretonchik.configuration.JdbcDatabaseConfiguration;
import com.tretonchik.models.Meme;
import com.tretonchik.models.User;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        DatabaseConfiguration configuration = new JdbcDatabaseConfiguration("jdbc:sqlite:C:\\Users\\77012\\Desktop\\бд\\dolbaniyeZnakomstva.db");
        Dao<Meme,Integer> memeDao= DaoManager.createDao(configuration.connectionSource(), Meme.class);

    }
}
