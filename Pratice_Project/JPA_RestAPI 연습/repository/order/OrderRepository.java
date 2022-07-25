package jpabook.jpashop.repository.order;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    /**
     * QueryDSL 로 바꾸기 나중에!!
     */
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인
        List<Predicate> criteria = new ArrayList<>();
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"),
                    orderSearch.getOrderStatus());
            criteria.add(status);
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("name"), "%" +
                            orderSearch.getMemberName() + "%");
            criteria.add(name);
        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); //최대 1000건
        return query.getResultList();

    }
    /**
     * JPQL로 처리
     */
    public List<Order> findAllByString(OrderSearch orderSearch) {
        String jpql = "select o from Order o join o.member m";
        boolean isFirstCondition = true;

        //주문 상태 검색 - ORDER, CANCEL
        if(orderSearch.getOrderStatus()!= null) {
            if(isFirstCondition) {
                jpql += " where";
                isFirstCondition =false;
            }else {
                jpql += " and";
            }
            jpql +=" o.status = :status";
        }

        //회원 이름 검색
        if(StringUtils.hasText(orderSearch.getMemberName())) {
            if(isFirstCondition){
                jpql += " where";
                isFirstCondition =false;
            }else {
                jpql +=" and";
            }
            jpql += " m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class);

        if(orderSearch.getOrderStatus() !=null) {
            query.setParameter("status", orderSearch.getOrderStatus());
        }
        if(StringUtils.hasText(orderSearch.getMemberName())) {
            query.setParameter("name", orderSearch.getMemberName());
        }

        return query.getResultList();
    }

    /**
     * QueryDsl 로 작성
     */
    public List<Order> findAll(OrderSearch orderSearch) {
//        QOrder order = QOrder.order;
//        QMember member = QMember.member;
        JPAQueryFactory query = new JPAQueryFactory(em); //이것도 그냥 EntityManager 주입받을때(생성자) 같이 넣어주면 생략가능.!

        return query.select(QOrder.order)
                .from(QOrder.order)
                .join(QOrder.order.member , QMember.member)
                .where(statusEq(orderSearch.getOrderStatus()), nameLike(orderSearch.getMemberName()))
                .limit(1000)
                .fetch();
    }

    private BooleanExpression statusEq(OrderStatus statusCond) {
        if(statusCond==null)
            return null;
        return QOrder.order.status.eq(statusCond);
    }
    private BooleanExpression nameLike(String nameCond) {
        if(!StringUtils.hasText(nameCond))
            return null;
        return QMember.member.name.like(nameCond);
    }





    /**
     * API - fetch join 사용
     */
    public List<Order> findAllWithMemberDelivery() {
        String sql = "select o from Order o join fetch o.member m join fetch o.delivery d";
        List<Order> result = em.createQuery(sql, Order.class)
                .getResultList();
        return result;
    }
    public List<Order> findAllwithMemberDelivery_QueryDsl() {
        JPAQueryFactory query = new JPAQueryFactory(em); //이것도 그냥 EntityManager 주입받을때(생성자) 같이 넣어주면 생략가능.!
        return query.select(QOrder.order)
                .from(QOrder.order)
                .join(QOrder.order.member, QMember.member)
                .fetchJoin()
                .join(QOrder.order.delivery, QDelivery.delivery)
                .fetchJoin()
                .fetch();
    }



    public List<Order> findAllWithMemberDelivery(int offset, int limit) { //페이징!!!
        String sql = "select o from Order o join fetch o.member m join fetch o.delivery d";
        List<Order> result = em.createQuery(sql, Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
        return result;
    }
    public List<Order> findAllWithMemberDelivery_QueryDsl(int offset , int limit) {
        JPAQueryFactory query = new JPAQueryFactory(em); //이것도 그냥 EntityManager 주입받을때(생성자) 같이 넣어주면 생략가능.!

        return query.select(QOrder.order)
                .from(QOrder.order)
                .join(QOrder.order.member, QMember.member)
                .fetchJoin()
                .join(QOrder.order.delivery, QDelivery.delivery)
                .fetchJoin()
                .offset(offset)
                .limit(limit)
                .fetch();
    }


    public List<Order> findAllWithItem() {
        /**
         * distinct 기능
         * 1. 쿼리문에 직접 넣어준다. -> DB에 입장에서는 모든 데이터가 같아야 중복을 없애주는데 해당 경우에는 order의 필드가 같고 orderItem이 다르니깐 중복제거가 안된다.
         * 2. JPA가 쿼리의 결과로 "Order"의 객체에서 같은 id(Order의 id)가 같은건 날려준다. -> 중복 제거가 된다.!!
         */
        String sql = "select distinct o from Order o join fetch o.member m join fetch o.delivery d join fetch o.orderItems oi join fetch oi.item i";
        return em.createQuery(sql, Order.class)
                .getResultList();
    }
}
