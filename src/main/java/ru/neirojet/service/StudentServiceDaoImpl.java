package ru.neirojet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.neirojet.domain.Student;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.List;

/**
 * Created by icetusk on 09.08.16.
 */
@Service
@Profile("jpadao")
public class StudentServiceDaoImpl implements StudentService {


    StudentFactory factory;

    @PersistenceUnit
    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManagerFactory emf;

    @Override
    public List<Student> findAll() {

        EntityManager em = emf.createEntityManager();

        return em.createQuery("from Student", Student.class).getResultList();

    }

    @Override
    public Student findByName(String name) {
        EntityManager em = emf.createEntityManager();

        return (Student) em.createQuery("from Student s where s.name like :name")
                .setParameter("name", "%"+name+"%")
                .getSingleResult();
    }

    @Override
    public Student findById(Integer id) {
        EntityManager em = emf.createEntityManager();

        return em.find(Student.class, id);

    }

    @Override
    public Student saveStudent(Student s) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Student savedStudent = em.merge(s);
        em.getTransaction().commit();
        return savedStudent;
    }

    @Override
    public void delete(Integer id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.remove(em.find(Student.class, id));
        em.getTransaction().commit();

    }

    @Override
    public Student createStudent() {

        return factory.createStudent();
    }

    @Autowired
    @Qualifier("studentFactoryFileImpl")
    @Override
    public void setStudentFactory(StudentFactory factory) {
        this.factory = factory;
    }
}
