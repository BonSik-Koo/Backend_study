package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    //@Rollback(value = false)
    public void 상품주문() throws Exception {
        //given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000 , 10);
        int orderCount = 2;

        //when
        Long orderId = orderService.Order(member.getId(), item.getId(), 2);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다.", 1, getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격* 수량이다.", 10000*orderCount, getOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 한다." , 8, item.getStockQuantity());

    }

    @Test()
    public void 상품주문_재고수량초과() throws Exception {
        //given
        Member member =createMember();
        Item item = createBook("시골JPA", 10000, 10);
        int orderCount =11;

        //when - then

        Assertions.assertThrows(NotEnoughStockException.class, () -> {
            orderService.Order(member.getId(), item.getId(), orderCount);
        });

    }

    @Test
    @Rollback(value = false)
    public void 상품취소() throws Exception {
        //given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000,10);
        int orderCount =2;

        Long orderId = orderService.Order(member.getId(), item.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        org.assertj.core.api.Assertions.assertThat(OrderStatus.CANCEL).isEqualTo(getOrder.getStatus());
        org.assertj.core.api.Assertions.assertThat(item.getStockQuantity()).isEqualTo(10);

    }



    private Member createMember() {
        Member member  = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가" , "123-123"));
        em.persist(member);
        return member;
    }
    private Book createBook(String name, int price, int stockQuantity){
        Book book  = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

}