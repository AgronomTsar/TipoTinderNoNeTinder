package com.tretonchik.service;
import com.j256.ormlite.dao.Dao;
import com.tretonchik.models.Meme;
import com.tretonchik.models.MemeReview;
import com.tretonchik.models.User;
import com.tretonchik.models.UserInteraction;

import java.sql.SQLException;
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
                return finalTargets;
            }
        }
        else if(targets.size()<size){
            for(int i=0;i<targets.size();i++){
                finalTargets.add(targets.get(i));
            }
            return finalTargets;
        }
        else {
            return finalTargets;
        }
        return finalTargets;
    }
//    public Boolean CityChecker(User source, User target){
//        if(source.getCity().equals(target.getCity())){
//            return true;
//        }
//        else {
//            return false;
//        }
//    }
//    public Boolean ReactionChecker(User source, User target, Meme meme) throws SQLException {
//        MemeReview sourceMemeReview=memeReviewDao.queryBuilder().where().eq("memeId",meme.getId()).eq("userId",source.getId()).queryForFirst();
//        MemeReview targetMemeReview=memeReviewDao.queryBuilder().where().eq("memeId",meme.getId()).eq("userId",target.getId()).queryForFirst();
//        if(targetMemeReview!=null){
//            if(sourceMemeReview.getRating().equals(Reactions.LIKED.toString())&&targetMemeReview.getRating().equals(Reactions.LIKED.toString())){
//                return true;
//            }
//            else {
//                return  false;
//            }
//        }
//        else {
//            return false;
//        }
//    }
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
    public List<User> appropriateUserSelector(List<User> usersList,List<Integer> indexList){
        List<User> finalTargets=new ArrayList<>();
        for(int i=0;i<indexList.size();i++){
            finalTargets.add(usersList.get(indexList.get(i)));
        }
        return finalTargets;
    }
    public List<Integer> appropriateMatchesSelector(List<User> countList,int needSize){
        int triesCounter=0;
        int max=0;
        int currentIndex=0;
        List<Integer> selectedMemes=new ArrayList<>();
        while(triesCounter<needSize){
            
            selectedMemes.add(currentIndex,max);
            triesCounter++;
            countList.remove(currentIndex);
        }
        return selectedMemes;
    }
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
}
