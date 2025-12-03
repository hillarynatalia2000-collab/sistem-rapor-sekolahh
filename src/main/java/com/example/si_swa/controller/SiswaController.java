package com.example.si_swa.controller;

import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.si_swa.entity.Siswa;
import com.example.si_swa.service.SiswaService;
import com.example.si_swa.service.PersonService;
import com.example.si_swa.service.WaliKelasService;
import com.example.si_swa.entity.WaliKelas;
import com.example.si_swa.service.MataPelajaranService;

@Controller
public class SiswaController {
    
    @org.springframework.web.bind.annotation.InitBinder("siswaInfo")
    public void initBinder(org.springframework.web.bind.WebDataBinder binder) {
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
        df.setLenient(false);
        binder.registerCustomEditor(java.util.Date.class, new org.springframework.beans.propertyeditors.CustomDateEditor(df, true));
    }
    @Autowired
    private SiswaService siswaService;

    @Autowired
    private PersonService personService;

    @Autowired
    private WaliKelasService waliKelasService;

    @Autowired
    private MataPelajaranService mataPelajaranService;

    @Autowired
    private com.example.si_swa.service.RaportService raportService;


    @GetMapping(value={"/siswa", "/siswa/"})
    public String siswaPage(Model model, jakarta.servlet.http.HttpSession session) {
        Object sid = session != null ? session.getAttribute("siswaId") : null;
        Object wid = session != null ? session.getAttribute("waliKelasId") : null;
        Object allowedMenuObj = session != null ? session.getAttribute("allowedMenu") : null;
        String allowedMenu = allowedMenuObj != null ? allowedMenuObj.toString() : null;
        
        if (sid == null && wid == null && !"admin".equals(allowedMenu)) {
            return "redirect:/login";
        }
        
        List<Siswa> list = siswaService.getAllSiswa();
        Set<Long> siswaIds = new java.util.HashSet<>();
        if (list != null) {
            for (Siswa s : list) {
                if (s != null && s.getId() != null) {
                    siswaIds.add(s.getId());
                }
            }
        }
        List<WaliKelas> wkList = waliKelasService.getAllWaliKelas();
        Set<Long> wkIds = new java.util.HashSet<>();
        for (WaliKelas wk : wkList) {
            if (wk != null && wk.getId() != null) {
                wkIds.add(wk.getId());
            }
        }
        
        Long siswaId = null;
        if (sid != null) {
            Long tempSiswaId = sid instanceof Long ? (Long) sid : (sid != null ? Long.valueOf(sid.toString()) : null);
            if (tempSiswaId != null) {
                siswaId = tempSiswaId;
                java.util.List<Siswa> filteredList = new java.util.ArrayList<>();
                for (Siswa s : list) {
                    if (s != null && s.getId() != null && s.getId().equals(tempSiswaId)) {
                        filteredList.add(s);
                    }
                }
                list = filteredList;
            }
        } else if (wid != null) {
            Long tempWkId = wid instanceof Long ? (Long) wid : (wid != null ? Long.valueOf(wid.toString()) : null);
            if (tempWkId != null) {
                WaliKelas loggedInWk = waliKelasService.getWaliKelasById(tempWkId);
                if (loggedInWk != null && loggedInWk.getNamaKelasWalian() != null) {
                    String namaKelasWalian = loggedInWk.getNamaKelasWalian();
                    java.util.List<Siswa> filteredList = new java.util.ArrayList<>();
                    for (Siswa s : list) {
                        if (s != null && s.getKelas() != null && s.getKelas().equals(namaKelasWalian)) {
                            filteredList.add(s);
                        }
                    }
                    list = filteredList;
                }
            }
        }
        
        model.addAttribute("siswaList", list);
        model.addAttribute("siswaInfo", new Siswa());
        model.addAttribute("personList", personService.getAllPerson());
        model.addAttribute("siswaIds", siswaIds);
        model.addAttribute("wkList", wkList);
        model.addAttribute("wkIds", wkIds);
        model.addAttribute("mapelList", mataPelajaranService.getAll());
        model.addAttribute("loggedInSiswaId", siswaId);
        
        if (sid != null) {
            model.addAttribute("allowedMenu", "siswa");
            if (session != null) {
                session.setAttribute("allowedMenu", "siswa");
            }
        } else if ("admin".equals(allowedMenu)) {
            model.addAttribute("allowedMenu", "admin");
        } else {
            model.addAttribute("allowedMenu", "all");
            if (session != null) {
                session.setAttribute("allowedMenu", "all");
            }
        }
        
        return "siswa.html";
    }

