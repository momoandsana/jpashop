package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Category;
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
}
