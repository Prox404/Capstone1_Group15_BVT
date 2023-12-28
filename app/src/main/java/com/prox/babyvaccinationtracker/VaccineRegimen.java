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

    private static Date currentDate = new Date();

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

        int[] monthsToAdd = {2, 3, 4, 18, 84};
        for (int months : monthsToAdd) {
            Date date = addMonth(birthday, months);
            if (isHepatitisB && date.before(currentDate)) {
                schedules.add(new Regimen("Viêm gan B", date, true, "hepatitis_b"));
            }else{
                schedules.add(new Regimen("Viêm gan B", date, false, "hepatitis_b"));
            }
        }

        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Bạch hầu, ho gà, uốn ván
    public static List<Regimen> RegimenDiphtheriaWhoopingCoughPoliomyelitis(Date birthday, BabyCheckList checkList) {
        List<Regimen> schedules = new ArrayList<>();
        Boolean isDiphtheriaWhoopingCoughPoliomyelitis = checkList.isDiphtheria_whooping_cough_poliomyelitis();

        int[] monthsToAdd = {2, 3, 4, 18, 60};
        for (int months : monthsToAdd) {
            Date date = addMonth(birthday, months);
            schedules.add(new Regimen("Bạch hầu, ho gà, uốn ván", date, isDiphtheriaWhoopingCoughPoliomyelitis && date.before(currentDate), "diphtheria_whooping_cough_poliomyelitis"));
        }

        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Bại liệt
    public static List<Regimen> RegimenParalysis(Date birthday, BabyCheckList checkList) {
        List<Regimen> schedules = new ArrayList<>();
        Boolean isParalysis = checkList.isParalysis();

        int[] monthsToAdd = {2, 3, 4, 18};
        for (int months : monthsToAdd) {
            Date date = addMonth(birthday, months);
            schedules.add(new Regimen("Bại liệt", date, isParalysis && date.before(currentDate), "paralysis"));
        }

        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Viêm phổi, viêm màng não mủ do Hib
    public static List<Regimen> RegimenHib(Date birthday, BabyCheckList checkList) {
        List<Regimen> schedules = new ArrayList<>();
        Boolean isPneumoniaHibMeningitis = checkList.isPneumonia_hib_meningitis();

        int[] monthsToAdd = {2, 3, 4, 18};
        for (int months : monthsToAdd) {
            Date date = addMonth(birthday, months);
            schedules.add(new Regimen("Viêm phổi, viêm màng não mủ do Hib", date, isPneumoniaHibMeningitis && date.before(currentDate), "pneumonia_hib_meningitis"));
        }

        return schedules;
    }


    //  tạo hàm lênh lịch cho bệnh Tiêu chảy do Rota Virus
    public static List<Regimen> RegimenRotaVirus(Date birthday, BabyCheckList checkList) {
        List<Regimen> schedules = new ArrayList<>();
        Boolean isRotavirusDiarrhea = checkList.isRotavirus_diarrhea();

        int[] monthsToAdd = {2, 3, 4};
        for (int months : monthsToAdd) {
            Date date = addMonth(birthday, months);
            schedules.add(new Regimen("Tiêu chảy do Rota Virus", date, isRotavirusDiarrhea && date.before(currentDate), "rotavirus_diarrhea"));
        }

        return schedules;
    }


    //  tạo hàm lênh lịch cho bệnh Viêm phổi, viêm màng não, viêm tai giữa do phế cầu khuẩn
    public static List<Regimen> RegimenPneumococcal(Date birthday, BabyCheckList checkList) {
        List<Regimen> schedules = new ArrayList<>();
        Boolean isPneumoniaMeningitisOtitisMediaCausedByStreptococcus = checkList.isPneumonia_meningitis_otitis_media_caused_by_streptococcus();

        int[] monthsToAdd = {2, 3, 4, 10};
        for (int months : monthsToAdd) {
            Date date = addMonth(birthday, months);
            schedules.add(new Regimen("Viêm phổi, viêm màng não, viêm tai giữa do phế cầu khuẩn", date, isPneumoniaMeningitisOtitisMediaCausedByStreptococcus && date.before(currentDate), "pneumonia_meningitis_otitis_media_caused_by_streptococcus"));
        }

        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Viêm màng não, nhiễm khuẩn huyết, viêm phổi do não mô cầu khuẩn B,C
    public static List<Regimen> RegimenMeningococcal(Date birthday, BabyCheckList checkList) {
        List<Regimen> schedules = new ArrayList<>();
        Boolean isMeningitisSepsisPneumoniaCausedByNeisseriaMeningitidisBC = checkList.isMeningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_b_c();

        int[] monthsToAdd = {6, 8};
        for (int months : monthsToAdd) {
            Date date = addMonth(birthday, months);
            schedules.add(new Regimen("Viêm màng não, nhiễm khuẩn huyết, viêm phổi do não mô cầu khuẩn B,C", date, isMeningitisSepsisPneumoniaCausedByNeisseriaMeningitidisBC && date.before(currentDate), "meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_b_c"));
        }

        return schedules;
    }


    //  tạo hàm lênh lịch cho bệnh Cúm
    public static List<Regimen> RegimenInfluenza(Date birthday, BabyCheckList checkList) {
        List<Regimen> schedules = new ArrayList<>();
        Boolean isInfluenza = checkList.isInfluenza();

        int[] monthsToAdd = {6, 7};
        for (int months : monthsToAdd) {
            Date date = addMonth(birthday, months);
            schedules.add(new Regimen("Cúm", date, isInfluenza && date.before(currentDate), "influenza"));
        }

        for (int i = 0; i < 12 * 18; i += 12) {
            Date date = addMonth(birthday, 6 + i);
            schedules.add(new Regimen("Cúm", date, false, "influenza"));
        }

        return schedules;
    }


    //  tạo hàm lênh lịch cho bệnh Sởi
    public static List<Regimen> RegimenMeasles(Date birthday, BabyCheckList checkList) {
        List<Regimen> schedules = new ArrayList<>();
        Boolean isMeasles = checkList.isMeasles();

        int[] monthsToAdd = {9, 18};
        for (int months : monthsToAdd) {
            Date date = addMonth(birthday, months);
            schedules.add(new Regimen("Sởi", date, isMeasles && date.before(currentDate), "measles"));
        }

        return schedules;
    }


    //  tạo hàm lênh lịch cho bệnh Viêm màng não, nhiễm khuẩn huyết, viêm phổi do não mô cầu khuẩn A,C,W,Y
    public static List<Regimen> RegimenMeningococcalACWY(Date birthday, BabyCheckList checkList) {
        List<Regimen> schedules = new ArrayList<>();
        Boolean isMeningitisSepsisPneumoniaCausedByNeisseriaMeningitidisACWY = checkList.isMeningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_a_c_w_y();

        int[] monthsToAdd = {9, 12};
        for (int months : monthsToAdd) {
            Date date = addMonth(birthday, months);
            schedules.add(new Regimen("Viêm màng não, nhiễm khuẩn huyết, viêm phổi do não mô cầu khuẩn A,C,W,Y", date, isMeningitisSepsisPneumoniaCausedByNeisseriaMeningitidisACWY && date.before(currentDate), "meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_a_c_w_y"));
        }

        return schedules;
    }


    //  tạo hàm lênh lịch cho bệnh Viêm não Nhật Bản
    public static List<Regimen> RegimenJapaneseEncephalitis(Date birthday, BabyCheckList checkList) {
        List<Regimen> schedules = new ArrayList<>();
        Boolean isJapaneseEncephalitis = checkList.isJapanese_encephalitis();

        int[] monthsToAdd = {9, 21};
        for (int months : monthsToAdd) {
            Date date = addMonth(birthday, months);
            schedules.add(new Regimen("Viêm não Nhật Bản", date, isJapaneseEncephalitis && date.before(currentDate), "japanese_encephalitis"));
        }

        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Sởi, Quai bị, Rubella
    public static List<Regimen> RegimenMeaslesMumpsRubella(Date birthday, BabyCheckList checkList) {
        List<Regimen> schedules = new ArrayList<>();
        Boolean isMeaslesMumpsRubella = checkList.isMeasles_mumps_rubella();

        int[] monthsToAdd = {12, 36};
        for (int months : monthsToAdd) {
            Date date = addMonth(birthday, months);
            schedules.add(new Regimen("Sởi, Quai bị, Rubella", date, isMeaslesMumpsRubella && date.before(currentDate), "measles_mumps_rubella"));
        }

        return schedules;
    }


    //  tạo hàm lênh lịch cho bệnh Thủy đậu
    public static List<Regimen> RegimenVaricella(Date birthday, BabyCheckList checkList) {
        List<Regimen> schedules = new ArrayList<>();
        Boolean isChickenpox = checkList.isChickenpox();

        int[] monthsToAdd = {9, 12};
        for (int months : monthsToAdd) {
            Date date = addMonth(birthday, months);
            schedules.add(new Regimen("Thủy đậu", date, isChickenpox && date.before(currentDate), "chickenpox"));
        }

        return schedules;
    }


    //  tạo hàm lênh lịch cho bệnh Viêm gan A
    public static List<Regimen> RegimenHepatitisA(Date birthday, BabyCheckList checkList) {
        List<Regimen> schedules = new ArrayList<>();
        Boolean isHepatitisA = checkList.isHepatitis_a();

        int[] monthsToAdd = {12, 18};
        for (int months : monthsToAdd) {
            Date date = addMonth(birthday, months);
            schedules.add(new Regimen("Viêm gan A", date, isHepatitisA && date.before(currentDate), "hepatitis_a"));
        }

        return schedules;
    }


    //  tạo hàm lênh lịch cho bệnh Viêm gan A + B
    public static List<Regimen> RegimenHepatitisAB(Date birthday, BabyCheckList checkList) {
        List<Regimen> schedules = new ArrayList<>();
        Boolean isHepatitisAB = checkList.isHepatitis_a_b();

        int[] monthsToAdd = {12, 18};
        for (int months : monthsToAdd) {
            Date date = addMonth(birthday, months);
            schedules.add(new Regimen("Viêm gan A + B", date, isHepatitisAB && date.before(currentDate), "hepatitis_a_b"));
        }

        return schedules;
    }


    //  tạo hàm lênh lịch cho bệnh Thương hàn
    public static List<Regimen> RegimenTetanus(Date birthday, BabyCheckList checkList) {
        List<Regimen> schedules = new ArrayList<>();
        Boolean isTetanus = checkList.isTetanus();

        Date date_ = addMonth(birthday, 24);
        schedules.add(new Regimen("Thương hàn", date_, isTetanus && date_.before(currentDate), "tetanus"));

        for (int i = 12; i < 12 * 18; i += 12) {
            Date date = addMonth(birthday, 24 + i);
            schedules.add(new Regimen("Thương hàn", date, false, "tetanus"));
        }

        return schedules;
    }


    //  tạo hàm lênh lịch cho bệnh Bệnh tả
    public static List<Regimen> RegimenTyphoid(Date birthday, BabyCheckList checkList) {
        List<Regimen> schedules = new ArrayList<>();
        Boolean isTyphoid = checkList.isAnthrax();

        int[] monthsToAdd = {24, 25};
        for (int months : monthsToAdd) {
            Date date = addMonth(birthday, months);
            schedules.add(new Regimen("Bệnh tả", date, isTyphoid, "anthrax"));
        }

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
