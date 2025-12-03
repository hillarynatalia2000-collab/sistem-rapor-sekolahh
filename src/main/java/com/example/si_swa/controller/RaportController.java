package com.example.si_swa.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.si_swa.entity.Raport;
import com.example.si_swa.entity.Siswa;
import com.example.si_swa.entity.WaliKelas;
import com.example.si_swa.service.RaportService;
import com.example.si_swa.service.SiswaService;
import com.example.si_swa.service.WaliKelasService;
import com.example.si_swa.service.MataPelajaranService;

@Controller
public class RaportController {
    @Autowired
    private RaportService raportService;

    @Autowired
    private SiswaService siswaService;

    @Autowired
    private WaliKelasService waliKelasService;

    @Autowired
    private MataPelajaranService mataPelajaranService;

    @org.springframework.web.bind.annotation.InitBinder("raportInfo")
    public void initBinder(org.springframework.web.bind.WebDataBinder binder) {
        binder.setDisallowedFields("nilai");
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
        df.setLenient(false);
        binder.registerCustomEditor(java.util.Date.class, new org.springframework.beans.propertyeditors.CustomDateEditor(df, true));
    }

    @GetMapping(value={"/raport", "/raport/"})
    public String page(Model model, jakarta.servlet.http.HttpSession session) {
        Object wid = session != null ? session.getAttribute("waliKelasId") : null;
        Object allowedMenuObj = session != null ? session.getAttribute("allowedMenu") : null;
        String allowedMenu = allowedMenuObj != null ? allowedMenuObj.toString() : null;
        
        List<Raport> list = raportService.getAll();
        List<Siswa> siswaList = siswaService.getAllSiswa();
        
        if (wid != null && "all".equals(allowedMenu)) {
            Long waliKelasId = wid instanceof Long ? (Long) wid : (wid != null ? Long.valueOf(wid.toString()) : null);
            if (waliKelasId != null) {
                WaliKelas wk = waliKelasService.getWaliKelasById(waliKelasId);
                if (wk != null && wk.getNamaKelasWalian() != null) {
                    String namaKelasWalian = wk.getNamaKelasWalian();
                    java.util.List<Siswa> filteredList = new java.util.ArrayList<>();
                    for (Siswa s : siswaList) {
                        if (s != null && namaKelasWalian.equals(s.getKelas())) {
                            filteredList.add(s);
                        }
                    }
                    siswaList = filteredList;
                }
            }
        }
        
        List<WaliKelas> wkList = waliKelasService.getAllWaliKelas();
        java.util.Map<Long, String> siswaNames = new java.util.HashMap<>();
        for (Siswa s : siswaList) {
            if (s != null && s.getId() != null) {
                siswaNames.put(s.getId(), s.getNama());
            }
        }
        java.util.Map<Long, String> wkNames = new java.util.HashMap<>();
        for (WaliKelas wk : wkList) {
            if (wk != null && wk.getId() != null) {
                wkNames.put(wk.getId(), wk.getNama());
            }
        }
        model.addAttribute("raportList", list);
        model.addAttribute("raportInfo", new Raport());
        model.addAttribute("siswaList", siswaList);
        model.addAttribute("wkList", wkList);
        model.addAttribute("mapelList", mataPelajaranService.getAll());
        model.addAttribute("siswaNames", siswaNames);
        model.addAttribute("wkNames", wkNames);
        model.addAttribute("generatedNomorRapot", raportService.generateNomorRapot(null));
        return "raport.html";
    }

    @GetMapping("/raport/{id}")
    public String getRec(Model model, @PathVariable("id") long id, jakarta.servlet.http.HttpSession session) {
        Object wid = session != null ? session.getAttribute("waliKelasId") : null;
        Object allowedMenuObj = session != null ? session.getAttribute("allowedMenu") : null;
        String allowedMenu = allowedMenuObj != null ? allowedMenuObj.toString() : null;
        
        List<Raport> list = raportService.getAll();
        Raport rec = raportService.getById(id);
        List<Siswa> siswaList = siswaService.getAllSiswa();
        
        if (wid != null && "all".equals(allowedMenu)) {
            Long waliKelasId = wid instanceof Long ? (Long) wid : (wid != null ? Long.valueOf(wid.toString()) : null);
            if (waliKelasId != null) {
                WaliKelas wk = waliKelasService.getWaliKelasById(waliKelasId);
                if (wk != null && wk.getNamaKelasWalian() != null) {
                    String namaKelasWalian = wk.getNamaKelasWalian();
                    java.util.List<Siswa> filteredList = new java.util.ArrayList<>();
                    for (Siswa s : siswaList) {
                        if (s != null && namaKelasWalian.equals(s.getKelas())) {
                            filteredList.add(s);
                        }
                    }
                    siswaList = filteredList;
                }
            }
        }
        
        List<WaliKelas> wkList = waliKelasService.getAllWaliKelas();
        java.util.Map<Long, String> siswaNames = new java.util.HashMap<>();
        for (Siswa s : siswaList) {
            if (s != null && s.getId() != null) {
                siswaNames.put(s.getId(), s.getNama());
            }
        }
        java.util.Map<Long, String> wkNames = new java.util.HashMap<>();
        for (WaliKelas wk : wkList) {
            if (wk != null && wk.getId() != null) {
                wkNames.put(wk.getId(), wk.getNama());
            }
        }
        model.addAttribute("raportList", list);
        model.addAttribute("raportRec", rec);
        model.addAttribute("raportInfo", rec != null ? rec : new Raport());
        model.addAttribute("siswaList", siswaList);
        model.addAttribute("wkList", wkList);
        model.addAttribute("mapelList", mataPelajaranService.getAll());
        model.addAttribute("siswaNames", siswaNames);
        model.addAttribute("wkNames", wkNames);
        model.addAttribute("generatedNomorRapot", raportService.generateNomorRapot(rec != null ? rec.getTanggalDiterbitkan() : null));
        return "raport.html";
    }

