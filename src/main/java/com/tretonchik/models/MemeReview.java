package com.tretonchik.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.LocalDate;
import java.util.Objects;

@DatabaseTable(tableName="MemeReview")
public class MemeReview {
    @DatabaseField(foreign=true, foreignAutoRefresh = true)
    Meme memeId;
    @DatabaseField(foreign=true, foreignAutoRefresh = true)
    User userId;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private LocalDate date;
    @DatabaseField
    private String rating;
    public MemeReview(){

    }
    public MemeReview(Meme memeId, User userId, LocalDate date, String rating) {
        this.memeId = memeId;
        this.userId = userId;
        this.date = date;
        this.rating = rating;
    }

    public Meme getMemeId() {
        return memeId;
    }

    public void setMemeId(Meme memeId) {
        this.memeId = memeId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemeReview that = (MemeReview) o;
        return Objects.equals(memeId, that.memeId) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(date, that.date) &&
                Objects.equals(rating, that.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memeId, userId, date, rating);
    }
}
