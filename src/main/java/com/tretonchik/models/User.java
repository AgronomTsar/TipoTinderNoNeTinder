package com.tretonchik.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.LocalDate;
import java.util.Objects;

@DatabaseTable(tableName="User")
public class User{
    @DatabaseField(columnName = "id",id=true)
    private int id;
    @DatabaseField
    private String fname;
    @DatabaseField
    private String lname;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private LocalDate birthday;
    @DatabaseField
    private String role;
    @DatabaseField
    private String sex;
    @DatabaseField
    private String country;
    @DatabaseField
    private String city;
    @DatabaseField
    private String phone;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private LocalDate date;
    @DatabaseField
    private String password;
    public User(){
    }

    public User(int id, String fname, String lname, LocalDate birthday,
                String role, String sex, String country, String city, String phone, LocalDate date,String password) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.birthday = birthday;
        this.role = role;
        this.sex = sex;
        this.country = country;
        this.city = city;
        this.phone = phone;
        this.date = date;
        this.password=password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(fname, user.fname) &&
                Objects.equals(lname, user.lname) &&
                Objects.equals(birthday, user.birthday) &&
                Objects.equals(role, user.role) &&
                Objects.equals(sex, user.sex) &&
                Objects.equals(country, user.country) &&
                Objects.equals(city, user.city) &&
                Objects.equals(phone, user.phone) &&
                Objects.equals(date, user.date) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fname, lname, birthday, role, sex, country, city, phone, date, password);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
