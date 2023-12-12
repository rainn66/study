package jpql;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setAge(10);
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setAge(10);
            member2.setTeam(teamB);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setAge(10);
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            //String query = "select m from Member m join fetch m.team";
            String query = "select t from Team t";

            List<Team> resultList = em.createQuery(query, Team.class)
                    .setFirstResult(0)
                    .setMaxResults(2)
                    .getResultList();

            System.out.println("resultList = " + resultList.size());

            for (Team s : resultList) {
                System.out.println("s.getUsername() = " + s.getName() + ", " + s.getMembers().size());
                for (Member member: s.getMembers()) {
                    System.out.println("member = " + member);
                }
            }

            tx.commit(); //commit 하는 시점에 객체가 바뀐항목이 있으면 update 쿼리 날림
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();
    }

}