    @GetMapping("/siswa/{id}")
    public String siswaGetRec(Model model, @PathVariable("id") long id, jakarta.servlet.http.HttpSession session) {
        Object sid = session != null ? session.getAttribute("siswaId") : null;
        Object allowedMenuObj = session != null ? session.getAttribute("allowedMenu") : null;
        String allowedMenu = allowedMenuObj != null ? allowedMenuObj.toString() : null;
        
        List<Siswa> list = siswaService.getAllSiswa();
        Set<Long> siswaIds = new java.util.HashSet<>();
        for (Siswa s : list) {
            if (s != null && s.getId() != null) {
                siswaIds.add(s.getId());
            }
        }
        List<WaliKelas> wkList = waliKelasService.getAllWaliKelas();
        Set<Long> wkIds = new java.util.HashSet<>();
        for (WaliKelas wk : wkList) {
            if (wk != null && wk.getId() != null) {
                wkIds.add(wk.getId());
            }
        }
        Siswa rec = siswaService.getSiswaById(id);
        
        model.addAttribute("siswaList", list);
        model.addAttribute("siswaRec", rec);
        model.addAttribute("siswaInfo", rec != null ? rec : new Siswa());
        model.addAttribute("personList", personService.getAllPerson());
        model.addAttribute("siswaIds", siswaIds);
        model.addAttribute("wkList", wkList);
        model.addAttribute("wkIds", wkIds);
        model.addAttribute("mapelList", mataPelajaranService.getAll());
        
        if (sid != null) {
            model.addAttribute("allowedMenu", "siswa");
        } else if ("admin".equals(allowedMenu)) {
            model.addAttribute("allowedMenu", "admin");
        } else {
            model.addAttribute("allowedMenu", "all");
        }
        
        return "siswa.html";
    }

