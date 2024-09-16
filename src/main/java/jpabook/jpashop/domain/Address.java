package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter // 값 타입은 생성할 때만 만들어지고 나중에는 get만함
public class Address {
    private String city;
    private String street;
    private String zipcode;

    protected Address() // 기본 생성자는 protected 로 쓰기
    {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
