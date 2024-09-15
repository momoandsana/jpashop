package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// 싱글테이블로 관리하기 때문에 상속전략이 필요함. book, album, movie 모두 하나의 테이블로 설정, 성능이 좋음
@DiscriminatorColumn(name="dtype") // 싱글테이블이기 때문에 dtype으로 구분
public class Item {

    @Id
    @GeneratedValue
    @Column(name="item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity; // 아이템을 부모로 가지는 자식들의 공통된 속성

    @ManyToMany(mappedBy = "items")
    private List<Category> categories=new ArrayList<>();

    // ==비즈니스 로직==//

    /*
    객체지향적으로 생각해보면 데이터를 가지고 있는 쪽에 비즈니스로직이 있는게 좋음
    데이터의 응집력이  있음
     */


    /*
    재고를 수정해야 하는 일이 있다면 setter로 변경하는게 아니라 밑에 처럼 비즈니스 로직함수로 재고를 변경해야 한다
     */

    /**
     *
     * stock 증가
     */
    public void addStock(int quantity)
    {
        this.stockQuantity+=quantity;
    }

    /**
     * stock 감소
     */
    public void removeStock(int quantity)
    {
        int restStock=this.stockQuantity-quantity;
        if(restStock<0)
        {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity=restStock;
    }
}
