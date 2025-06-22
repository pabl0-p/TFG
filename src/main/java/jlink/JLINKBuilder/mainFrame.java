package jlink.JLINKBuilder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author carlos
 */
public class mainFrame implements ActionListener {
    JFrame frame = new JFrame();

    JButton button1;
    JButton button2;
    JButton button3;
    JButton button4;
    JButton button5;
    JButton aux;

    public mainFrame() {
        button1 = new JButton("Create new JLINK");
        button1.setFocusable(false);
        button1.addActionListener(this);

        button2 = new JButton("Build XML");
        button2.setFocusable(false);
        button2.addActionListener(this);
        button2.setVisible(true);

        button3 = new JButton("Explore JLINK");
        button3.setFocusable(false);
        button3.addActionListener(this);
        button3.setVisible(true);

        button4 = new JButton("Merge JLINK into JPEG");
        button4.setFocusable(false);
        button4.addActionListener(this);
        button4.setVisible(true);

        button5 = new JButton("Exit");
        button5.setFocusable(false);
        button5.addActionListener(this);
        button5.setVisible(true);

        aux = new JButton();
        aux.setFocusable(false);
        aux.addActionListener(this);
        aux.setVisible(false);

        button1.setBounds(50, 200, 200, 150);
        button2.setBounds(300, 200, 200, 150);
        button3.setBounds(550, 200, 200, 150);
        button4.setBounds(800, 200, 200, 150);
        button5.setBounds(1050, 200, 200, 150);

        frame.add(button1);
        frame.add(button2);
        frame.add(button3);
        frame.add(button4);
        frame.add(button5);
        frame.add(aux);
        frame.setSize(1300, 600);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Create new JLINK":
                try {
                    new Builder("Main JlINK", true, null);
                } catch (Exception ex) {
                    Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "Exit":
                frame.dispose();
                break;
            case "Explore JLINK":
                fileGetter fg = new fileGetter();
                try {
                    // File f = fg.getFile("Select a JLINK file");

                    // JLINKSuperBox jlink = utils
                    // .shapeJLINKSuperBox(utils.getJUMBFUtils().getBoxesFromFile(f.getAbsolutePath()));
                    // utils.analizeJPEGFile(f);

                } catch (Exception ex) {
                    Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                fg.dispose();
                break;
            case "Merge JLINK into JPEG":
                fileGetter fg2 = new fileGetter();
                fileGetter fg3 = new fileGetter();

                // File image = fg2.getFile("Select JPEG file");
                // File jumbf = fg3.getFile("Select JUMBF file");

                try {
                    // utils.getJUMBFUtils().mergeJUMBF(image.getAbsolutePath(),
                    // jumbf.getAbsolutePath());
                } catch (Exception ex) {
                    Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                fg2.dispose();
                fg3.dispose();
                break;
            case "Build XML":
                new XMLBuilder();
        }
    }
}