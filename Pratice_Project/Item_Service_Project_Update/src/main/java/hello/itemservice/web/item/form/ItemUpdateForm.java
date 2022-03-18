package hello.itemservice.web.item.form;

import hello.itemservice.domain.item.ItemType;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ItemUpdateForm {

    @NotNull
    private Long id;

    @NotBlank
    private String itemName;

    @NotNull
    @Range(min=1000, max =1000000)
    private Integer price;

    //수정시에는 수량은 자유롭게 변경할 수 있다.
    private Integer quantity;

    private Boolean open; //판매 여부
    private List<String> regions; //등록지역
    private ItemType itemType; // 상품 타입
    private String deliveryCode; //배송 방식
}
