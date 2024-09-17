package jpabook.jpashop.controller;

import jakarta.validation.Valid;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService; // final 이 붙은 memberService 를 자동으로 생성자 주입

    @GetMapping(value = "/members/new")
    // 클라이언트가 get 요청으로 /members/new 를 보내면 여기서 처리
    public String createForm(Model model)
    // model 은 컨트롤러에서 뷰로 넘겨줄 때 정보를 담는 객체, 모델을 스프링에서 자동으로 주입하는 인수
    // 여기서 모델이 버스라면 addAttributes 를 통해 데이터(승객)을 넣어서 보낸다
    {
        model.addAttribute("memberForm", new MemberForm());
        // 비어 있는 form 이지만 이렇게 해야지 유효성 검증이 가능함
        return "members/createMemberForm";
        // members/createMemberForm 이라는 뷰를 랜더링함
    }

    @PostMapping(value = "/members/new") // post 는 리다이렉션 하는게 원칙, 보통 post 는 데이터를 처리하거나 변경을 하기 때문에
    // /members/new 가 같지만 이거는 post 이므로 다른 api 임
    public String create(@Valid MemberForm form, BindingResult result) {
        //valid 어노테이션은 폼 데이터의 유효성을 검증. form 안에는 NotEmpty 같은 어노테이션이 있어야 함
        // member 객체가 아니라 memberForm 을 사용하는 이유 -> validation 같은 코드까지 member 에 추가하면 너무 지저분해짐
        // 핵심 비즈니스 로직 정도만 member 객체 안에 만들어

        if (result.hasErrors()) // 이름은 필수로 입력해야 하므로 없으면 에러가 생김
        {
            return "members/createMemberForm";
            /*
            포워딩은 서버 내부에서 지정된 뷰 템플릿 members/createMemberForm 을 랜더링하여 클라이언트에게 html 을 반환
            포워딩은 서버 내부에서 처리
            브라우저의 url 은 그대로 유지되고 서버 내부적으로 뷰를 처리함
            주로 데이터를 보여줄 때나 단순 페이지 이동 시 사용
            여기서 포워딩을 하는 이유는, 클라이언트가 폼을 다시 작성하도록 하기 위함
            서버에서는 그대로 createMemberForm 페이지를 다시 랜더링
            여기서는 url 이 변경되지 않는다, 사용자는 폼 데이터를 그대로 볼 수 있어 오류 수정하기 좋다
            폼을 다시 보여줘야 하기 때문에 포워딩을 사용
            기존의 틀리지 않는 부분들은 유지가 되고 이름 부분만 지워지고 경고 문구가 나옴
            원래 다시 폼을 랜더링하면 다른 정보들도 사라지는데 여기서는 타임리프가 BindingResult 를 사용해서 기존의 정보는 유지시킴
            유효성 검증에 실패하면 스프링은 BindingResult 객체를 포함하여 다시 폼 페이지로 넘겨줌
            저기에는 검증에서 발생한 오류 정보가 포함되어 있으며, 타임리프가 이를 사용하여 오류 메시지 랜더링
             */
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);
        memberService.join(member);
        return "redirect:/"; // 루트 경로(홈페이지)로 이동-> http://localhost:8080
        /*
        redirect 는 클라이언트(브라우저)에게 새 url 로 다시 요청하라고 지시하는 http 응답을 보냄
        클라이언트는 해당 url 로 다시 get 요청을 보내게 되므로 url 이 바뀌고, 페이지가 새로고침된 것처럼 보인다
        주로 데이터 변경 후 새로운 페이지로 이동할 때 사용
        여기서 새로운 맴버가 추가되어 상태가 변경되었기 때문에 다른 페이지로 이동하게 만든다
         */
    }
    /*
    get 은 폼을 보여주는 api
    post 는 그 폼을 제출하는 api
    같은 /members/new 를 사용하지만 post, get 이므로 각각 다른 api 다
     */

    @GetMapping("/members")
    public String list(Model model)
    {
//        List<Member> members=memberService.findMembers();
//        model.addAttribute("members",members);
//        여기에다가 컨트롤 t 해서 인라인하면 밑에처럼 된다. members 에 마우스 커서 두고 해야 함
        model.addAttribute("members", memberService.findMembers());

        return "members/memberList";
        /*
        포워딩함->서버 내부적으로 처리, 같은 url 에 머물고 있다
        url 은 같지만 다른 페이지가 나올 수 있다
         */
    }

    /*
    리다이렉트는 클라이언트에게 새로운 url 로 다시 요청을 보내라고 지시하며, url 이 변경된다
    포워딩은 서버 내부에서 요청을 처리하고 뷰를 직접 반환하여, url 이 변경되지 않는다
    리다이렉트는 주로 데이터 변경 후 사용하고, 포워딩은 단순 페이지 이동이나 오류 처리 시 사용

    데이터를 생성하고 리다이렉트가 아닌 포워딩을 사용하면 사용자가 새로고침할 경우, 이전에 처리된 post 요청이 다시 실행된다
    이로 인해 같은 데이터가 중복으로 생성될 수 있다
     */

}