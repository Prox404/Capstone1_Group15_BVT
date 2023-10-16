package com.prox.babyvaccinationtracker;

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
    public static List<Regimen> RegimenTuberculosis(Date birthday) {
        List<Regimen> schedules = new ArrayList<>();
        schedules.add(new Regimen("Lao", birthday, false, "tuberculosis"));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Viêm gan B
    public static List<Regimen> RegimenHepatitisB(Date birthday) {
        List<Regimen> schedules = new ArrayList<>();
        schedules.add(new Regimen("Viêm gan B", birthday, false, "hepatitis_b"));
        schedules.add(new Regimen("Viêm gan B", addMonth(birthday, 2), false, "hepatitis_b"));
        schedules.add(new Regimen("Viêm gan B", addMonth(birthday, 3), false, "hepatitis_b"));
        schedules.add(new Regimen("Viêm gan B", addMonth(birthday, 4), false, "hepatitis_b"));
        schedules.add(new Regimen("Viêm gan B", addMonth(birthday, 18), false, "hepatitis_b"));
        schedules.add(new Regimen("Viêm gan B", addMonth(birthday, 84), false, "hepatitis_b"));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Bạch hầu, ho gà, uốn ván
    public static List<Regimen> RegimenDiphtheriaWhoopingCoughPoliomyelitis(Date birthday) {
        List<Regimen> schedules = new ArrayList<>();
        schedules.add(new Regimen("Bạch hầu, ho gà, uốn ván", birthday, false, "diphtheria_whooping_cough_poliomyelitis"));
        schedules.add(new Regimen("Bạch hầu, ho gà, uốn ván", addMonth(birthday, 2), false, "diphtheria_whooping_cough_poliomyelitis"));
        schedules.add(new Regimen("Bạch hầu, ho gà, uốn ván", addMonth(birthday, 3), false, "diphtheria_whooping_cough_poliomyelitis"));
        schedules.add(new Regimen("Bạch hầu, ho gà, uốn ván", addMonth(birthday, 4), false, "diphtheria_whooping_cough_poliomyelitis"));
        schedules.add(new Regimen("Bạch hầu, ho gà, uốn ván", addMonth(birthday, 18), false, "diphtheria_whooping_cough_poliomyelitis"));
        schedules.add(new Regimen("Bạch hầu, ho gà, uốn ván", addMonth(birthday, 60), false, "diphtheria_whooping_cough_poliomyelitis"));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Bại liệt
    public static List<Regimen> RegimenParalysis(Date birthday) {
        List<Regimen> schedules = new ArrayList<>();
        schedules.add(new Regimen("Bại liệt", birthday, false, "paralysis"));
        schedules.add(new Regimen("Bại liệt", addMonth(birthday, 2), false, "paralysis"));
        schedules.add(new Regimen("Bại liệt", addMonth(birthday, 3), false, "paralysis"));
        schedules.add(new Regimen("Bại liệt", addMonth(birthday, 4), false, "paralysis"));
        schedules.add(new Regimen("Bại liệt", addMonth(birthday, 18), false, "paralysis"));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Viêm phổi, viêm màng não mủ do Hib
    public static List<Regimen> RegimenHib(Date birthday) {
        List<Regimen> schedules = new ArrayList<>();
        schedules.add(new Regimen("Viêm phổi, viêm màng não mủ do Hib", birthday, false, "pneumonia_hib_meningitis"));
        schedules.add(new Regimen("Viêm phổi, viêm màng não mủ do Hib", addMonth(birthday, 2), false, "pneumonia_hib_meningitis"));
        schedules.add(new Regimen("Viêm phổi, viêm màng não mủ do Hib", addMonth(birthday, 3), false, "pneumonia_hib_meningitis"));
        schedules.add(new Regimen("Viêm phổi, viêm màng não mủ do Hib", addMonth(birthday, 4), false, "pneumonia_hib_meningitis"));
        schedules.add(new Regimen("Viêm phổi, viêm màng não mủ do Hib", addMonth(birthday, 18), false, "pneumonia_hib_meningitis"));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Tiêu chảy do Rota Virus
    public static List<Regimen> RegimenRotaVirus(Date birthday) {
        List<Regimen> schedules = new ArrayList<>();
        schedules.add(new Regimen("Tiêu chảy do Rota Virus", addMonth(birthday, 2), false, "rotavirus_diarrhea"));
        schedules.add(new Regimen("Tiêu chảy do Rota Virus", addMonth(birthday, 3), false, "rotavirus_diarrhea"));
        schedules.add(new Regimen("Tiêu chảy do Rota Virus", addMonth(birthday, 4), false, "rotavirus_diarrhea"));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Viêm phổi, viêm màng não, viêm tai giữa do phế cầu khuẩn
    public static List<Regimen> RegimenPneumococcal(Date birthday) {
        List<Regimen> schedules = new ArrayList<>();
        schedules.add(new Regimen("Viêm phổi, viêm màng não, viêm tai giữa do phế cầu khuẩn", addMonth(birthday, 2), false, "pneumonia_meningitis_otitis_media_caused_by_streptococcus"));
        schedules.add(new Regimen("Viêm phổi, viêm màng não, viêm tai giữa do phế cầu khuẩn", addMonth(birthday, 3), false, "pneumonia_meningitis_otitis_media_caused_by_streptococcus"));
        schedules.add(new Regimen("Viêm phổi, viêm màng não, viêm tai giữa do phế cầu khuẩn", addMonth(birthday, 4), false, "pneumonia_meningitis_otitis_media_caused_by_streptococcus"));
        schedules.add(new Regimen("Viêm phổi, viêm màng não, viêm tai giữa do phế cầu khuẩn", addMonth(birthday, 10), false, "pneumonia_meningitis_otitis_media_caused_by_streptococcus"));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Viêm màng não, nhiễm khuẩn huyết, viêm phổi do não mô cầu khuẩn B,C
    public static List<Regimen> RegimenMeningococcal(Date birthday) {
        List<Regimen> schedules = new ArrayList<>();
        schedules.add(new Regimen("Viêm màng não, nhiễm khuẩn huyết, viêm phổi do não mô cầu khuẩn B,C", addMonth(birthday, 6), false, "meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_b_c"));
        schedules.add(new Regimen("Viêm màng não, nhiễm khuẩn huyết, viêm phổi do não mô cầu khuẩn B,C", addMonth(birthday, 8), false, "meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_b_c"));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Cúm
    public static List<Regimen> RegimenInfluenza(Date birthday) {
        List<Regimen> schedules = new ArrayList<>();
        schedules.add(new Regimen("Cúm", addMonth(birthday, 6), false, "influenza"));
        schedules.add(new Regimen("Cúm", addMonth(birthday, 7), false, "influenza"));
        for (int i = 0; i < 12*18; i+= 12) {
            schedules.add(new Regimen("Cúm", addMonth(birthday, 6 + i), false, "influenza"));
        }
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Sởi
    public static List<Regimen> RegimenMeasles(Date birthday) {
        List<Regimen> schedules = new ArrayList<>();
        schedules.add(new Regimen("Sởi", addMonth(birthday, 9), false, "measles"));
        schedules.add(new Regimen("Sởi", addMonth(birthday, 18), false, "measles"));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Viêm màng não, nhiễm khuẩn huyết, viêm phổi do não mô cầu khuẩn A,C,W,Y
    public static List<Regimen> RegimenMeningococcalACWY(Date birthday) {
        List<Regimen> schedules = new ArrayList<>();
        schedules.add(new Regimen("Viêm màng não, nhiễm khuẩn huyết, viêm phổi do não mô cầu khuẩn A,C,W,Y", addMonth(birthday, 9), false, "meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_a_c_w_y"));
        schedules.add(new Regimen("Viêm màng não, nhiễm khuẩn huyết, viêm phổi do não mô cầu khuẩn A,C,W,Y", addMonth(birthday, 12), false, "meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_a_c_w_y"));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Viêm não Nhật Bản
    public static List<Regimen> RegimenJapaneseEncephalitis(Date birthday) {
        List<Regimen> schedules = new ArrayList<>();
        schedules.add(new Regimen("Viêm não Nhật Bản", addMonth(birthday, 9), false,  "japanese_encephalitis"));
        schedules.add(new Regimen("Viêm não Nhật Bản", addMonth(birthday, 21), false,  "japanese_encephalitis"));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Sởi, Quai bị, Rubella
    public static List<Regimen> RegimenMeaslesMumpsRubella(Date birthday) {
        List<Regimen> schedules = new ArrayList<>();
        schedules.add(new Regimen("Sởi, Quai bị, Rubella", addMonth(birthday, 12),false, "measles_mumps_rubella"));
        schedules.add(new Regimen("Sởi, Quai bị, Rubella", addMonth(birthday, 36),false, "measles_mumps_rubella"));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Thủy đậu
    public static List<Regimen> RegimenVaricella(Date birthday) {
        List<Regimen> schedules = new ArrayList<>();
        schedules.add(new Regimen("Thủy đậu", addMonth(birthday, 9), false, "chickenpox"));
        schedules.add(new Regimen("Thủy đậu", addMonth(birthday, 12), false, "chickenpox"));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Viêm gan A
    public static List<Regimen> RegimenHepatitisA(Date birthday) {
        List<Regimen> schedules = new ArrayList<>();
        schedules.add(new Regimen("Viêm gan A", addMonth(birthday, 12), false, "hepatitis_a"));
        schedules.add(new Regimen("Viêm gan A", addMonth(birthday, 18), false, "hepatitis_a"));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Viêm gan A + B
    public static List<Regimen> RegimenHepatitisAB(Date birthday) {
        List<Regimen> schedules = new ArrayList<>();
        schedules.add(new Regimen("Viêm gan A + B", addMonth(birthday, 12), false, "hepatitis_a_b"));
        schedules.add(new Regimen("Viêm gan A + B", addMonth(birthday, 18), false, "hepatitis_a_b"));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Thương hàn
    public static List<Regimen> RegimenTetanus(Date birthday) {
        List<Regimen> schedules = new ArrayList<>();
        schedules.add(new Regimen("Thương hàn", addMonth(birthday, 24), false, "tetanus"));
        for (int i = 0; i < 12*18; i+= 12) {
            schedules.add(new Regimen("Thương hàn", addMonth(birthday, 24 + i), false, "tetanus"));
        }
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Bệnh tả
    public static List<Regimen> RegimenTyphoid(Date birthday) {
        List<Regimen> schedules = new ArrayList<>();
        schedules.add(new Regimen("Bệnh tả", addMonth(birthday, 24), false, "anthrax"));
        schedules.add(new Regimen("Bệnh tả", addMonth(birthday, 25), false, "anthrax"));
        return schedules;
    }


    public static List<Regimen> getVaccinationRegimen(String dateOfBirth) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date birthday = sdf.parse(dateOfBirth);
        List<Regimen> schedules = new ArrayList<>();
        schedules.addAll(RegimenTuberculosis(birthday));
        schedules.addAll(RegimenHepatitisB(birthday));
        schedules.addAll(RegimenDiphtheriaWhoopingCoughPoliomyelitis(birthday));
        schedules.addAll(RegimenParalysis(birthday));
        schedules.addAll(RegimenHib(birthday));
        schedules.addAll(RegimenRotaVirus(birthday));
        schedules.addAll(RegimenPneumococcal(birthday));
        schedules.addAll(RegimenMeningococcal(birthday));
        schedules.addAll(RegimenInfluenza(birthday));
        schedules.addAll(RegimenMeasles(birthday));
        schedules.addAll(RegimenMeningococcalACWY(birthday));
        schedules.addAll(RegimenJapaneseEncephalitis(birthday));
        schedules.addAll(RegimenMeaslesMumpsRubella(birthday));
        schedules.addAll(RegimenVaricella(birthday));
        schedules.addAll(RegimenHepatitisA(birthday));
        schedules.addAll(RegimenHepatitisAB(birthday));
        schedules.addAll(RegimenTetanus(birthday));
        schedules.addAll(RegimenTyphoid(birthday));

        Collections.sort(schedules);

        return schedules;
    }
}
