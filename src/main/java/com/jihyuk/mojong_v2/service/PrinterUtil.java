package com.jihyuk.mojong_v2.service;

import org.springframework.stereotype.Component;

public class PrinterUtil {

    private static final int CHAR_WIDTH = 42;

    // 텍스트 크기 조절 (가로, 세로)
    public static String setTextSize(int widthMultiplier, int heightMultiplier) {
        int n = ((widthMultiplier - 1) << 4) | (heightMultiplier - 1);
        return new String(new byte[]{0x1D, 0x21, (byte) n});
    }

    public static String setAlignCenter() {
        return new String(new byte[]{0x1B, 0x61, 0x01});
    }

    public static String setAlignLeft() {
        return new String(new byte[]{0x1B, 0x61, 0x00});
    }

    public static String setBold(boolean on) {
        return new String(new byte[]{0x1B, 0x45, (byte) (on ? 1 : 0)});
    }

    public static String line(char c) {
        return String.valueOf(c).repeat(CHAR_WIDTH) + "\n";
    }

    public static String setLineSpacing(int n) {
        return new String(new byte[]{0x1B, 0x33, (byte) n});
    }

    public static String resetLineSpacing() {
        return new String(new byte[]{0x1B, 0x32});
    }

    // 상품명 줄바꿈 + 가격정보 한 줄 아래
    public static String formatProduct(String name, String quantity, String price, String total) {

        int marginWidth = 21;
        int nameWidth = getDisplayWidth(name);
        int quantityWidth = quantity.length();

        StringBuilder sb = new StringBuilder();
        sb.append(setLineSpacing(75)); // ← 여기서만 줄간격 넓힘

        //상품명 길어지면 줄바꿈
        if (nameWidth + quantityWidth > marginWidth) {
            sb.append(String.format("%s\n             └> %-5s %9s %9s\n", name, quantity, price, total));
        }else{
            int padding = marginWidth - nameWidth - quantityWidth + 1;
            String space = " ".repeat(padding);
            sb.append(String.format("%s%s%s %9s %9s\n", name, space, quantity, price, total));
        }

        sb.append(resetLineSpacing()); // 원래대로 복원

        //한 줄로 출력
        return sb.toString();
    }


    //총 넓이 구하는 계산기
    //한글 2글자, 영어 1글자 <= 개빡치네
    public static int getDisplayWidth(String str) {
        int width = 0;
        for (char c : str.toCharArray()) {
            // 한글: 유니코드 블록이 HANGUL_SYLLABLES
            if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HANGUL_SYLLABLES) {
                width += 2;
            } else {
                width += 1;
            }
        }
        return width;
    }
}
