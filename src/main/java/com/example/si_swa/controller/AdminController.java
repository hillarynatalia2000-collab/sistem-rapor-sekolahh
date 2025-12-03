package com.example.si_swa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.si_swa.service.PersonService;
import com.example.si_swa.service.SiswaService;
import com.example.si_swa.service.WaliKelasService;
import com.example.si_swa.service.MataPelajaranService;
import com.example.si_swa.service.RaportService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminController {
    @Autowired
    private PersonService personService;

    @Autowired
    private SiswaService siswaService;

    @Autowired
    private WaliKelasService waliKelasService;

    @Autowired
    private MataPelajaranService mataPelajaranService;

    @Autowired
    private RaportService raportService;

    @GetMapping(value={"/admin", "/admin/", "/dashboard", "/dashboard/"})
    public String adminPage(Model model, jakarta.servlet.http.HttpSession session) {
        Object allowedMenuObj = session != null ? session.getAttribute("allowedMenu") : null;
        Object wid = session != null ? session.getAttribute("waliKelasId") : null;
        Object sid = session != null ? session.getAttribute("siswaId") : null;
        String allowedMenu = allowedMenuObj != null ? allowedMenuObj.toString() : null;
        
        if (allowedMenu == null || (!"admin".equals(allowedMenu) && !"all".equals(allowedMenu) && !"siswa".equals(allowedMenu))) {
            return "redirect:/login";
        }

        if ("siswa".equals(allowedMenu) && sid != null) {
            Long siswaId = sid instanceof Long ? (Long) sid : (sid != null ? Long.valueOf(sid.toString()) : null);
            if (siswaId != null) {
                com.example.si_swa.entity.Siswa siswa = siswaService.getSiswaById(siswaId);
                if (siswa != null) {
                    model.addAttribute("siswaKelas", siswa.getKelas() != null ? siswa.getKelas() : "-");
                    model.addAttribute("siswaNIS", siswa.getNis() != null ? siswa.getNis() : "-");
                    
                    java.util.List<com.example.si_swa.entity.Raport> raportList = raportService.findBySiswa(siswaId);
                    model.addAttribute("totalRaportSiswa", raportList != null ? raportList.size() : 0);
                    model.addAttribute("raportListSiswa", raportList != null ? raportList : new java.util.ArrayList<>());
                    model.addAttribute("siswa", siswa);
                    model.addAttribute("allowedMenu", "siswa");
                } else {
                    model.addAttribute("siswaKelas", "-");
                    model.addAttribute("siswaNIS", "-");
                    model.addAttribute("totalRaportSiswa", 0);
                    model.addAttribute("allowedMenu", "siswa");
                }
            } else {
                model.addAttribute("siswaKelas", "-");
                model.addAttribute("siswaNIS", "-");
                model.addAttribute("totalRaportSiswa", 0);
                model.addAttribute("allowedMenu", "siswa");
            }
            return "dashboard.html";
        }

        boolean isWaliKelas = "all".equals(allowedMenu) && wid != null;
        
        if (isWaliKelas) {
            Long wkId = wid instanceof Long ? (Long) wid : (wid != null ? Long.valueOf(wid.toString()) : null);
            if (wkId != null) {
                com.example.si_swa.entity.WaliKelas loggedInWk = waliKelasService.getWaliKelasById(wkId);
                
                if (loggedInWk != null && loggedInWk.getNamaKelasWalian() != null) {
                    String namaKelasWalian = loggedInWk.getNamaKelasWalian();
                    
                    java.util.List<com.example.si_swa.entity.Siswa> allSiswa = siswaService.getAllSiswa();
                    long totalSiswa = 0;
                    for (com.example.si_swa.entity.Siswa s : allSiswa) {
                        if (s != null && s.getKelas() != null && s.getKelas().equals(namaKelasWalian)) {
                            totalSiswa++;
                        }
                    }
                    
                    java.util.List<com.example.si_swa.entity.Raport> allRaport = raportService.getAll();
                    long totalRaport = 0;
                    for (com.example.si_swa.entity.Raport r : allRaport) {
                        if (r != null && r.getSiswaId() != null) {
                            com.example.si_swa.entity.Siswa s = siswaService.getSiswaById(r.getSiswaId());
                            if (s != null && s.getKelas() != null && s.getKelas().equals(namaKelasWalian)) {
                                totalRaport++;
                            }
                        }
                    }
                    
                    model.addAttribute("totalSiswa", totalSiswa);
                    model.addAttribute("totalRaport", totalRaport);
                    model.addAttribute("namaKelasWalian", namaKelasWalian);
                    model.addAttribute("allowedMenu", "all");
                } else {
                    model.addAttribute("totalSiswa", 0);
                    model.addAttribute("totalRaport", 0);
                    model.addAttribute("namaKelasWalian", "-");
                    model.addAttribute("allowedMenu", "all");
                }
            } else {
                model.addAttribute("totalSiswa", 0);
                model.addAttribute("totalRaport", 0);
                model.addAttribute("namaKelasWalian", "-");
                model.addAttribute("allowedMenu", "all");
            }
        } else {
            long totalPerson = personService.getAllPerson().size();
            long totalSiswa = siswaService.getAllSiswa().size();
            long totalWaliKelas = waliKelasService.getAllWaliKelas().size();
            long totalMataPelajaran = mataPelajaranService.getAll().size();
            long totalRaport = raportService.getAll().size();

            model.addAttribute("totalPerson", totalPerson);
            model.addAttribute("totalSiswa", totalSiswa);
            model.addAttribute("totalWaliKelas", totalWaliKelas);
            model.addAttribute("totalMataPelajaran", totalMataPelajaran);
            model.addAttribute("totalRaport", totalRaport);
            model.addAttribute("allowedMenu", "admin");
        }

        return "dashboard.html";
    }

    @GetMapping(value={"/admin/konfirmasi-siswa", "/admin/konfirmasi-siswa/"})
    public String konfirmasiSiswaPage(Model model, jakarta.servlet.http.HttpSession session) {
        Object allowedMenu = session != null ? session.getAttribute("allowedMenu") : null;
        if (allowedMenu == null || !"admin".equals(allowedMenu.toString())) {
            return "redirect:/login";
        }

        java.util.List<com.example.si_swa.entity.Siswa> belumKonfirmasi = siswaService.getSiswaBelumKonfirmasi();
        java.util.List<com.example.si_swa.entity.Siswa> sudahKonfirmasi = siswaService.getSiswaSudahKonfirmasi();

        model.addAttribute("belumKonfirmasi", belumKonfirmasi);
        model.addAttribute("sudahKonfirmasi", sudahKonfirmasi);
        model.addAttribute("allowedMenu", "admin");

        return "admin-konfirmasi-siswa.html";
    }

    @PostMapping(value={"/admin/konfirmasi-siswa/{id}/accept", "/admin/konfirmasi-siswa/{id}/accept/"})
    public String acceptSiswa(@PathVariable("id") long id, RedirectAttributes ra, jakarta.servlet.http.HttpSession session) {
        Object allowedMenu = session != null ? session.getAttribute("allowedMenu") : null;
        if (allowedMenu == null || !"admin".equals(allowedMenu.toString())) {
            return "redirect:/login";
        }

        com.example.si_swa.entity.Siswa siswa = siswaService.updateStatusKonfirmasi(id, true);
        if (siswa != null) {
            ra.addFlashAttribute("success", "Siswa " + siswa.getNama() + " berhasil dikonfirmasi");
        } else {
            ra.addFlashAttribute("error", "Gagal mengkonfirmasi siswa");
        }
        return "redirect:/admin/konfirmasi-siswa";
    }

    @PostMapping(value={"/admin/konfirmasi-siswa/{id}/reject", "/admin/konfirmasi-siswa/{id}/reject/"})
    public String rejectSiswa(@PathVariable("id") long id, RedirectAttributes ra, jakarta.servlet.http.HttpSession session) {
        Object allowedMenu = session != null ? session.getAttribute("allowedMenu") : null;
        if (allowedMenu == null || !"admin".equals(allowedMenu.toString())) {
            return "redirect:/login";
        }

        com.example.si_swa.entity.Siswa siswa = siswaService.updateStatusKonfirmasi(id, false);
        if (siswa != null) {
            ra.addFlashAttribute("success", "Konfirmasi siswa " + siswa.getNama() + " telah ditolak");
        } else {
            ra.addFlashAttribute("error", "Gagal menolak konfirmasi siswa");
        }
        return "redirect:/admin/konfirmasi-siswa";
    }
}

