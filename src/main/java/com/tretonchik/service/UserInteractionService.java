package com.tretonchik.service;
import com.j256.ormlite.dao.Dao;
import com.tretonchik.models.Meme;
import com.tretonchik.models.MemeReview;
import com.tretonchik.models.User;
import com.tretonchik.models.UserInteraction;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class UserInteractionService extends AbstractService<UserInteraction,Integer> {
    private final Dao<MemeReview, Integer> memeReviewDao;
    private final Dao<User, Integer> userDao;
    private final Dao<UserInteraction, Integer> userInteractionsDao;
    public UserInteractionService(Dao<UserInteraction, Integer> dao,Dao<MemeReview, Integer> memeReviewDao,Dao<User, Integer> userDao) {
        super(dao);
        this.memeReviewDao=memeReviewDao;
        this.userDao=userDao;
        this.userInteractionsDao=dao;
    }
    public List<User> MatchesFinder(User user,int size) throws SQLException {
        int userId=user.getId();
        List<Meme> memesId=memesSourceUserFinder(user);//all user's liked memes
        List<User> targets=sameCityUserFinder(user);
        List<User> finalTargets=userInteractionMatchesFinder(user,targets);
        if(finalTargets.size()<size&&targets.size()>size){
            List<User> newList=MatchesByMemesFinder(user,memesId,size,targets,finalTargets.size());
            if(newList.size()!=0){
                for(int i=0;i<size-finalTargets.size();i++){
                    finalTargets.add(newList.get(i));
                }
            }
            else {
                watchedStatusPutter(finalTargets,user);
                return finalTargets;
            }
        }
        else if(targets.size()<size){
            for(int i=0;i<targets.size();i++){
                finalTargets.add(targets.get(i));
            }
            watchedStatusPutter(finalTargets,user);
            return finalTargets;
        }
        else {
            watchedStatusPutter(finalTargets,user);
            return finalTargets;
        }
        watchedStatusPutter(finalTargets,user);
        return finalTargets;
    }
    public void watchedStatusPutter(List<User> targets,User source) throws SQLException {
        for(int i=0;i<targets.size();i++){
            userInteractionsDao.create(userInteractionReturner(targets.get(i),source,userInteractionsDao.queryForAll().size()+1));
        }
    }
    public UserInteraction userInteractionReturner(User target,User source,int id){
        return new UserInteraction(source,target,Reactions.WATCHED.toString(), LocalDate.now(),id);
    }
    public List<User> sameCityUserFinder(User user) throws SQLException {
        if(user.getSex().equals("Male")){
            return (List<User>) userDao.queryBuilder().where().eq("city",user.getCity()).and().eq("sex","Female").query();
        }
        else{
            return (List<User>) userDao.queryBuilder().where().eq("city",user.getCity()).and().eq("sex","Male").query();
        }
    }
   public List<Meme> memesSourceUserFinder(User user) throws SQLException {
        List<MemeReview> memeReviews=memeReviewDao.queryForEq("userId",user.getId());
        List<Meme> meme=new ArrayList<>();
        for(int i=0;i<memeReviews.size();i++){
            if(memeReviews.get(i).getRating().equals(Reactions.LIKED)){
                meme.add(memeReviews.get(i).getMemeId());
            } }
        return meme;
    }
    public List<User> MatchesByMemesFinder(User user,List<Meme> memes,int size,List<User> targets,int currentSize) throws SQLException {
        int matchCounter=0;
        int counter5=0;
        List<User> tempTargets=new ArrayList<>();
        List<Integer> memesLikedAmount=new ArrayList<>();
        int differ=0;
        for(int i=0;i<targets.size();i++){
            for(int j=0;j<memes.size();j++){
                MemeReview memeReview=memeReviewDao.queryBuilder().where().eq("memeId",memes.get(j)).eq("userId",targets.get(i)).queryForFirst();
                if(memeReview!=null&&memeReview.getRating().equals(Reactions.LIKED)){
                    if(matchCounter==0){
                        tempTargets.add(i,targets.get(i));
                    }
                    matchCounter++;
                }
            }
            memesLikedAmount.add(i,matchCounter);
            matchCounter=0;
        }
        return tempTargets;
    }
//    public List<User> appropriateUserSelector(List<User> usersList,List<Integer> indexList){
//        List<User> finalTargets=new ArrayList<>();
//        for(int i=0;i<indexList.size();i++){
//            finalTargets.add(usersList.get(indexList.get(i)));
//        }
//        return finalTargets;
//    }
//    public List<Integer> appropriateMatchesSelector(List<User> countList,int needSize){
//        int triesCounter=0;
//        int max=0;
//        int currentIndex=0;
//        List<Integer> selectedMemes=new ArrayList<>();
//        while(triesCounter<needSize){
//
//            selectedMemes.add(currentIndex,max);
//            triesCounter++;
//            countList.remove(currentIndex);
//        }
//        return selectedMemes;
//    }
    public List<User> userInteractionMatchesFinder(User source,List<User> targets) throws SQLException {
        List<User> likedTargets=new ArrayList<>();
        for(int i=0;i<targets.size();i++){
        UserInteraction userInteraction=userInteractionsDao.queryBuilder().where().eq("source",targets.get(i)).and().eq("target",source).queryForFirst();
            if(userInteraction!=null&& userInteraction.getReaction().equals(Reactions.LIKED.toString())){
                likedTargets.add(targets.get(i));
            }
        }
        return likedTargets;
    }
    public LocalDate lastSessionTimeGetter(int userId,LocalDate localDateNow) throws SQLException {
        List<UserInteraction> userInteractions=userInteractionsDao.queryForEq("source",userId);
        int max=ZERO;
        for(int i=ZERO;i<userInteractions.size();i++){
            UserInteraction userInteractionCurrent=userInteractions.get(i);
            LocalDate localDateCurrent=userInteractionCurrent.getDate();
            if(localDateCurrent==localDateNow){
                return localDateCurrent;
            }
            else if(i==ZERO){
                max=i;
            }
            else if(localDateCurrent.getYear()==userInteractions.get(max).getDate().getYear()){
                if(localDateCurrent.getDayOfYear()>=userInteractions.get(max).getDate().getDayOfYear()){
                    max=i;
                }
            }
            else if(localDateCurrent.getYear()>userInteractions.get(max).getDate().getYear()){
                max=i;
            }
        }
        if(userInteractions.size()==0){
            return null;
        }
        else {
            return userInteractions.get(max).getDate();
        }
    }
    private  final int ZERO=0;
}
