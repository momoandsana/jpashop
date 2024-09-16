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

    public void save(Item item) // 이거는 병합을 사용하는 방법. 준영속을 영속으로 만들어서 수정사항이 디비에 반영이 되게
    {
        if(item.getId()==null) // 아이디가 없다면 아예 신규임
        {
            em.persist(item);
        }
        else
        {
            em.merge(item);
            /*
            아이디가 있으므로 이미 등록된 것을 가지고 옴->update 같은 느낌, 준영속을 영속으로 만들어
            여기에서 넘어온 item 은 영속성이 아님
            만약에 영속인 것으로 계속 쓰고 싶으면 Item merge = em.merge(item); 이렇게 만들어서 merge 를 사용해야 한다

            변경 감지 기능을 사용하면 원하는 속성만 변경할 수 있지만, 병합을 사용하면 모든 속성이 변경된다
            병합시 값이 없으면 null 로 업데이트 된다
            병합은 모든 필드를 교체한다
             */
        }
    }

    public Item findOne(Long id) // 이 함수는 영속성 엔티티를 찾아준다
    {
        return em.find(Item.class,id);
    }

    public List<Item> findAll()
    {
        return em.createQuery("select i from Item i",Item.class)
                .getResultList();
    }
}
