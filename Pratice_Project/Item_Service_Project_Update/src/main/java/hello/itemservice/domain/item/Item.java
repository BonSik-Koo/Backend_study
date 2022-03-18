package hello.itemservice.domain.item;

import hello.itemservice.domain.item.check.SaveCheck;
import hello.itemservice.domain.item.check.UpdateCheck;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * item 등록, 수정 폼 분리한 item 폼(V4) -> 검증을 여기서 하지않는다!!! 각 등록,수정 폼에서 --> 검증 애노테이션 주석 처리
 * 주석 해제시 V3 버전
 **/
@Data
public class Item {

    /** Bean Validation groups 사용 ***/
    //@NotNull(groups = UpdateCheck.class)
    private Long id; //상품 고유 아이디

    /** Bean Validation 사용 ***/
    //@NotBlank(groups= {SaveCheck.class, UpdateCheck.class}) //@NotBlank(message= "공백X") //default 메시지 설정도 가능
    private String itemName; //상품 이름

    //@NotNull(groups= {SaveCheck.class, UpdateCheck.class})
    //@Range(min=1000, max=1000000 , groups= {SaveCheck.class, UpdateCheck.class})
    private Integer price; //가격과 수량이 null들어갈수 있기 때문에 -> Integer자료형 사용

    //@NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    //@Max(value = 9999, groups = SaveCheck.class)
    private Integer quantity; //상품 수량

    private Boolean open; //판매 여부
    private List<String> regions; //등록지역
    private ItemType itemType; // 상품 타입
    private String deliveryCode; //배송 방식


    public Item() {
    }
    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
