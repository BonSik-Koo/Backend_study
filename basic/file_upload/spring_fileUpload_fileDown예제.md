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
    
    

