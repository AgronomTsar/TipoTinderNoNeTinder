package com.tretonchik.service;

import com.j256.ormlite.dao.Dao;
import com.tretonchik.models.MemeReview;

public class MemeReviewService extends AbstractService<MemeReview,Integer>{
    public MemeReviewService(Dao<MemeReview, Integer> dao) {
        super(dao);
    }
}
