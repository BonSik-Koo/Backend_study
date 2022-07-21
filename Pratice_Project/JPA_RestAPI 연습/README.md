🔎 RestAPI 개발 기본
==========================
* 회원 등록 API
    <details>
    <summary>@PostMapping("/api/v2/members")</summary>

    ![image](https://user-images.githubusercontent.com/96917871/180197438-79daef42-bd34-4aa4-99cd-b217430a904d.png)
    </details>

* 회원 수정 API
    <details>
    <summary>@PutMapping("/api/v2/members/{id}")</summary>

    ![image](https://user-images.githubusercontent.com/96917871/180197737-b1fdab09-b30d-42b7-a80b-c6ac13be72f3.png)
    </details>

* 회원 조회 API
    <details>
    <summary>@GetMapping("/api/v2/members")</summary>

    ![image](https://user-images.githubusercontent.com/96917871/180197913-f2e1b4df-f513-4ca3-ab46-96333fcde8df.png)
    </details>
    
    
🔎 RestAPI 개발 고급
====================================================================
① 지연로딩과 조회 성능 최적화("xToOne(ManyToOne, OneToOne)" 관계 최적화)
-----------------------------------------------------------------------------

* 주문 조회 V1 - 엔티티를 조회해서 DTO로 반환(fetch join 사용x)
  > 지연연로딩으로 쿼리 N번 호출(DTO 를 생성할때 지연로딩으로된 객체의 값을 사용하는 시점에서 쿼리문 호출) !! => "N+1"문제 여전히 존재
  > 
  > JPQL 결과로 order 가 2개 반환 => 1(JPQL) + order(member(2) + delivery(2)) => "총 5번 쿼리문 발생" (N+1문제 발생)!!!
   <details>
    <summary> @GetMapping("/api/v2/simple-orders")</summary>

    ![image](https://user-images.githubusercontent.com/96917871/180198940-9a30f4eb-3d08-42d4-91cb-f3a3051d8848.png)
    </details>
    
* 주문 조회 V2 - 엔티티를 조회해서 DTO 로 변환(fetch join 사용O)
  > fetch join 으로 쿼리 1번 호출(필요한 데이터들을 아니깐 해당 데이터들만 뽑는 "join fetch" 쿼리문 작성해서 사용하기 => "N+1" 문제 해결
   <details>
    <summary> @GetMapping("/api/v3/simple-orders")</summary>
    
    ```
     public List<Order> findAllWithMemberDelivery() {
        String sql = "select o from Order o join fetch o.member m join fetch o.delivery d";
        List<Order> result = em.createQuery(sql, Order.class)
                .getResultList();
        return result;
    }
    ```
    
    ![image](https://user-images.githubusercontent.com/96917871/180200730-a8d109f4-cad0-4e9c-b3b0-c143489ebeb8.png)
    </details>
    
* 주문 조회 V3 - 엔티티를 조회해서 JPA 결과로 DTO객체 바로 변환
  > * 쿼리 1번 호출
  > * "select 절에서 원하는 데이터만 선택해서 조회"
  > 
  > -> API 스펙에 최적화된 레포지토리 기능을 하게 된다.! -> 패키지를 따로 만드는것이 좋다.!
  > 
  > => "패치조인"으로 해결되지 않은 "성능최적화"를 조금 해결할수 있다.!
   <details>
    <summary> @GetMapping("/api/v4/simple-orders")</summary>
    
    ```
    public List<OrderSimpleQueryDto> findOrderDtos() {
        String sql = "select new jpabook.jpashop.repository.order.simpleQuery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                " from Order o join o.member m join o.delivery d";

        return em.createQuery(sql, OrderSimpleQueryDto.class)
                .getResultList();
    }
    ```
    
    ![image](https://user-images.githubusercontent.com/96917871/180200839-dba36eb7-b353-4e8c-913e-ffe4c0eb8bed.png)
    </details>

② 컬렉션 조회 성능 최적화("OneToMany" 관계 최적화)
-----------------------------------------------------------------------------
* 주문 조회 V1 - 엔티티를 조회해서 DTO 로 변환(fetch join 사용X)
  > 트랜잭션 안에서 지연 로딩 필요 -> "N+1" 문제 발생!
   <details>
    <summary>@GetMapping("/api/v2/orders")</summary>
    
    ```
    {
    "orders": [
        {
            "orderId": 1,
            "name": "useA",
            "orderDate": "2022-07-21T19:53:13.231747",
            "orderStatus": "ORDER",
            "address": {
                "city": "서울",
                "street": "1",
                "zipcode": "111"
            },
            "orderItems": [
                {
                    "itemName": "JPA1 BOOK",
                    "orderPrice": 10000,
                    "count": 1
                },
                {
                    "itemName": "JPA2 BOOK",
                    "orderPrice": 20000,
                    "count": 2
                }
            ]
        },
        {
            "orderId": 2,
            "name": "userB",
            "orderDate": "2022-07-21T19:53:13.309542",
            "orderStatus": "ORDER",
            "address": {
                "city": "진주",
                "street": "2",
                "zipcode": "2222"
            },
            "orderItems": [
                {
                    "itemName": "SPRING1 BOOK",
                    "orderPrice": 20000,
                    "count": 3
                },
                {
                    "itemName": "SPRING2 BOOK",
                    "orderPrice": 40000,
                    "count": 4
                }
            ]
        }
      ],
      "count": 2
    }
    ```
    </details>
    
* 주문 조회 V2 - 엔티티를 조회해서 DTO 로 변환(fetch join 사용 o)
  > * "페치조인"으로 SQL문이 한번만 실행됨
  > * distinct를 사용한 이유는 "1:N"조인이므로 DB의 결과의 row가 증가한다. 그 결과 order엔티티는 2개지만 orderItem이 4개 이므로 4개가 된다.
  > * **JPA의 distinct는 SQL에 distinct를 추가하고, 더해서 같은 엔티티가 조회되면, 애플리케이션에서 중복을 걸러준다. 이 예에서 order가 컬렉션 페치 조인 때문에 중복 조회 되는 것을 막아준다**
  > * **페이징 기능을 사용할수 없다.!!** , 컬렉션 폐치조인을 사용하면 페이징이 불가능하다(데이터가 뻥튀기 되니). 하이버네이트는 경로 로그를 남기고 모든 데이터를 DB에 읽고 메모리차원에서 페이징을 한다.(매우 위험)
  
   <details>
    <summary>@GetMapping("/api/v3/orders")</summary>
    
    ```
    public List<Order> findAllWithItem() {
        /**
         * distinct 기능
         * 1. 쿼리문에 직접 넣어준다. -> DB에 입장에서는 모든 데이터가 같아야 중복을 없애주는데 해당 경우에는 order의 필드가 같고 orderItem이 다르니깐 중복제거가 안된다.
         * 2. JPA가 쿼리의 결과로 "Order"의 객체에서 같은 id(Order의 id)가 같은건 날려준다. -> 중복 제거가 된다.!!
         */
        String sql = "select distinct o from Order o join fetch o.member m join fetch o.delivery d join fetch o.orderItems oi join fetch oi.item i";
        return em.createQuery(sql, Order.class)
                .getResultList();
    }
    ```
    
   ```
   {
    "orders": [
        {
            "orderId": 1,
            "name": "useA",
            "orderDate": "2022-07-21T19:53:13.231747",
            "orderStatus": "ORDER",
            "address": {
                "city": "서울",
                "street": "1",
                "zipcode": "111"
            },
            "orderItems": [
                {
                    "itemName": "JPA1 BOOK",
                    "orderPrice": 10000,
                    "count": 1
                },
                {
                    "itemName": "JPA2 BOOK",
                    "orderPrice": 20000,
                    "count": 2
                }
            ]
        },
        {
            "orderId": 2,
            "name": "userB",
            "orderDate": "2022-07-21T19:53:13.309542",
            "orderStatus": "ORDER",
            "address": {
                "city": "진주",
                "street": "2",
                "zipcode": "2222"
            },
            "orderItems": [
                {
                    "itemName": "SPRING1 BOOK",
                    "orderPrice": 20000,
                    "count": 3
                },
                {
                    "itemName": "SPRING2 BOOK",
                    "orderPrice": 40000,
                    "count": 4
                }
            ]
        }
      ],
     "count": 2
   }
   
   ```
    </details>


* 주문 조회 V3 - 컬랙션 조인시 페이징 한계 극복
  * 컬렉션을 페치 조인하면 페이징이 불가능하다.
    > 일다대에서 일(1)을 기준으로 페이징을 하는 것이 목적이다. 그런데 데이터는 다(N)를 기준으로 row가 생성된다
  * 이 경우 하이버네이트는 경고 로그를 남기고 모든 DB 데이터를 읽어서 메모리에서 페이징을 시도한다. 최악의 경우 장애로 이어질 수 있다.
  * **해결법❗**
    > 1. 먼저 ToOne(OneToOne, ManyToOne) 관계를 모두 페치조인 한다. ToOne 관계는 row수를 증가시키지 않으므로 페이징 쿼리에 영향을 주지 않음
    > 2. 컬렉션은 지연 로딩으로 조회한다.(패치조인하지 말기!)
    > 3. **지연 로딩 성능 최적화를 위해 hibernate.default_batch_fetch_size , @BatchSize 를 적용한다.** , 이 옵션을 사용하면 컬렉션이나, 프록시 객체를 한꺼번에 설정한 size 만큼 IN 쿼리로 조회한다.
    > ![image](https://user-images.githubusercontent.com/96917871/180218800-64395b0f-1714-45be-be5a-a4a4f2d9aac5.png)
  
   * **결과❗**
     > * 쿼리 호출수가 "1+N"에서 "1+1"로 최적화 된다. -> 1+N문제를 완전히 해결하는게 아니다 최적화를 
     > * 컬렉션 페치 조인은 페이징이 불가능하지만 이 방법은 가능하다.
       
   <details>
    <summary>@GetMapping("/api/v3.1/orders")</summary>
    
    ```
    public List<Order> findAllWithMemberDelivery(int offset, int limit) { //페이징!!!
        String sql = "select o from Order o join fetch o.member m join fetch o.delivery d";
        List<Order> result = em.createQuery(sql, Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
        return result;
    }
    ```
    
    ```
    {
    "orders": [
        {
            "orderId": 1,
            "name": "useA",
            "orderDate": "2022-07-21T19:53:13.231747",
            "orderStatus": "ORDER",
            "address": {
                "city": "서울",
                "street": "1",
                "zipcode": "111"
            },
            "orderItems": [
                {
                    "itemName": "JPA1 BOOK",
                    "orderPrice": 10000,
                    "count": 1
                },
                {
                    "itemName": "JPA2 BOOK",
                    "orderPrice": 20000,
                    "count": 2
                }
            ]
        },
        {
            "orderId": 2,
            "name": "userB",
            "orderDate": "2022-07-21T19:53:13.309542",
            "orderStatus": "ORDER",
            "address": {
                "city": "진주",
                "street": "2",
                "zipcode": "2222"
            },
            "orderItems": [
                {
                    "itemName": "SPRING1 BOOK",
                    "orderPrice": 20000,
                    "count": 3
                },
                {
                    "itemName": "SPRING2 BOOK",
                    "orderPrice": 40000,
                    "count": 4
                }
            ]
        }
      ],
      "count": 2
    }
    ```
    </details>


* 주문 조회 V4 - JPA에서 DTO로 바로 조회, 컬랙션 N 조회(1+N쿼리) , 페이징 가능
  * <details>
    <summary>Dto</summary>
       
    ```
    public class OrderItemQueryDto {

        @JsonIgnore
        private Long id; //V5에서 직접 map 매핑할때 사용

        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemQueryDto(Long id, String itemName, int orderPrice, int count) {
         this.id = id;
         this.itemName = itemName;
         this.orderPrice = orderPrice;
         this.count = count;
        }
    } 
    ```
    ```
    public class OrderQueryDto {

        private Long orderId;
        private String name; //주문 고객 이름
        private LocalDateTime orderDate; //주문 시간
        private OrderStatus orderStatus;
        private Address address;
         private List<OrderItemQueryDto> orderItems;

        public OrderQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
         }
    } 
    ```
    </details>
       
   * Repository
     > 1. 1:N(컬렉션을 제외한) 관계를 join을 통해 바로 Dto객체로 생성
     > 2. 1:N 관계인 orderItem을 item과 join을 통해 가져온다.
     > order가 2개이고(각 다른고객) 각 order마다 orderitem이 2개이기 때문에 "컬렉션 2번 쿼리문 발생
     *  <details>
        <summary>repository</summary>
       
        ```
        /**
        * 컬렉션은 별도로 조회
        * Query: 루트 1번, 컬렉션 N 번 쿼리문이 실행된다.!!
        * <단건 조회에서 많이 사용하는 방식></단건>
        */
        public List<OrderQueryDto> findOrderQueryDtos() {
            List<OrderQueryDto> result = findOrders();
            result.stream()
                    .forEach(o -> {
                        List<OrderItemQueryDto> orderItem = findOrderItem(o.getOrderId()); // 루프를 도며 "orderItem"은 여러개니깐 직접 넣어준다.
                        o.setOrderItems(orderItem);
                    } );
            return result;
        }
        /**
        * 1:N 관계인 orderItems 조회
        */
        private List<OrderItemQueryDto> findOrderItem(Long orderId) {
            String sql = "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id,i.name, oi.orderPrice, oi.count)" +
                    " from OrderItem oi " +
                    " join oi.item i" +
                    " where oi.order.id = :orderId";

          return em.createQuery(sql, OrderItemQueryDto.class)
                  .setParameter("orderId", orderId)
                  .getResultList();
        }
    
        /**
         * 1:N 관계(컬렉션)를 제외한 나머지를 한번에 조회
        */
        public List<OrderQueryDto> findOrders() { //1:N 관계인 "OrderItem" 리스트이므로 바로 생성자(OrderQueryDto)에 넣을수 없다.
             String sql = "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id,m.name,o.orderDate,o.status,d.address)" +
                " from Order o join o.member m join o.delivery d";

             return em.createQuery(sql, OrderQueryDto.class)
                .getResultList();
             }
        ```
        </details>
       
    * Http Reponse body
      
      <details>
      <summary>@GetMapping("/api/v4/orders")</summary>
          
      ```
      {
        "orders": [
            {
            "orderId": 1,
            "name": "useA",
            "orderDate": "2022-07-21T19:53:13.231747",
            "orderStatus": "ORDER",
            "address": {
                "city": "서울",
                "street": "1",
                "zipcode": "111"
            },
            "orderItems": [
                {
                    "itemName": "JPA1 BOOK",
                    "orderPrice": 10000,
                    "count": 1
                },
                {
                    "itemName": "JPA2 BOOK",
                    "orderPrice": 20000,
                    "count": 2
                }
            ]
          },
         {
            "orderId": 2,
            "name": "userB",
            "orderDate": "2022-07-21T19:53:13.309542",
            "orderStatus": "ORDER",
            "address": {
                "city": "진주",
                "street": "2",
                "zipcode": "2222"
            },
            "orderItems": [
                {
                    "itemName": "SPRING1 BOOK",
                    "orderPrice": 20000,
                    "count": 3
                },
                {
                    "itemName": "SPRING2 BOOK",
                    "orderPrice": 40000,
                    "count": 4
                }
            ]
         }
         ],
         "count": 2
         }
      ```
      </details>
       

       
       
