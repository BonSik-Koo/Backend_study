package hello.itemservice.web.item;

import hello.itemservice.domain.item.DeliveryCode;
import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.ItemType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

/** Basic 컨트롤러에 검증 로직 추가!! V1 **/
@Controller
@RequestMapping("/basicV1/items")
@RequiredArgsConstructor //"final"이 붙은 생성자를 자동 생성후 의존관계 자동 주입!! ->lombok 기능
@Slf4j
public class ValidationItemControllerV1 {

    private final ItemRepository itemRepository;

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
        return "items/basicV1/items";
    }


    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "items/basicV1/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "items/basicV1/addForm";
    }


    @PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item, RedirectAttributes redirectAttributes, Model model) {  //->객체니깐 "@Model~"적용 , 일반 자료형이면 "@Request~" 적용

        //검증 오류 결과를 보관
        Map<String , String> errors = new HashMap<>();

        //특정 필드 검증 로직
        if(!StringUtils.hasText(item.getItemName())) { // item.getItemName()==null
            errors.put("itemName", "상품 이름은 필수 입니다.");
        }
        if(item.getPrice()==null || item.getPrice()<100 || item.getPrice()>1000000) {
            errors.put("price", "가격은 1,000 ~ 1,000,000 까지 허용합니다");
        }
        if(item.getQuantity()==null || item.getQuantity()>=9999) {
            errors.put("quantity", "수량은 최대 9,999 까지 허용합니다");
        }
        //특정 필드가 아닌 복합 룰 검증
        if(item.getPrice()!=null && item.getQuantity()!=null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000) {
                errors.put("globalError", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재값:"+resultPrice+"원");
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if(!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "items/basicV1/addForm";
        }


        //검증에 성공한 로직
        log.info("add -> item.open={}", item.getOpen());
        log.info("add-> item.regions={} ", item.getRegions());
        log.info("add-> item.itemType={}", item.getItemType());
        log.info("add-> item.deliverCode={}", item.getDeliveryCode());


        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId",savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basicV1/items/{itemId}";
    }


    @GetMapping("/{itemId}/edit")
    public String edit(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "items/basicV1/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable long itemId, @ModelAttribute Item item, Model model) {
        itemRepository.update(itemId, item);

        log.info("edit-> item.open={}", item.getOpen());
        log.info("edit-> item.regions={} ", item.getRegions());
        log.info("edit-> item.itemType={}", item.getItemType());
        log.info("add-> item.deliverCode={}", item.getDeliveryCode());

        return "redirect:/basicV1/items/{itemId}"; //"@PathVariable"의 값도 사용할수 있다!!!!
    }

}
