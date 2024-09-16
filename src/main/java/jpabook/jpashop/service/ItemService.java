package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item)
    {
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, String name, int price,int stockQuantity)// 변경 감지 사용
    {
        Item findItem=itemRepository.findOne(itemId); // 여기서는 영속성 엔티티를 꺼냄. 변경감지가 작동하기 때문에 수정사항이 생기면 업데이트 쿼리가 날라감
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
        // 별도의 save 함수가 없어도 findItem 이 영속 상태이고, Transactional 이 있기 때문에 수정이 일어나면 자동으로 디비에 반영이 된다. flush 과정에서 반영

        /*
        지금처럼 set 으로 바꾸는 것보다
        findItem.change(price,name,stockQuantity) 같이 함수를 통해 바꾸는게 맞다

        item 엔티티 코드에서
        public void change(int price, String name, int stockQuantity) {
            if (stockQuantity < 0) {
                throw new IllegalArgumentException("Stock quantity cannot be negative");
            }
            this.price = price;
            this.name = name;
            this.stockQuantity = stockQuantity;
        }
        이런 느낌으로 코드 추가하는게 맞다

        단순한 값 변경 외에도, 변경 시 유효성 검증이나, 추가적인 로직을 포함할 수 있다
        내부 필드를 외부에서 바로 수정하지 못하게 해서, 객체 스스로 자신의 상태를 관리하게 만든다->캡슐화 강화
         */
    }


    public List<Item> findItems()
    {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId)
    {
        return itemRepository.findOne(itemId);
    }
}
