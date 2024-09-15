package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository; // 롬복 생성자 주입
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional // 주문을 하기 때문에 변경이 필요함. readOnly=true 가 적용되면 안 된다
    public Long order(Long memberId,Long itemId,int count)
    {
        //엔티티 조회
        Member member=memberRepository.findOne(memberId);
        Item item =itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery=new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품 생성
        OrderItem orderItem= OrderItem.createOrderItem(item,item.getPrice(),count);

        //주문 생성
        Order order=Order.createOrder(member, delivery,orderItem);

        //주문 저장
        orderRepository.save(order);
        /*
        order 엔티티에서 orderItems, delivery 를 cascade all 로 설정해서 order 만 save 해도 모두 persist
        cascade 를 하는 범위-> order 가 orderItems, delivery 가 관리해야 하는 경우
        여기서는 order 만 orderItems, delivery 참조하기 때문에 cascade 로 묶어 놓음
        다른 곳에서도 참조를 하면 cascasde 로 묶으면 안 된다
         */

        return order.getId();
    }

    /**
     * 주문 취소
     */
    @Transactional// 변경사항이 생기기 때문에 readOnly=true 이면 안 된다
    public void cancelOrder(Long orderId)
    {
        //주문 엔티티 조회
        Order order=orderRepository.findOne(orderId);
        //주문 취소
        order.cancel();
    }

    //검색
//    public List<Order> findOrders(OrderSearch orderSearch)
//    {
//        return orderRepository.findAll(orderSearch);
//    }
}

/*
현재 비즈니스 로직들은 도메인(엔티티)에 있다
서비스 계층에서는 단순히 엔티티에 필요한 요청을 위임하는 역할만 한다
엔티티가 이렇게 비즈니스 로직을 가지고 객체 지향의 특성을 적극 활용하는 것을 도메인 모델 패턴이라고 한다
->orm 에서 선호

반대로 엔티티에는 비즈니스 로직이 거의 없고
서비스 계층에서 대부분의 비즈니스 로직을 처리하는 것을 트랜잭션 스크립트 패턴이라고 한다
 */