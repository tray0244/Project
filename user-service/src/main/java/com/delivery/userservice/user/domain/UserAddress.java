package com.delivery.userservice.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user_addresses") // ERD의 테이블명과 일치
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 핵심 User와 N : 1 관계 설정
    // FetchType.LAZY: 주소 조회할 때 유저 정보까지 다 가져오지 말라는 것이다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // FK 칼럼명 지정
    private User user;

    @Column(nullable = false, length = 50)
    private String addressName; // 우리집, 회사, 장소 A 등등..

    @Column(nullable = false, length = 50)
    private String city;

    @Column(nullable = false, length = 100)
    private String street;

    @Column(nullable = false, length = 10)
    private String zipcode;

    @Column(nullable = false, length = 100)
    private String detailAddress;

    // 생성자
    public UserAddress(User user, String addressName, String city, String street, String zipcode, String detailAddress){
        this.user = user;
        this.addressName = addressName;
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
        this.detailAddress = detailAddress;
    }

    // 주소 정보 수정
    public void updateAddress(String addressName, String city, String street, String zipcode, String detailAddress){
        this.addressName = addressName;
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
        this.detailAddress = detailAddress;
    }
}
