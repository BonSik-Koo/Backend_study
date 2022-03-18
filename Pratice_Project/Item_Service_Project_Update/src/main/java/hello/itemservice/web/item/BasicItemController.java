package hello.itemservice.web.item;

import hello.itemservice.domain.item.DeliveryCode;
import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.ItemType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor //"final"이 붙은 생성자를 자동 생성후 의존관계 자동 주입!! ->lombok 기능
@Slf4j
public class BasicItemController {

    private final ItemRepository itemRepository;

    /*
    @Autowired //생성자가 하나면 생략 가능
    public BasicItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
    */

    /** "@ModelAttribute"의 또다른 기능 : 파라미터로 받을때 말고 사용하게 되는경우 지정한 이름으로 자동으로 "Model"에 담아준다.!!!
     -> 해당 Controller의 URL로 Controller가 호출될때 해당 "ModelAttribute"로 지정한 것들을 "Model"에 담아준다!!!!
     --> 해당 컨트롤러가 호출될때마다 생성하기때문에 미리 하나를 생성해서 가져다 쓰는게 더 효율적**/
    @ModelAttribute("regions")
    public Map<String , String > regions() {
        Map<String , String > regions= new LinkedHashMap<>();
        regions.put("SEOUL", "서울");
        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");
        return regions;
    }
    @ModelAttribute("itemTypes")
    public ItemType[] itemTypes() {
        return ItemType.values();
    }
    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes() {
        List<DeliveryCode> deliveryCodes = new ArrayList<>();
        deliveryCodes.add(new DeliveryCode("FAST", "빠른 배송"));
        deliveryCodes.add(new DeliveryCode("NORMAL", "일반 배송"));
        deliveryCodes.add(new DeliveryCode("SLOW", "느린 배송"));
        return deliveryCodes;
    }


    @GetMapping
    public String items(Model model) {
        List<Item> items =itemRepository.findAll();
        model.addAttribute("items", items);
        return "items/basic/items";
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
        return "items/basic/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "items/basic/addForm";
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
        return "items/basic/item";
    }
    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, Model model) {

        itemRepository.save(item);
//        model.addAttribute("item", item); //자동 추가 , 생략가능
        return "items/basic/item";
    }
    //@PostMapping("/add")
    public String addItemV3(Item item, Model model) {  //->객체니깐 "@Model~"적용 , 일반 자료형이면 "@Request~" 적용

        itemRepository.save(item);
//        model.addAttribute("item", item); //자동 추가 , 생략가능
        return "items/basic/item";
    }
    /**위의 버전들은 상품을 저장하고 새로고침을 하게 되면 계속적으로 등록된다!!!!! --> "redirect"로 "URL"을 다시 보내도록 한다. **/
    //@PostMapping("/add")
    public String addItemV4(Item item, Model model) {  //->객체니깐 "@Model~"적용 , 일반 자료형이면 "@Request~" 적용

        itemRepository.save(item);
        return "redirect:/basic/items/" +item.getId();
    }
    /** 저장이 잘 되었으면 상품 상세 화면에 "저장되었습니다"라는 메시지를 보여달라는 문구 기능 추가 -> "RedirectAttributes"사용
     --> pathVariable 바인딩: {itemId} + (나머지)쿼리 파라미터로 처리: "?status=true" ---> "URL: ~/basic/items/{itemId}?status=true" **/
    @PostMapping("/add")
    public String addItemV5(Item item, RedirectAttributes redirectAttributes) {  //->객체니깐 "@Model~"적용 , 일반 자료형이면 "@Request~" 적용

        log.info("add -> item.open={}", item.getOpen());
        log.info("add-> item.regions={} ", item.getRegions());
        log.info("add-> item.itemType={}", item.getItemType());
        log.info("add-> item.deliverCode={}", item.getDeliveryCode());


        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId",savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
    }



    @GetMapping("/{itemId}/edit")
    public String edit(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "items/basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable long itemId, @ModelAttribute Item item, Model model) {
        itemRepository.update(itemId, item);

        log.info("edit-> item.open={}", item.getOpen());
        log.info("edit-> item.regions={} ", item.getRegions());
        log.info("edit-> item.itemType={}", item.getItemType());
        log.info("add-> item.deliverCode={}", item.getDeliveryCode());

        return "redirect:/basic/items/{itemId}"; //"@PathVariable"의 값도 사용할수 있다!!!!
    }

}
