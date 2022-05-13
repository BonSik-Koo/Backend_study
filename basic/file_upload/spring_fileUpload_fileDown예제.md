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
- DB에 저장할 Item 클래스 
- DB에 저장할때 첨부파일,이미지를 저장하는것이 아니라 서버에 저장된 경로를 저장하는 것이다.
-------------------------

__<UploadFile-업로드 파일 정보 보관>__
```
@Data
public class UploadFile {
 private String uploadFileName;
 private String storeFileName;
 public UploadFile(String uploadFileName, String storeFileName) {
 this.uploadFileName = uploadFileName;
 this.storeFileName = storeFileName;
 }
}
```
- uploadFileName : 고객이 업로드한 파일명
- storeFileName : 서버 내부에서 관리하는 파일명      
: 고객이 업로드한 파일명으로 서버 내부에 파일을 저장하면 안된다. 왜냐하면 서로 다른 고객이 같은 파일이름을 업로드 하는 경우 기존 파일 이름과 충돌이 날 수 있다. 서버에서는 저장할 파일명이 겹치지 않도록 내부에서 관리하는 별도의 파일명이 필요하다. 겹치지 않기 위해 "UUID"를 사용하여 파일명을 생성한다.!!!
------------------------------------

__<FileStore-파일 저장과 관련된 업무 처리>__
```
@Component
public class FileStore {

    @Value ("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename){
        return fileDir + filename;
    }

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();

        for(MultipartFile multipartFile : multipartFiles){
            storeFileResult.add(storeFile(multipartFile));
        }

        return storeFileResult;
    }

    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {

        if(multipartFile.isEmpty()){
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename(); //사용자가 지정한 파일 이름
        String storeFilename = createStoreFileName(originalFilename); //서버에 저장할 파일 이름 생성

        multipartFile.transferTo(new File(getFullPath(storeFilename))); //실제 서버 저장소에 저장

        return new UploadFile(originalFilename, storeFilename);
    }

    private String createStoreFileName(String originalFilename) { //서버에 저장할 파일 이름 생성 함수
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) { //".png" 확장자를 추출하는 함수
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
```
- 멀티파트 파일을 서버에 저장하는 역할을 담당한다. -> "@Repository"로 등록하는거와 같이 "스프링 빈"으로 등록하여 사용한다.!!!!    
- createStoreFileName() : 서버 내부에서 관리하는 파일명은 유일한 이름을 생성하는 UUID 를 사용해서 충돌하지 않도록 한다.
- extractExt() : 확장자를 별도로 추출해서 서버 내부에서 관리하는 파일명에도 붙여준다. 예를 들어서 고객이 a.png 라는 이름으로 업로드 하면 51041c62-86e4-4274-801d-614a7d994edb.png 와 같이 저장한다.
---------------------------------------------------

__<ItemForm-상품 저장용 폼(domain에 지정하는 Item은 DB에 저장할 폼이다, 추가로 저장용 폼과 수정용 폼을 분리시켜 정의하는것이 좋다)>__
```
@Data
public class ItemForm {
 private Long itemId;
 private String itemName;
 private List<MultipartFile> imageFiles;
 private MultipartFile attachFile;
}
```
- List<MultipartFile> imageFiles : 이미지를 다중 업로드 하기 위해 MultipartFile 를 사용했다.
- MultipartFile attachFile : 멀티파트는 @ModelAttribute 에서 사용할 수 있다.!!!
-----------------------------------------------------
    
