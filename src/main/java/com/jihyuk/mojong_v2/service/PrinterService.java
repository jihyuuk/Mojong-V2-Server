package com.jihyuk.mojong_v2.service;

import com.jihyuk.mojong_v2.model.dto.HistoryDetailDTO;
import com.jihyuk.mojong_v2.model.dto.SaleItemDTO;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.time.format.DateTimeFormatter;

import static com.jihyuk.mojong_v2.service.PrinterUtil.*;
import static com.jihyuk.mojong_v2.service.PrinterUtil.setBold;

@Service
@Slf4j
public class PrinterService {

    //프린터 주소 및 포트번호
    private final String PRINTER_IP;
    private final int PRINTER_PORT;

    //연결할 소켓 정보
    private Socket socket = null;
    private OutputStream outputStream = null;

    //생성자로 주소, 포트 받아오기
    public PrinterService(@Value("${spring.printer.ip}") String ip, @Value("${spring.printer.port}") int port) {
        PRINTER_IP = ip;
        PRINTER_PORT = port;
    }


    //소켓 연결 로직
    public void connect() {
        try {
            log.info("소켓 연결시도 => connect() 함수 실행");
            SocketAddress endpoint = new InetSocketAddress(PRINTER_IP, PRINTER_PORT);
            socket = new Socket();
            socket.connect(endpoint, 3000); // 3초 타임아웃
            outputStream = socket.getOutputStream();
            log.info("프린터 연결 성공!");
        } catch (Exception e) {
            socket = null;
            log.warn("프린터 연결 실패 (무시하고 서버 계속 실행): {}", e.getMessage());
        }
    }


    @Transactional
    public boolean printReceipt(HistoryDetailDTO dto) {

        //소켓 연결 체크하기
        if(!checkPrinterStatus()){
            //연결시도
            connect();

            if(!checkPrinterStatus()){
                return false;
            }
        }


        //소켓 연결 성공시

        //여기서는 메세지만만들고
        StringBuilder sb = new StringBuilder();

        // 제목
        sb.append(setAlignCenter());
        sb.append(setBold(true));
        sb.append(setTextSize(2, 2));
        sb.append("[주문내역]\n");

        // 스타일 복원
        sb.append(setTextSize(1, 1));
        sb.append(setBold(false));
        sb.append(setAlignLeft());

        // 주문 정보
        sb.append("\n");
        sb.append(String.format("주문번호: #%d\n", dto.getId()));
        sb.append("상호: (주)그린아그로\n");
        sb.append(String.format("주문일시: %s\n", dto.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));


        // 구분선 + 헤더 (Bold)
        sb.append(line('-'));
        sb.append(setBold(true));
        sb.append("상 품 명          수 량    단 가     금 액\n");
        sb.append(setBold(false));
        sb.append(line('-'));

        // 상품 목록 (Bold X, 줄 바꿈)
        for (SaleItemDTO item : dto.getItems()) {
            sb.append(formatProduct(
                    item.getName(),
                    String.format("%,d", item.getQuantity()),
                    String.format("%,d", item.getPrice()),
                    String.format("%,d", item.getTotalAmount())
            ));
        }

        //공백
        sb.append("\n\n");

        // 합계, 할인
        if (dto.getDiscountAmount() > 0) {
            sb.append(line('-'));
            sb.append(String.format("합계 %35s원\n", String.format("%,d", dto.getTotalAmount())));
            sb.append(String.format("할인 %35s원\n", "-" + String.format("%,d", dto.getDiscountAmount())));
        }


        // 총금액 (Bold만 적용)
        sb.append(line('=')); // 구분선
        sb.append(setTextSize(1, 2)); // 폰트 2배 확대
        sb.append(setBold(true));
        sb.append(String.format("총 금액 %32s원\n", String.format("%,d", dto.getFinalAmount())));
        sb.append(setBold(false));
        sb.append(setTextSize(1, 1)); // 폰트 원래대로

        // Feed + Cut
        sb.append(new String(new byte[]{0x1B, 'd', 5})); // Feed
        sb.append(new String(new byte[]{0x1D, 0x56, 0x01})); // Partial cut


        //1번째 프린트 시도
        boolean firstPrint = print(sb.toString());

        //1번째 프린트 결과
        if (!firstPrint) {
            log.info("1번째 프린트 시도 실패!");
            //프린트 실패시 재연결
            connect();
        } else {
            //프린트 성공시
            log.info("1번째 프린트 시도 성공");
            return true;
        }

        //2번째 프린트
        log.info("2번째 프린트 시도");
        return print(sb.toString());
    }

    //출력로직
    public boolean print(String message) {

        // 프린트 보내기 시도
        try {
            outputStream.write(message.getBytes("EUC-KR"));
            outputStream.flush();
        } catch (Exception e) {
            log.error("프린터 outputStream 실패!: {}", e.getMessage());
            return false;
        }

        return true;
    }


    //프린터 상태체크
    private boolean checkPrinterStatus() {
        try {
            if (socket == null || socket.isClosed()) {
                return false;
            }

            byte[] statusCommand = new byte[]{0x10, 0x04, 0x01}; // DLE EOT 1
            outputStream.write(statusCommand);
            outputStream.flush();

            byte[] response = new byte[1];
            socket.setSoTimeout(1000); // 1초 타임아웃
            int read = socket.getInputStream().read(response);

            if (read == 1) {
                byte status = response[0];
                if ((status & 0b00001000) != 0) {
                    log.warn("프린터 커버 열림 감지");
                    return false;
                }
                if ((status & 0b00100000) != 0) {
                    log.warn("프린터 용지 없음 감지");
                    return false;
                }
                return true;
            }
        } catch (Exception e) {
            log.warn("프린터 상태 체크 실패: {}", e.getMessage());
        }
        return false;
    }



    @PreDestroy // 서비스가 종료될 때 실행
    public void close() {
        try {
            if (outputStream != null) outputStream.close();
            if (socket != null) socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}