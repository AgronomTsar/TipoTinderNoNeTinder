package com.tretonchik.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.LocalDate;
import java.util.Objects;

@DatabaseTable(tableName="Meme")
public class Meme {
    @DatabaseField(columnName = "id",id=true)
    private int id;
    @DatabaseField
    private String link;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private LocalDate date;
    public Meme(){
    }
    public Meme(int id, String link, LocalDate date) {
        this.id = id;
        this.link = link;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meme meme = (Meme) o;
        return id == meme.id &&
                Objects.equals(link, meme.link) &&
                Objects.equals(date, meme.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, link, date);
    }
}
