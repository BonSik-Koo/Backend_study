package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;
import java.util.List;

/**
 * 구현해본것 - 카테고리
 */
@Repository
@Transactional
@RequiredArgsConstructor
public class CategoryRepository {

    private final EntityManager em;

    public void saveCategory(Category category){
        em.persist(category);
    }

    public Category findOne(Long categoryId) {
        Category findCategory = em.find(Category.class, categoryId);
        return findCategory;
    }

    public List<Category> findAll() {
        return em.createQuery("select c from Category c")
                     .getResultList();
    }
}
