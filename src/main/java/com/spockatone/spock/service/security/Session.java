package com.spockatone.spock.service.security;

import com.spockatone.spock.entity.User;

import java.time.LocalDateTime;
import java.util.Objects;

public class Session {

    private User user;
    private LocalDateTime expireTime;
    private String token;

    Session(String token) {
        this.token = token;
    }

    public Session() {

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return Objects.equals(token, session.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }

    @Override
    public String toString() {
        return "Session{" +
                "user=" + user +
                ", expireTime=" + expireTime +
                ", token='" + token + '\'' +
                '}';
    }
}