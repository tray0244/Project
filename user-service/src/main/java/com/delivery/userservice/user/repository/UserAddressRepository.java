package com.delivery.userservice.user.repository;

import com.delivery.userservice.user.domain.User;
import com.delivery.userservice.user.domain.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    // 특정 유저의 모든 주소 조회
    // select * from user_address where user_id = ?
    List<UserAddress> findAllByUserId(Long userId);
    List<UserAddress> findAllByUser(User user);
}