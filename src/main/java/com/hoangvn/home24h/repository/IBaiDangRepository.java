package com.hoangvn.home24h.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hoangvn.home24h.models.post.BaiDang;

public interface IBaiDangRepository extends JpaRepository<BaiDang, Long> {
    public Optional<BaiDang> findByLoaiNhaDat(String loaiNhaDat);

    @Query(value = "SELECT * FROM posts WHERE da_ban_chua = false AND trang_thai='confirmed' ORDER BY ngay_tao DESC LIMIT :soBai", nativeQuery = true)
    List<BaiDang> findByNgayTaoChuaBan(@Param("soBai") Long soBai);

    @Query(value = "SELECT * FROM posts WHERE da_ban_chua = false AND trang_thai='confirmed'", nativeQuery = true)
    Page<BaiDang> findByNgayTaoChuaBanPageable(Pageable pageable);

    @Query(value = "SELECT * FROM posts WHERE user_id = :userId", nativeQuery = true)
    List<BaiDang> findByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT * FROM posts", nativeQuery = true)
    Page<BaiDang> baiDangAdmin(Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM `posts` WHERE trang_thai= :status", nativeQuery = true)
    Long findByTrangThai(@Param("status") String status);
}
