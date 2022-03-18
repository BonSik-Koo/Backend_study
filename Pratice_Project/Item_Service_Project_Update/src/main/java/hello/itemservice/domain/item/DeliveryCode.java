package hello.itemservice.domain.item;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * FAST: 빠른 배송
 * NORMAL: 일반 배송
 * SLOW: 느린 배송
 */
@Data
@AllArgsConstructor //필드를 모드 포함한 생성자
public class DeliveryCode {
    private String code; //FAST, NORMAL..
    private String displayName; //빠른 배송, 일반 배송...

}
