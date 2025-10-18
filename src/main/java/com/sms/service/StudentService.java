package com.sms.service;

import com.sms.dto.StudentDTO;
import com.sms.entity.Student;
import com.sms.exception.ResourceNotFoundException;
import com.sms.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {
    
    private final StudentRepository studentRepository;
    
    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public StudentDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        return convertToDTO(student);
    }
    
    public StudentDTO createStudent(StudentDTO studentDTO) {
        if (studentRepository.existsByEmail(studentDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (studentRepository.existsByRollNumber(studentDTO.getRollNumber())) {
            throw new RuntimeException("Roll number already exists");
        }
        
        Student student = convertToEntity(studentDTO);
        Student savedStudent = studentRepository.save(student);
        return convertToDTO(savedStudent);
    }
    
    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        
        existingStudent.setFirstName(studentDTO.getFirstName());
        existingStudent.setLastName(studentDTO.getLastName());
        existingStudent.setEmail(studentDTO.getEmail());
        existingStudent.setDateOfBirth(studentDTO.getDateOfBirth());
        existingStudent.setPhone(studentDTO.getPhone());
        existingStudent.setDepartment(studentDTO.getDepartment());
        existingStudent.setYear(studentDTO.getYear());
        existingStudent.setRollNumber(studentDTO.getRollNumber());
        
        Student updatedStudent = studentRepository.save(existingStudent);
        return convertToDTO(updatedStudent);
    }
    
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }
    
    public List<StudentDTO> getStudentsByDepartment(String department) {
        return studentRepository.findByDepartment(department).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<StudentDTO> getStudentsByYear(Integer year) {
        return studentRepository.findByYear(year).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private StudentDTO convertToDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setEmail(student.getEmail());
        dto.setDateOfBirth(student.getDateOfBirth());
        dto.setPhone(student.getPhone());
        dto.setDepartment(student.getDepartment());
        dto.setYear(student.getYear());
        dto.setRollNumber(student.getRollNumber());
        return dto;
    }
    
    private Student convertToEntity(StudentDTO dto) {
        Student student = new Student();
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());
        student.setDateOfBirth(dto.getDateOfBirth());
        student.setPhone(dto.getPhone());
        student.setDepartment(dto.getDepartment());
        student.setYear(dto.getYear());
        student.setRollNumber(dto.getRollNumber());
        return student;
    }
}