package com.tretonchik.Service;

import com.j256.ormlite.dao.Dao;
import com.tretonchik.models.Meme;

public class MemeService extends AbstractService<Meme,Integer> {
    public MemeService(Dao<Meme, Integer> dao) {
        super(dao);
    }
}
