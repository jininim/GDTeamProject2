package com.example.gdteamproject11;

// 사용자 계정 정보 모델 클래스
public class UserAccount {
    private String userName;
    private String userEmail;
    private String idToken; //Firebase Uid (고유 토큰정보)
    private String userPwd; // 이메일 아이디
    private String userPhone;

    //Firebase 오류방지용 빈생성자
    public UserAccount(){ }
    public String getUserName() {return userName;}
    public void setUserName(String userName) {this.userName = userName;}
    public String getUserEmail() {return userEmail;}
    public void setUserEmail(String userEmail) {this.userEmail = userEmail;}
    public String getIdToken() {return idToken;}
    public void setIdToken(String idToken) {this.idToken = idToken;}
    public String getUserPwd() {return userPwd;}
    public void setUserPwd(String userPwd) {this.userPwd = userPwd;}
    public String getUserPhone() {return userPhone;}
    public void setUserPhone(String userPhone) {this.userPhone = userPhone;}
}
