package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue
    @Column(name="member_id")// 테이블에서 컬럼 이름을 member_id 라고 설정했기 때문에
    private Long id;

    private String name;

    @Embedded// 원래는 반대쪽에 Embeddable 어노테이션이나 여기서 지금처럼 Embedded 어노테이션만 있으면 되지만 지금은 둘 다 설정함
    private Address address;

    @OneToMany(mappedBy="member") // 양방향이기 때문에 설정, 나는 order 의 member 필드와 매핑된다, 나는 매핑된 거울일 뿐이다. 조회만 가능함
    private List<Order> orders=new ArrayList<>(); // 1대다 관계이기 때문에 상대방을 리스트로 가짐
//    양방향 관계이기 때문에 비주인인 member 쪽에도 order 에 대한 정보가 있음
//    양방향 관계여도 여기서는 조회만 가능하고 수정은 관계의 주인인 order 에서만 가능
}
