package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor // EntityManager 를 스프링이 자동 주입
public class MemberRepository {

//    @PersistenceContext // 원래는 이 방법이 맞음
//    private EntityManager em;

//    public MemberRepository(EntityManager em)
//    {
//        this.em=em;
//    }

    @Autowired // 스프링 데이터 jpa 에서 자동으로 생성자 생성
    private final EntityManager em;

    public void save(Member member)
    {
        em.persist(member);// jpa가 저장함,영속성 컨텍스트에 객체 올림, member 의 id 가 생김. 아직 디비에 들어가지 않은 시점에서도
        // persist 를 하면 나중에 트랜잭션을 커밋하는 순간에 디비에 들어감(인서트)
    }

    public Member findOne(Long id)
    {
        return em.find(Member.class,id);
        // 조회하려는 엔티티가 member 클래스 타입임을 명시
        // 특정 엔티티를 조회할 때 id(pk)를 기준으로 조회
        // 타입+pk 넣기
    }// 단건 조회

    public List<Member> findAll()
    {
        return em.createQuery("select m from Member", Member.class)
                .getResultList(); // jpql 로 조회
        // jpql 은  테이블을 대상으로 쿼리하는게 아니라 객체(엔티티)를 대상으로 쿼리
    }// 목록으로 조회

    public List<Member> findByName(String name)
    {
        return em.createQuery("select m from m where m.name=:name",Member.class)
                .setParameter("name",name)
                .getResultList();
    }// 이름으로 멤버 리스트 찾기, jpql 이용
}