    @PostMapping(value={"/raport/submit", "/raport/submit/"}, params={"add"})
    public String add(@ModelAttribute("raportInfo") Raport raportInfo, 
                      jakarta.servlet.http.HttpSession session,
                      org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        Object wid = session != null ? session.getAttribute("waliKelasId") : null;
        Long wkId = null;
        if (wid != null) {
            wkId = wid instanceof Long ? (Long) wid : (wid != null ? Long.valueOf(wid.toString()) : null);
        }
        
        if (wkId == null && raportInfo.getSiswaId() != null) {
            Siswa s = siswaService.getSiswaById(raportInfo.getSiswaId());
            if (s != null && s.getKelas() != null) {
                List<WaliKelas> wkList = waliKelasService.getAllWaliKelas();
                for (WaliKelas wk : wkList) {
                    if (s.getKelas().equals(wk.getNamaKelasWalian())) {
                        wkId = wk.getId();
                        break;
                    }
                }
            }
        }
        
        if (wkId != null) {
            raportInfo.setWaliKelasId(wkId);
        }
        
        raportService.add(raportInfo);
        return "redirect:/raport";
    }

    @PostMapping(value="/raport/submit/{id}", params={"edit"})
    public String edit(@ModelAttribute("raportInfo") Raport raportInfo,
                       @PathVariable("id") long id,
                       @RequestParam(value="mapelIds", required=false) java.util.List<String> mapelIds,
                       @RequestParam(value="nilai", required=false) java.util.List<Integer> nilai,
                       org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        if (mapelIds != null && nilai != null) {
            int n = Math.min(mapelIds.size(), nilai.size());
            for (int i = 0; i < n; i++) {
                String mid = mapelIds.get(i);
                Integer v = nilai.get(i);
                if (mid != null && v != null) {
                    com.example.si_swa.entity.MataPelajaran mp = mataPelajaranService.getById(mid);
                    if (mp != null) {
                        raportInfo.tambahNilai(mp, v);
                    }
                }
            }
        }
        raportService.update(id, raportInfo);
        return "redirect:/raport";
    }

    @PostMapping(value="/raport/submit/{id}", params={"delete"})
    public String delete(@PathVariable("id") long id, org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        raportService.delete(id);
        return "redirect:/raport";
    }

