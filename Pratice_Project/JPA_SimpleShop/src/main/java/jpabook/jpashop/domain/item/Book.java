package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@DiscriminatorValue("B")
public class Book extends Item {

    private String author;
    private String isbn;

    //==생성 메서드==//
    public static Book createBook(String name, int price, int stockQuantity, String author, String isbn) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        book.setAuthor(author);
        book.setIsbn(isbn);

        return book;
    }
    /**
     * 구현해본것 - 카테고리
     */
    public static Book createBookV2(String name, int price, int stockQuantity, String author, String isbn, CategoryItem... categoryItems) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        book.setAuthor(author);
        book.setIsbn(isbn);

        for(CategoryItem categoryItem:categoryItems) {
            book.setCategoryItems(categoryItem);
        }

        return book;
    }



    //==비지니스 로직==//
    public void changeItem(String name, int price, int stockQuantity, String author, String isbn) {
        this.setName(name);
        this.setPrice(price);
        this.setStockQuantity(stockQuantity);
        this.setAuthor(author);
        this.setIsbn(isbn);
    }

}
