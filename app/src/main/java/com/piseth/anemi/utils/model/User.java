package com.piseth.anemi.utils.model;

import com.google.type.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@Entity(tableName = "table_user")
public class User {
//    @Exclude
//    @PrimaryKey
    private int id;
    private String username;
    private String email;
    private String password;
    private String dob;
    private int roleId;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String avatar;
    private String salt;
    private String regDate;
    private List<CourseDetail> courseDetails;
    private List<QuizTaker> quizTakers;
    private List<RefreshToken> refreshTokens;
    private Role role;
//    @ColumnInfo(defaultValue = "0")
//    private int isDeleted;

    public User() {
        courseDetails = new ArrayList<>();
        quizTakers = new ArrayList<>();
        refreshTokens = new ArrayList<>();
    }

    public User(String username, String password) {
        new User();
        this.username = username;
        this.password = password;
    }

    public  User(String username, String email, String password, /*String dob,*/ int roleId, /*String firstName, String lastName, String address,*/ String phone, String avatar) {

        new User(username, password);
//        this.username = username;
        this.email = email;
//        this.password = password;
//        this.dob = dob;
        this.roleId = roleId;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.address = address;
        this.phone = phone;
        this.avatar = avatar;

    }
//    public User(String username, String email, String password, int userRoleId, String phone) {
//        this.username = username;
//        this.email = email;
//        this.password = password;
//        this.userRoleId = userRoleId;
//        this.phone = phone;
//    }


//    @Exclude
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public List<CourseDetail> getCourseDetails() {
        return courseDetails;
    }

    public void setCourseDetails(List<CourseDetail> courseDetails) {
        this.courseDetails = courseDetails;
    }

    public List<QuizTaker> getQuizTakers() {
        return quizTakers;
    }

    public void setQuizTakers(List<QuizTaker> quizTakers) {
        this.quizTakers = quizTakers;
    }

    public List<RefreshToken> getRefreshTokens() {
        return refreshTokens;
    }

    public void setRefreshTokens(List<RefreshToken> refreshTokens) {
        this.refreshTokens = refreshTokens;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

//    public Bitmap getPhoto() {
//        return photo;
//    }
//
//    public void setPhoto(Bitmap photo) {
//        this.photo = photo;
//    }


}
