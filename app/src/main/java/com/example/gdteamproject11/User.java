package com.example.gdteamproject11;

import com.google.firebase.database.DataSnapshot;

// 사용자 계정 정보 모델 클래스
public class User {
    public String email;
    public String name;
    public String phone;
    public String uid;

    /** DataSnapshot.getValue(User.class) 호출에 필요한 기본 생성자 **/
    public User(){}

    public User(String email, String name, String phone, String uid){
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.uid = uid;
    }

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getPhone() {return phone;}
    public void setPhone(String phone) {this.phone = phone;}
    public String getUid() {return uid;}
    public void setUid(String uid) {this.uid = uid;}

    public String toString(){
        return "User {" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
