package com.example.si_swa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @Autowired
    private com.example.si_swa.service.SiswaService siswaService;

    @Autowired
    private com.example.si_swa.service.PersonService personService;

    @Autowired
    private com.example.si_swa.service.WaliKelasService waliKelasService;
    @GetMapping(value={"/login", "/login/"})
    public String loginPage() {
        return "login.html";
    }

    @PostMapping(value={"/login", "/login/"})
    public String doLogin(@RequestParam("username") String username, 
                         @RequestParam("password") String password, 
                         jakarta.servlet.http.HttpSession session,
                         org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        com.example.si_swa.entity.Person p = personService.getByUsername(username);
        if (p == null || !p.checkPassword(password)) {
            ra.addFlashAttribute("error", "Username atau password salah");
            return "redirect:/login";
        }
        
        Long pid = p.getId();
        String role = p.getRole();
        
        com.example.si_swa.entity.Siswa sById = siswaService.getSiswaById(pid);
        if (sById != null && "siswa".equalsIgnoreCase(role)) {
            session.removeAttribute("waliKelasId");
            session.setAttribute("siswaId", sById.getId());
            session.setAttribute("allowedMenu", "siswa");
            session.setAttribute("loginName", sById.getNama());
            session.setAttribute("siswaPhone", sById.getNomorTelepon());
            return "redirect:/siswa";
        }
        
        com.example.si_swa.entity.WaliKelas wkById = waliKelasService.getWaliKelasById(pid);
        if (wkById != null && "walikelas".equalsIgnoreCase(role)) {
            session.removeAttribute("siswaId");
            session.setAttribute("waliKelasId", wkById.getId());
            session.setAttribute("allowedMenu", "all");
            session.setAttribute("loginName", wkById.getNama());
            return "redirect:/admin";
        }
        
        session.setAttribute("loginName", p.getNama());
        if ("admin".equalsIgnoreCase(role)) {
            session.setAttribute("allowedMenu", "admin");
            session.setAttribute("adminUsername", username);
            return "redirect:/admin";
        }
        
        ra.addFlashAttribute("error", "Role tidak dikenali. Silakan hubungi administrator.");
        return "redirect:/login";
    }
}