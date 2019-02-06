package com.example.varuns.capstone.model;

import java.util.Collection;

public class User {
    private Integer userId;

    private String password;
    private String company;
    private String firstName;
    private String lastName;
    private String email;

    private String userName;

    private String phone;
    private String address1;
    private String address2;
    private String country;
    private String postal;



    private Collection<UserRole> roles;

    private Integer isActive;

    public User(){
        this("new", "PASSWORD", "new", "new", "",  1, "", "", "", "", "", "");
    }

    public User(String userName, String password, String firstName, String lastName){
        this(userName, password, firstName, lastName, "", 1, "", "", "", "", "", "");
    }

    public User(String userName, String password, String firstName, String lastName, Integer isActive){
        this(userName, password, firstName, lastName, "",  isActive, "", "", "", "", "", "");
    }

    public User(String userName, String password, String firstName, String lastName, String email, Integer isActive,
                String company, String phone, String address1, String address2, String country, String postal
    ){
        this.setUserName(userName);
        this.setEmail(email);
        this.setPassword(password);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setActive(isActive);
        this.setCompany(company);
        this.setPhone(phone);
        this.setAddress1(address1);
        this.setAddress2(address2);
        this.setCountry(country);
        this.setPostal(postal);
    }



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public Integer isActive() {
        return isActive;
    }

    public void setActive(Integer isActive) {
        this.isActive = isActive;
    }

    public String getFullName(){
        return this.firstName + this.lastName;
    }

    public Collection<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Collection<UserRole> roles) {
        this.roles = roles;
    }

}
