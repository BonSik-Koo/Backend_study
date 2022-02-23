package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//DB를 사용하지 않고 그냥 메모리 저장소 사용
@Repository
public class ItemRepository {

    /**
     * static -> 객체를 생성해도 해당 변수는 하나만 선정
     * 추가로 멀티 스레드 환경에서 공유자원에 동시에 접근 할수 있기 때문에 "HashMap" 이 아니라 "ConcurrentHashMap"사용해야 된다.
     */
    private  static final Map<Long, Item> store = new HashMap<>();
    private static long sequence = 0L; //static

    public Item save(Item item) {
        item.setId(sequence++);
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id){
        return store.get(id);
    }

    public List<Item> findAll() {
        return new ArrayList<>(store.values()); //한번 감싸서 반환하게 되면 "List"의 값을 변동시켜도 실제 저장소인 "store"에는 영향이 없으니
    }

    public void update(Long itemId, Item updateParam) {
        Item findItem = findById(itemId);

        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    public void clearStore() {
        store.clear();
    }

}
