package com.pwojczyn.testproject.models.dao.impl;

import com.pwojczyn.testproject.models.MysqlConnector;
import com.pwojczyn.testproject.models.UserSession;
import com.pwojczyn.testproject.models.Utils;
import com.pwojczyn.testproject.models.dao.ContactDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactDaoImpl implements ContactDao {

    private MysqlConnector connector = MysqlConnector.getInstance();
    private UserSession session = UserSession.getInstance();


    @Override
    public List<String> getAllContactsNames(String username) {
        List<String> list = new ArrayList<>();
        try {
            PreparedStatement preparedStatementID = connector.getConnection().prepareStatement("SELECT * FROM user WHERE username = ?");
            preparedStatementID.setString(1, username);
            ResultSet resultSetID = preparedStatementID.executeQuery();
            int userID = 0;

            while (resultSetID.next()) {
                         userID = resultSetID.getInt("id");
            }

            PreparedStatement preparedStatement = connector.getConnection().prepareStatement("SELECT * FROM contact WHERE userid = ?");
            preparedStatement.setInt(1,userID);
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {

                list.add(resultSet.getString("name"));
            }

        } catch (SQLException e){
            e.printStackTrace();
        }


        return list;
    }

    @Override
    public String getNumber(String contact) {
        try {
            PreparedStatement preparedStatement = connector.getConnection().prepareStatement(
                    "SELECT * FROM `contact` WHERE `name` = ?"
            );

            preparedStatement.setString(1,contact);

            ResultSet resultSet = preparedStatement.executeQuery();

            //System.out.println("SQL: "+resultSet.getString("number"));
            while (resultSet.next()) {
                return resultSet.getString("number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addContact(String name, String number) {
        try {
            PreparedStatement preparedStatement = connector.getConnection().prepareStatement(
                    "INSERT INTO contact VALUES(?,?,?,?)"
            );

            preparedStatement.setInt(1,0);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, number);
            preparedStatement.setInt(4, session.getId());
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void removeContact(String name) {
        try {
            PreparedStatement preparedStatement = connector.getConnection().prepareStatement(
                    "DELETE FROM `contact` WHERE `name` = ?"
            );
            preparedStatement.setString(1, name);
            preparedStatement.execute();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean editContact(String newName, String number, String oldName) {
        try {
            PreparedStatement preparedStatement = connector.getConnection().prepareStatement(
                    "UPDATE contact SET number = ?, name = ? WHERE name = ?"
            );

            preparedStatement.setString(1, number);
            preparedStatement.setString(2, newName);
            preparedStatement.setString(3, oldName);

            preparedStatement.execute();
            preparedStatement.close();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