    @PostMapping(value={"/siswa/submit", "/siswa/submit/", "/siswa/submit/{id}"}, params={"add"})
    public String siswaAdd(@ModelAttribute("siswaInfo") Siswa siswaInfo,
                           org.springframework.web.servlet.mvc.support.RedirectAttributes ra,
                           jakarta.servlet.http.HttpSession session) {
        if (siswaInfo.getNama() == null || siswaInfo.getNama().trim().isEmpty()) {
            ra.addFlashAttribute("error", "Nama harus diisi");
            return "redirect:/siswa";
        }
        
        if (siswaInfo.getTanggalLahir() == null) {
            ra.addFlashAttribute("error", "Tanggal lahir harus diisi");
            return "redirect:/siswa";
        }
        
        if (siswaInfo.getNomorTelepon() == null || siswaInfo.getNomorTelepon().trim().isEmpty()) {
            ra.addFlashAttribute("error", "Nomor telepon harus diisi");
            return "redirect:/siswa";
        }
        
        if (!siswaInfo.validasiNomorTelepon(siswaInfo.getNomorTelepon())) {
            ra.addFlashAttribute("error", "Nomor telepon harus dimulai dengan 0 dan 10-14 digit");
            return "redirect:/siswa";
        }
        
        if (siswaInfo.getAlamat() == null || siswaInfo.getAlamat().trim().isEmpty()) {
            ra.addFlashAttribute("error", "Alamat harus diisi");
            return "redirect:/siswa";
        }
        
        if (siswaInfo.getUsername() == null || siswaInfo.getUsername().trim().isEmpty()) {
            ra.addFlashAttribute("error", "Username harus diisi");
            return "redirect:/siswa";
        }
        
        com.example.si_swa.entity.Person existingPerson = personService.getByUsername(siswaInfo.getUsername());
        if (existingPerson != null) {
            ra.addFlashAttribute("error", "Username sudah digunakan");
            return "redirect:/siswa";
        }
        
        if (siswaInfo.getPassword() == null || siswaInfo.getPassword().trim().isEmpty()) {
            ra.addFlashAttribute("error", "Password harus diisi");
            return "redirect:/siswa";
        }
        
        if (siswaInfo.getEmail() == null || siswaInfo.getEmail().trim().isEmpty()) {
            ra.addFlashAttribute("error", "Email harus diisi");
            return "redirect:/siswa";
        }
        
        if (siswaInfo.getRole() == null || siswaInfo.getRole().trim().isEmpty()) {
            siswaInfo.setRole("siswa");
        }
        
        if (siswaInfo.getNis() == null || siswaInfo.getNis().trim().isEmpty()) {
            ra.addFlashAttribute("error", "NIS harus diisi");
            return "redirect:/siswa";
        }
        
        if (!siswaInfo.validasiNIS(siswaInfo.getNis())) {
            ra.addFlashAttribute("error", "NIS harus 4-20 karakter dan hanya angka");
            return "redirect:/siswa";
        }
        
        Siswa existingByNis = siswaService.findSiswaByNIS(siswaInfo.getNis());
        if (existingByNis != null) {
            ra.addFlashAttribute("error", "NIS sudah digunakan");
            return "redirect:/siswa";
        }
        
        if (siswaInfo.getKelas() == null || siswaInfo.getKelas().trim().isEmpty()) {
            ra.addFlashAttribute("error", "Kelas harus diisi");
            return "redirect:/siswa";
        }
        
        if (siswaInfo.getNilai() != null && !siswaService.validasiRangeNilai(siswaInfo.getNilai())) {
            ra.addFlashAttribute("error", "Nilai harus antara 0.00 - 100.00");
            return "redirect:/siswa";
        }
        
        siswaInfo.setStatusKonfirmasi(false);
        siswaService.addSiswa(siswaInfo);
        return "redirect:/siswa";
    }

    @PostMapping(value="/siswa/submit/{id}", params={"edit"})
    public String siswaEdit(@ModelAttribute("siswaInfo") Siswa siswaInfo, 
                           @PathVariable("id") long id,
                           org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        Siswa existing = siswaService.getSiswaById(id);
        if (existing == null) {
            ra.addFlashAttribute("error", "Siswa tidak ditemukan");
            return "redirect:/siswa";
        }
        
        if (siswaInfo.getKelas() == null || siswaInfo.getKelas().trim().isEmpty()) {
            ra.addFlashAttribute("error", "Kelas harus diisi");
            return "redirect:/siswa/" + id;
        }
        
        if (siswaInfo.getNis() != null && !siswaInfo.getNis().trim().isEmpty()) {
            if (!siswaInfo.validasiNIS(siswaInfo.getNis())) {
                ra.addFlashAttribute("error", "NIS harus 4-20 karakter dan hanya angka");
                return "redirect:/siswa/" + id;
            }
            Siswa existingByNis = siswaService.findSiswaByNIS(siswaInfo.getNis());
            if (existingByNis != null && !existingByNis.getId().equals(id)) {
                ra.addFlashAttribute("error", "NIS sudah digunakan oleh siswa lain");
                return "redirect:/siswa/" + id;
            }
            existing.setNis(siswaInfo.getNis());
        }
        
        if (siswaInfo.getNilai() != null && !siswaService.validasiRangeNilai(siswaInfo.getNilai())) {
            ra.addFlashAttribute("error", "Nilai harus antara 0.00 - 100.00");
            return "redirect:/siswa/" + id;
        }
        
        // Update field dari Person
        if (siswaInfo.getNama() != null && !siswaInfo.getNama().trim().isEmpty()) {
            existing.setNama(siswaInfo.getNama());
        }
        if (siswaInfo.getTanggalLahir() != null) {
            existing.setTanggalLahir(siswaInfo.getTanggalLahir());
        }
        if (siswaInfo.getAlamat() != null && !siswaInfo.getAlamat().trim().isEmpty()) {
            existing.setAlamat(siswaInfo.getAlamat());
        }
        if (siswaInfo.getNomorTelepon() != null && !siswaInfo.getNomorTelepon().trim().isEmpty()) {
            existing.setNomorTelepon(siswaInfo.getNomorTelepon());
        }
        if (siswaInfo.getEmail() != null && !siswaInfo.getEmail().trim().isEmpty()) {
            existing.setEmail(siswaInfo.getEmail());
        }
        
        // Update field dari Siswa
        existing.setKelas(siswaInfo.getKelas());
        if (siswaInfo.getNilai() != null) {
            existing.setNilai(siswaInfo.getNilai());
        }
        
        // Pastikan ID tidak berubah dan save langsung (jangan gunakan addSiswa yang mengubah ID)
        existing.setId(id);
        siswaService.updateSiswa(id, existing);
        
        return "redirect:/siswa";
    }

