package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookFormV2 {

    private Long id;

    private String name;
    private int price;
    private int stockQuantity;
    private String author;
    private String isbn;

    /**
     * 카테고리 구현해본 것
     */
    private Long categoryId;

}
