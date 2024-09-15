package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor // private final 필드 만들면 알아서 생성자 주입 받음
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item)
    {
        if(item.getId()==null) // 아이디가 없다면 아예 신규임
        {
            em.persist(item);
        }
        else
        {
            em.merge(item); // 아이디가 있으므로 이미 등록된 것을 가지고 옴->update 같은 느낌
        }
    }

    public Item findOne(Long id)
    {
        return em.find(Item.class,id);
    }

    public List<Item> findAll()
    {
        return em.createQuery("select i from Item i",Item.class)
                .getResultList();
    }
}
