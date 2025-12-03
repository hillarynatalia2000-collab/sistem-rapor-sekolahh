package com.example.si_swa.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;

@ControllerAdvice
public class GlobalModelAdvice {
    @Autowired
    private com.example.si_swa.service.SiswaService siswaService;

    @Autowired
    private com.example.si_swa.service.WaliKelasService waliKelasService;

    @Autowired
    private com.example.si_swa.service.PersonService personService;
    @ModelAttribute
    public void injectAllowedMenu(org.springframework.ui.Model model, jakarta.servlet.http.HttpSession session) {
        Object allowed = session != null ? session.getAttribute("allowedMenu") : null;
        if (allowed == null && session != null) {
            if (session.getAttribute("siswaId") != null) {
                allowed = "siswa";
                session.setAttribute("allowedMenu", allowed);
            } else if (session.getAttribute("waliKelasId") != null) {
                allowed = "all";
                session.setAttribute("allowedMenu", allowed);
            }
        }
        if (allowed != null) { model.addAttribute("allowedMenu", allowed.toString()); }

        String loginName = null;
        if (session != null) {
            Object ln = session.getAttribute("loginName");
            if (ln != null) {
                loginName = ln.toString();
            } else if (session.getAttribute("siswaId") != null) {
                Object sid = session.getAttribute("siswaId");
                Long siswaId = sid instanceof Long ? (Long) sid : (sid != null ? Long.valueOf(sid.toString()) : null);
                com.example.si_swa.entity.Siswa s = siswaId != null ? siswaService.getSiswaById(siswaId) : null;
                if (s != null) loginName = s.getNama();
            } else if (session.getAttribute("waliKelasId") != null) {
                Object wid = session.getAttribute("waliKelasId");
                Long wkId = wid instanceof Long ? (Long) wid : (wid != null ? Long.valueOf(wid.toString()) : null);
                com.example.si_swa.entity.WaliKelas wk = wkId != null ? waliKelasService.getWaliKelasById(wkId) : null;
                if (wk != null) loginName = wk.getNama();
            }
        }
        if (loginName != null && !loginName.isEmpty()) {
            model.addAttribute("loginName", loginName);
        }
    }
}