    @PostMapping(value="/siswa/submit/{id}", params={"delete"})
    public String siswaDelete(@PathVariable("id") long id, org.springframework.web.servlet.mvc.support.RedirectAttributes ra,
                             jakarta.servlet.http.HttpSession session) {
        Object allowedMenuObj = session != null ? session.getAttribute("allowedMenu") : null;
        String allowedMenu = allowedMenuObj != null ? allowedMenuObj.toString() : null;
        
        if (!"admin".equals(allowedMenu)) {
            ra.addFlashAttribute("error", "Anda tidak memiliki izin untuk menghapus siswa");
            return "redirect:/siswa";
        }
        
        siswaService.deleteSiswa(id);
        return "redirect:/siswa";
    }


    @PostMapping(value={"/siswa/mapel/{id}"})
    public String siswaPilihMapel(@PathVariable("id") long id, 
                                  @org.springframework.web.bind.annotation.RequestParam(value="mapelIds", required=false) java.util.List<String> mapelIds, 
                                  org.springframework.web.servlet.mvc.support.RedirectAttributes ra,
                                  jakarta.servlet.http.HttpSession session) {
        Object allowedMenuObj = session != null ? session.getAttribute("allowedMenu") : null;
        String allowedMenu = allowedMenuObj != null ? allowedMenuObj.toString() : null;
        
        if (!"admin".equals(allowedMenu)) {
            ra.addFlashAttribute("error", "Anda tidak memiliki izin untuk mengatur mata pelajaran");
            return "redirect:/siswa";
        }
        
        Siswa siswa = siswaService.getSiswaById(id);
        if (siswa == null) {
            ra.addFlashAttribute("error", "Siswa tidak ditemukan");
            return "redirect:/siswa";
        }
        
        try {
            siswaService.assignMapel(id, mapelIds);
            int jumlahMapel = (mapelIds != null && !mapelIds.isEmpty()) ? mapelIds.size() : 0;
            if (jumlahMapel > 0) {
                ra.addFlashAttribute("success", "Mata pelajaran berhasil ditambahkan untuk siswa " + siswa.getNama() + " (" + jumlahMapel + " mata pelajaran)");
            } else {
                ra.addFlashAttribute("success", "Mata pelajaran untuk siswa " + siswa.getNama() + " berhasil dihapus");
            }
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal mengatur mata pelajaran: " + e.getMessage());
        }
        
        return "redirect:/siswa";
    }


    @PostMapping(value={"/siswa/konfirmasi/{id}/accept", "/siswa/konfirmasi/{id}/accept/"})
    public String acceptSiswa(@PathVariable("id") long id, org.springframework.web.servlet.mvc.support.RedirectAttributes ra, jakarta.servlet.http.HttpSession session) {
        Object allowedMenuObj = session != null ? session.getAttribute("allowedMenu") : null;
        String allowedMenu = allowedMenuObj != null ? allowedMenuObj.toString() : null;
        
        if (!"admin".equals(allowedMenu)) {
            return "redirect:/login";
        }

        Siswa siswa = siswaService.updateStatusKonfirmasi(id, true);
        if (siswa != null) {
            ra.addFlashAttribute("success", "Siswa " + siswa.getNama() + " berhasil dikonfirmasi");
        } else {
            ra.addFlashAttribute("error", "Gagal mengkonfirmasi siswa");
        }
        return "redirect:/siswa";
    }

