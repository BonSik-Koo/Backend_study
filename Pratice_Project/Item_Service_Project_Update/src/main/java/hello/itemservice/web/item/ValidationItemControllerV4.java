package hello.itemservice.web.item;

import hello.itemservice.domain.item.*;
import hello.itemservice.web.item.form.ItemSaveForm;
import hello.itemservice.web.item.form.ItemUpdateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** item 의 등록폼 폼 , 수정 폼 객체 분리 V4버전!!!!**/
@Controller
@RequestMapping("/basicV4/items")
@RequiredArgsConstructor //"final"이 붙은 생성자를 자동 생성후 의존관계 자동 주입(생성자가 하나면 @Autowired 가 자동 적용)!! ->lombok 기능
@Slf4j
public class ValidationItemControllerV4 {

    private final ItemRepository itemRepository; //생성자가 하나기 때문에 @Autowired를 사용하지 않고도 자동 의존관계 주입됨

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
        return "items/basicV4/items";
    }


    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "items/basicV4/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "items/basicV4/addForm";
    }

    /** item 의 등록폼 폼 , 수정 폼 객체 분리 **/
    // "@ModelAttribute("item")" -> 뷰 템플릿에서 th:object=${item}으로 지정해놔서 그대로 사용하기 위해서 / 기본-> model 의 key 로 지정한 타입의 이름으로 저장됨
    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute("item") ItemSaveForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {  //->객체니깐 "@Model~"적용 , 일반 자료형이면 "@Request~" 적용

        /** @BeanValidation 를 사용할 때 오브젝트 오류 같은 경우에는 기존의 "reject"방식을 권장한다.!!  **/
        if(form.getPrice()!=null && form.getQuantity()!=null) {
            int resultPrice = form.getPrice() * form.getQuantity();
            if(resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "items/basicV4/addForm";
        }
        //검증에 성공한 로직
        log.info("add -> item.open={}", form.getOpen());
        log.info("add-> item.regions={} ", form.getRegions());
        log.info("add-> item.itemType={}", form.getItemType());
        log.info("add-> item.deliverCode={}", form.getDeliveryCode());

        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setPrice(form.getPrice());
        item.setQuantity(form.getQuantity());
        item.setItemType(form.getItemType());
        item.setDeliveryCode(form.getDeliveryCode());
        item.setOpen(form.getOpen());
        item.setRegions(form.getRegions());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId",savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basicV4/items/{itemId}";
    }


    @GetMapping("/{itemId}/edit")
    public String edit(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "items/basicV4/editForm";
    }

    /** item 의 등록폼 폼 , 수정 폼 객체 분리 **/
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable long itemId, @Validated @ModelAttribute("item") ItemUpdateForm form, BindingResult bindingResult, Model model) {

        /** @BeanValidation 를 사용할 때 오브젝트 오류 같은 경우에는 기존의 "reject"방식을 권장한다.!!  **/
        if(form.getPrice()!=null && form.getQuantity()!=null) {
            int resultPrice = form.getPrice() * form.getQuantity();
            if(resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "items/basicV4/editForm";
        }

        //검증 성공 로직
        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setPrice(form.getPrice());
        item.setQuantity(form.getQuantity());
        item.setItemType(form.getItemType());
        item.setDeliveryCode(form.getDeliveryCode());
        item.setOpen(form.getOpen());
        item.setRegions(form.getRegions());

        itemRepository.update(itemId, item);

        log.info("edit-> item.open={}", form.getOpen());
        log.info("edit-> item.regions={} ", form.getRegions());
        log.info("edit-> item.itemType={}", form.getItemType());
        log.info("add-> item.deliverCode={}", form.getDeliveryCode());

        return "redirect:/basicV4/items/{itemId}";
    }
}
