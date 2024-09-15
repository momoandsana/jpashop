package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
/*
protected 생성자로만 생성 가능
여기서 이렇게 하는 이유->orderItem 을 createOrderItem() 함수로만 생성해서 사용하게 만들기 위해(정해진 형식으로만)
 */
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name="order_item_id")
    private Long id;

    @ManyToOne(fetch= LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    @ManyToOne(fetch= LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    private int OrderPrice;
    private int count;

    //==생성 메서드==/
    public static OrderItem createOrderItem(Item item, int orderPrice, int count)
    {
        OrderItem orderItem=new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }

    //==비즈니스 로직==/
    /*
    도메인 안에 비즈니스 로직 넣는 스타일 -> ddd
     */
    public void cancel()
    {
        getItem().addStock(count); // 재고 원복
    }

    //==조회 로직==/

    /**
     * 주문상품 전체 가격 조회
     *
     * */
    public int getTotalPrice()
    {
        return getOrderPrice()*getCount();
    }

}
