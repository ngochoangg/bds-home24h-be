package com.hoangvn.home24h.models.token;

import java.util.Date;

import javax.persistence.*;

import com.hoangvn.home24h.models.user.BaseEntity;

@Entity
@Table(name = "tokens")
public class Token extends BaseEntity {
    @Column(length = 9999)
    private String tokenString;
    private Date expireDate;

    public String getToken() {
        return tokenString;
    }

    public void setToken(String token) {
        this.tokenString = token;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }
}
