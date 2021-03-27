package com.tretonchik.Service;

import com.j256.ormlite.dao.Dao;
import com.tretonchik.models.UserInteraction;

public class UserInteractionService extends AbstractService<UserInteraction,Integer> {
    public UserInteractionService(Dao<UserInteraction, Integer> dao) {
        super(dao);
    }
}
