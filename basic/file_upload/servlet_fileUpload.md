__servlet를 사용해서 Html form으로 부터 입력받은 파일 출력해보기__
-------------------------------------------------------

```
-HTML-
<form th:action method="post" enctype="multipart/form-data">
 <ul>
 <li>상품명 <input type="text" name="itemName"></li>
 <li>파일<input type="file" name="file" ></li>
 </ul>
 <input type="submit"/>
 </form>
 ```
 
