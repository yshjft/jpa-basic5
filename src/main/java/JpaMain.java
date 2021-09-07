import jpql.Member;

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
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(24);
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
