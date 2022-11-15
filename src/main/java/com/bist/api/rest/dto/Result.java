package com.bist.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    @JsonProperty("Tanim")
    private String Tanim;
    @JsonProperty("SonFiyat")
    private String SonFiyat;
    @JsonProperty("Fark")
    private String Fark;
    @JsonProperty("FarkYuzde")
    private String FarkYuzde;
    @JsonProperty("OncGun")
    private String OncGun;
    @JsonProperty("OncHafta")
    private String OncHafta;
    @JsonProperty("Onc1Hafta")
    private String Onc1Hafta;
    @JsonProperty("OncAy")
    private String OncAy;
    @JsonProperty("Onc1Ay")
    private String Onc1Ay;
    @JsonProperty("Onc3Ay")
    private String Onc3Ay;
    @JsonProperty("Onc6Ay")
    private String Onc6Ay;
    @JsonProperty("OncYil")
    private String OncYil;
    @JsonProperty("Onc1Yil")
    private String Onc1Yil;
    @JsonProperty("FK")
    private String FK;
    @JsonProperty("HalkakAciklik")
    private String HalkakAciklik;
    @JsonProperty("PiyDegDefterDeg")
    private String PiyDegDefterDeg;
    @JsonProperty("PiyasaDegeri")
    private String PiyasaDegeri;
    @JsonProperty("DefterDegeri")
    private String DefterDegeri;
    @JsonProperty("BilancoDonemi")
    private String BilancoDonemi;
    @JsonProperty("Sermaye")
    private String Sermaye;
    @JsonProperty("OzSermaye")
    private String OzSermaye;
    @JsonProperty("NetKar")
    private String NetKar;
    @JsonProperty("SenetSayisi")
    private String SenetSayisi;
    @JsonProperty("Hacim")
    private String Hacim;
    @JsonProperty("Acilis")
    private String Acilis;
    @JsonProperty("Yuksek")
    private String Yuksek;
    @JsonProperty("Dusuk")
    private String Dusuk;
    @JsonProperty("Ortalama")
    private String Ortalama;
    @JsonProperty("Tarih")
    private String Tarih;
    @JsonProperty("Grup")
    private String Grup;
    @JsonProperty("Sektor")
    private String Sektor;
    @JsonProperty("Taban")
    private String Taban;
    @JsonProperty("Tavan")
    private String Tavan;
    @JsonProperty("Lot")
    private String Lot;
    @JsonProperty("Saat")
    private String Saat;
    @JsonProperty("SonDortCeyrek")
    private String SonDortCeyrek;
    @JsonProperty("ParaGiris_Cikis")
    private String ParaGiris_Cikis;
    @JsonProperty("PiyasaDegeriDolar")
    private String PiyasaDegeriDolar;
}
