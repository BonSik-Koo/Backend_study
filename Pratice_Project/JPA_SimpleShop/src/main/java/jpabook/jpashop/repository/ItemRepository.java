package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        em.persist(item);
    }

    public Item findOne(Long id) {
        return em.find(Item.class,id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i",Item.class)
                .getResultList();
    }


    /**
     * 구현해본것 - 카테고리
     */
    public List<Item> findByString(CategorySearch categorySearch) {
        String sql = "select i.items from CategoryItem i join i.categorys c";

        if(categorySearch.getId()!=null) {
            sql += " where c.id = :id";
        }

        TypedQuery<Item> query = em.createQuery(sql, Item.class);

        if(categorySearch.getId()!=null){
            log.info("sql={}", sql);
            log.info("categoryId ={}", categorySearch.getId());

            query.setParameter("id", categorySearch.getId());
        }

        List<Item> resultList = query.getResultList();
        return resultList;
    }
}
