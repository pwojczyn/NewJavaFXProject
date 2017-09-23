package com.pwojczyn.testproject.models.dao;

public interface UserDao {
    boolean login(String name, String password);
    boolean register(String name, String password);
    void removerUser(int id);
}