__<ItemController>__
```
@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;
    private final FileStore fileStore;

    @GetMapping("/items/new")
    public String newItem(@ModelAttribute ItemForm form) {
        return "item-form";
    }

    @PostMapping("/items/new")
    public String newItem(@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes) throws IOException {

        UploadFile attachFile = fileStore.storeFile(form.getAttachFile());
        List<UploadFile> storeFiles = fileStore.storeFiles(form.getImageFiles());

        //데이터베이스에 저장 -> 여기선 메모리 -> 데이터베이스에는 파일의 경로만 저장한다.!!!!!! 그러니 item 도메인에 파일 부분의 변수도 경로만 저장하게 클래스를 지정한것이다.!!
        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setAttachFile(attachFile);
        item.setImageFiles(storeFiles);
        itemRepository.save(item);

        redirectAttributes.addAttribute("itemId", item.getId());
        return "redirect:/items/{itemId}";
    }

    @GetMapping("/items/{id}")
    public String items (@PathVariable Long id, Model model) {
        Item item = itemRepository.findById(id);
        model.addAttribute("item", item);
        return "item-view";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }

    @GetMapping("/attach/{itemId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long itemId) throws MalformedURLException {
        Item item = itemRepository.findById(itemId);
        String uploadFileName = item.getAttachFile().getUploadFileName();
        String storeFileName = item.getAttachFile().getStoreFileName();

        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));

        log.info("uploadFileName={}", uploadFileName);
        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

}
```
- @GetMapping("/items/new") : 등록 폼을 보여준다.
- @PostMapping("/items/new") : 폼의 데이터를 저장하고 보여주는 화면으로 redirect 한다.
- @GetMapping("/items/{id}") : 상품을 보여준다.
- @GetMapping("/images/{filename}") : <img> 태그로 이미지를 조회할 때 사용한다. UrlResource로 이미지 파일을 읽어서 @ResponseBody 로 이미지 바이너리를 반환한다. -> 웹 브라우저에 이미지가 보여지게 된다.
- @GetMapping("/attach/{itemId}") : 파일을 다운로드 할 때 실행한다. 예제를 더 단순화 할 수 있지만, 파일 다운로드 시 권한 체크같은 복잡한 상황까지 가정한다 생각하고 이미지 id 를 요청하도록 했다. 파일 다운로드시에는 고객이 업로드한 파일 이름으로 다운로드 하는게 좋다. 이때는 Content-Disposition 해더에 attachment; filename="업로드 파일명" 값을 주면 된다.         

※ @GetMapping("/images/{filename}"), @GetMapping("/attach/{itemId}") 은 HTML에서 타임리프를 이용하여 href, src를 사용하여 해당 URL를 호출하게 된다.
    
--------------------------------------------------------
    
__<등록 폼 뷰.html>__
```
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
 <meta charset="utf-8">
</head>
<body>
<div class="container">
 <div class="py-5 text-center">
 <h2>상품 등록</h2>
 </div>
 <form th:action method="post" enctype="multipart/form-data">
 <ul>
 <li>상품명 <input type="text" name="itemName"></li>
 <li>첨부파일<input type="file" name="attachFile" ></li>
 <li>이미지 파일들<input type="file" multiple="multiple"
name="imageFiles" ></li>
 </ul>
 <input type="submit"/>
 </form>
</div> <!-- /container -->
</body>
</html>
```
- 다중 파일 업로드를 하려면 multiple="multiple" 옵션을 주면 된다. ItemForm 의 다음 코드에서 여러 이미지 파일을 받을 수 있다.
private List<MultipartFile> imageFiles; (Controller에서)
----------------------------------
    
__<조회 뷰.html>__
```
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
 <meta charset="utf-8">
</head>
<body>
<div class="container">
 <div class="py-5 text-center">
 <h2>상품 조회</h2>
 </div>
 상품명: <span th:text="${item.itemName}">상품명</span><br/>
 첨부파일: <a th:if="${item.attachFile}" th:href="|/attach/${item.id}|" th:text="${item.getAttachFile().getUploadFileName()}" /><br/>
 <img th:each="imageFile : ${item.imageFiles}" th:src="|/images/$ {imageFile.getStoreFileName()}|" width="300" height="300"/>
</div> <!-- /container -->
</body>
</html>
```
- 첨부 파일은 링크로 걸어두고, 이미지는 <img> 태그를 반복해서 출력한다.
-----------------------------------

    
    
    
    

