package com.spockatone.spock.dao;

import com.spockatone.spock.entity.User;

public interface UserDao {
    User getByLogin(String login);
}
