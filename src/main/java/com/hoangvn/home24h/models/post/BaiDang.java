package com.hoangvn.home24h.models.post;

import java.util.Date;

import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.hoangvn.home24h.models.address.Address;

@Entity
@Table(name = "posts")
public class BaiDang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "loai_nha_dat")
    private String loaiNhaDat; // Nhà đất, đất, chung cư

    @Column(name = "hinh_thuc") // ban, cho thue
    private String hinhThuc;

    @JoinColumn(name = "vi_tri")
    @OneToOne
    @MapsId
    private Address viTri;

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

    public Address getViTri() {
        return viTri;
    }

    public void setViTri(Address viTri) {
        this.viTri = viTri;
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

}
