package com.prox.babyvaccinationtracker;

import com.prox.babyvaccinationtracker.model.BabyCheckList;
import com.prox.babyvaccinationtracker.model.Regimen;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class VaccineRegimen {
    public static Date addMonth(Date date, int monthsToAdd) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, monthsToAdd);
        return calendar.getTime();
    }

    //  tạo hàm lênh lịch cho bệnh Lao
    public static List<Regimen> RegimenTuberculosis(Date birthday, BabyCheckList checkList) {
        List<Regimen> schedules = new ArrayList<>();
        Boolean isTuberculosis = checkList.isTuberculosis();
        schedules.add(new Regimen("Lao", birthday, isTuberculosis, "tuberculosis"));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Viêm gan B
    public static List<Regimen> RegimenHepatitisB(Date birthday, BabyCheckList checkList) {
        List<Regimen> schedules = new ArrayList<>();
        Boolean isHepatitisB = checkList.isHepatitis_b();
        schedules.add(new Regimen("Viêm gan B", birthday, isHepatitisB, "hepatitis_b"));
        schedules.add(new Regimen("Viêm gan B", addMonth(birthday, 2), isHepatitisB, "hepatitis_b"));
        schedules.add(new Regimen("Viêm gan B", addMonth(birthday, 3), isHepatitisB, "hepatitis_b"));
        schedules.add(new Regimen("Viêm gan B", addMonth(birthday, 4), isHepatitisB, "hepatitis_b"));
        schedules.add(new Regimen("Viêm gan B", addMonth(birthday, 18), isHepatitisB, "hepatitis_b"));
        schedules.add(new Regimen("Viêm gan B", addMonth(birthday, 84), isHepatitisB, "hepatitis_b"));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Bạch hầu, ho gà, uốn ván
    public static List<Regimen> RegimenDiphtheriaWhoopingCoughPoliomyelitis(Date birthday, BabyCheckList checkList) {
        List<Regimen> schedules = new ArrayList<>();
        Boolean isDiphtheriaWhoopingCoughPoliomyelitis = checkList.isDiphtheria_whooping_cough_poliomyelitis();
        schedules.add(new Regimen("Bạch hầu, ho gà, uốn ván", birthday, isDiphtheriaWhoopingCoughPoliomyelitis, "diphtheria_whooping_cough_poliomyelitis"));
        schedules.add(new Regimen("Bạch hầu, ho gà, uốn ván", addMonth(birthday, 2), isDiphtheriaWhoopingCoughPoliomyelitis, "diphtheria_whooping_cough_poliomyelitis"));
        schedules.add(new Regimen("Bạch hầu, ho gà, uốn ván", addMonth(birthday, 3), isDiphtheriaWhoopingCoughPoliomyelitis, "diphtheria_whooping_cough_poliomyelitis"));
        schedules.add(new Regimen("Bạch hầu, ho gà, uốn ván", addMonth(birthday, 4), isDiphtheriaWhoopingCoughPoliomyelitis, "diphtheria_whooping_cough_poliomyelitis"));
        schedules.add(new Regimen("Bạch hầu, ho gà, uốn ván", addMonth(birthday, 18), isDiphtheriaWhoopingCoughPoliomyelitis, "diphtheria_whooping_cough_poliomyelitis"));
        schedules.add(new Regimen("Bạch hầu, ho gà, uốn ván", addMonth(birthday, 60), isDiphtheriaWhoopingCoughPoliomyelitis, "diphtheria_whooping_cough_poliomyelitis"));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Bại liệt
    public static List<Regimen> RegimenParalysis(Date birthday, BabyCheckList checkList) {
        List<Regimen> schedules = new ArrayList<>();
        Boolean isParalysis = checkList.isParalysis();
        schedules.add(new Regimen("Bại liệt", birthday, isParalysis, "paralysis"));
        schedules.add(new Regimen("Bại liệt", addMonth(birthday, 2), isParalysis, "paralysis"));
        schedules.add(new Regimen("Bại liệt", addMonth(birthday, 3), isParalysis, "paralysis"));
        schedules.add(new Regimen("Bại liệt", addMonth(birthday, 4), isParalysis, "paralysis"));
        schedules.add(new Regimen("Bại liệt", addMonth(birthday, 18), isParalysis, "paralysis"));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Viêm phổi, viêm màng não mủ do Hib
    public static List<Regimen> RegimenHib(Date birthday, BabyCheckList checkList) {
        List<Regimen> schedules = new ArrayList<>();
        Boolean isPneumoniaHibMeningitis = checkList.isPneumonia_hib_meningitis();
        schedules.add(new Regimen("Viêm phổi, viêm màng não mủ do Hib", birthday, isPneumoniaHibMeningitis, "pneumonia_hib_meningitis"));
        schedules.add(new Regimen("Viêm phổi, viêm màng não mủ do Hib", addMonth(birthday, 2), isPneumoniaHibMeningitis, "pneumonia_hib_meningitis"));
        schedules.add(new Regimen("Viêm phổi, viêm màng não mủ do Hib", addMonth(birthday, 3), isPneumoniaHibMeningitis, "pneumonia_hib_meningitis"));
        schedules.add(new Regimen("Viêm phổi, viêm màng não mủ do Hib", addMonth(birthday, 4), isPneumoniaHibMeningitis, "pneumonia_hib_meningitis"));
        schedules.add(new Regimen("Viêm phổi, viêm màng não mủ do Hib", addMonth(birthday, 18), isPneumoniaHibMeningitis, "pneumonia_hib_meningitis"));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Tiêu chảy do Rota Virus
    public static List<Regimen> RegimenRotaVirus(Date birthday, BabyCheckList checkList) {
        List<Regimen> schedules = new ArrayList<>();
        Boolean isRotavirusDiarrhea = checkList.isRotavirus_diarrhea();
        schedules.add(new Regimen("Tiêu chảy do Rota Virus", addMonth(birthday, 2), isRotavirusDiarrhea, "rotavirus_diarrhea"));
        schedules.add(new Regimen("Tiêu chảy do Rota Virus", addMonth(birthday, 3), isRotavirusDiarrhea, "rotavirus_diarrhea"));
        schedules.add(new Regimen("Tiêu chảy do Rota Virus", addMonth(birthday, 4), isRotavirusDiarrhea, "rotavirus_diarrhea"));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Viêm phổi, viêm màng não, viêm tai giữa do phế cầu khuẩn
    public static List<Regimen> RegimenPneumococcal(Date birthday, BabyCheckList checkList) {
        List<Regimen> schedules = new ArrayList<>();
        Boolean isPneumoniaMeningitisOtitisMediaCausedByStreptococcus = checkList.isPneumonia_meningitis_otitis_media_caused_by_streptococcus();
        schedules.add(new Regimen("Viêm phổi, viêm màng não, viêm tai giữa do phế cầu khuẩn", addMonth(birthday, 2), isPneumoniaMeningitisOtitisMediaCausedByStreptococcus, "pneumonia_meningitis_otitis_media_caused_by_streptococcus"));
        schedules.add(new Regimen("Viêm phổi, viêm màng não, viêm tai giữa do phế cầu khuẩn", addMonth(birthday, 3), isPneumoniaMeningitisOtitisMediaCausedByStreptococcus, "pneumonia_meningitis_otitis_media_caused_by_streptococcus"));
        schedules.add(new Regimen("Viêm phổi, viêm màng não, viêm tai giữa do phế cầu khuẩn", addMonth(birthday, 4), isPneumoniaMeningitisOtitisMediaCausedByStreptococcus, "pneumonia_meningitis_otitis_media_caused_by_streptococcus"));
        schedules.add(new Regimen("Viêm phổi, viêm màng não, viêm tai giữa do phế cầu khuẩn", addMonth(birthday, 10), isPneumoniaMeningitisOtitisMediaCausedByStreptococcus, "pneumonia_meningitis_otitis_media_caused_by_streptococcus"));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Viêm màng não, nhiễm khuẩn huyết, viêm phổi do não mô cầu khuẩn B,C
    public static List<Regimen> RegimenMeningococcal(Date birthday, BabyCheckList checkList) {
        List<Regimen> schedules = new ArrayList<>();
        Boolean isMeningitisSepsisPneumoniaCausedByNeisseriaMeningitidisBC = checkList.isMeningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_b_c();
        schedules.add(new Regimen("Viêm màng não, nhiễm khuẩn huyết, viêm phổi do não mô cầu khuẩn B,C", addMonth(birthday, 6), isMeningitisSepsisPneumoniaCausedByNeisseriaMeningitidisBC, "meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_b_c"));
        schedules.add(new Regimen("Viêm màng não, nhiễm khuẩn huyết, viêm phổi do não mô cầu khuẩn B,C", addMonth(birthday, 8), isMeningitisSepsisPneumoniaCausedByNeisseriaMeningitidisBC, "meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_b_c"));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Cúm
    public static List<Regimen> RegimenInfluenza(Date birthday, BabyCheckList checkList) {
        List<Regimen> schedules = new ArrayList<>();
        Boolean isInfluenza = checkList.isInfluenza();
        schedules.add(new Regimen("Cúm", addMonth(birthday, 6), isInfluenza, "influenza"));
        schedules.add(new Regimen("Cúm", addMonth(birthday, 7), isInfluenza, "influenza"));
        for (int i = 0; i < 12*18; i+= 12) {
            schedules.add(new Regimen("Cúm", addMonth(birthday, 6 + i), false, "influenza"));
        }
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Sởi
    public static List<Regimen> RegimenMeasles(Date birthday, BabyCheckList checkList) {
        List<Regimen> schedules = new ArrayList<>();
        Boolean isMeasles = checkList.isMeasles();
        schedules.add(new Regimen("Sởi", addMonth(birthday, 9), isMeasles, "measles"));
        schedules.add(new Regimen("Sởi", addMonth(birthday, 18), isMeasles, "measles"));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Viêm màng não, nhiễm khuẩn huyết, viêm phổi do não mô cầu khuẩn A,C,W,Y
    public static List<Regimen> RegimenMeningococcalACWY(Date birthday, BabyCheckList checkList) {
        List<Regimen> schedules = new ArrayList<>();
        Boolean isMeningitisSepsisPneumoniaCausedByNeisseriaMeningitidisACWY = checkList.isMeningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_a_c_w_y();
        schedules.add(new Regimen("Viêm màng não, nhiễm khuẩn huyết, viêm phổi do não mô cầu khuẩn A,C,W,Y", addMonth(birthday, 9), isMeningitisSepsisPneumoniaCausedByNeisseriaMeningitidisACWY, "meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_a_c_w_y"));
        schedules.add(new Regimen("Viêm màng não, nhiễm khuẩn huyết, viêm phổi do não mô cầu khuẩn A,C,W,Y", addMonth(birthday, 12), isMeningitisSepsisPneumoniaCausedByNeisseriaMeningitidisACWY, "meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_a_c_w_y"));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Viêm não Nhật Bản
    public static List<Regimen> RegimenJapaneseEncephalitis(Date birthday, BabyCheckList checkList) {
        List<Regimen> schedules = new ArrayList<>();
        Boolean isJapaneseEncephalitis = checkList.isJapanese_encephalitis();
        schedules.add(new Regimen("Viêm não Nhật Bản", addMonth(birthday, 9), isJapaneseEncephalitis,  "japanese_encephalitis"));
        schedules.add(new Regimen("Viêm não Nhật Bản", addMonth(birthday, 21), isJapaneseEncephalitis,  "japanese_encephalitis"));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Sởi, Quai bị, Rubella
    public static List<Regimen> RegimenMeaslesMumpsRubella(Date birthday, BabyCheckList checkList) {
        List<Regimen> schedules = new ArrayList<>();
        Boolean isMeaslesMumpsRubella = checkList.isMeasles_mumps_rubella();
        schedules.add(new Regimen("Sởi, Quai bị, Rubella", addMonth(birthday, 12),isMeaslesMumpsRubella, "measles_mumps_rubella"));
        schedules.add(new Regimen("Sởi, Quai bị, Rubella", addMonth(birthday, 36),isMeaslesMumpsRubella, "measles_mumps_rubella"));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Thủy đậu
    public static List<Regimen> RegimenVaricella(Date birthday, BabyCheckList checkList) {
        List<Regimen> schedules = new ArrayList<>();
        Boolean isChickenpox = checkList.isChickenpox();
        schedules.add(new Regimen("Thủy đậu", addMonth(birthday, 9), isChickenpox, "chickenpox"));
        schedules.add(new Regimen("Thủy đậu", addMonth(birthday, 12), isChickenpox, "chickenpox"));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Viêm gan A
    public static List<Regimen> RegimenHepatitisA(Date birthday, BabyCheckList checkList) {
        List<Regimen> schedules = new ArrayList<>();
        Boolean isHepatitisA = checkList.isHepatitis_a();
        schedules.add(new Regimen("Viêm gan A", addMonth(birthday, 12), isHepatitisA, "hepatitis_a"));
        schedules.add(new Regimen("Viêm gan A", addMonth(birthday, 18), isHepatitisA, "hepatitis_a"));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Viêm gan A + B
    public static List<Regimen> RegimenHepatitisAB(Date birthday, BabyCheckList checkList) {
        List<Regimen> schedules = new ArrayList<>();
        Boolean isHepatitisAB = checkList.isHepatitis_a_b();
        schedules.add(new Regimen("Viêm gan A + B", addMonth(birthday, 12), isHepatitisAB, "hepatitis_a_b"));
        schedules.add(new Regimen("Viêm gan A + B", addMonth(birthday, 18), isHepatitisAB, "hepatitis_a_b"));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Thương hàn
    public static List<Regimen> RegimenTetanus(Date birthday, BabyCheckList checkList) {
        List<Regimen> schedules = new ArrayList<>();
        Boolean isTetanus = checkList.isTetanus();
        schedules.add(new Regimen("Thương hàn", addMonth(birthday, 24), isTetanus, "tetanus"));
        for (int i = 0; i < 12*18; i+= 12) {
            schedules.add(new Regimen("Thương hàn", addMonth(birthday, 24 + i), false, "tetanus"));
        }
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Bệnh tả
    public static List<Regimen> RegimenTyphoid(Date birthday, BabyCheckList checkList) {
        List<Regimen> schedules = new ArrayList<>();
        Boolean isTyphoid = checkList.isAnthrax();
        schedules.add(new Regimen("Bệnh tả", addMonth(birthday, 24), isTyphoid, "anthrax"));
        schedules.add(new Regimen("Bệnh tả", addMonth(birthday, 25), isTyphoid, "anthrax"));
        return schedules;
    }


    public static List<Regimen> getVaccinationRegimen(String dateOfBirth, BabyCheckList checkList) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date birthday = sdf.parse(dateOfBirth);
        List<Regimen> schedules = new ArrayList<>();
        schedules.addAll(RegimenTuberculosis(birthday, checkList));
        schedules.addAll(RegimenHepatitisB(birthday, checkList));
        schedules.addAll(RegimenDiphtheriaWhoopingCoughPoliomyelitis(birthday, checkList));
        schedules.addAll(RegimenParalysis(birthday, checkList));
        schedules.addAll(RegimenHib(birthday, checkList));
        schedules.addAll(RegimenRotaVirus(birthday, checkList));
        schedules.addAll(RegimenPneumococcal(birthday, checkList));
        schedules.addAll(RegimenMeningococcal(birthday, checkList));
        schedules.addAll(RegimenInfluenza(birthday, checkList));
        schedules.addAll(RegimenMeasles(birthday, checkList));
        schedules.addAll(RegimenMeningococcalACWY(birthday, checkList));
        schedules.addAll(RegimenJapaneseEncephalitis(birthday, checkList));
        schedules.addAll(RegimenMeaslesMumpsRubella(birthday, checkList));
        schedules.addAll(RegimenVaricella(birthday, checkList));
        schedules.addAll(RegimenHepatitisA(birthday, checkList));
        schedules.addAll(RegimenHepatitisAB(birthday, checkList));
        schedules.addAll(RegimenTetanus(birthday, checkList));
        schedules.addAll(RegimenTyphoid(birthday, checkList));

        Collections.sort(schedules);

        return schedules;
    }
}
