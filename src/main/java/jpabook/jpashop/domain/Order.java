package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.DeliveryStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Table(name="orders") // 이거를 따로 설정하지 않으면 기본 이름이 order가 된다, order라는 이름은 자바에서 따로 쓰이므로 이름 변경
@Getter @Setter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
/*
createOrder() 함수로만 order 를 생성하기 만들기 위해
Order order=new Order(); 이런 형식으로 만들어서 쓰면 안 된다. 정해진 형식으로만 만들어서 써야 된다
 */
public class Order {

    @Id
    @GeneratedValue
    @Column(name="order_id") // 테이블 이름과 아이디 이름을 맞추는게 좋음
    private Long id;

    @ManyToOne(fetch= LAZY) // 양방향이기 때문에 설정, 값의 변경이 오면 member의 orders 도 업데이트 해야 함
    @JoinColumn(name="member_id") // fk가 member_id 이다
    private Member member; // order 에 있는 member를 바꿔야 함. order가 주인이기 때문에

    @OneToMany(mappedBy = "order",cascade=CascadeType.ALL)
    private List<OrderItem> orderItems=new ArrayList<>();
//    cascade를 사용하지 않으면 persist(orderItemA),persist(orderItemB),... 등을 한 번씩 persist 해줘야 하는데
//    cascade를 사용하면 persist(order) 로 orderItem을 모두 persist 해줄 수 있다
//    delete 할 때도 같이 다 지워버린다

    @OneToOne(fetch = LAZY,cascade = CascadeType.ALL)
    // 1대1 관계인데 fk를 order 가 가지고 있음. 보통 order 를 보면서 delivery 를 찾으니까
    // 원래는 persist(order), persist(delivery) 를 해야 하는데
    // cascade 덕분에 persist(order)로 둘 다 가능
    @JoinColumn(name="delivery_id")
    private Delivery delivery;

    private LocalDateTime OrderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [order,cancel]

    // 연관관계 편의 메서드 -> 양방향인 애들 서로에게 정보 업데이트 해주기, 연관관계의 주인 쪽에서 컨트롤
    public void addOrderItem(OrderItem orderItem)
    {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery)
    {
        this.delivery=delivery;
        delivery.setOrder(this);
    }

    //==생성 메서드==//
    public static Order createOrder(Member member,Delivery delivery,OrderItem... orderItems)
    {
        Order order=new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems)
        {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비즈니스 로직==/
    /**
     * 주문 취소->엔티티 안에 있음
     */
    public void cancel()
    {
        if (delivery.getStatus() == DeliveryStatus.COMP)
        {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다");
        }

        this.setStatus(OrderStatus.CANCEL);
        for(OrderItem orderItem:orderItems)
        {
            orderItem.cancel();
        }
    } // 주문을 생성. 주문에 필요한 모든 것을 넣는다

    //==조회 로직==/
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice()
    {
        int totalPrice=0;
//        for(OrderItem orderItem:orderItems)
//        {
//            totalPrice+=orderItem.getTotalPrice();
//        }
        totalPrice += orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum(); // 위랑 같은 식
        return totalPrice;
    }
}
