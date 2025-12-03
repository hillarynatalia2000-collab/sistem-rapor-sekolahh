package com.example.si_swa.entity;

public interface ClassroomActions {
    void mengelolaData(Object data);
    String membuatLaporan();
    void mengupdateNilaiRaport(double nilai);
    double menghitungNilaiRataRata();
    boolean validasiLogin(String username, String password);
}