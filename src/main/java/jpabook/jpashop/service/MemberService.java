package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // readOnlu=true 로 하면 더티체킹도 안 하고 좀 더 조회에 성능최적화를 시켜줌
// jpa 에서 데이터 변경은 트랜잭션 안에서 이루어져야 한다
// 트랜잭션 어노테이션은 2개가 있는데 스프링꺼 쓰는게 좋음->옵션이 더 많음
@RequiredArgsConstructor // private final ~ 만 보고 생성자 알아서 만들어주고 주입 받음
public class MemberService {
//    @Autowired // 필드 인젝션
    private final MemberRepository memberRepository; // final 을 통해 컴파일 시점에 의존성이 들어왔는지 확인

    // @Autowired // 생성자 주입. 스프링이 뜰 때 인젝션함. 중간에 바꾸짐 못함(set 인젝션의 단점)
    // 테스트 케이스 만들 때도 좋음. 생성자가 하나이면 autowired 어노테이션 없어도 자동으로 인젝션 해줌
//    public MemberService(MemberRepository memberRepository)
//    {
//        this.memberRepository=memberRepository;
//    }

    // 회원 가입
    @Transactional // 기본은 readOnly=false, 여기에서는 조회가 아니라 값을 저장하니까
    public Long join(Member member)
    {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
        // em.persist() 를 하면 아직 디비에 들어가지 않은 시점임에도 영속성 컨텍스트에서 아이디를 부여해서 아이디가 존재함
    }

    private void validateDuplicateMember(Member member) {
        // exception
        List<Member> findMembers = memberRepository.findByName(member.getName());
        //해당 이름을 가진 맴버들이 있는지 찾는다
        if(!findMembers.isEmpty())
        {
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }

    }

    // 회원 전체 조회
    public List<Member>findMembers()
    {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId)
    {
        return memberRepository.findOne(memberId);
    }
}
