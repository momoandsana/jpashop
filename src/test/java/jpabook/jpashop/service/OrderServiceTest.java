package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Fail.fail;
import static org.junit.Assert.assertEquals;
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {
    /*
    원래 좋은 테스트는 스프링이나 디비를 엮지 않고 순수 함수만 테스트하는 것이 좋은 테스트
    지금은 jpa 동작을 보기 위해 다 엮어 놓았다

    지금처럼 엔티티 안에 비즈니스 로직이 있으면 단위 테스트하기 좋음
    ->DDD
    디비 연결 없이
     */


    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {

        //Given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10); //이름, 가격, 재고 int orderCount = 2;
        int orderCount=2;
        //When
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //Then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals("상품 주문시 상태는 ORDER",OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다.",1, getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량이다.", 10000 * 2, getOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 한다.",8, item.getStockQuantity()); }

    @Test(expected = NotEnoughStockException.class) // try catch 없이 익셉션 처리
    public void 상품주문_재고수량초과() throws Exception
    {
        //given
        Member member=createMember();
        Item item=createBook("시골 JPA",10000,10);

        int orderCount=11;

        //when
        orderService.order(member.getId(),item.getId(),orderCount);

        //then
        fail("재고 수량 부족 예외가 발생해야 한다");
    }

    @Test
    public void 주문취소()
    {
      //given
        Member member = createMember();
        Book item = createBook("시골 JPA", 10000, 10);

        int orderCount=2;

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder=orderRepository.findOne(orderId);
        assertEquals("주문 취소시 상태는 CANCEL",OrderStatus.CANCEL,getOrder.getStatus());
        assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 한다",10,item.getStockQuantity());

    }

    // 테스트마다 따로 member, book 만들기 번거롭기 때문에 따로 만들었음
    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123")); em.persist(member);
        return member;
    }
    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setStockQuantity(stockQuantity);
        book.setPrice(price);
        em.persist(book);
        return book;
    } }