import jpql.Address;
import jpql.Member;
import jpql.MemberDTO;
import jpql.Team;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
            em.persist(member);

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
                List<Member> result = em.createQuery("select m from Member m join Team  t on m.username = t.name", Member.class)
                        .getResultList();
                System.out.println("result size = "+result.size());
            */



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
