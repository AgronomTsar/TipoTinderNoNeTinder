package com.tretonchik.service;

public class ReactionOperations {
    public Reactions reactionConverter(String reaction){
        if(reaction.equals("LIKED")||reaction.equals("liked")){
            return Reactions.LIKED;
        }
        if(reaction.equals("DISLIKED")||reaction.equals("disliked")){
            return Reactions.DISLIKED;
        }
        return null;
    }
}
