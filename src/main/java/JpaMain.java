import jpql.*;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(24);
            member.changeTeam(team);
            member.setMemberType(MemberType.ADMIN);
            em.persist(member);

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setAge(20);
            member2.changeTeam(team);
            em.persist(member2);

            /*  (TypedQuery & query)
                TypedQuery<Member> query1 = em.createQuery("select m from Member m ", Member.class);
                TypedQuery<String> query2 = em.createQuery("select m.username from Member m ", String.class);
                Query query3 = em.createQuery("select m.username, m.age from Member m ", Member.class); // 반환 타입이 명확하지 않을 때 사용
            */
            
            /*  (getSingleResult)
                // 결과가 하나, 진짜 결과가 하나일 떄만 사용해야하 한다. 아니면 Exception 발생한다
                TypedQuery<Member> query4 = em.createQuery("select m from Member m where m.id = 1", Member.class);
                Member result = query4.getSingleResult();
                System.out.println("result =" + result);
            */

            /*  (getResultList)
                // 결과가 하나 이상일 때, 리스트 반환
                TypedQuery<Member> query5 = em.createQuery("select m from Member m ", Member.class);
                List<Member> resultList = query5.getResultList();
                for(Member member1 : resultList) {
                    System.out.println("member1 = " + member1);
                }
            */

            /*  (파라미터 바인딩)
                Member singleResult = em.createQuery("select m from Member m where m.username = :username", Member.class)
                        .setParameter("username", "member1")
                        .getSingleResult();
                System.out.println(singleResult.getUsername());
            */

            /*  (프로젝션)
                // 영속성 컨텍스트에서 관리됨
                em.createQuery("select m from Member m", Member.class).getResultList();
                em.createQuery("select o.address from Order o", Address.class).getResultList();

                // 여러 값 조회 : Object[] 타입으로 조회
                // List<Object[]> resultList = em.createQuery("select m.username, m.age from Member m")
                //         .getResultList();
                // Object[] result = resultList.get(0);

                // 패키지명 다 써줘야합니다.
                List<MemberDTO> result = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
                        .getResultList();
                MemberDTO memberDTO = result.get(0);
                System.out.println("username = "+memberDTO.getUsername());
                System.out.println("age = "+memberDTO.getAge());
            */

            /*  (페이징)
                for(int i = 0; i < 100; i++) {
                    Member member = new Member();
                    member.setUsername("member"+i);
                    member.setAge(i);
                    em.persist(member);
                }

                List<Member> result = em.createQuery("select m from Member m order by m.age desc", Member.class)
                        .setFirstResult(0)
                        .setMaxResults(10)
                        .getResultList();

                System.out.println("result = "+result.size());
                for(Member m : result) {
                    System.out.println("member = "+ m);
                }
            */

            /* (조인)
                List<Member> result = em.createQuery("select m from Member m join Team  t on m.username = t.name", M ember.class)
                        .getResultList();
                System.out.println("result size = "+result.size());
            */

            /*  (jpql 타입 표현)
                String query = "select m.username, 'HELLO', true From Member m " +
                                "where m.memberType = :userType";
                List<Object[]> result = em.createQuery(query)
                        .setParameter("userType", MemberType.ADMIN)
                        .getResultList();

                for(Object[] objects : result) {
                    System.out.println("objects = "+ objects[0]);
                    System.out.println("objects = "+ objects[1]);
                    System.out.println("objects = "+ objects[2]);
                }
            */

            /*  (조건식)
                String query = "select " +
                        "case when m.age <= 10 then '학생요금' " +
                        "     when m.age >= 60 then '경로요금' " +
                        "     else '일반요금'" +
                        "end "+
                        "from Member m";
                List<String> result = em.createQuery(query).getResultList();
                for(String s : result) {
                    System.out.println("s = "+s);
                }

                String query2 = "select coalesce(m.username, '이름 없는 회원') from Member m";
                List<String> result2 = em.createQuery(query2, String.class).getResultList();
                for(String s : result2) {
                    System.out.println("s = "+s);
                }

                String query3 = "select NULLIF(m.username, '관리자') from Member m";
                List<String> result3 = em.createQuery(query3, String.class).getResultList();
                for(String s : result3) {
                    System.out.println("s = "+s);
                }
            */

            /*  (jpql 함수)
                // String query = "select concat('a', 'b') from Member m";
                String query = "select substring(m.username, 2, 3) from Member m";
                // String query = "select locate('de', 'abcdegf') from Member m"; // Integer.class

                List<String> result = em.createQuery(query, String.class).getResultList();
                for(String s : result) {
                    System.out.println("s = "+s);
                }
            */

            String query = "select m.team from Member m";
            List<Team> result = em.createQuery(query, Team.class).getResultList();
            for(Team s : result) {
                System.out.println("s = "+s);
            }

            String query2 = "select t.members.size from Team t";
            Integer result2 = em.createQuery(query2, Integer.class).getSingleResult();
            System.out.println("result2 = "+result2);

            String query3 = "select t.members from Team t";
            Collection result3 = em.createQuery(query3, Collection.class).getResultList();
            System.out.println("result3 = "+result3);

            String query4 = "select m.username from Team t join t.members m";
            Collection result4 = em.createQuery(query4, Collection.class).getResultList();
            System.out.println("result4 = "+result4);


            tx.commit();
        }catch(Exception e) {
            System.out.println(e.getMessage());
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }
}
