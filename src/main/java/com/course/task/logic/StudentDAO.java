package com.course.task.logic;

import org.hibernate.Session;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class StudentDAO implements AbstractDAOStudent {

	private ConnectionManager cm;

	StudentDAO(String connection) {
		cm = new ConnectionManager(connection);
	}

	StudentDAO() {
		cm = new ConnectionManager();
	}

	public void insert(StudentDTO student) throws DAOException {
		
		try {
			Session session = cm.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			session.save(student);
			session.getTransaction().commit();
		} catch(Exception e) {
			throw new DAOException("Error in DAOStudent insert method!", e);
		}
		
	}
	
	public void remove(StudentDTO student) throws DAOException {
		
		try {
			Session session = cm.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			if (student != null) {
				session.delete(student);
			}
			session.getTransaction().commit();
		} catch(Exception  e) {
			throw new DAOException("Error in DAOStudent remove method!", e);
		}

	}
	
	public void remove(long studentId) throws DAOException {
		
		try {
			Session session = cm.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			StudentDTO student = session.get(StudentDTO.class, studentId);
			if (student != null) {
				session.delete(student);
			}
			session.getTransaction().commit();
		} catch(Exception e) {
			throw new DAOException("Error in DAOStudent remove method!", e);
		}

	}
	
	public void update(StudentDTO student) throws DAOException {

		try {
			Session session = cm.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			if (student != null) {
				session.update(student);
			}
			session.getTransaction().commit();
		} catch(Exception e) {
			throw new DAOException("Error in DAOStudent update method!", e);
		}

	}
	
	public List<StudentDTO> getAll() throws DAOException {

		try {
			Session session = cm.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			List<StudentDTO> list = (List<StudentDTO>) session.createQuery("FROM StudentDTO").list();
			session.getTransaction().commit();
			return list;
		} catch (Exception e) {
			throw new DAOException("Error in DAOStudent getAll method!", e);
		}
		
	}
	
	public StudentDTO getStudentById(long id) throws DAOException {

		try {
			Session session = cm.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			StudentDTO student = (StudentDTO) session.createQuery("FROM StudentDTO student WHERE student.id = " + id)
					.uniqueResult();
			session.getTransaction().commit();
			return student;
		} catch (Exception e) {
			throw new DAOException("Error in DAOStudent getStudentById method!", e);
		}

	}
	
	public List<StudentDTO> getAllStudentsWhoStudySubject(long subjectId) throws DAOException {

		try {
			Session session = cm.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			List<StudentDTO> list = new ArrayList<>();
			List<Object[]> objects = (List<Object[]>) session.createQuery("SELECT student.id, student.firstName, " +
					"student.lastName FROM StudentDTO student, LearningSubjectsDTO LS WHERE LS.subjectId " +
					"= " + subjectId +" AND LS.studentId = student.id").list();
			for (Object[] object : objects) {
				long id = (long) object[0];
				String firstName = (String) object[1];
				String lastName = (String) object[2];
				StudentDTO student = new StudentDTO(id, firstName, lastName);
				list.add(student);
			}
			session.getTransaction().commit();
			return list;
		} catch (Exception e) {
			throw new DAOException("Error in DAOStudent getAllStudentsWhoStudySubject method!", e);
		}
		
	}
	
	public void assignSubjectForStudent(long studentId, long subjectId) throws DAOException {

		try {
			Session session = cm.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			LearningSubjectsDTO learningSubject = new LearningSubjectsDTO(studentId, subjectId);
			session.save(learningSubject);
			session.getTransaction().commit();
		} catch(Exception e) {
			throw new DAOException("Error in DAOStudent assignSubjectForStudent method!", e);
		}

	}
	
}