    @GetMapping("/raport/cetak/{siswaId}")
    public String cetak(Model model, @PathVariable("siswaId") long siswaId, jakarta.servlet.http.HttpSession session) {
        Object sid = session != null ? session.getAttribute("siswaId") : null;
        Object wid = session != null ? session.getAttribute("waliKelasId") : null;
        Object allowedMenuObj = session != null ? session.getAttribute("allowedMenu") : null;
        String allowedMenu = allowedMenuObj != null ? allowedMenuObj.toString() : null;
        
        boolean canView = false;
        if ("admin".equals(allowedMenu)) {
            canView = true;
        } else if ("siswa".equals(allowedMenu) && sid != null) {
            Long siswaIdSession = sid instanceof Long ? (Long) sid : (sid != null ? Long.valueOf(sid.toString()) : null);
            canView = siswaIdSession != null && siswaIdSession.equals(siswaId);
        } else if ("all".equals(allowedMenu) && wid != null) {
            Long waliKelasId = wid instanceof Long ? (Long) wid : (wid != null ? Long.valueOf(wid.toString()) : null);
            if (waliKelasId != null) {
                com.example.si_swa.entity.WaliKelas loggedInWk = waliKelasService.getWaliKelasById(waliKelasId);
                if (loggedInWk != null && loggedInWk.getNamaKelasWalian() != null) {
                    Siswa s = siswaService.getSiswaById(siswaId);
                    if (s != null && s.getKelas() != null && s.getKelas().equals(loggedInWk.getNamaKelasWalian())) {
                        canView = true;
                    }
                }
            }
        }
        
        if (!canView) {
            return "redirect:/login";
        }
        
        Siswa s = siswaService.getSiswaById(siswaId);
        List<Raport> rlist = raportService.findBySiswa(siswaId);
        Raport latest = null;
        if (rlist != null && !rlist.isEmpty()) {
            for (Raport r : rlist) {
                if (latest == null) {
                    latest = r;
                } else {
                    java.util.Date ta = r.getTanggalDiterbitkan();
                    java.util.Date tb = latest.getTanggalDiterbitkan();
                    long la = ta != null ? ta.getTime() : 0L;
                    long lb = tb != null ? tb.getTime() : 0L;
                    if (la > lb) {
                        latest = r;
                    }
                }
            }
        }
        
        com.example.si_swa.entity.WaliKelas wk = null;
        if (wid != null) {
            Long waliKelasId = wid instanceof Long ? (Long) wid : (wid != null ? Long.valueOf(wid.toString()) : null);
            if (waliKelasId != null) {
                wk = waliKelasService.getWaliKelasById(waliKelasId);
            }
        }
        
        if (wk == null && latest != null && latest.getWaliKelasId() != null) {
            wk = waliKelasService.getWaliKelasById(latest.getWaliKelasId());
        }
        java.util.HashMap<com.example.si_swa.entity.MataPelajaran, Integer> nilai = latest != null ? latest.getNilai() : new java.util.HashMap<>();
        float avg = 0f;
        if (!nilai.isEmpty()) {
            float sum = 0f;
            int count = 0;
            for (Integer v : nilai.values()) {
                if (v != null) {
                    sum += v;
                    count++;
                }
            }
            avg = count > 0 ? sum / count : 0f;
        }
        int totalMapel = nilai != null ? nilai.size() : 0;
        int passCount = 0;
        if (nilai != null) {
            for (Integer v : nilai.values()) {
                if (v != null && v >= 75) {
                    passCount++;
                }
            }
        }
        float persenLulus = totalMapel > 0 ? (passCount * 100f / totalMapel) : 0f;
        List<com.example.si_swa.entity.MataPelajaran> mlist = mataPelajaranService.getAll();
        java.util.Map<String, String> mapelNames = new java.util.HashMap<>();
        for (com.example.si_swa.entity.MataPelajaran m : mlist) {
            if (m != null && m.getIdMataPelajaran() != null) {
                mapelNames.put(m.getIdMataPelajaran(), m.getNama() + " (" + m.getKode() + ")");
            }
        }
        model.addAttribute("siswa", s);
        model.addAttribute("waliKelas", wk);
        model.addAttribute("nilai", nilai);
        model.addAttribute("rataRata", avg);
        model.addAttribute("raportTerbaru", latest);
        model.addAttribute("mapelNames", mapelNames);
        model.addAttribute("totalMapel", totalMapel);
        model.addAttribute("passCount", passCount);
        model.addAttribute("persenLulus", persenLulus);
        model.addAttribute("allowedMenu", allowedMenu != null ? allowedMenu : "");
        return "raport-cetak.html";
    }

    @PostMapping(value={"/raport/buat", "/raport/buat/"})
    public String buat(@RequestParam("siswaId") Long siswaId,
                       @RequestParam("nomorRapot") String nomorRapot,
                       @RequestParam("tanggalDiterbitkan") @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd") java.util.Date tanggalDiterbitkan,
                       @RequestParam(value="catatanWaliKelas", required=false) String catatanWaliKelas,
                       @RequestParam(value="statusKenaikanKelas", required=false) String statusKenaikanKelas,
                       @RequestParam(value="mapelIds", required=false) java.util.List<String> mapelIds,
                       @RequestParam(value="nilai", required=false) java.util.List<Integer> nilai,
                       jakarta.servlet.http.HttpSession session,
                       org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        Object wid = session != null ? session.getAttribute("waliKelasId") : null;
        Long wkId = null;
        if (wid != null) {
            wkId = wid instanceof Long ? (Long) wid : (wid != null ? Long.valueOf(wid.toString()) : null);
        }
        
        if (wkId == null && siswaId != null) {
            Siswa s = siswaService.getSiswaById(siswaId);
            if (s != null && s.getKelas() != null) {
                List<WaliKelas> wkList = waliKelasService.getAllWaliKelas();
                for (WaliKelas wk : wkList) {
                    if (s.getKelas().equals(wk.getNamaKelasWalian())) {
                        wkId = wk.getId();
                        break;
                    }
                }
            }
        }
        
        if (wkId == null) {
            ra.addFlashAttribute("error", "Wali Kelas tidak ditemukan");
            return "redirect:/raport";
        }
        
        java.util.Map<String, Integer> map = new java.util.HashMap<>();
        if (mapelIds != null && nilai != null) {
            int n = Math.min(mapelIds.size(), nilai.size());
            for (int i = 0; i < n; i++) {
                String mid = mapelIds.get(i);
                Integer v = nilai.get(i);
                if (mid != null && v != null) {
                    map.put(mid, v);
                }
            }
        }
        raportService.createRaport(wkId, siswaId, nomorRapot, tanggalDiterbitkan, catatanWaliKelas, statusKenaikanKelas, map);
        return "redirect:/raport";
    }
}