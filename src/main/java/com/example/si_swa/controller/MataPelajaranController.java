package com.example.si_swa.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.example.si_swa.entity.MataPelajaran;
import com.example.si_swa.service.MataPelajaranService;

@Controller
public class MataPelajaranController {
    @Autowired
    private MataPelajaranService mataPelajaranService;

    @GetMapping(value={"/mapel", "/mapel/"})
    public String page(Model model, jakarta.servlet.http.HttpSession session) {
        Object allowedMenuObj = session != null ? session.getAttribute("allowedMenu") : null;
        String allowedMenu = allowedMenuObj != null ? allowedMenuObj.toString() : null;
        
        // Hanya admin yang bisa akses
        if (!"admin".equals(allowedMenu)) {
            return "redirect:/login";
        }
        
        List<MataPelajaran> list = mataPelajaranService.getAll();
        model.addAttribute("mapelList", list);
        model.addAttribute("mapelInfo", new MataPelajaran());
        model.addAttribute("allowedMenu", "admin");
        return "matapelajaran.html";
    }

    @GetMapping("/mapel/{id}")
    public String getRec(Model model, @PathVariable("id") String id, jakarta.servlet.http.HttpSession session) {
        Object allowedMenuObj = session != null ? session.getAttribute("allowedMenu") : null;
        String allowedMenu = allowedMenuObj != null ? allowedMenuObj.toString() : null;
        
        // Hanya admin yang bisa akses
        if (!"admin".equals(allowedMenu)) {
            return "redirect:/login";
        }
        
        List<MataPelajaran> list = mataPelajaranService.getAll();
        MataPelajaran rec = mataPelajaranService.getById(id);
        if (rec == null) {
            return "redirect:/mapel";
        }
        model.addAttribute("mapelList", list);
        model.addAttribute("mapelRec", rec);
        model.addAttribute("mapelInfo", rec);
        model.addAttribute("allowedMenu", "admin");
        return "matapelajaran.html";
    }

    @PostMapping(value={"/mapel/submit", "/mapel/submit/"}, params={"add"})
    public String add(@ModelAttribute("mapelInfo") MataPelajaran mapelInfo, 
                      org.springframework.web.servlet.mvc.support.RedirectAttributes ra,
                      jakarta.servlet.http.HttpSession session) {
        Object allowedMenuObj = session != null ? session.getAttribute("allowedMenu") : null;
        String allowedMenu = allowedMenuObj != null ? allowedMenuObj.toString() : null;
        
        if (!"admin".equals(allowedMenu)) {
            ra.addFlashAttribute("error", "Anda tidak memiliki izin untuk menambah mata pelajaran");
            return "redirect:/login";
        }
        
        if (mapelInfo.getNama() == null || mapelInfo.getNama().trim().isEmpty()) {
            ra.addFlashAttribute("error", "Nama mata pelajaran harus diisi");
            return "redirect:/mapel";
        }
        
        if (mapelInfo.getKode() == null || mapelInfo.getKode().trim().isEmpty()) {
            ra.addFlashAttribute("error", "Kode mata pelajaran harus diisi");
            return "redirect:/mapel";
        }
        
        com.example.si_swa.entity.MataPelajaran existingByKode = mataPelajaranService.getRepository().findByKode(mapelInfo.getKode());
        if (existingByKode != null) {
            ra.addFlashAttribute("error", "Kode mata pelajaran sudah digunakan");
            return "redirect:/mapel";
        }
        
        if (mapelInfo.getJumlahJam() < 1) {
            ra.addFlashAttribute("error", "Jumlah jam harus minimal 1");
            return "redirect:/mapel";
        }
        
        if (mapelInfo.getIdMataPelajaran() == null || mapelInfo.getIdMataPelajaran().trim().isEmpty()) {
            mapelInfo.setIdMataPelajaran(mapelInfo.getKode().toUpperCase());
        }
        
        mataPelajaranService.add(mapelInfo);
        ra.addFlashAttribute("success", "Mata pelajaran berhasil ditambahkan");
        return "redirect:/mapel";
    }

    @PostMapping(value="/mapel/submit/{id}", params={"edit"})
    public String edit(@ModelAttribute("mapelInfo") MataPelajaran mapelInfo, @PathVariable("id") String id, 
                      org.springframework.web.servlet.mvc.support.RedirectAttributes ra,
                      jakarta.servlet.http.HttpSession session) {
        Object allowedMenuObj = session != null ? session.getAttribute("allowedMenu") : null;
        String allowedMenu = allowedMenuObj != null ? allowedMenuObj.toString() : null;
        
        if (!"admin".equals(allowedMenu)) {
            ra.addFlashAttribute("error", "Anda tidak memiliki izin untuk mengedit mata pelajaran");
            return "redirect:/login";
        }
        
        MataPelajaran existing = mataPelajaranService.getById(id);
        if (existing == null) {
            ra.addFlashAttribute("error", "Mata pelajaran tidak ditemukan");
            return "redirect:/mapel";
        }
        
        if (mapelInfo.getNama() == null || mapelInfo.getNama().trim().isEmpty()) {
            ra.addFlashAttribute("error", "Nama mata pelajaran harus diisi");
            return "redirect:/mapel";
        }
        
        if (mapelInfo.getKode() == null || mapelInfo.getKode().trim().isEmpty()) {
            ra.addFlashAttribute("error", "Kode mata pelajaran harus diisi");
            return "redirect:/mapel";
        }
        
        if (!existing.getKode().equals(mapelInfo.getKode())) {
            com.example.si_swa.entity.MataPelajaran existingByKode = mataPelajaranService.getRepository().findByKode(mapelInfo.getKode());
            if (existingByKode != null && !existingByKode.getIdMataPelajaran().equals(id)) {
                ra.addFlashAttribute("error", "Kode mata pelajaran sudah digunakan");
                return "redirect:/mapel";
            }
        }
        
        if (mapelInfo.getJumlahJam() < 1) {
            ra.addFlashAttribute("error", "Jumlah jam harus minimal 1");
            return "redirect:/mapel";
        }
        
        mataPelajaranService.update(id, mapelInfo);
        ra.addFlashAttribute("success", "Mata pelajaran berhasil disimpan");
        return "redirect:/mapel";
    }

    @PostMapping(value="/mapel/submit/{id}", params={"delete"})
    public String delete(@PathVariable("id") String id, 
                        org.springframework.web.servlet.mvc.support.RedirectAttributes ra,
                        jakarta.servlet.http.HttpSession session) {
        Object allowedMenuObj = session != null ? session.getAttribute("allowedMenu") : null;
        String allowedMenu = allowedMenuObj != null ? allowedMenuObj.toString() : null;
        
        if (!"admin".equals(allowedMenu)) {
            ra.addFlashAttribute("error", "Anda tidak memiliki izin untuk menghapus mata pelajaran");
            return "redirect:/login";
        }
        
        MataPelajaran mataPelajaran = mataPelajaranService.getById(id);
        if (mataPelajaran == null) {
            ra.addFlashAttribute("error", "Mata pelajaran tidak ditemukan");
            return "redirect:/mapel";
        }
        
        mataPelajaranService.delete(id);
        ra.addFlashAttribute("success", "Mata pelajaran berhasil dihapus");
        return "redirect:/mapel";
    }
}