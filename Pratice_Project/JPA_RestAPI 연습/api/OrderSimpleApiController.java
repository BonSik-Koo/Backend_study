package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.order.simpleQuery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simpleQuery.OrderSimpleQueryRepository;
import jpabook.jpashop.repository.order.OrderRepository;
import jpabook.jpashop.repository.order.OrderSearch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    /**
     * [xToOne(ManyToOne, OneToOne) 관계 최적화]
     * Order -> Member
     * Order -> Delivery
     *
     */


    /**
     * V2. 엔티티를 조회해서 DTO 로 변환(fetch join 사용X)
     * 단점: 지연로딩으로 쿼리 N번 호출(DTO 를 생성할때 지연로딩으로된 객체의 값을 사용하는 시점에서 쿼리문 호출) !! => "N+1"문제 여전히 존재
     * -> JPQL 결과로 order 가 2개 반환 => 1(JPQL) + order(member(2) + delivery(2)) => "총 5번 쿼리문 발생" (N+1문제 발생)!!!
     */
    @GetMapping("/api/v2/simple-orders")
    public Result ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<Object> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return new Result(result, result.size());
    }

    /**
     * V3. 엔티티를 조회해서 DTO 로 변환(fetch join 사용O) -> 결!"최적화" , "N+1"문제 해!!
     * -> fetch join 으로 쿼리 1번 호출(필요한 데이터들을 아니깐 해당 데이터들만 뽑는 "join fetch" 쿼리문 작성해서 사용하기
    */
    @GetMapping("/api/v3/simple-orders")
    public Result orderV3() {
        //List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<Order> orders = orderRepository.findAllwithMemberDelivery_QueryDsl();

        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return new Result(result, result.size());
    }


    /**
     * V4. JPA 에서 DTO 로 바로 조회
     * - 쿼리 1번 호출
     * - "select 절에서 원하는 데이터만 선택해서 조회"
     * -> API 스펙에 최적화된 레포지토리 기능을 하게 된다.! -> 패키지를 따로 만드는것이 좋다.!
     * => "패치조인"으로 해결되지 않은 "성능최적화"를 조금 해결할수 있다.!
     */
    @GetMapping("/api/v4/simple-orders")
    public Result orderV4() {
        List<OrderSimpleQueryDto> result = orderSimpleQueryRepository.findOrderDtos();
        return new Result(result, result.size());
    }



    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Result<T> { //마찬가지로 객체로 감싸서 반환하는것이 좋다.!!
        private T orders;
        private int ordersCount;
    }
    @Data
    static class SimpleOrderDto {

        private Long oderId;
        private String name;
        private LocalDateTime orderDate; //주문 시간
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            oderId = order.getId();
            name = order.getMember().getName(); //LAZY 가 초기화 되면서 쿼리문을 발생시킨다.
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); //LAZY 가 초기화 되면서 쿼리문을 발생시킨다.
        }
    }

}
