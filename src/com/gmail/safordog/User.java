package com.gmail.safordog;

public class User {

    private int idAuthor;
    private String name;
    private String surname;
    private String login;
    private String password;

    public User() {
    }

    public User(int idAuthor, String name, String surname, String login, String password) {
        this.idAuthor = idAuthor;
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
    }

    public int getIdAuthor() {
        return idAuthor;
    }

    public void setIdAuthor(int idAuthor) {
        this.idAuthor = idAuthor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "idAuthor=" + idAuthor +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", login='" + login + '\'' +
                ", password=" + password +
                '}';
    }
}
