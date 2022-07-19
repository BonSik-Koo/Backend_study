package jpabook.jpashop;

import jpabook.jpashop.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

//@RunWith(SpringRunner.class) //junit5 에서는 필요없다!!
@SpringBootTest
class JpaBasicTest {

    @Autowired EntityManager em;
//
//    @Test
//    @Transactional
//    //@Rollback(value = false)
//    public void test1() throws Exception {
//        //given
//
//        Team team  = new Team();
//        team.setName("TeamA");
//        //team.getMembers().add(member);
//        em.persist(team);
//
//        Member member = new Member();
//        member.setUsername("member1");
//        member.setTeam(team);
//        em.persist(member);
//
//       //team.getMembers().add(member); //객체 참조값에도 넣어준다. - DB 과는 무관
//    }
//
//    @Test
//    @Transactional
//    //@Rollback(value = false)
//    public void test2() throws Exception {
//
//        //given
//        Movie movie = new Movie();
//        movie.setName("temp");
//        movie.setPrice("2000");
//        movie.setAuthor("temp");
//        movie.setIsbn("temp");
//        em.persist(movie);
//
//        Movie movie1 = new Movie();
//        movie1.setName("temp2");
//        movie1.setPrice("20002");
//        movie1.setAuthor("temp2");
//        movie1.setIsbn("temp2");
//        em.persist(movie1);
//
//        //when
//        em.flush();
//        em.clear();
//
//        Movie findMovie = em.find(Movie.class, movie.getId());
//
//        System.out.println("===================");
//        System.out.println(findMovie);
//        System.out.println(findMovie.getPrice());
//        //then
//
//    }
//
//    @Test
//    @Transactional
//    //@Rollback(value = false)
//    public void test3() throws Exception {
//        //given
//        Team team = new Team();
//        team.setName("test22");
//
//        Member member  = new Member();
//        member.setUsername("test22");
//        member.setTeam(team);
//
//        em.persist(team);
//        //em.persist(member);
//        //team.getMembers().add(member);
//
//    }
//
//    @Test
//    @Transactional
//    @Rollback(value = false)
//    public void test4() throws Exception {
//
//        Team team = new Team();
//        team.setName("test1");
//        em.persist(team);
//
//        Member member = new Member();
//        member.setUsername("rnqhstlr");
//        member.setAddress(new Address("대구", "칠곡중앙대로"));
//        member.setTeam(team);
//
//        //em.persist(member);
//        team.getMembers().add(member);
//        em.persist(team);
//
//        em.flush();
//        em.clear();
//
//        //String sql  = "select t from Team t join fetch t.members";
//
//        String sql  = "select m from testMember m join  m.team";
//        List<Member> resultList = em.createQuery(sql, Member.class)
//                .getResultList();
//
//        System.out.println("===========================");
//        for (Member member1 : resultList) {
//            System.out.println(member1.getUsername());
//        }

//        em.flush();
//        em.clear();
//
//        System.out.println("-----------------");
//        Team team1 = em.find(Team.class, team.getId());
//
//        System.out.println("====================");
//        List<Member> members = team1.getMembers();
//        for (Member member1 : members) {
//            System.out.println(members.get(0).getUsername());
//
//        }

//        Member findMember = em.find(Member.class, member.getId());
//        Address findAddress = findMember.getAddress();
//        findMember.setAddress(new Address("부산",findAddress.getZipcode()));

//        @Test
//        @Transactional
//        @Rollback(value = false)
//        public void test5() throws Exception{
//
//            Member m1 =new Member();
//            m1.setName("member1");
//            em.persist(m1);
//
//            Member m2 =new Member();
//            m2.setName("member2");
//            em.persist(m2);
//
//            Team t1 = new Team();
//            t1.setName("team1");
//            t1.setMember(m1);
//            em.persist(t1);
//
//            Team t2 = new Team();
//            t2.setName("team2");
//            t2.setMember(m2);
//            em.persist(t2);
//
//            em.flush();
//            em.clear();
//
//            Team findTeam = em.find(Team.class, t1.getId());
//            Member findMember = findTeam.getMember();
//
//        }
}