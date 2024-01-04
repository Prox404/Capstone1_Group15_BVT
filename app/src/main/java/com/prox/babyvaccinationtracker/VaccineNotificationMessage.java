package com.prox.babyvaccinationtracker;

import com.prox.babyvaccinationtracker.model.BabyCheckList;
import com.prox.babyvaccinationtracker.model.NotificationMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class VaccineNotificationMessage {

    private static Date currentDate = new Date();
    private static BabyCheckList checkList = new BabyCheckList();

    public static Date addMonth(Date date, int monthsToAdd) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, monthsToAdd);
        return calendar.getTime();
    }

    //  tạo hàm lênh lịch cho bệnh Lao
    public static List<NotificationMessage> NotificationMessageTuberculosis(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();
        if (birthday.after(currentDate) || (birthday.before(currentDate) && !checkList.isTuberculosis()))
            schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Lao cho bé", birthday));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Viêm gan B
    public static List<NotificationMessage> NotificationMessageHepatitisB(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();

        int[] monthsToAdd = {0, 2, 3, 4, 18, 84};
        String message = "Đã đến lúc tiêm chủng phòng bệnh Viêm gan B cho bé";

        for (int months : monthsToAdd) {
            Date date = addMonth(birthday, months);
            if (date.after(currentDate) || (birthday.before(currentDate) && !checkList.isHepatitis_b())) {
                schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ", user_id, baby_id, message, date));
            }
        }

        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Bạch hầu, ho gà, uốn ván
    public static List<NotificationMessage> NotificationMessageDiphtheriaWhoopingCoughPoliomyelitis(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();

        int[] monthsToAdd = {2, 3, 4, 18, 60};
        String message = "Đã đến lúc tiêm chủng phòng bệnh Bạch hầu, ho gà, uốn ván cho bé";

        for (int months : monthsToAdd) {
            Date date = addMonth(birthday, months);
            if (date.after(currentDate) || (birthday.before(currentDate) && !checkList.isDiphtheria_whooping_cough_poliomyelitis())) {
                schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ", user_id, baby_id, message, date));
            }
        }

        return schedules;
    }


    //  tạo hàm lênh lịch cho bệnh Bại liệt
    public static List<NotificationMessage> NotificationMessageParalysis(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();

        int[] monthsToAdd = { 2, 3, 4, 18};
        String message = "Đã đến lúc tiêm chủng phòng bệnh Bại liệt cho bé";

        for (int months : monthsToAdd) {
            Date date = addMonth(birthday, months);
            if (date.after(currentDate) || (birthday.before(currentDate) && !checkList.isParalysis())) {
                schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ", user_id, baby_id, message, date));
            }
        }

        return schedules;
    }


    //  tạo hàm lênh lịch cho bệnh Viêm phổi, viêm màng não mủ do Hib
    public static List<NotificationMessage> NotificationMessageHib(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();

        int[] monthsToAdd = { 2, 3, 4, 18};
        String message = "Đã đến lúc tiêm chủng phòng bệnh Viêm màng não mủ do Hib cho bé";

        for (int months : monthsToAdd) {
            Date date = addMonth(birthday, months);
            if (date.after(currentDate) || (birthday.before(currentDate) && !checkList.isPneumonia_hib_meningitis())){
                schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ", user_id, baby_id, message, date));
            }
        }

        return schedules;
    }


    //  tạo hàm lênh lịch cho bệnh Tiêu chảy do Rota Virus
    public static List<NotificationMessage> NotificationMessageRotaVirus(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();

        int[] monthsToAdd = {2, 3, 4};
        String message = "Đã đến lúc tiêm chủng phòng bệnh Tiêu chảy do Rota Virus cho bé";

        for (int months : monthsToAdd) {
            Date date = addMonth(birthday, months);
            if (date.after(currentDate) || (birthday.before(currentDate) && !checkList.isRotavirus_diarrhea())) {
                schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ", user_id, baby_id, message, date));
            }
        }

        return schedules;
    }


    //  tạo hàm lênh lịch cho bệnh Viêm phổi, viêm màng não, viêm tai giữa do phế cầu khuẩn
    public static List<NotificationMessage> NotificationMessagePneumococcal(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();

        int[] monthsToAdd = {2, 3, 4, 10};
        String message = "Đã đến lúc tiêm chủng phòng bệnh Viêm phổi, viêm màng não, viêm tai giữa do phế cầu khuẩn cho bé";

        for (int months : monthsToAdd) {
            Date date = addMonth(birthday, months);
            if (date.after(currentDate) || (birthday.before(currentDate) && !checkList.isPneumonia_meningitis_otitis_media_caused_by_streptococcus())) {
                schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ", user_id, baby_id, message, date));
            }
        }

        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Viêm màng não, nhiễm khuẩn huyết, viêm phổi do não mô cầu khuẩn B,C
    public static List<NotificationMessage> NotificationMessageMeningococcal(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();

        int[] monthsToAdd = {6, 8};
        String message = "Đã đến lúc tiêm chủng phòng bệnh Viêm màng não, nhiễm khuẩn huyết, viêm phổi do não mô cầu khuẩn B,C cho bé";

        for (int months : monthsToAdd) {
            Date date = addMonth(birthday, months);
            if (date.after(currentDate) || (birthday.before(currentDate) && !checkList.isMeningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_b_c())) {
                schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ", user_id, baby_id, message, date));
            }
        }

        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Cúm
    public static List<NotificationMessage> NotificationMessageInfluenza(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();

        int[] monthsToAdd = {6, 7};
        String message = "Đã đến lúc tiêm chủng phòng bệnh Cúm cho bé";

        for (int months : monthsToAdd) {
            Date date = addMonth(birthday, months);
            if (date.after(currentDate) || (birthday.before(currentDate) && !checkList.isInfluenza())) {
                schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ", user_id, baby_id, message, date));
            }
        }

        for (int i = 0; i < 12 * 18; i += 12) {
            Date date = addMonth(birthday, 6 + i);
            if (date.after(currentDate) || (birthday.before(currentDate) && !checkList.isInfluenza())) {
                schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ", user_id, baby_id, message, date));
            }
        }

        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Sởi
    public static List<NotificationMessage> NotificationMessageMeasles(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();

        int[] monthsToAdd = {9, 18};
        String message = "Đã đến lúc tiêm chủng phòng bệnh Sởi cho bé";

        for (int months : monthsToAdd) {
            Date date = addMonth(birthday, months);
            if (date.after(currentDate) || (birthday.before(currentDate) && !checkList.isMeasles())) {
                schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ", user_id, baby_id, message, date));
            }
        }

        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Viêm màng não, nhiễm khuẩn huyết, viêm phổi do não mô cầu khuẩn A,C,W,Y
    public static List<NotificationMessage> NotificationMessageMeningococcalACWY(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();

        int[] monthsToAdd = {9, 12};
        String message = "Đã đến lúc tiêm chủng phòng bệnh Viêm màng não, nhiễm khuẩn huyết, viêm phổi do não mô cầu khuẩn A,C,W,Y cho bé";

        for (int months : monthsToAdd) {
            Date date = addMonth(birthday, months);
            if (date.after(currentDate) || (birthday.before(currentDate) && !checkList.isMeningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_a_c_w_y())) {
                schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ", user_id, baby_id, message, date));
            }
        }

        return schedules;
    }


    //  tạo hàm lênh lịch cho bệnh Viêm não Nhật Bản
    public static List<NotificationMessage> NotificationMessageJapaneseEncephalitis(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();

        int[] monthsToAdd = {9, 21};
        String message = "Đã đến lúc tiêm chủng phòng bệnh Viêm não Nhật Bản cho bé";

        for (int months : monthsToAdd) {
            Date date = addMonth(birthday, months);
            if (date.after(currentDate) || (birthday.before(currentDate) && !checkList.isJapanese_encephalitis())) {
                schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ", user_id, baby_id, message, date));
            }
        }

        return schedules;
    }


    //  tạo hàm lênh lịch cho bệnh Sởi, Quai bị, Rubella
    public static List<NotificationMessage> NotificationMessageMeaslesMumpsRubella(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();

        int[] monthsToAdd = {12, 36};
        String message = "Đã đến lúc tiêm chủng phòng bệnh Sởi, Quai bị, Rubella cho bé";

        for (int months : monthsToAdd) {
            Date date = addMonth(birthday, months);
            if (date.after(currentDate) || (birthday.before(currentDate) && !checkList.isMeasles_mumps_rubella())) {
                schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ", user_id, baby_id, message, date));
            }
        }

        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Thủy đậu
    public static List<NotificationMessage> NotificationMessageVaricella(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();

        int[] monthsToAdd = {9, 12};
        String message = "Đã đến lúc tiêm chủng phòng bệnh Thủy đậu cho bé";

        for (int months : monthsToAdd) {
            Date date = addMonth(birthday, months);
            if (date.after(currentDate) || (birthday.before(currentDate) && !checkList.isChickenpox())) {
                schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ", user_id, baby_id, message, date));
            }
        }

        return schedules;
    }


    //  tạo hàm lênh lịch cho bệnh Viêm gan A
    public static List<NotificationMessage> NotificationMessageHepatitisA(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();

        int[] monthsToAdd = {12, 18};
        String message = "Đã đến lúc tiêm chủng phòng bệnh Viêm gan A cho bé";

        for (int months : monthsToAdd) {
            Date date = addMonth(birthday, months);
            if (date.after(currentDate) || (birthday.before(currentDate) && !checkList.isHepatitis_a())) {
                schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ", user_id, baby_id, message, date));
            }
        }

        return schedules;
    }


    //  tạo hàm lênh lịch cho bệnh Viêm gan A + B
    public static List<NotificationMessage> NotificationMessageHepatitisAB(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();

        int[] monthsToAdd = {12, 18};
        String message = "Đã đến lúc tiêm chủng phòng bệnh Viêm gan A + B cho bé";

        for (int months : monthsToAdd) {
            Date date = addMonth(birthday, months);
            if (date.after(currentDate) || (birthday.before(currentDate) && !checkList.isHepatitis_a_b())) {
                schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ", user_id, baby_id, message, date));
            }
        }

        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Thương hàn
    public static List<NotificationMessage> NotificationMessageTetanus(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();

        int initialMonth = 24;
        int intervalMonths = 12;
        String message = "Đã đến lúc tiêm chủng phòng bệnh Thương hàn cho bé";

        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ", user_id, baby_id, message, addMonth(birthday, initialMonth)));

        for (int i = intervalMonths; i < 12 * 18; i += intervalMonths) {
            Date date = addMonth(birthday, initialMonth + i);
            if (date.after(currentDate) || (birthday.before(currentDate) && !checkList.isTetanus())) {
                schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ", user_id, baby_id, message, date));
            }
        }

        return schedules;
    }


    //  tạo hàm lênh lịch cho bệnh Bệnh tả
    public static List<NotificationMessage> NotificationMessageTyphoid(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();

        int initialMonth = 24;
        int secondDoseMonth = 25;
        String message = "Đã đến lúc tiêm chủng phòng bệnh Bệnh tả cho bé";

        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ", user_id, baby_id, message, addMonth(birthday, initialMonth)));
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ", user_id, baby_id, message, addMonth(birthday, secondDoseMonth)));

        return schedules;
    }



    public static List<NotificationMessage> getVaccinationNotificationMessage(String dateOfBirth, String user_id, String baby_id, BabyCheckList checkList) throws Exception{
        VaccineNotificationMessage.checkList = checkList;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date birthday = sdf.parse(dateOfBirth + " 07:00");
        List<NotificationMessage> schedules = new ArrayList<>();
        schedules.addAll(NotificationMessageTuberculosis(birthday, user_id, baby_id));
        schedules.addAll(NotificationMessageHepatitisB(birthday, user_id, baby_id));
        schedules.addAll(NotificationMessageDiphtheriaWhoopingCoughPoliomyelitis(birthday, user_id, baby_id));
        schedules.addAll(NotificationMessageParalysis(birthday, user_id, baby_id));
        schedules.addAll(NotificationMessageHib(birthday, user_id, baby_id));
        schedules.addAll(NotificationMessageRotaVirus(birthday, user_id, baby_id));
        schedules.addAll(NotificationMessagePneumococcal(birthday, user_id, baby_id));
        schedules.addAll(NotificationMessageMeningococcal(birthday, user_id, baby_id));
        schedules.addAll(NotificationMessageInfluenza(birthday, user_id, baby_id));
        schedules.addAll(NotificationMessageMeasles(birthday, user_id, baby_id));
        schedules.addAll(NotificationMessageMeningococcalACWY(birthday, user_id, baby_id));
        schedules.addAll(NotificationMessageJapaneseEncephalitis(birthday, user_id, baby_id));
        schedules.addAll(NotificationMessageMeaslesMumpsRubella(birthday, user_id, baby_id));
        schedules.addAll(NotificationMessageVaricella(birthday, user_id, baby_id));
        schedules.addAll(NotificationMessageHepatitisA(birthday, user_id, baby_id));
        schedules.addAll(NotificationMessageHepatitisAB(birthday, user_id, baby_id));
        schedules.addAll(NotificationMessageTetanus(birthday, user_id, baby_id));
        schedules.addAll(NotificationMessageTyphoid(birthday, user_id, baby_id));


        Collections.sort(schedules);

        return schedules;
    }
}