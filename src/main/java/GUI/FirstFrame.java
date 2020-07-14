package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class FirstFrame extends JFrame {
    private JPanel startPanel = new JPanel();
    private JButton runButton = new JButton("Run Application");

    private JPanel upPanel = new JPanel();
    private JPanel midPanel = new JPanel();
    private JPanel downPanel = new JPanel();

    public FirstFrame() {
        this.add(startPanel);
        this.setVisible(true);

        this.setSize(500, 500);

        startPanel.setLayout(new GridLayout(3, 1));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        startPanel.add(upPanel);
        startPanel.add(midPanel);
        startPanel.add(downPanel);

        midPanel.setLayout(new GridLayout(1, 3));
        midPanel.add(new JPanel());
        midPanel.add(new JPanel().add(runButton));
        runButton.addActionListener(e -> {
            close();
            try {
                new MainGUI();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        midPanel.add(new JPanel());

    }


    public void close() {
        if (runButton.isEnabled()) {
            this.dispose();
        }
    }
}

