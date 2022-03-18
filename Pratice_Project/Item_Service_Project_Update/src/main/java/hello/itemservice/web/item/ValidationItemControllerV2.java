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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

/** Basic 컨트롤러에 검증 로직 추가!! V2-BindingResult **/
@Controller
@RequestMapping("/basicV2/items")
@RequiredArgsConstructor //"final"이 붙은 생성자를 자동 생성후 의존관계 자동 주입(생성자가 하나면 @Autowired 가 자동 적용)!! ->lombok 기능
@Slf4j
public class ValidationItemControllerV2 {


    private final ItemRepository itemRepository; //생성자가 하나기 때문에 @Autowired를 사용하지 않고도 자동 의존관계 주입됨
    private final ItemValidator itemValidator; //생성자가 하나기 때문에 @Autowired를 사용하지 않고도 자동 의존관계 주입됨


    @InitBinder
    public void init(WebDataBinder dataBinder) {
        log.info("init binder {} ", dataBinder);
        dataBinder.addValidators(itemValidator);
    }

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
        return "items/basicV2/items";
    }


    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "items/basicV2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "items/basicV2/addForm";
    }

    /**
     * "BindingResult"에 "objectName"의 ModelAttribute의 변수이름(Model에 자동으로 들어가는이름)을 적게 되면 자동으로 Model("item")에 error로 들어가게 된다!!!
       -> "@ModelAttribute의 이름과 같게 적는다. 해당 애노테이션이 자동적으로 Model에 담아주는 기능도 포함되어있으니
     * "FieldError"으로 저장한 것은 model 에서 꺼내는 것과 마찬가지로 타임리프에서 "th:errors"를 사용해서 error를 저장한 "필드이름"으로 꺼내면 된다.
     * "ObjectError"으로 저장한 것은 타임리프에서 "globalerrors"를 사용해서 꺼낼수 있다.
     **/
    //첫번째 생성자 ->fieldError
    //@PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult,  RedirectAttributes redirectAttributes, Model model) {  //->객체니깐 "@Model~"적용 , 일반 자료형이면 "@Request~" 적용

        //특정 필드 검증 로직
        if(!StringUtils.hasText(item.getItemName())) { // item.getItemName()==null
            bindingResult.addError(new FieldError("item","itemName","상품 이름은 필수 입니다."));
        }
        if(item.getPrice()==null || item.getPrice()<100 || item.getPrice()>1000000) {
            bindingResult.addError(new FieldError("item", "price", "가격은 1,000 ~ 1,000,000 까지 허용합니다"));
        }
        if(item.getQuantity()==null || item.getQuantity()>=9999) {
            bindingResult.addError(new FieldError("item", "quantity",  "수량은 최대 9,999 까지 허용합니다"));
        }

        //특정 필드가 아닌 복합 룰 검증
        if(item.getPrice()!=null && item.getQuantity()!=null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item","가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재값:"+resultPrice+"원" ));
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "items/basicV2/addForm";
        }

        //검증에 성공한 로직
        log.info("add -> item.open={}", item.getOpen());
        log.info("add-> item.regions={} ", item.getRegions());
        log.info("add-> item.itemType={}", item.getItemType());
        log.info("add-> item.deliverCode={}", item.getDeliveryCode());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId",savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basicV2/items/{itemId}";
    }

    //두번째 생성자 ->fieldError
    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult,  RedirectAttributes redirectAttributes, Model model) {  //->객체니깐 "@Model~"적용 , 일반 자료형이면 "@Request~" 적용

        //특정 필드 검증 로직
        if(!StringUtils.hasText(item.getItemName())) { // item.getItemName()==null
            bindingResult.addError(new FieldError("item","itemName",item.getItemName(), false, null, null,  "상품 이름은 필수 입니다."));
        }
        if(item.getPrice()==null || item.getPrice()<100 || item.getPrice()>1000000) {
            bindingResult.addError(new FieldError("item", "price",item.getPrice(), false, null, null,  "가격은 1,000 ~ 1,000,000 까지 허용합니다"));
        }
        if(item.getQuantity()==null || item.getQuantity()>=9999) {
            bindingResult.addError(new FieldError("item", "quantity",item.getQuantity(), false, null, null, "수량은 최대 9,999 까지 허용합니다"));
        }
        //특정 필드가 아닌 복합 룰 검증
        if(item.getPrice()!=null && item.getQuantity()!=null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item",null,null,"가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재값:"+resultPrice+"원" ));
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "items/basicV2/addForm";
        }

        //검증에 성공한 로직
        log.info("add -> item.open={}", item.getOpen());
        log.info("add-> item.regions={} ", item.getRegions());
        log.info("add-> item.itemType={}", item.getItemType());
        log.info("add-> item.deliverCode={}", item.getDeliveryCode());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId",savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basicV2/items/{itemId}";
    }

    //오류메시지 코드화!
    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult,  RedirectAttributes redirectAttributes, Model model) {  //->객체니깐 "@Model~"적용 , 일반 자료형이면 "@Request~" 적용

        //특정 필드 검증 로직
        if(!StringUtils.hasText(item.getItemName())) { // item.getItemName()==null
            bindingResult.addError(new FieldError("item","itemName",item.getItemName(), false,
                    new String[]{"required.item.itemName"}, null,  null));
        }
        if(item.getPrice()==null || item.getPrice()<100 || item.getPrice()>1000000) {
            bindingResult.addError(new FieldError("item", "price",item.getPrice(), false,
                   new String[]{"range.item.price"}, new Object[]{1000,1000000},  null));
        }
        if(item.getQuantity()==null || item.getQuantity()>=9999) {
            bindingResult.addError(new FieldError("item", "quantity",item.getQuantity(), false,
                    new String[]{"max.item.quantity"}, new Object[]{9999}, null));
        }
        //특정 필드가 아닌 복합 룰 검증
        if(item.getPrice()!=null && item.getQuantity()!=null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item",new String[]{"totalPriceMin"},
                        new Object[]{10000, resultPrice },null));
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "items/basicV2/addForm";
        }

        //검증에 성공한 로직
        log.info("add -> item.open={}", item.getOpen());
        log.info("add-> item.regions={} ", item.getRegions());
        log.info("add-> item.itemType={}", item.getItemType());
        log.info("add-> item.deliverCode={}", item.getDeliveryCode());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId",savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basicV2/items/{itemId}";
    }

    //BindingResult 가 제공하는 rejectValue() , reject() 사용 ->FieldError, ObjectError 사용 xxx
    // 위의 두 메소드에서 자동으로 "fieldError"를 만들어준다.!!!
    //@PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult,  RedirectAttributes redirectAttributes, Model model) {  //->객체니깐 "@Model~"적용 , 일반 자료형이면 "@Request~" 적용

        //타입 에러(스프링 내부)처리 -> 필드 에러를 포함하여 2개의 오류메시지가 발생하는것을 방지
        if(bindingResult.hasErrors()) {
            return "items/basicV2/addForm";
        }

        //특정 필드 검증 로직
        if(!StringUtils.hasText(item.getItemName())) { // -> String의 null검사를 하지않아도 되는 유틸리티 클래스 / item.getItemName()==null
            bindingResult.rejectValue("itemName", "required");
        }
        if(item.getPrice()==null || item.getPrice()<100 || item.getPrice()>1000000) {
            bindingResult.rejectValue("price", "range",new Object[]{1000,1000000}, null);
        }
        if(item.getQuantity()==null || item.getQuantity()>=9999) {
            bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);
        }
        //특정 필드가 아닌 복합 룰 검증
        if(item.getPrice()!=null && item.getQuantity()!=null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "items/basicV2/addForm";
        }

        //검증에 성공한 로직
        log.info("add -> item.open={}", item.getOpen());
        log.info("add-> item.regions={} ", item.getRegions());
        log.info("add-> item.itemType={}", item.getItemType());
        log.info("add-> item.deliverCode={}", item.getDeliveryCode());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId",savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basicV2/items/{itemId}";
    }

    //검증 로직을 분리한것! ->스프링이 제공하는 Validator 인터페이스 구현
    //@PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item, BindingResult bindingResult,  RedirectAttributes redirectAttributes, Model model) {  //->객체니깐 "@Model~"적용 , 일반 자료형이면 "@Request~" 적용

        //타입 에러(스프링 내부)처리 -> 필드 에러를 포함하여 2개의 오류메시지가 발생하는것을 방지
        if(bindingResult.hasErrors()) {
            return "items/basicV2/addForm";
        }

        itemValidator.validate(item, bindingResult); //검증 로직 호출!

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "items/basicV2/addForm";
        }

        //검증에 성공한 로직
        log.info("add -> item.open={}", item.getOpen());
        log.info("add-> item.regions={} ", item.getRegions());
        log.info("add-> item.itemType={}", item.getItemType());
        log.info("add-> item.deliverCode={}", item.getDeliveryCode());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId",savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basicV2/items/{itemId}";
    }

    //스프링이 제공하는 Validator 인터페이스 구현 + @Validated 애노테이션 사용
    @PostMapping("/add")
    public String addItemV6(@Validated  @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {  //->객체니깐 "@Model~"적용 , 일반 자료형이면 "@Request~" 적용

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "items/basicV2/addForm";
        }

        //검증에 성공한 로직
        log.info("add -> item.open={}", item.getOpen());
        log.info("add-> item.regions={} ", item.getRegions());
        log.info("add-> item.itemType={}", item.getItemType());
        log.info("add-> item.deliverCode={}", item.getDeliveryCode());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId",savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basicV2/items/{itemId}";
    }


    @GetMapping("/{itemId}/edit")
    public String edit(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "items/basicV2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable long itemId, @ModelAttribute Item item, Model model) {
        itemRepository.update(itemId, item);

        log.info("edit-> item.open={}", item.getOpen());
        log.info("edit-> item.regions={} ", item.getRegions());
        log.info("edit-> item.itemType={}", item.getItemType());
        log.info("add-> item.deliverCode={}", item.getDeliveryCode());

        return "redirect:/items/basicV2/items/{itemId}"; //"@PathVariable"의 값도 사용할수 있다!!!!
    }

}
