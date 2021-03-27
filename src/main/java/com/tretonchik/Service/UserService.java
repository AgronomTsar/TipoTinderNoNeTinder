package com.tretonchik.Service;

import com.j256.ormlite.dao.Dao;
import com.tretonchik.models.User;

public class UserService extends AbstractService<User,Integer> {
    public UserService(Dao<User, Integer> dao) {
        super(dao);
    }
}
