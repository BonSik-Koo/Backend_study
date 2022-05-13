__spring을 이용한 파일업로드, 다운로드 예제__
=========================================

__<Item-상품 도메인>__
```
@Data
public class Item {

    private Long id;
    private String itemName;
    private UploadFile attachFile;
    private List<UploadFile> imageFiles;
}
```


