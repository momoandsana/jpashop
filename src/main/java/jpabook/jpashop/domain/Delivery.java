package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.DeliveryStatus;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name="delivery_id")
    private Long id;

    @OneToOne(mappedBy="delivery") // order 에서 delivery 필드로 인식. 여기는 거울. 조회만 가능. 비주인
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    // ordinal 에서는 enum 에 숫자가 부여되서 숫자로 구별함. 숫자로 구분하면 중간에 다른 속성 들어가면 다 꼬임. string 으로 구분하는게 좋음
    private DeliveryStatus status; // READY, COMP
}
