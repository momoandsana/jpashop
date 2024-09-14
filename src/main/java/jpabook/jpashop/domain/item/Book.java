package jpabook.jpashop.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@DiscriminatorValue("B") // 싱글테이블에서 B를 통해 구분
public class Book extends Item{

    private String author;
    private String isbn;
}
