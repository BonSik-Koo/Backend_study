package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.order.OrderRepository;
import jpabook.jpashop.repository.order.OrderSearch;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import jpabook.jpashop.service.OrderService;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;
    /**
     * [OneToMany 관계 최적화] -컬렉션
     */


    /**
     * V2. 엔티티를 조회해서 DTO 로 변환(fetch join 사용X)
     * - 트랜잭션 안에서 지연 로딩 필요 -> "N+1" 문제 발생!
     */
    @GetMapping("/api/v2/orders")
    public Result orderV2() {
        List<OrderDto> result = orderRepository.findAllByString(new OrderSearch())
                .stream().map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return new Result(result, result.size());
    }

    /**
     * V3. 엔티티를 조회해서 DTO 로 변환(fetch join 사용O)
     * - 페이징 시에는 N 부분을 포기해야함(대신에 batch fetch size? 옵션 주면 N -> 1 쿼리로 변경 가능) -> "distinct"를 써서 중복은 없앨수 있지만 "페이징"은 안됨!!!
     */
    @GetMapping("/api/v3/orders")
    public Result orderV3() {
        List<OrderDto> result = orderRepository.findAllWithItem()
                .stream().map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return new Result(result, result.size());
    }


    /**
     * V3.1 엔티티를 조회해서 DTO 로 변환 페이징 고려
     * - ToOne 관계만 우선 모두 페치 조인으로 최적화
     * - 컬렉션 관계는 hibernate.default_batch_fetch_size, @BatchSize로 최적화
     * => V3버전은 distinct 를사용해서 컬렉션을 페치조인은 했지만 "페이징"이 안되니 "V3.1"버전처럼 사용하자!!!!!
     */
    @GetMapping("/api/v3.1/orders")
    public Result orderV3_page(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit)  {

        //List<OrderDto> result = orderRepository.findAllWithMemberDelivery(offset,limit)
        List<OrderDto> result = orderRepository.findAllWithMemberDelivery_QueryDsl(offset,limit)
                .stream().map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return new Result(result, result.size());
    }




    /**
     * V4 JPA에서 DTO로 바로 조회, 컬렉션 N 조회 (1 + N Query)
     * 페이징 당연히 가능
     */
    @GetMapping("/api/v4/orders")
    public Result orderV4() {
        List<OrderQueryDto> result = orderQueryRepository.findOrderQueryDtos();
        return new Result(result, result.size());
    }

    /**
     * V5 - JPA에서 DTO로 바로 조회, 컬렉션 1 조회 최적화 버전 (1 + 1 Query)
     * 페이징 당연히 가능
     */
    @GetMapping("/api/v5/orders")
    public Result orderV5() {
        List<OrderQueryDto> result = orderQueryRepository.findOrderQueryDtos_optimication();
        return new Result(result,result.size());
    }



    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Result<T>{
        private T orders;
        private int count;
    }
    @Data
    static class OrderDto {
        private Long orderId;
        private String name; //주문자 이름
        private LocalDateTime orderDate; //주문 시간
        private OrderStatus orderStatus;
        private Address address; //배송 주소
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();  //프록시 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); //프록시 초기화
            orderItems = order.getOrderItems().stream() //프록시 초기화
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(Collectors.toList());
        }
    }
    @Data
    static class OrderItemDto {
        private String itemName; //상품명
        private int orderPrice; //주문 가격
        private int count; //주문 수량

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName(); //프록시 초기화
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }


    }


}
