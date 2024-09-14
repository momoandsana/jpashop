package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name="category_id")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name="category_item",
        joinColumns = @JoinColumn(name="cateogry_id"),
        inverseJoinColumns = @JoinColumn(name="item_id")) // 다대다 관계이기 때문에 중간 테이블을 만들어서 다대다 관계를 풀어낸다
    // 객체는 컬렉션이 있기 때문에 다대다가 가능한데 관계형 디비에서는 다대다가 불가능함-> 중간 테이블이 필요
    // 실제로는 이렇게 사용하지는 않는데 jpa 공부를 위하여 만든 느낌
    // 원래는 중간 테이블에 양쪽의 fk 말고도 추가 컬럼들을 넣어야 하는데 이렇게 생성하면 추가 컬럼을 넣지 못한다
    private List<Item> items=new ArrayList<>();

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category>child=new ArrayList<>();
    // 셀프로 양방향 연관관계를 만들어

    // 연관관계 편의 메서드
    public void addChildCategory(Category child)
    {
        this.child.add(child); // 나에게 자식 추가
        child.setParent(this); // 자식에게 부모(나) 추가, 현재 전달된 자식 하나에게만
    }
}
