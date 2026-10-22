package com.studenthub.app.controller;

import com.studenthub.app.model.Student;
import com.studenthub.app.repository.StudentRepository;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class StudentController {
    private final StudentRepository repo;
    public StudentController(StudentRepository repo) { this.repo = repo; }

    @GetMapping("/") public String dashboard(Model model) {
        model.addAttribute("total", repo.count());
        model.addAttribute("active", repo.countByStatus(Student.Status.ACTIVE));
        model.addAttribute("inactive", repo.countByStatus(Student.Status.INACTIVE));
        model.addAttribute("recentStudents", repo.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "id")).stream().limit(5).toList());
        return "index";
    }
    @GetMapping("/students") public String students(@RequestParam(defaultValue = "") String q, Model model) {
        model.addAttribute("students", q.isBlank() ? repo.findAll(org.springframework.data.domain.Sort.by("name")) : repo.search(q.trim()));
        model.addAttribute("q", q); return "students";
    }
    @GetMapping("/students/new") public String createForm(Model model) { model.addAttribute("student", new Student()); model.addAttribute("pageTitle", "Add student"); return "student-form"; }
    @GetMapping("/students/{id}/edit") public String editForm(@PathVariable Long id, Model model) {
        Student student = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Student not found"));
        model.addAttribute("student", student); model.addAttribute("pageTitle", "Edit student"); return "student-form";
    }
    @PostMapping("/students/save") public String save(@Valid @ModelAttribute Student student, BindingResult result, Model model, RedirectAttributes redirect) {
        if (student.getId() == null) {
            if (repo.existsByRollNumber(student.getRollNumber())) result.rejectValue("rollNumber", "duplicate", "This roll number already exists");
            if (repo.existsByEmail(student.getEmail())) result.rejectValue("email", "duplicate", "This email already exists");
        } else {
            Student existing = repo.findById(student.getId()).orElse(null);
            if (existing != null && !existing.getRollNumber().equals(student.getRollNumber()) && repo.existsByRollNumber(student.getRollNumber())) result.rejectValue("rollNumber", "duplicate", "This roll number already exists");
            if (existing != null && !existing.getEmail().equals(student.getEmail()) && repo.existsByEmail(student.getEmail())) result.rejectValue("email", "duplicate", "This email already exists");
        }
        if (result.hasErrors()) { model.addAttribute("pageTitle", student.getId() == null ? "Add student" : "Edit student"); return "student-form"; }
        try { repo.save(student); }
        catch (DataIntegrityViolationException ex) { redirect.addFlashAttribute("error", "Roll number or email already exists."); return "redirect:/students"; }
        redirect.addFlashAttribute("success", "Student saved successfully."); return "redirect:/students";
    }
    @PostMapping("/students/{id}/delete") public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        repo.deleteById(id); redirect.addFlashAttribute("success", "Student deleted."); return "redirect:/students";
    }
    @GetMapping("/students/{id}") public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("student", repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Student not found"))); return "student-detail";
    }
}
