package com.hoangvn.home24h.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.hoangvn.home24h.models.post.BaiDang;
import com.hoangvn.home24h.models.user.User;
import com.hoangvn.home24h.repository.IBaiDangRepository;
import com.hoangvn.home24h.repository.address.IDistrictRepository;
import com.hoangvn.home24h.repository.address.IProvinceRepository;
import com.hoangvn.home24h.repository.address.IWardRepository;
import com.hoangvn.home24h.repository.user.IUserRepository;
import com.hoangvn.home24h.services.BaiDangService;

@RestController
@CrossOrigin
public class BaiDangController {
    @Autowired
    IBaiDangRepository baiDangRepository;
    @Autowired
    IDistrictRepository districtRepository;
    @Autowired
    IProvinceRepository provinceRepository;
    @Autowired
    IWardRepository wardRepository;
    @Autowired
    BaiDangService postService;

    @Autowired
    IUserRepository userRepository;

    // Create

    @PostMapping(path = "/post")
    @PreAuthorize("hasAuthority('CREATE')")
    public ResponseEntity<Object> createPost(@RequestBody Map<@NotEmpty String, Object> baiMoi) {
        try {
            BaiDang saved = postService.convertToBaiDang(baiMoi);
            saved.setNgayTao(new Date());
            return new ResponseEntity<>(baiDangRepository.save(saved), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    // Read

    @GetMapping(path = "/posts")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> danhSachBaiDang(@RequestParam(defaultValue = "0", required = false) String p,
            @RequestParam(defaultValue = "10", required = false) String s) {
        try {
            Pageable pageWithTen = PageRequest.of(Integer.parseInt(p), Integer.parseInt(s));
            List<BaiDang> list = new ArrayList<>();
            baiDangRepository.findAll(pageWithTen).forEach(list::add);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/post/lts")
    public ResponseEntity<Object> getLastestPost(@RequestParam(required = false, defaultValue = "6") String post) {
        try {
            return new ResponseEntity<>(baiDangRepository.findByNgayTaoChuaBan(Long.parseLong(post)), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    // Post only admin and manager access
    @GetMapping(path = "/aposts")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<Object> getPostForAdmin(@RequestParam(defaultValue = "0", required = false) String p,
            @RequestParam(required = false, defaultValue = "10") String s) {
        try {
            Pageable pageWithTen = PageRequest.of(Integer.parseInt(p), Integer.parseInt(s));

            return new ResponseEntity<>(baiDangRepository.baiDangAdmin(pageWithTen).getContent(), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @GetMapping(path = "{username}/post/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.name")
    public ResponseEntity<Object> getPostById(@PathVariable Long id, @PathVariable String username) {
        Optional<User> optional = userRepository.findByUsername(username);
        try {
            if (optional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            AtomicReference<BaiDang> reference = new AtomicReference<>(null);
            User user = optional.get();
            user.getCacBaiDang().forEach(p -> {
                if (p.getId() == id) {
                    reference.set(p);
                }
            });
            if (null == reference.get()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(reference.get(), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    // Get status summary
    @GetMapping(path = "/post/stat")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> getPostByStatus(@RequestParam(required = true) String s) {
        Set<String> validStatuses = Set.of("open", "confirmed", "closed");
        try {

            if (validStatuses.contains(s)) {
                return new ResponseEntity<>(baiDangRepository.findByTrangThai(s), HttpStatus.OK);
            }
            return ResponseEntity.unprocessableEntity().body("Status only accept: `open`, `confirmed`, `closed`");

        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    // Get post with id
    @GetMapping(params = "/post/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<Object> getPostById(@PathVariable Long id) {
        try {
            Optional<BaiDang> optional = baiDangRepository.findById(id);
            if (optional.isPresent()) {
                return new ResponseEntity<>(optional.get(), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    // Update

    // Người dùng chỉ được sửa bài đăng của chính họ
    // và không được phép sửa trường status. Chỉ duy nhất
    // quyền `ROLE_ADMIN` là có thể.
    @PutMapping(path = "/{username}/post/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER') or (#username == authentication.name and #mPost['status'] == null)")
    public ResponseEntity<Object> updateUserPostById(@PathVariable Long id,
            @RequestBody Map<String, Object> mPost,
            @PathVariable String username) {
        try {
            AtomicReference<BaiDang> reference = new AtomicReference<>(null);
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                userOptional.get().getCacBaiDang().forEach(p -> {
                    if (p.getId() == id) {
                        reference.set(p);
                    }
                });
            }
            if (null == reference.get() || baiDangRepository.findById(id).isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            BaiDang update = postService.updateBaiDang(mPost, reference.get());
            update.setNgayCapNhat(new Date());

            return new ResponseEntity<>(baiDangRepository.save(update), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    // Update with id
    @PutMapping(path = "/post/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<Object> updatePostById(@PathVariable Long id,
            @RequestBody Map<String, Object> mPost) {
        try {
            Optional<BaiDang> optional = baiDangRepository.findById(id);
            if (optional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            BaiDang update = postService.updateBaiDang(mPost, optional.get());
            update.setNgayCapNhat(new Date());

            return new ResponseEntity<>(baiDangRepository.save(update), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    // Delete

    @DeleteMapping(path = "/post/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> deletePostById(@PathVariable Long id) {
        try {
            Optional<BaiDang> optional = baiDangRepository.findById(id);
            if (optional.isPresent()) {
                baiDangRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

}
