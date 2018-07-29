package com.spockatone.spock.service;

import com.spockatone.spock.dao.UserDao;
import com.spockatone.spock.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    private User getByName(String name) {return userDao.getByLogin(name);}

    public User autenticate(String name, String password) throws SecurityException {
        User user = getByName(name);

        if (user != null) {
            String encryptedPassword = encrypt(password + user.getSole());

            if (user.getPassword().equals(encryptedPassword)){
                return user;
            }
        }
        throw new SecurityException("No such user found: " + name);
    }

    public String encrypt(String text) {
        StringBuilder encryptedPassword = new StringBuilder();
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            LOG.info("Security: no such algorithm: MD5");
        }
        byte bytes[] = text.getBytes();
        byte digest[] = new byte[0];
        if (messageDigest != null) {
            digest = messageDigest.digest(bytes);
        }
        for (byte digestByte : digest) {
            encryptedPassword.append(Integer.toHexString(0x0100 + (digestByte & 0x00FF)).substring(1));
        }

        return encryptedPassword.toString();
    }
}