    @PostMapping(value={"/siswa/konfirmasi/{id}/reject", "/siswa/konfirmasi/{id}/reject/"})
    public String rejectSiswa(@PathVariable("id") long id, org.springframework.web.servlet.mvc.support.RedirectAttributes ra, jakarta.servlet.http.HttpSession session) {
        Object allowedMenuObj = session != null ? session.getAttribute("allowedMenu") : null;
        String allowedMenu = allowedMenuObj != null ? allowedMenuObj.toString() : null;
        
        if (!"admin".equals(allowedMenu)) {
            return "redirect:/login";
        }

        Siswa siswa = siswaService.updateStatusKonfirmasi(id, false);
        if (siswa != null) {
            ra.addFlashAttribute("success", "Konfirmasi siswa " + siswa.getNama() + " telah ditolak");
        } else {
            ra.addFlashAttribute("error", "Gagal menolak konfirmasi siswa");
        }
        return "redirect:/siswa";
    }

    @GetMapping(value={"/siswa/nilai", "/siswa/nilai/"})
    public String nilaiSiswa(Model model, jakarta.servlet.http.HttpSession session) {
        Object sid = session != null ? session.getAttribute("siswaId") : null;
        Object allowedMenuObj = session != null ? session.getAttribute("allowedMenu") : null;
        String allowedMenu = allowedMenuObj != null ? allowedMenuObj.toString() : null;
        
        if (sid == null || !"siswa".equals(allowedMenu)) {
            return "redirect:/login";
        }
        
        Long siswaId = sid instanceof Long ? (Long) sid : (sid != null ? Long.valueOf(sid.toString()) : null);
        if (siswaId == null) {
            return "redirect:/login";
        }
        
        Siswa siswa = siswaService.getSiswaById(siswaId);
        if (siswa == null) {
            return "redirect:/login";
        }
        
        java.util.List<com.example.si_swa.entity.Raport> raportList = raportService.findBySiswa(siswaId);
        
        com.example.si_swa.entity.Raport latestRaport = null;
        if (raportList != null && !raportList.isEmpty()) {
            for (com.example.si_swa.entity.Raport r : raportList) {
                if (latestRaport == null) {
                    latestRaport = r;
                } else {
                    java.util.Date ta = r.getTanggalDiterbitkan();
                    java.util.Date tb = latestRaport.getTanggalDiterbitkan();
                    long la = ta != null ? ta.getTime() : 0L;
                    long lb = tb != null ? tb.getTime() : 0L;
                    if (la > lb) {
                        latestRaport = r;
                    }
                }
            }
        }
        
        float rataRataNilai = 0f;
        int totalMapel = 0;
        int mapelLulus = 0;
        int nilaiTertinggi = 0;
        int nilaiTerendah = 100;
        
        if (latestRaport != null && latestRaport.getNilai() != null && !latestRaport.getNilai().isEmpty()) {
            java.util.HashMap<com.example.si_swa.entity.MataPelajaran, Integer> nilai = latestRaport.getNilai();
            totalMapel = nilai.size();
            float sum = 0f;
            int count = 0;
            
            for (Integer v : nilai.values()) {
                if (v != null) {
                    sum += v;
                    count++;
                    if (v >= 75) {
                        mapelLulus++;
                    }
                    if (v > nilaiTertinggi) {
                        nilaiTertinggi = v;
                    }
                    if (v < nilaiTerendah) {
                        nilaiTerendah = v;
                    }
                }
            }
            rataRataNilai = count > 0 ? sum / count : 0f;
        }
        
        model.addAttribute("siswa", siswa);
        model.addAttribute("raportList", raportList);
        model.addAttribute("latestRaport", latestRaport);
        model.addAttribute("rataRataNilai", rataRataNilai);
        model.addAttribute("totalMapel", totalMapel);
        model.addAttribute("mapelLulus", mapelLulus);
        model.addAttribute("nilaiTertinggi", nilaiTertinggi);
        model.addAttribute("nilaiTerendah", nilaiTerendah);
        model.addAttribute("allowedMenu", "siswa");
        
        return "siswa-nilai.html";
    }
}
