package hello.itemservice.domain.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor //"final"이 붙은 생성자를 자동 생성후 의존관계 자동 주입!! ->lombok 기능
public class BasicItemController {

    private final ItemRepository itemRepository;

    /*
    @Autowired //생성자가 하나면 생략 가능
    public BasicItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
    */

    @GetMapping
    public String items(Model model) {
        List<Item> items =itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    /** "RedirectAttribute"를 사용했을때 html의 "th:if"의 기능을 사용하지 않았을 경우 -> 직접 model에 담아주어야 된다. **/
/*    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model , @RequestParam(value = "status" , required = false) String status) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        if(status!=null)
            model.addAttribute("status", status);
        return "basic/item";
    }*/
    /** "RedirectAttribute"를 사용했을때 html의 "th:if"의 기능을 사용했을 경우 **/
    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }


    /**
     * "@ModelAttribute"의 객체의 없는 변수는 null 값이 된다.
     * "@ModelAttribute("item") Item item"이라고 선언하게 되면 자동으로 "Model"에 key는 "item"으로 value는 가져온 값으로 자동 들어간다!!!
     * "@ModelAttribute Item item" 처럼 이름을 생랼하게 되면  해당 클래스(Item)의 첫글자를 소문자로 바꾸고(item) "Model"에 동일하게 담아준다.!!!!!!
     * "@ModelAttribute"도 생략가능하다!! 나머지는 바로 위와 마찬가지로 담긴다.
     */
    //@PostMapping("/add")
    public String addItemV1(@ModelAttribute("item") Item item, Model model) {

        itemRepository.save(item);
//        model.addAttribute("item", item); //자동 추가 , 생략가능
        return "basic/item";
    }
    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, Model model) {

        itemRepository.save(item);
//        model.addAttribute("item", item); //자동 추가 , 생략가능
        return "basic/item";
    }
    //@PostMapping("/add")
    public String addItemV3(Item item, Model model) {  //->객체니깐 "@Model~"적용 , 일반 자료형이면 "@Request~" 적용

        itemRepository.save(item);
//        model.addAttribute("item", item); //자동 추가 , 생략가능
        return "basic/item";
    }
    /**위의 버전들은 상품을 저장하고 새로고침을 하게 되면 계속적으로 등록된다!!!!! --> "redirect"로 "URL"을 다시 보내도록 한다. **/
    //@PostMapping("/add")
    public String addItemV4(Item item, Model model) {  //->객체니깐 "@Model~"적용 , 일반 자료형이면 "@Request~" 적용

        itemRepository.save(item);
        return "redirect:/basic/items/" +item.getId();
    }
    /** 저장이 잘 되었으면 상품 상세 화면에 "저장되었습니다"라는 메시지를 보여달라는 문구 기능 추가 -> "RedirectAttributes"사용
     --> pathVariable 바인딩: {itemId} + 쿼리 파라미터로 처리: "?status=true" ---> "URL: ~/basic/items/{itemId}?status=true" **/
    @PostMapping("/add")
    public String addItemV5(Item item, RedirectAttributes redirectAttributes) {  //->객체니깐 "@Model~"적용 , 일반 자료형이면 "@Request~" 적용

        Item savedItem = itemRepository.save(item);

        redirectAttributes.addAttribute("itemId",savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
    }



    @GetMapping("/{itemId}/edit")
    public String edit(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit2(@PathVariable long itemId, @ModelAttribute Item item, Model model) {

        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}"; //"@PathVariable"의 값도 사용할수 있다!!!!
    }


    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct //자동 의존관계 주입후 실행
    public void init() {
        itemRepository.save(new Item("testA", 10000,10));
        itemRepository.save(new Item("testB", 20000, 20));
    }
}
