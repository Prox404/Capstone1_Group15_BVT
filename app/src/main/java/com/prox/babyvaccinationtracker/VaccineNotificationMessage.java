package com.prox.babyvaccinationtracker;

import com.prox.babyvaccinationtracker.model.NotificationMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class VaccineNotificationMessage {

    public static Date addMonth(Date date, int monthsToAdd) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, monthsToAdd);
        return calendar.getTime();
    }

    //  tạo hàm lênh lịch cho bệnh Lao
    public static List<NotificationMessage> NotificationMessageTuberculosis(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Lao cho bé", birthday));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Viêm gan B
    public static List<NotificationMessage> NotificationMessageHepatitisB(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Viêm gan B cho bé", birthday));
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Viêm gan B cho bé", addMonth(birthday, 2)));
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Viêm gan B cho bé", addMonth(birthday, 3)));
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Viêm gan B cho bé", addMonth(birthday, 4)));
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Viêm gan B cho bé", addMonth(birthday, 18)));
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Viêm gan B cho bé", addMonth(birthday, 84)));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Bạch hầu, ho gà, uốn ván
    public static List<NotificationMessage> NotificationMessageDiphtheriaWhoopingCoughPoliomyelitis(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Bạch hầu, ho gà, uốn ván cho bé", birthday));
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Bạch hầu, ho gà, uốn ván cho bé", addMonth(birthday, 2)));
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Bạch hầu, ho gà, uốn ván cho bé", addMonth(birthday, 3)));
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Bạch hầu, ho gà, uốn ván cho bé", addMonth(birthday, 4)));
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Bạch hầu, ho gà, uốn ván cho bé", addMonth(birthday, 18)));
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Bạch hầu, ho gà, uốn ván cho bé", addMonth(birthday, 60)));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Bại liệt
    public static List<NotificationMessage> NotificationMessageParalysis(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Bại liệt cho bé", birthday));
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Bại liệt cho bé", addMonth(birthday, 2)));
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Bại liệt cho bé", addMonth(birthday, 3)));
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Bại liệt cho bé", addMonth(birthday, 4)));
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Bại liệt cho bé", addMonth(birthday, 18)));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Viêm phổi, viêm màng não mủ do Hib
    public static List<NotificationMessage> NotificationMessageHib(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Viêm màng não mủ do Hib cho bé", birthday));
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Viêm màng não mủ do Hib cho bé", addMonth(birthday, 2)));
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Viêm màng não mủ do Hib cho bé", addMonth(birthday, 3)));
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Viêm màng não mủ do Hib cho bé", addMonth(birthday, 4)));
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Viêm màng não mủ do Hib cho bé", addMonth(birthday, 18)));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Tiêu chảy do Rota Virus
    public static List<NotificationMessage> NotificationMessageRotaVirus(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Tiêu chảy do Rota Virus cho bé", addMonth(birthday, 2)));
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Tiêu chảy do Rota Virus cho bé", addMonth(birthday, 3)));
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Tiêu chảy do Rota Virus cho bé", addMonth(birthday, 4)));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Viêm phổi, viêm màng não, viêm tai giữa do phế cầu khuẩn
    public static List<NotificationMessage> NotificationMessagePneumococcal(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Viêm phổi, viêm màng não, viêm tai giữa do phế cầu khuẩn cho bé", addMonth(birthday, 2)));
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Viêm phổi, viêm màng não, viêm tai giữa do phế cầu khuẩn cho bé", addMonth(birthday, 3)));
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Viêm phổi, viêm màng não, viêm tai giữa do phế cầu khuẩn cho bé", addMonth(birthday, 4)));
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Viêm phổi, viêm màng não, viêm tai giữa do phế cầu khuẩn cho bé", addMonth(birthday, 10)));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Viêm màng não, nhiễm khuẩn huyết, viêm phổi do não mô cầu khuẩn B,C
    public static List<NotificationMessage> NotificationMessageMeningococcal(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Viêm màng não, nhiễm khuẩn huyết, viêm phổi do não mô cầu khuẩn B,C cho bé", addMonth(birthday, 6)));
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Viêm màng não, nhiễm khuẩn huyết, viêm phổi do não mô cầu khuẩn B,C cho bé", addMonth(birthday, 8)));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Cúm
    public static List<NotificationMessage> NotificationMessageInfluenza(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Cúm cho bé", addMonth(birthday, 6)));
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Cúm cho bé", addMonth(birthday, 7)));
        for (int i = 0; i < 12*18; i+= 12) {
            schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Cúm cho bé", addMonth(birthday, 6 + i)));
        }
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Sởi
    public static List<NotificationMessage> NotificationMessageMeasles(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Sởi cho bé", addMonth(birthday, 9)));
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Sởi cho bé", addMonth(birthday, 18)));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Viêm màng não, nhiễm khuẩn huyết, viêm phổi do não mô cầu khuẩn A,C,W,Y
    public static List<NotificationMessage> NotificationMessageMeningococcalACWY(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Viêm màng não, nhiễm khuẩn huyết, viêm phổi do não mô cầu khuẩn A,C,W,Y cho bé", addMonth(birthday, 9)));
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Viêm màng não, nhiễm khuẩn huyết, viêm phổi do não mô cầu khuẩn A,C,W,Y cho bé", addMonth(birthday, 12)));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Viêm não Nhật Bản
    public static List<NotificationMessage> NotificationMessageJapaneseEncephalitis(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Viêm não Nhật Bản cho bé", addMonth(birthday, 9)));
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Viêm não Nhật Bản cho bé", addMonth(birthday, 21)));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Sởi, Quai bị, Rubella
    public static List<NotificationMessage> NotificationMessageMeaslesMumpsRubella(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Sởi, Quai bị, Rubella cho bé", addMonth(birthday, 12)));
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Sởi, Quai bị, Rubella cho bé", addMonth(birthday, 36)));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Thủy đậu
    public static List<NotificationMessage> NotificationMessageVaricella(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Thủy đậu cho bé", addMonth(birthday, 9)));
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Thủy đậu cho bé", addMonth(birthday, 12)));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Viêm gan A
    public static List<NotificationMessage> NotificationMessageHepatitisA(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Viêm gan A cho bé", addMonth(birthday, 12)));
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Viêm gan A cho bé", addMonth(birthday, 18)));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Viêm gan A + B
    public static List<NotificationMessage> NotificationMessageHepatitisAB(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Viêm gan A + B cho bé", addMonth(birthday, 12)));
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Viêm gan A + B cho bé", addMonth(birthday, 18)));
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Thương hàn
    public static List<NotificationMessage> NotificationMessageTetanus(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Thương hàn cho bé", addMonth(birthday, 24)));
        for (int i = 0; i < 12*18; i+= 12) {
            schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Thương hàn cho bé", addMonth(birthday, 24 + i)));
        }
        return schedules;
    }

    //  tạo hàm lênh lịch cho bệnh Bệnh tả
    public static List<NotificationMessage> NotificationMessageTyphoid(Date birthday, String user_id, String baby_id) {
        List<NotificationMessage> schedules = new ArrayList<>();
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Bệnh tả cho bé", addMonth(birthday, 24)));
        schedules.add(new NotificationMessage("Nhắc nhở tiêm chủng! ",user_id, baby_id,"Đã đến lúc tiêm chủng phòng bệnh Bệnh tả cho bé", addMonth(birthday, 25)));
        return schedules;
    }


    public static List<NotificationMessage> getVaccinationNotificationMessage(String dateOfBirth, String user_id, String baby_id) throws Exception{
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