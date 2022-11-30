package com.hoangvn.home24h.models.address;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hoangvn.home24h.models.user.User;

@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "country_id")
    @MapsId
    private Country country;

    @OneToOne
    @JoinColumn(name = "province_id")
    @MapsId
    private Province province;

    @OneToOne
    @JoinColumn(name = "district_id")
    @MapsId
    private District district;

    @OneToOne
    @JoinColumn(name = "ward_id")
    @MapsId
    private Ward ward;

    @JoinColumn(name = "user_id")
    @ManyToOne(optional = false)
    @JsonIgnore
    private User user;

    @Column(name = "so_nha")
    private String soNha;

    public String getSoNha() {
        return soNha;
    }

    public void setSoNha(String soNha) {
        this.soNha = soNha;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Province getRegion() {
        return province;
    }

    public void setRegion(Province region) {
        this.province = region;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Ward getWard() {
        return ward;
    }

    public void setWard(Ward ward) {
        this.ward = ward;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
