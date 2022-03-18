package hello.itemservice.web.item;

import hello.itemservice.domain.item.Item;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/** 스프링이 제공해주는 "Validator"인터페이스 사용!! + 스프링 빈으로 등록하여 싱글톤 유지및 재사용 가능 **/
@Component
public class ItemValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz); //????????????????????????????????????????????????????????
    }

    @Override
    public void validate(Object target, Errors errors) { //실제 검증하는 메소드
        Item item = (Item)target;

        //타입 에러(스프링 내부)처리 -> 필드 에러를 포함하여 2개의 오류메시지가 발생하는것을 방지
        if(errors.hasErrors()) {
            return ;
        }

        //특정 필드 검증 로직
        if(!StringUtils.hasText(item.getItemName())) { // -> String의 null검사를 하지않아도 되는 유틸리티 클래스 / item.getItemName()==null
            errors.rejectValue("itemName", "required");
        }
        if(item.getPrice()==null || item.getPrice()<100 || item.getPrice()>1000000) {
            errors.rejectValue("price", "range",new Object[]{1000,1000000}, null);
        }
        if(item.getQuantity()==null || item.getQuantity()>=9999) {
            errors.rejectValue("quantity", "max", new Object[]{9999}, null);
        }
        //특정 필드가 아닌 복합 룰 검증
        if(item.getPrice()!=null && item.getQuantity()!=null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000) {
                errors.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

    }
}
