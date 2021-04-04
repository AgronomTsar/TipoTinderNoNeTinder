package com.tretonchik;

import com.j256.ormlite.dao.Dao;
import com.tretonchik.models.Meme;
import com.tretonchik.models.User;
import com.tretonchik.models.UserRole;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class QuickShit {
//    int id=0;
//    String Mfname="Malefname";
//    String Ffname="Femalefname";
//    String Mlname="Malelname";
//    String Flname="Femalefname";
//    String fname;
//    String lname;
//    String sex;
//    String country="Kazakhstan";
//    String birthday="10-11-1999";
//    String date="11-11-1999";
//    DateTimeFormatter f=DateTimeFormatter.ofPattern("dd-MM-yyyy");
//    String phone="+77012528426";
//    String role="user";
//    String password="Guss";
//    LocalDate birthdayNew=LocalDate.parse(birthday,f);
//    LocalDate dateNew=LocalDate.parse(date,f);
//    ArrayList<String> cityArray=new ArrayList<>();
//    private final Dao<User,Integer> daoUser;
//    public QuickShit(Dao<User, Integer> daoUser) {
//        this.daoUser = daoUser;
//    }
//    public void cityShitter(){
//        cityArray.add("Almaty");
//        cityArray.add("Shimkent");
//        cityArray.add("Nur-sultan");
//        cityArray.add("Pavlodar");
//        cityArray.add("Kostanai");
//    }
//    public void UserShitter() throws SQLException {
//        User user=new User();
//        cityShitter();
//        for(int i=1;i<=100;i++){
//            id=i;
//            int number=i;
//            String numberString=String.valueOf(number);
//            if(i%2==0){
//                fname=Mfname+numberString;
//                lname=Mlname+numberString;
//                sex="Male";
//            }
//            else {
//                fname=Ffname+numberString;
//                lname=Flname+numberString;
//                sex="Female";
//            }
//            String passwordString=password+numberString;
//            String hashed = BCrypt.hashpw(passwordString, BCrypt.gensalt());
//            int index= (int) (Math.random()*5);
//            String city=cityArray.get(index);
//            daoUser.create(userReturner(id,fname,lname,sex,city,hashed));
//            System.out.println(id+" /"+passwordString);
//
//        }
//
//    }
//    public User userReturner(int id,String fname,String lname,String sex,String city,String password){
//        return new User(id,fname,lname,birthdayNew, UserRole.USER,sex,country,city,phone,dateNew,password);
//    }
//    private final Dao<Meme,Integer> daoMeme;
//    int id=0;
//    String link="https://nesmotrisuda/nadeysnekal";
//    String date="10-11-1999";
//    DateTimeFormatter f=DateTimeFormatter.ofPattern("dd-MM-yyyy");
//    LocalDate dateNew=LocalDate.parse(date,f);
//    public QuickShit(Dao<Meme,Integer> daoMeme) {
//        this.daoMeme=daoMeme;
//    }
//    public void QuickShitter() throws SQLException {
//        Meme meme=new Meme();
//        for(int i=1;i<=100;i++){
//            id=i;
//            daoMeme.create(memeReturner(id));
//            System.out.println(i);
//        }
//    }
//    public Meme memeReturner(int id){
//        return new Meme(id,link,dateNew);
//    }
}
