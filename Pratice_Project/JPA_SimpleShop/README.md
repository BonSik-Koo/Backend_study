<Simple_Shop>
========================
✏ 기술 연습
------------------------------
- CRUD, 회원가입(중복회원), 회원 목록
- 상품등록, 상품수정, 상품 재고 관리,  카테고리별 상품 조회(목록)
- 주문, 주문 취소(상품 재고 관리), 회원&주문상태 별 조회(목록)

🛠 사용기술 & 개발 환경
----------------------
- JPA(엔티티 매핑, 연관관계 매핑, 상속관계 매핑등), JPQL, Spring MVC, thymeleaf, Spring Vaild, Mysql, SpringBoot

📝 DB설계
------------------
* DB 설계
  > <img src="https://user-images.githubusercontent.com/96917871/179701248-e0d0b8dd-02c4-4731-99a3-add9633c9ef5.png" width="700" height="600"/>

* 엔티티 매핑
  > * 아래 사진과는 다르게 Item-Category사이에  Category_Item을 두어 "1:N", "N:1"로 풀어냈음 
  > ![image](https://user-images.githubusercontent.com/96917871/179701680-2fd118b6-b257-4e37-afeb-522bc464b1f1.png) 

  

* 엔티티 매핑
 


📖 View 소개
--------------------
* Home
  > <img src = "https://user-images.githubusercontent.com/96917871/179693618-7870d110-ae5b-4e43-a411-663e6ca40789.png" width="300" hgight="300"/>
  
* 회원
  * 회원등록
    > <img src = "https://user-images.githubusercontent.com/96917871/179693877-eeca7816-ee40-4d40-ab1d-3652289718a7.png" width="300" hgight="300"/>
  
  * 회원 목록
  * > <img src = "https://user-images.githubusercontent.com/96917871/179694006-0061aac3-e7d6-4c66-b91c-b3c4f91f1018.png" width ="500" height="300"/>

* 상품
  * 상품 등록
    > * 간단히 Item(Book,Movie,Album)중 Book의 필드로 입력받게 하였음
    >  <img src = "https://user-images.githubusercontent.com/96917871/179694553-f0180553-69e1-48b4-a860-e218e552ef1b.png" width="300" height="200"/>

  * 상품 목록
    > * 카테고리별 목록 검색(Book,Movie,Album으로 간단히 설정)
    > ![image](https://user-images.githubusercontent.com/96917871/179695442-b049fd6b-856e-4492-85c6-532aa4b50a48.png)

* 주문
  * 상품 주문
    * 정상적인 경우(상품 재고가 있는경우), 주문수량 만큼 삼품 재고 감소
      > ![image](https://user-images.githubusercontent.com/96917871/179695943-f009ed0b-5553-4ee1-b8d1-470d98281293.png)

    * 비정상적인 경우(상품 재고가 부족한 경우)
      > ![image](https://user-images.githubusercontent.com/96917871/179696095-215bfd63-8d13-49f5-9f0d-9aeab467f179.png)
      
    
  * 상품 목록
    * 카테고리(회원이름, 주문상태)별 주문 목록
      > ![image](https://user-images.githubusercontent.com/96917871/179696849-e983fa46-dd45-447f-9965-49cad1f24ba5.png)

    * 주문 취소, 삼품 재고 주문수량 만큼 증가
      > <img src = "https://user-images.githubusercontent.com/96917871/179697071-b8570c6c-fc08-4918-85aa-a15c354bd242.png" width="300" height="300"/>


   


 




