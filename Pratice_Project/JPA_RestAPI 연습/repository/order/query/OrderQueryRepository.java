package jpabook.jpashop.repository.order.query;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpabook.jpashop.domain.QDelivery;
import jpabook.jpashop.domain.QMember;
import jpabook.jpashop.domain.QOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    /**
     * 컬렉션은 별도로 조회
     * Query: 루트 1번, 컬렉션 N 번 쿼리문이 실행된다.!!
     * <단건 조회에서 많이 사용하는 방식></단건>
     */
    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> result = findOrders_QueryDsl();
        result.stream()
                .forEach(o -> {
                    List<OrderItemQueryDto> orderItem = findOrderItem(o.getOrderId()); // 루프를 도며 "orderItem"은 여러개니깐 직접 넣어준다.
                    o.setOrderItems(orderItem);
                } );
        return result;
    }
    /**
     * 1:N 관계인 orderItems 조회
     */
    private List<OrderItemQueryDto> findOrderItem(Long orderId) {
        String sql = "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id,i.name, oi.orderPrice, oi.count)" +
                " from OrderItem oi " +
                " join oi.item i" +
                " where oi.order.id = :orderId";

        return em.createQuery(sql, OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }
    /**
    * 1:N 관계(컬렉션)를 제외한 나머지를 한번에 조회
    */
    public List<OrderQueryDto> findOrders() { //1:N 관계인 "OrderItem" 리스트이므로 바로 생성자(OrderQueryDto)에 넣을수 없다.
        String sql = "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id,m.name,o.orderDate,o.status,d.address)" +
                " from Order o join o.member m join o.delivery d";

        return em.createQuery(sql, OrderQueryDto.class)
                .getResultList();
    }
    public List<OrderQueryDto> findOrders_QueryDsl() {
        JPAQueryFactory query = new JPAQueryFactory(em);
        return query.select(Projections.constructor(OrderQueryDto.class,
                    QOrder.order.id, QMember.member.name, QOrder.order.orderDate, QOrder.order.status, QDelivery.delivery.address
                ))
                .from(QOrder.order)
                .join(QOrder.order.member, QMember.member)
                .join(QOrder.order.delivery, QDelivery.delivery)
                .fetch();
    }


    /**
     * 최적화
     * Query: 루트 1번, 컬렉션 1번 쿼리문 -> 특정조건으로 찾지 않는다.
     * <데이터를 한꺼번에 처리할 때 많이 사용하는 방식></데이터를>
     *
     */
    public List<OrderQueryDto> findOrderQueryDtos_optimication() {
        List<OrderQueryDto> result = findOrders();

        List<Long> orderIds = toOrderIds(result);

        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(orderIds);

        result.stream()
                .forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));
        return result;
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems = em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                                " from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(OrderItemQueryDto::getId));
        return orderItemMap;
    }

    private List<Long> toOrderIds(List<OrderQueryDto> result) {
        List<Long> orderIds = result.stream()
                .map(o -> o.getOrderId())
                .collect(Collectors.toList());
        return orderIds;
    }

}
