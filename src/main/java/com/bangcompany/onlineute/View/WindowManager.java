package com.bangcompany.onlineute.View;

import javax.swing.*;
import java.awt.*;

public final class WindowManager extends JFrame {
    private static WindowManager instance;
    private static JPanel container;

    /**
     * Khởi tạo cửa sổ window chính
     */
    private WindowManager() {
        setTitle("OnlineUTE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //tắt chương trình khi cửa sổ ắt
        setSize(1200, 800);
        setResizable(true); // có thể tùy chỉnh kích thước
        setLocationRelativeTo(null);
      //  setUndecorated(true);   //xóa viền của window
        container = new JPanel(new BorderLayout()); // tạo container làm nội dung
        add(container);
    }

    private static void init() {
        if (instance == null) {
            instance = new WindowManager(); // khởi tạo cửa số khi bắt đầu
        }
    }

    public static void show(JPanel panel) {
        init();
        container.removeAll();  // xóa hết các phần tử bên trong
        container.add(panel, BorderLayout.CENTER);  //thêm 1 panel mới
        container.revalidate(); //kiểm tra lại
        container.repaint();    // vẽ lại

        if (!instance.isVisible()) {    //kiểm tra nếu chưa hiện thì hiện lại cửa sổ
            instance.setVisible(true);
        }
    }

    public static void exit() {// khi thoát
        if (instance != null) {
            instance.dispose();
        }
        System.exit(0);
    }
}
