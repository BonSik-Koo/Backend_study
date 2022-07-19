package jpabook.jpashop.domain.item;


import jpabook.jpashop.domain.OrderItem;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class CategoryItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="category_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="item_id")
    private Item items;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category categorys;

    //==생성 메서드==//
    public static CategoryItem createCategoryItem(Category category) {
        CategoryItem categoryItem = new CategoryItem();
        categoryItem.setCategorys(category);

        return categoryItem;
    }
}
