package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

// 여기서의 테스트는 격리된 환경에서 진행하는 테스트가 아님
@RunWith(SpringRunner.class) // junit 와 스프링 같이 쓰겠다
@SpringBootTest // 스프링 부트 띄우고 테스트하고 싶으면, autowired 같은거 사용할 수 있게
@Transactional
// 테스트이기 때문에 자동 롤백(디비에 남으면 안 되니까). 서비스나 리포지터리 클래스에서는 롤백을 하지 않는다
public class MemberServiceTest {
    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    @Rollback(false)
    // 기본 스프링 transactional 은 커밋을 하지 않고 롤백을 한다. 엔티티가 인서트 되는 것을 보고 싶다면 rollback을 false로 설정해야지 인서트 되는 모습을 볼 수 있다
    public void 회원가입() throws Exception{
        //given
        Member member=new Member();
        member.setName("kim");

        //when
        Long savedId=memberService.join(member);

        //then
        //em.flush(); // Rollback(false) 어노테이션을 사용하지 않고 인서트하는 방법
        assertEquals(member,memberRepository.findOne(savedId));
    }

    @Test(expected=IllegalStateException.class)
    public void 중복_회원_예외() throws Exception{
        //given
        Member member1= new Member();
        member1.setName("kim");

        Member member2=new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
        memberService.join(member2); // IllegalStateException 을 생성. try catch 없이 익셉션 확인
        //then

    }

}