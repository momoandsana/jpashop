package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String createForm(Model model)
    {
        List<Member> members=memberService.findMembers();
        List<Item> items=itemService.findItems();

        model.addAttribute("members",members);
        model.addAttribute("items",items); // 파라미터들을 바인딩해서 보냄

        return "order/orderForm";
    }

    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId,
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count)
    {
        orderService.order(memberId,itemId,count); // 상품을 하나만 주문
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch")OrderSearch orderSearch,Model model)
    {
        List<Order> orders = orderService.findOrders(orderSearch); // 이런 식의 단순 호출은 원래 바로 리포지터리에서 호출해도 괜찮음
        model.addAttribute("orders",orders);

        return "order/orderList";
    }
    /*
    여기서는 @ModelAttribute("orderSearch")OrderSearch orderSearch 를 이용해서 객체 단위로 주고 받는다
    모델 단위로 주고 받고 싶다면 해당 모델에 해당하는 모든 속성들을 입력 받고 보내줘야 한다
    만약에 입력받지 않은 속성이 있는데 보내준다면 해당 속성에 null 이 들어간다

    ModelAttribute 는 html 폼에서의 속성값과 서버 측 모델 객체의 필드명이 일치하면 스프링이 자동으로 바인딩한다
     */

    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId)
    {
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }
}
