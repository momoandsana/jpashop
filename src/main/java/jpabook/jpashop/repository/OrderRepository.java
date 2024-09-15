package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em; // 별도의 생성자 없이 EnitityManager 를 자동으로 주입해준다, autowired 가 필요 없음

    public void save(Order order)
    {
        em.persist(order);
    }

    public Order findOne(Long id)
    {
        return em.find(Order.class,id);
    }

    //public List<Order> findAll(OrderSearch orderSearch){}
}
