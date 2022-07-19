package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Category;
import jpabook.jpashop.domain.item.CategoryItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.CategoryRepository;
import jpabook.jpashop.repository.CategorySearch;
import jpabook.jpashop.service.ItemService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    /**
     * 구현해본것 - 카테고리
     */
    private final CategoryRepository categoryRepository;



    //@GetMapping("/items/new")
    public String createForm(@ModelAttribute("form") BookForm form) {
        return "items/createItemForm";
    }

    //@PostMapping("/items/new")
    public String create(@ModelAttribute BookForm form) {

        Book book = Book.createBook(form.getName(), form.getPrice(), form.getStockQuantity(), form.getAuthor(), form.getIsbn());
        itemService.saveItem(book);

        return "redirect:/";
    }
    /**
     * 구현해본것 - 카테고리
     */
    @GetMapping("/items/new")
    public String createFormV2(@ModelAttribute("form") BookFormV2 form, Model model) {
        List<Category> findCategorys = categoryRepository.findAll();
        model.addAttribute("categorys", findCategorys);

        return "items/createItemFormV2";
    }
    @PostMapping("/items/new")
    public String createV2(@ModelAttribute BookFormV2 form) {

        Category findCategory = categoryRepository.findOne(form.getCategoryId());
        CategoryItem categoryItem = CategoryItem.createCategoryItem(findCategory);

        Book book = Book.createBookV2(form.getName(), form.getPrice(), form.getStockQuantity(), form.getAuthor(), form.getIsbn(),categoryItem);
        itemService.saveItem(book);

        return "redirect:/";
    }


    /**
     * 여기서도 원래는 엔티티를 그대로 보내면 안된다.!! "DTO"로 API 만들기!! -> 여기서는 서버사이드 랜더링 하는거니깐!!
     */
    //@GetMapping("/items")
    public String list(Model model) {
        List<Item> items =itemService.findAll();
        model.addAttribute("items", items);
        return "items/itemList";
    }
    /**
     * 구현해본것 - 카테고리
     */
    @GetMapping("/items")
    public String listV2(@ModelAttribute("CategorySearch") CategorySearch categorySearch, Model model) {
        List<Item> items =itemService.findByString(categorySearch);
        model.addAttribute("items", items);

        List<Category> findCategorys = categoryRepository.findAll();
        model.addAttribute("categorys", findCategorys);

        return "items/itemListV2";
    }



    @GetMapping("items/{itemId}/edit")
    public String updateItem(@PathVariable("itemId") Long itemId, Model model) {

        Book item = (Book)itemService.findOne(itemId);

        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    @PostMapping("items/{itemId}/edit")
    public String update(@ModelAttribute("form")BookForm form) {

        itemService.updateItem(form.getId(), form.getName(),form.getPrice(), form.getStockQuantity(),form.getAuthor(),form.getIsbn());
        return "redirect:/items";
    }
}
