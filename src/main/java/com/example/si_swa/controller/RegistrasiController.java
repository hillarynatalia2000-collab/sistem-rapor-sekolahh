package com.example.si_swa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.example.si_swa.entity.Person;
import com.example.si_swa.entity.Admin;
import com.example.si_swa.service.PersonService;
import com.example.si_swa.service.AdminService;

@Controller
public class RegistrasiController {
    @Autowired
    private PersonService personService;
    
    @Autowired
    private AdminService adminService;

    @org.springframework.web.bind.annotation.InitBinder("personInfo")
    public void initBinder(org.springframework.web.bind.WebDataBinder binder) {
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
        df.setLenient(false);
        binder.registerCustomEditor(java.util.Date.class, new org.springframework.beans.propertyeditors.CustomDateEditor(df, true));
    }

    @GetMapping(value={"/registrasi", "/registrasi/"})
    public String registrasiPage(Model model) {
       
        model.addAttribute("personInfo", new Admin());
        return "registrasi.html";
    }

    @PostMapping(value={"/registrasi", "/registrasi/"})
    public String doRegistrasi(@ModelAttribute("personInfo") Person personInfo, 
                                org.springframework.web.servlet.mvc.support.RedirectAttributes ra,
                                jakarta.servlet.http.HttpSession session) {
        if (personInfo.getUsername() == null || personInfo.getUsername().trim().isEmpty()) {
            ra.addFlashAttribute("error", "Username harus diisi");
            return "redirect:/registrasi";
        }
        
        if (personInfo.getPassword() == null || personInfo.getPassword().trim().isEmpty()) {
            ra.addFlashAttribute("error", "Password harus diisi");
            return "redirect:/registrasi";
        }
        
        if (personInfo.getRole() == null || personInfo.getRole().trim().isEmpty()) {
            ra.addFlashAttribute("error", "Role harus dipilih");
            return "redirect:/registrasi";
        }
        
        Person existingPerson = personService.getByUsername(personInfo.getUsername());
        if (existingPerson != null) {
            ra.addFlashAttribute("error", "Username sudah digunakan");
            return "redirect:/registrasi";
        }
        
        Person savedPerson = personService.addPerson(personInfo);
        
        if ("admin".equalsIgnoreCase(personInfo.getRole())) {
            Admin admin = adminService.addAdminFromPerson(savedPerson.getId(), null);
            if (admin != null) {
                session.setAttribute("loginName", admin.getNama());
                session.setAttribute("allowedMenu", "admin");
                session.setAttribute("adminUsername", admin.getUsername());
                ra.addFlashAttribute("success", "Registrasi admin berhasil! Anda telah masuk sebagai admin.");
                return "redirect:/dashboard";
            }
        }
        
        ra.addFlashAttribute("success", "Registrasi berhasil! Silakan login dengan username dan password Anda.");
        return "redirect:/login";
    }
}

