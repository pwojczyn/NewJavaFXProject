package com.pwojczyn.testproject.models.dao.impl;

import com.pwojczyn.testproject.models.MysqlConnector;
import com.pwojczyn.testproject.models.UserSession;
import com.pwojczyn.testproject.models.Utils;
import com.pwojczyn.testproject.models.dao.UserDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl  implements UserDao{
    private MysqlConnector connector=MysqlConnector.getInstance();
    private UserSession session = UserSession.getInstance();
    @Override
    public boolean login(String name, String password) {
        try {
            PreparedStatement preparedStatement = connector.getConnection().prepareStatement("SELECT * FROM user WHERE username = ?");
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()){
                return false;
            }
            session.setId(resultSet.getInt("id"));
            return resultSet.getString("password").equals(Utils.shaHash(password));

        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public boolean register(String name, String password) {
        try {
            PreparedStatement preparedStatement = connector.getConnection().prepareStatement("SELECT * FROM user WHERE username = ?");
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
            return false;

            }
            PreparedStatement preparedStatementInsert = connector.getConnection().prepareStatement("INSERT INTO user VALUES(?,?,?)");

            preparedStatementInsert.setInt(1, 0);
            preparedStatementInsert.setString(2, name);
            preparedStatementInsert.setString(3, Utils.shaHash(password));
            preparedStatementInsert.execute();
            preparedStatement.close();
            preparedStatementInsert.close();

        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public void removerUser(int id) {
        try {
            PreparedStatement preparedStatement = connector.getConnection().prepareStatement("DELETE FROM user WHERE id = ?");
            preparedStatement.setInt(1,id);
            preparedStatement.execute();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
