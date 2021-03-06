package com.tretonchik.service;
import com.j256.ormlite.dao.Dao;
import com.tretonchik.models.Meme;
import com.tretonchik.models.MemeReview;
import com.tretonchik.models.User;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class UserService extends AbstractService<User,Integer> {
    private final Dao<Meme, Integer> memeDao;
    private final Dao<MemeReview, Integer> memeReviewDao;
    private final Dao<User, Integer> userDao;
    private final int COOL_DOWN=1;
    public UserService(Dao<User, Integer> dao, Dao<Meme, Integer> memeDao,
                       Dao<MemeReview, Integer> memeReviewDao) {
        super(dao);
        this.memeDao = memeDao;
        this.memeReviewDao = memeReviewDao;
        this.userDao = dao;
    }

    public List<Meme> UserMemeGetter(int size, int userId) throws SQLException {
        ArrayList<Meme> memeList = new ArrayList<>();
        int counter = 0;
        int memeReviewPrimaryKey = memeReviewDao.queryForAll().size() + 1;
        if (memeReviewDao.queryForAll().size() != 0) {
            int memeIndex = 0;
            boolean found=false;
            while (counter < size) {
                memeIndex = 1 + (int) (Math.random() * memeDao.queryForAll().size());
                for (int j = 1; j <= size; j++) {
                    if (memeReviewDao.queryForId(j).getUserId().getId() == userId &&
                            memeReviewDao.queryForId(j).getMemeId().getId() == memeIndex) {
                        found=true;
                    }
                    else {
                        found=false;
                    }
                }
                if(found==false){
                    memeReviewDao.create(MemeReviewReturner(memeDao.queryForId(memeIndex),userDao.queryForId(userId),memeReviewPrimaryKey));
                    memeList.add(memeDao.queryForId(memeIndex));
                    counter++;
                }
                memeReviewPrimaryKey=memeReviewDao.queryForAll().size()+1;
            }
        }
        else{
                for (int i = 1; i <= size; i++) {
                    memeReviewDao.create(MemeReviewReturner(memeDao.queryForId(i), userDao.queryForId(userId), memeReviewPrimaryKey));
                    counter++;
                    memeList.add(memeDao.queryForId(i));
                    if (counter == size) {
                        return memeList;
                    }
                    memeReviewPrimaryKey = memeReviewDao.queryForAll().size() + 1;
                }
            }
            return memeList;
        }
        public MemeReview MemeReviewReturner (Meme memeId, User userId,int id){
//
//            String date = "03-04-2021";
//            DateTimeFormatter f = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//            LocalDate dateNew = LocalDate.parse(date,f);
            LocalDate localDate=LocalDate.now();
            return new MemeReview(id, memeId, userId, localDate, Reactions.WATCHED.toString());
        }
        public MemeReview MemeReviewFinder(int memeId,int userId) throws SQLException {
            for(int i=1;i<=memeReviewDao.queryForAll().size();i++){
                if(memeReviewDao.queryForId(i).getUserId().getId()==userId&&memeReviewDao.queryForId(i).getMemeId().getId()==memeId){
                    return memeReviewDao.queryForId(i);
                }
            }
            return null;
        }
        public MemeReview MemeReviewer(String reactions,int memeId,User user) throws SQLException {
                MemeReview memeReview=MemeReviewFinder(memeId,user.getId());
                memeReview.setRating(reactions);
                memeReviewDao.update(memeReview);
                return memeReview;
        }
        public LocalDate lastSessionTimeGetter(int userId,LocalDate localDateNow) throws SQLException {
            List<MemeReview> memeReviews=memeReviewDao.queryForEq("userId",userId);
            int max=ZERO;
            for(int i=ZERO;i<memeReviews.size();i++){
                MemeReview memeReviewCurrent=memeReviews.get(i);
                LocalDate localDateCurrent=memeReviewCurrent.getDate();
                if(localDateCurrent==localDateNow){
                    return localDateCurrent;
                }
                else if(i==ZERO){
                    max=i;
                }
                else if(localDateCurrent.getYear()==memeReviews.get(max).getDate().getYear()){
                    if(localDateCurrent.getDayOfYear()>=memeReviews.get(max).getDate().getDayOfYear()){
                        max=i;
                    }
                }
                else if(localDateCurrent.getYear()>memeReviews.get(max).getDate().getYear()){
                    max=i;
                }
            }
            if(memeReviews.size()==0){
                return null;
            }
            else {
                return memeReviews.get(max).getDate();
            }
        }
        private  final int ZERO=0;

}