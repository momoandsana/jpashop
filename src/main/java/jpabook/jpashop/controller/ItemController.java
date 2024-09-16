package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController
{
    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model)
    {
        model.addAttribute("form",new BookForm());
        return "items/createItemForm";
    }
    /*
    여기서는 단순히 폼을 보여주는 작업이기 때문에 get 요청+포워딩
     */

    @PostMapping("/items/new")
    public String create(BookForm form)
    {
        /*
        원래는 유효성 체크해야 함
        원래는 이렇게 set 하는 것보다 static createBook 같은 함수 만들어서 생성하는게 좋은 방법임
         */
        Book book=new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/";
    }
    /*
    먼저 해당 페이지에 들어가는 버튼을 클릭하면 get 요청을 통해 폼을 받는다
    그 폼을 받고 나서 작성을 완료하면 post 요청을 통해 폼을 제출한다
     */

    @GetMapping("/items")
    public String list(Model model)
    {
        List<Item> items=itemService.findItems();
        model.addAttribute("items",items);
        return "items/itemList"; // 단순 조회해서 아이템 목록을 보여줌
    }


    @GetMapping("items/{itemId}/edit") // {itemId}는 pathVariable, 요청에 동적인 itemId가 포함되어서 전송된다
    public String updateItemForm(@PathVariable("itemId") Long itemId,Model model)// 위에서 받은 동적인 부분을 추출해서 사용
    {
        Book item=(Book)itemService.findOne(itemId);
        // 원래 이렇게 다운캐스팅 하는거 안 좋음

        BookForm form=new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form",form);
        return "items/updateItemForm";
    }// 타임리프는 서버에서 받아온 아이템아이디들이 있고 거기서 사용자가 선택하면 해당 아이템아이디를 포함한 요청을 다시 보냄

    @PostMapping("items/{itemId}/edit")
    public String updateItem(@ModelAttribute("form") BookForm form)
    {
        Book book=new Book();
        book.setId(form.getId());
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/items";
    }
}
