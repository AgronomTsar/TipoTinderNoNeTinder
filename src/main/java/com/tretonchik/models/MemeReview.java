package com.tretonchik.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.tretonchik.service.Reactions;

import java.time.LocalDate;
import java.util.Objects;

@DatabaseTable(tableName="MemeReview")
public class MemeReview implements Model<Integer> {
    @DatabaseField(columnName = "id",id=true)
    int id;
    @DatabaseField(foreign=true, foreignAutoRefresh = true,columnName = "memeId")
    Meme memeId;
    @DatabaseField(foreign=true, foreignAutoRefresh = true,columnName = "userId")
    User userId;
    @DatabaseField(dataType = DataType.SERIALIZABLE,columnName = "date")
    private LocalDate date;
    @DatabaseField(columnName = "rating")
    private String rating;
    public MemeReview(){
    }
    public MemeReview(int id,Meme memeId, User userId, LocalDate date, String rating) {
        this.id=id;
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

    @Override
    public void setId(Integer id) {
        this.id=id;
    }

    @Override
    public Integer getId() {
        return this.id;
    }
}
