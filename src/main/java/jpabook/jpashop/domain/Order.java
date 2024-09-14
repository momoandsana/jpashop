package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders") // 이거를 따로 설정하지 않으면 기본 이름이 order가 된다, order라는 이름은 자바에서 따로 쓰이므로 이름 변경
@Getter @Setter
public class Order {

    @Id
    @GeneratedValue
    @Column(name="order_id") // 테이블 이름과 아이디 이름을 맞추는게 좋음
    private Long id;

    @ManyToOne // 양방향이기 때문에 설정, 값의 변경이 오면 member의 orders 도 업데이트 해야 함
    @JoinColumn(name="member_id") // fk가 member_id 이다
    private Member member; // order 에 있는 member를 바꿔야 함. order가 주인이기 때문에

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems=new ArrayList<>();

    @OneToOne
    // 1대1 관계인데 fk를 order 가 가지고 있음. 보통 order 를 보면서 delivery 를 찾으니까
    @JoinColumn(name="delivery_id")
    private Delivery delivery;

    private LocalDateTime OrderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [order,cancel]
}
