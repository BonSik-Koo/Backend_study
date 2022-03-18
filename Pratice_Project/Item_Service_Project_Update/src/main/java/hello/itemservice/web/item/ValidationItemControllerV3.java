package hello.itemservice.web.item;

import hello.itemservice.domain.item.DeliveryCode;
import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.ItemType;
import hello.itemservice.domain.item.check.SaveCheck;
import hello.itemservice.domain.item.check.UpdateCheck;
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


/** Basic 컨트롤러에 검증 로직 추가!! V3- Bean Validation , Bean groups 사용**/
@Controller
@RequestMapping("/basicV3/items")
@RequiredArgsConstructor //"final"이 붙은 생성자를 자동 생성후 의존관계 자동 주입(생성자가 하나면 @Autowired 가 자동 적용)!! ->lombok 기능
@Slf4j
public class ValidationItemControllerV3 {

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
        return "items/basicV3/items";
    }


    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "items/basicV3/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "items/basicV3/addForm";
    }

    /** "@Validated"가 붙은 객체를 Bean Validation 실행 -> 타입에러가 없는 한 **/
    //@PostMapping("/add")
    public String addItemV1(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {  //->객체니깐 "@Model~"적용 , 일반 자료형이면 "@Request~" 적용

        /** @BeanValidation 를 사용할 때 오브젝트 오류 같은 경우에는 기존의 "reject"방식을 권장한다.!!  **/
        if(item.getPrice()!=null && item.getQuantity()!=null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }
        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "items/basicV3/addForm";
        }
        //검증에 성공한 로직
        log.info("add -> item.open={}", item.getOpen());
        log.info("add-> item.regions={} ", item.getRegions());
        log.info("add-> item.itemType={}", item.getItemType());
        log.info("add-> item.deliverCode={}", item.getDeliveryCode());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId",savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basicV3/items/{itemId}";
    }

    /** "@Validated"가 붙은 객체를 Bean Validation 실행 -> 타입에러가 없는 한 , Bean groups 사용 **/
    @PostMapping("/add")
    public String addItemV2(@Validated(value= SaveCheck.class) @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {  //->객체니깐 "@Model~"적용 , 일반 자료형이면 "@Request~" 적용

        /** @BeanValidation 를 사용할 때 오브젝트 오류 같은 경우에는 기존의 "reject"방식을 권장한다.!!  **/
        if(item.getPrice()!=null && item.getQuantity()!=null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }
        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "items/basicV3/addForm";
        }
        //검증에 성공한 로직
        log.info("add -> item.open={}", item.getOpen());
        log.info("add-> item.regions={} ", item.getRegions());
        log.info("add-> item.itemType={}", item.getItemType());
        log.info("add-> item.deliverCode={}", item.getDeliveryCode());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId",savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basicV3/items/{itemId}";
    }


    @GetMapping("/{itemId}/edit")
    public String edit(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "items/basicV3/editForm";
    }

    /** "@Validated"가 붙은 객체를 Bean Validation 실행 **/
    //@PostMapping("/{itemId}/edit")
    public String editV1(@PathVariable long itemId, @Validated @ModelAttribute Item item, BindingResult bindingResult, Model model) {

        /** @BeanValidation 를 사용할 때 오브젝트 오류 같은 경우에는 기존의 "reject"방식을 권장한다.!!  **/
        if(item.getPrice()!=null && item.getQuantity()!=null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "items/basicV3/editForm";
        }

        itemRepository.update(itemId, item);

        log.info("edit-> item.open={}", item.getOpen());
        log.info("edit-> item.regions={} ", item.getRegions());
        log.info("edit-> item.itemType={}", item.getItemType());
        log.info("add-> item.deliverCode={}", item.getDeliveryCode());

        return "redirect:/basicV3/items/{itemId}"; //"@PathVariable"의 값도 사용할수 있다!!!!
    }

    /** "@Validated"가 붙은 객체를 Bean Validation 실행 , Bean groups 사용 **/
    @PostMapping("/{itemId}/edit")
    public String editV2(@PathVariable long itemId, @Validated(value= UpdateCheck.class) @ModelAttribute Item item, BindingResult bindingResult, Model model) {

        /** @BeanValidation 를 사용할 때 오브젝트 오류 같은 경우에는 기존의 "reject"방식을 권장한다.!!  **/
        if(item.getPrice()!=null && item.getQuantity()!=null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "items/basicV3/editForm";
        }

        itemRepository.update(itemId, item);

        log.info("edit-> item.open={}", item.getOpen());
        log.info("edit-> item.regions={} ", item.getRegions());
        log.info("edit-> item.itemType={}", item.getItemType());
        log.info("add-> item.deliverCode={}", item.getDeliveryCode());

        return "redirect:/basicV3/items/{itemId}"; //"@PathVariable"의 값도 사용할수 있다!!!!
    }
}
