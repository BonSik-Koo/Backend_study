package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.CategorySearch;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }


    @Transactional
    public void updateItem(Long id, String name, int price, int stockQuantity, String author, String isbn) {
        Book findBook = (Book) itemRepository.findOne(id); //book만 있다는 가정
        findBook.changeItem(name, price,stockQuantity,author,isbn);

    }

    public Item findOne(Long id){
        return itemRepository.findOne(id);
    }

    public List<Item> findAll(){
        return itemRepository.findAll();
    }


    /**
     * 구현해본것 - 카테고리
     */
    public List<Item> findByString(CategorySearch categorySearch) {
        return itemRepository.findByString(categorySearch);
    }

}
