package com.hoangvn.home24h.models.post;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hoangvn.home24h.models.address.District;
import com.hoangvn.home24h.models.address.Province;
import com.hoangvn.home24h.models.user.User;

@Entity
@Table(name = "posts")
public class BaiDang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "loai_nha_dat")
    private String loaiNhaDat; // Nhà đất, đất, chung cư

    @Column(name = "hinh_thuc") // ban, cho thue
    @NotEmpty(message = "Bài đăng cần bán hay cho thuê?")
    private String hinhThuc;

    @JoinColumn(name = "tinh_thanh")
    @OneToOne(targetEntity = Province.class)
    private Province tinhThanh;

    @JoinColumn(name = "quan_huyen")
    @OneToOne(targetEntity = District.class)
    private District quanHuyen;

    @Column(name = "so_nha")
    private String soNha;

    @Column(name = "dien_tich")
    private Double dienTich;

    @Column(name = "so_phong")
    private int soPhong;

    @Column(name = "so_tang")
    private int soTang;

    @Column(name = "du_an")
    private String duAn;

    @Column(name = "da_ban_chua")
    private boolean daBan;

    @Column(name = "trang_thai")
    private String status;

    @Column(name = "link_hinh_anh")
    @NotNull
    private String linkAnh;

    @Column(name = "gia_tien")
    private double giaTien;

    @Column(name = "ghi_chu")
    private String ghiChu;

    @JoinColumn(name = "user_id")
    @ManyToOne
    @JsonIgnoreProperties({ "role", "hoTen", "password", "diaChi", "soDienThoai", "email" })
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @Column(nullable = true, name = "ngay_tao")
    private Date ngayTao;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    @Column(name = "ngay_cap_nhat", nullable = true)
    private Date ngayCapNhat;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLoaiNhaDat() {
        return loaiNhaDat;
    }

    public void setLoaiNhaDat(String loaiNhaDat) {
        this.loaiNhaDat = loaiNhaDat;
    }

    public String getHinhThuc() {
        return hinhThuc;
    }

    public void setHinhThuc(String hinhThuc) {
        this.hinhThuc = hinhThuc;
    }

    public Double getDienTich() {
        return dienTich;
    }

    public void setDienTich(Double dienTich) {
        this.dienTich = dienTich;
    }

    public int getSoPhong() {
        return soPhong;
    }

    public void setTinhThanh(Province tinhThanh) {
        this.tinhThanh = tinhThanh;
    }

    public District getQuanHuyen() {
        return quanHuyen;
    }

    public void setQuanHuyen(District quanHuyen) {
        this.quanHuyen = quanHuyen;
    }

    public String getSoNha() {
        return soNha;
    }

    public void setSoNha(String soNha) {
        this.soNha = soNha;
    }

    public void setSoPhong(int soPhong) {
        this.soPhong = soPhong;
    }

    public int getSoTang() {
        return soTang;
    }

    public void setSoTang(int soTang) {
        this.soTang = soTang;
    }

    public String getDuAn() {
        return duAn;
    }

    public void setDuAn(String duAn) {
        this.duAn = duAn;
    }

    public boolean isDaBan() {
        return daBan;
    }

    public void setDaBan(boolean daBan) {
        this.daBan = daBan;
    }

    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }

    public Date getNgayCapNhat() {
        return ngayCapNhat;
    }

    public void setNgayCapNhat(Date ngayCapNhat) {
        this.ngayCapNhat = ngayCapNhat;
    }

    public double getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(double giaTien) {
        this.giaTien = giaTien;
    }

    public Province getTinhThanh() {
        return tinhThanh;
    }

    public String getLinkAnh() {
        return linkAnh;
    }

    public void setLinkAnh(String linkAnh) {
        this.linkAnh = linkAnh;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
