package com.tretonchik.service;

import com.j256.ormlite.dao.Dao;
import com.tretonchik.models.User;
import com.tretonchik.models.UserInteraction;

import java.util.List;

public class UserInteractionService extends AbstractService<UserInteraction,Integer> {
    public UserInteractionService(Dao<UserInteraction, Integer> dao) {
        super(dao);
    }
}
