/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jlink.JLINKBuilder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author carlos
 */
public class XMLBuilder implements ActionListener {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    StringBuilder sb = new StringBuilder("<x:xmpmeta xmlns:x=\"adobe:ns:meta/\" x:xmptk=\"XMP Core 5.5.0\">\n" +
            "\t<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">\n" +
            "\t\t<rdf:Description rdf:about=\"\" xmlns:xmp=\"http://ns.adobe.com/xap/1.0/\" xmlns:umf=\"http://ns.intel.com/umf/2.0\">\n");
    Scene scene;

    String fileName;

    JFrame frame = new JFrame();

    JButton button0;
    JButton button1;

    JTextField fileNameField;

    JLabel label0;

    Properties properties = new Properties();
    String folderName;

    public XMLBuilder() {
        try {
            properties.load(XMLBuilder.class.getClassLoader().getResourceAsStream("app.properties"));
        } catch (IOException ex) {
            Logger.getLogger(XMLBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        folderName = properties.getProperty("TestFolderPath");

        button0 = new JButton("Done");
        button0.setFocusable(false);
        button0.addActionListener(this);

        button1 = new JButton("Set Scene Data");
        button1.setFocusable(false);
        button1.addActionListener(this);

        fileNameField = new JTextField();
        label0 = new JLabel("Set File name");

        label0.setBounds(100, 100, 200, 30);
        fileNameField.setBounds(100, 130, 200, 30);

        JButton baux = new JButton();

        button0.setBounds(350, 200, 200, 100);
        button1.setBounds(100, 200, 200, 100);

        baux.setBounds(0, 0, 0, 0);
        baux.setVisible(false);

        frame.add(label0);
        frame.add(fileNameField);
        frame.add(button0);
        frame.add(button1);
        frame.add(baux);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(0, 0, 600, 400);
        frame.setVisible(true);
    }

    public class Scene implements ActionListener {
        String name;
        String version;
        String title;
        String note;
        Viewport viewport;
        Image image;
        Link link;

        XMLBuilder parent;

        JFrame frame2;

        JTextField fieldForName = new JTextField();
        JTextField fieldForVersion = new JTextField();
        JTextField fieldForTitle = new JTextField();
        JTextField fieldForNote = new JTextField();

        JLabel label1 = new JLabel("Name");
        JLabel label2 = new JLabel("Version");
        JLabel label3 = new JLabel("Title");
        JLabel label4 = new JLabel("Note");

        JButton button2;
        JButton button3;
        JButton button4;
        JButton button5;

        public Scene(XMLBuilder parent) {
            this.parent = parent;
            frame2 = new JFrame();

            button2 = new JButton("Set Viewport Data");
            button2.setFocusable(false);
            button2.addActionListener(this);

            button3 = new JButton("Set Image Data");
            button3.setFocusable(false);
            button3.addActionListener(this);

            button4 = new JButton("Set Link Data");
            button4.setFocusable(false);
            button4.addActionListener(this);

            button5 = new JButton("Done");
            button5.setFocusable(false);
            button5.addActionListener(this);

            JButton baux = new JButton();

            button2.setBounds(300, 50, 200, 100);
            button3.setBounds(300, 150, 200, 100);
            button4.setBounds(300, 250, 200, 100);
            button5.setBounds(350, 400, 200, 100);
            baux.setBounds(0, 0, 0, 0);
            baux.setVisible(false);

            label1.setBounds(50, 50, 200, 30);
            label2.setBounds(50, 120, 200, 30);
            label3.setBounds(50, 190, 200, 30);
            label4.setBounds(50, 260, 200, 30);

            fieldForName.setBounds(50, 80, 200, 30);
            fieldForNote.setBounds(50, 150, 200, 30);
            fieldForTitle.setBounds(50, 220, 200, 30);
            fieldForVersion.setBounds(50, 290, 200, 30);

            frame2.add(label1);
            frame2.add(label2);
            frame2.add(label3);
            frame2.add(label4);
            frame2.add(fieldForName);
            frame2.add(fieldForVersion);
            frame2.add(fieldForTitle);
            frame2.add(fieldForNote);
            frame2.add(button2);
            frame2.add(button3);
            frame2.add(button4);
            frame2.add(button5);
            frame2.add(baux);

            frame2.setBounds(400, 400, 600, 600);

            frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame2.setVisible(true);
        }

        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "Set Viewport Data":
                    new Viewport(this);
                    break;
                case "Set Image Data":
                    new Image(this);
                    break;
                case "Set Link Data":
                    new Link(this);
                    break;
                case "Done":
                    this.name = (this.fieldForName.getText() != null) ? this.fieldForName.getText() : "";
                    this.version = (this.fieldForVersion.getText() != null) ? this.fieldForVersion.getText() : "";
                    this.title = (this.fieldForTitle.getText() != null) ? this.fieldForTitle.getText() : "";
                    this.note = (this.fieldForNote.getText() != null) ? this.fieldForNote.getText() : "";
                    parent.scene = this;
                    frame2.dispose();
                    break;
            }
        }
    };

    public class Viewport implements ActionListener {
        String name;
        float x;
        float y;
        float xFov;
        float yFov;
        int id;
        Scene parent;

        JFrame frame3;

        JTextField fieldForName = new JTextField();
        JTextField fieldForX = new JTextField();
        JTextField fieldForY = new JTextField();
        JTextField fieldForXFov = new JTextField();
        JTextField fieldForYFov = new JTextField();
        JTextField fieldForId = new JTextField();

        JLabel label5 = new JLabel("Name");
        JLabel label6 = new JLabel("X");
        JLabel label7 = new JLabel("Y");
        JLabel label8 = new JLabel("xFov");
        JLabel label9 = new JLabel("yFov");
        JLabel label10 = new JLabel("Id");

        JButton button6;

        public Viewport(Scene parent) {
            this.parent = parent;
            frame3 = new JFrame();

            button6 = new JButton("Done");
            button6.setFocusable(false);
            button6.addActionListener(this);

            JButton baux = new JButton();

            button6.setBounds(350, 400, 200, 100);
            baux.setBounds(0, 0, 0, 0);
            baux.setVisible(false);

            label5.setBounds(50, 50, 200, 30);
            label6.setBounds(50, 120, 200, 30);
            label7.setBounds(50, 190, 200, 30);
            label8.setBounds(50, 260, 200, 30);
            label9.setBounds(50, 330, 200, 30);
            label10.setBounds(50, 400, 200, 30);

            fieldForName.setBounds(50, 80, 200, 30);
            fieldForX.setBounds(50, 150, 200, 30);
            fieldForY.setBounds(50, 220, 200, 30);
            fieldForXFov.setBounds(50, 290, 200, 30);
            fieldForYFov.setBounds(50, 360, 200, 30);
            fieldForId.setBounds(50, 430, 200, 30);

            frame3.add(label5);
            frame3.add(label6);
            frame3.add(label7);
            frame3.add(label8);
            frame3.add(label9);
            frame3.add(label10);
            frame3.add(fieldForName);
            frame3.add(fieldForX);
            frame3.add(fieldForY);
            frame3.add(fieldForXFov);
            frame3.add(fieldForYFov);
            frame3.add(fieldForId);
            frame3.add(button6);
            frame3.add(baux);

            frame3.setBounds(400, 400, 600, 600);

            frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame3.setVisible(true);
        }

        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "Done":
                    this.name = (this.fieldForName.getText() != null) ? this.fieldForName.getText() : "";
                    this.x = (Float.valueOf(this.fieldForX.getText()) != null) ? Float.valueOf(this.fieldForX.getText())
                            : 0;
                    this.y = (Float.valueOf(this.fieldForY.getText()) != null)
                            ? this.y = Float.valueOf(this.fieldForY.getText())
                            : 0;
                    this.xFov = (Float.valueOf(this.fieldForXFov.getText()) != null)
                            ? Float.valueOf(this.fieldForXFov.getText())
                            : 0;
                    this.yFov = (Float.valueOf(this.fieldForYFov.getText()) != null)
                            ? Float.valueOf(this.fieldForYFov.getText())
                            : 0;
                    this.id = (Integer.valueOf(this.fieldForId.getText()) != null)
                            ? Integer.valueOf(this.fieldForId.getText())
                            : 0;
                    parent.viewport = this;
                    frame3.dispose();
                    break;
            }
        }
    };

    public class Image implements ActionListener {
        String name;
        String format;
        String href;

        Scene parent;

        JFrame frame4;

        JTextField fieldForName = new JTextField();
        JTextField fieldForFormat = new JTextField();
        JTextField fieldForHref = new JTextField();

        JLabel label11 = new JLabel("Name");
        JLabel label12 = new JLabel("Format");
        JLabel label13 = new JLabel("Href");

        JButton button7;

        public Image(Scene parent) {
            this.parent = parent;
            frame4 = new JFrame();

            button7 = new JButton("Done");
            button7.setFocusable(false);
            button7.addActionListener(this);

            JButton baux = new JButton();

            button7.setBounds(350, 400, 200, 100);
            baux.setBounds(0, 0, 0, 0);
            baux.setVisible(false);

            label11.setBounds(50, 50, 200, 30);
            label12.setBounds(50, 120, 200, 30);
            label13.setBounds(50, 190, 200, 30);

            fieldForName.setBounds(50, 80, 200, 30);
            fieldForFormat.setBounds(50, 150, 200, 30);
            fieldForHref.setBounds(50, 220, 200, 30);

            frame4.add(label11);
            frame4.add(label12);
            frame4.add(label13);

            frame4.add(fieldForName);
            frame4.add(fieldForFormat);
            frame4.add(fieldForHref);

            frame4.add(button7);
            frame4.add(baux);

            frame4.setBounds(400, 400, 600, 600);

            frame4.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame4.setVisible(true);
        }

        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "Done":
                    this.name = (this.fieldForName.getText() != null) ? this.fieldForName.getText() : "";
                    this.format = (this.fieldForFormat.getText() != null) ? this.fieldForFormat.getText() : "";
                    this.href = (this.fieldForHref.getText() != null) ? this.fieldForHref.getText() : "";
                    parent.image = this;
                    frame4.dispose();
                    break;
            }
        }
    };

    public class Link implements ActionListener {
        Region region;
        String name;
        int duration;
        int vpid;
        String sprite;
        String to;

        Scene parent;

        JFrame frame5;

        JTextField fieldForName = new JTextField();
        JTextField fieldForDuration = new JTextField();
        JTextField fieldForVpid = new JTextField();
        JTextField fieldForSprite = new JTextField();
        JTextField fieldForTo = new JTextField();

        JLabel label14 = new JLabel("Name");
        JLabel label15 = new JLabel("Duration");
        JLabel label16 = new JLabel("Vpid");
        JLabel label17 = new JLabel("Sprite");
        JLabel label18 = new JLabel("To");

        JButton button8;
        JButton button9;

        public Link(Scene parent) {
            this.parent = parent;
            frame5 = new JFrame();

            button8 = new JButton("Set Region Data");
            button8.setFocusable(false);
            button8.addActionListener(this);

            button9 = new JButton("Done");
            button9.setFocusable(false);
            button9.addActionListener(this);

            JButton baux = new JButton();

            button8.setBounds(350, 200, 200, 100);
            button9.setBounds(350, 400, 200, 100);
            baux.setBounds(0, 0, 0, 0);
            baux.setVisible(false);

            label14.setBounds(50, 50, 200, 30);
            label15.setBounds(50, 120, 200, 30);
            label16.setBounds(50, 190, 200, 30);
            label17.setBounds(50, 260, 200, 30);
            label18.setBounds(50, 330, 200, 30);

            fieldForName.setBounds(50, 80, 200, 30);
            fieldForDuration.setBounds(50, 150, 200, 30);
            fieldForVpid.setBounds(50, 220, 200, 30);
            fieldForSprite.setBounds(50, 290, 200, 30);
            fieldForTo.setBounds(50, 360, 200, 30);

            frame5.add(label14);
            frame5.add(label15);
            frame5.add(label16);
            frame5.add(label17);
            frame5.add(label18);

            frame5.add(fieldForName);
            frame5.add(fieldForDuration);
            frame5.add(fieldForVpid);
            frame5.add(fieldForSprite);
            frame5.add(fieldForTo);

            frame5.add(button8);
            frame5.add(button9);
            frame5.add(baux);

            frame5.setBounds(400, 400, 600, 600);

            frame5.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame5.setVisible(true);
        }

        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "Set Region Data":
                    new Region(this);
                    break;
                case "Done":
                    this.name = (this.fieldForName.getText() != null) ? this.fieldForName.getText() : "";
                    this.duration = (Integer.valueOf(this.fieldForDuration.getText()) != null)
                            ? Integer.valueOf(this.fieldForDuration.getText())
                            : 0;
                    this.vpid = (Integer.valueOf(this.fieldForVpid.getText()) != null)
                            ? Integer.valueOf(this.fieldForVpid.getText())
                            : 0;
                    this.sprite = (this.fieldForSprite.getText() != null) ? this.fieldForSprite.getText() : "";
                    this.to = (this.fieldForTo.getText() != null) ? this.fieldForTo.getText() : "";
                    parent.link = this;
                    frame5.dispose();
                    break;
            }
        }
    }

    public class Region implements ActionListener {
        String name;
        String shape;
        float x;
        float y;
        float w;
        float h;
        float rotation;

        Link parent;

        JFrame frame6;

        JTextField fieldForName = new JTextField();
        JTextField fieldForShape = new JTextField();
        JTextField fieldForX = new JTextField();
        JTextField fieldForY = new JTextField();
        JTextField fieldForW = new JTextField();
        JTextField fieldForH = new JTextField();
        JTextField fieldForRotation = new JTextField();

        JLabel label19 = new JLabel("Name");
        JLabel label20 = new JLabel("Shape");
        JLabel label21 = new JLabel("X");
        JLabel label22 = new JLabel("Y");
        JLabel label23 = new JLabel("W");
        JLabel label24 = new JLabel("H");
        JLabel label25 = new JLabel("Rotation");

        JButton button10;

        public Region(Link parent) {
            this.parent = parent;
            frame6 = new JFrame();

            button10 = new JButton("Done");
            button10.setFocusable(false);
            button10.addActionListener(this);

            JButton baux = new JButton();

            button10.setBounds(350, 400, 200, 100);
            baux.setBounds(0, 0, 0, 0);
            baux.setVisible(false);

            label19.setBounds(50, 50, 200, 30);
            label20.setBounds(50, 120, 200, 30);
            label21.setBounds(50, 190, 200, 30);
            label22.setBounds(50, 260, 200, 30);
            label23.setBounds(50, 330, 200, 30);
            label24.setBounds(50, 400, 200, 30);
            label25.setBounds(50, 470, 200, 30);

            fieldForName.setBounds(50, 80, 200, 30);
            fieldForShape.setBounds(50, 150, 200, 30);
            fieldForX.setBounds(50, 220, 200, 30);
            fieldForY.setBounds(50, 290, 200, 30);
            fieldForW.setBounds(50, 360, 200, 30);
            fieldForH.setBounds(50, 430, 200, 30);
            fieldForRotation.setBounds(50, 500, 200, 30);

            frame6.add(label19);
            frame6.add(label20);
            frame6.add(label21);
            frame6.add(label22);
            frame6.add(label23);
            frame6.add(label24);
            frame6.add(label25);

            frame6.add(fieldForName);
            frame6.add(fieldForShape);
            frame6.add(fieldForX);
            frame6.add(fieldForY);
            frame6.add(fieldForW);
            frame6.add(fieldForH);
            frame6.add(fieldForRotation);

            frame6.add(button10);
            frame6.add(baux);

            frame6.setBounds(400, 400, 600, 600);

            frame6.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame6.setVisible(true);
        }

        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "Done":
                    this.name = (this.fieldForName.getText() != null) ? this.fieldForName.getText() : "";
                    this.shape = (this.fieldForShape.getText() != null) ? this.fieldForShape.getText() : "";
                    this.x = (Float.valueOf(this.fieldForX.getText()) != null) ? Float.valueOf(this.fieldForX.getText())
                            : 0;
                    this.y = (Float.valueOf(this.fieldForY.getText()) != null) ? Float.valueOf(this.fieldForY.getText())
                            : 0;
                    this.w = (Float.valueOf(this.fieldForW.getText()) != null) ? Float.valueOf(this.fieldForW.getText())
                            : 0;
                    this.h = (Float.valueOf(this.fieldForH.getText()) != null) ? Float.valueOf(this.fieldForH.getText())
                            : 0;
                    this.rotation = (Float.valueOf(this.fieldForRotation.getText()) != null)
                            ? Float.valueOf(this.fieldForRotation.getText())
                            : 0;
                    parent.region = this;
                    frame6.dispose();
                    break;
            }
        }
    }

    public String setData(Scene scene) {
        sb.append("<umf:next-id>0</umf:next-id>\n" +
                "            \n" +
                "            <umf:schemas>\n" +
                "                <rdf:Bag>\n" +
                "                    <rdf:li rdf:parseType=\"Resource\">\n" +
                "                        <!-- JLINK Metadata -->\n" +
                "                        <umf:schema>scene</umf:schema>\n" +
                "                        <umf:descriptors>\n" +
                "                            \n" +
                "                            <rdf:Bag>\n" +
                "                                <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                    <umf:name>version</umf:name>\n" +
                "                                    <umf:type>integer</umf:type>\n" +
                "                                </rdf:li>\n" +
                "\n" +
                "                                <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                    <umf:name>title</umf:name>\n" +
                "                                    <umf:type>string</umf:type>\n" +
                "                                </rdf:li>\n" +
                "\n" +
                "                                <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                    <umf:name>note</umf:name>\n" +
                "                                    <umf:type>string</umf:type>\n" +
                "                                </rdf:li>\n" +
                "\n" +
                "                                <rdf:li rdf:parseType=\"Resource\">\n" +
                "\n" +
                "                                    <umf:schema>viewport</umf:schema>\n" +
                "\n" +
                "                                    <umf:descriptors>\n" +
                "                                        <rdf:Bag>\n" +
                "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                                <umf:name>x</umf:name>\n" +
                "                                                <umf:type>real</umf:type>\n" +
                "                                            </rdf:li>\n" +
                "\n" +
                "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                                <umf:name>y</umf:name>\n" +
                "                                                <umf:type>real</umf:type>\n" +
                "                                            </rdf:li>\n" +
                "\n" +
                "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                                <umf:name>xFOV</umf:name>\n" +
                "                                                <umf:type>real</umf:type>\n" +
                "                                            </rdf:li>\n" +
                "\n" +
                "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                                <umf:name>yFOV</umf:name>\n" +
                "                                                <umf:type>real</umf:type>\n" +
                "                                            </rdf:li>\n" +
                "\n" +
                "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                                <umf:name>id</umf:name>\n" +
                "                                                <umf:type>integer</umf:type>\n" +
                "                                            </rdf:li>\n" +
                "                                        </rdf:Bag>\n" +
                "                                    </umf:descriptors>\n" +
                "                                </rdf:li>\n" +
                "\n" +
                "                                <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                    <umf:schema>image</umf:schema>\n" +
                "\n" +
                "                                    <umf:descriptors>\n" +
                "                                        <rdf:Bag>\n" +
                "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                                <umf:name>format</umf:name>\n" +
                "                                                <umf:type>string</umf:type>\n" +
                "                                            </rdf:li>\n" +
                "\n" +
                "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                                <umf:name>href</umf:name>\n" +
                "                                                <umf:type>string</umf:type>\n" +
                "                                            </rdf:li>\n" +
                "                                        </rdf:Bag>\n" +
                "                                    </umf:descriptors>\n" +
                "                                </rdf:li>\n" +
                "                            </rdf:Bag>\n" +
                "                        </umf:descriptors>\n" +
                "                    </rdf:li>\n" +
                "                    \n" +
                "                    <rdf:li rdf:parseType=\"Resource\">\n" +
                "                        <umf:schema>link</umf:schema>\n" +
                "                    \n" +
                "                        <umf:descriptors>\n" +
                "                            <rdf:Bag>\n" +
                "                                <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                    <umf:schema>region</umf:schema>\n" +
                "                                    \n" +
                "                                    <umf:descriptors>\n" +
                "                                        <rdf:Bag>\n" +
                "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                                <umf:name>shape</umf:name>\n" +
                "                                                <umf:type>string</umf:type>\n" +
                "                                            </rdf:li>\n" +
                "                                            \n" +
                "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                                <umf:name>x</umf:name>\n" +
                "                                                <umf:type>real</umf:type>\n" +
                "                                            </rdf:li>\n" +
                "                                            \n" +
                "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                                <umf:name>y</umf:name>\n" +
                "                                                <umf:type>real</umf:type>\n" +
                "                                            </rdf:li>\n" +
                "                                            \n" +
                "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                                <umf:name>w</umf:name>\n" +
                "                                                <umf:type>real</umf:type>\n" +
                "                                            </rdf:li>\n" +
                "                                            \n" +
                "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                                <umf:name>h</umf:name>\n" +
                "                                                <umf:type>real</umf:type>\n" +
                "                                            </rdf:li>\n" +
                "                                            \n" +
                "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                                <umf:name>rotation</umf:name>\n" +
                "                                                <umf:type>real</umf:type>\n" +
                "                                            </rdf:li>\n" +
                "                                            \n" +
                "                                        </rdf:Bag>\n" +
                "                                    </umf:descriptors>\n" +
                "                                </rdf:li>\n" +
                "                    \n" +
                "                                <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                    <umf:name>duration</umf:name>\n" +
                "                                    <umf:type>integer</umf:type>\n" +
                "                                </rdf:li>\n" +
                "                                \n" +
                "                                <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                    <umf:name>vpid</umf:name>\n" +
                "                                    <umf:type>integer</umf:type>\n" +
                "                                </rdf:li>\n" +
                "                                \n" +
                "                                <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                    <umf:name>sprite</umf:name>\n" +
                "                                    <umf:type>string</umf:type>\n" +
                "                                </rdf:li>\n" +
                "                                \n" +
                "                                <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                    <umf:name>to</umf:name>\n" +
                "                                    <umf:type>string</umf:type>\n" +
                "                                </rdf:li>\n" +
                "                                \n" +
                "                            </rdf:Bag>\n" +
                "                        </umf:descriptors>\n" +
                "                    </rdf:li>\n" +
                "                </rdf:Bag>\n" +
                "\n" +
                "            </umf:schemas>");

        sb.append("<umf:metadata>\n" +
                "                <rdf:Bag>\n" +
                "                    <rdf:li rdf:parseType=\"Resource\">\n" +
                "                        <umf:schema>scene</umf:schema>\n" +
                "                        <umf:set>\n" +
                "                            <rdf:Bag>\n" +
                "                                <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                    <rdf:value>" + scene.version + "</rdf:value>\n" +
                "                                    <umf:name>version</umf:name>\n" +
                "                                </rdf:li>\n" +
                "                                <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                    <rdf:value>" + scene.title + "</rdf:value>\n" +
                "                                    <umf:name>title</umf:name>\n" +
                "                                </rdf:li>\n" +
                "                                <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                    <rdf:value>" + scene.note + "</rdf:value>\n" +
                "                                    <umf:name>note</umf:name>\n" +
                "                                </rdf:li>\n" +
                "                                <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                    <umf:schema>viewport</umf:schema>\n" +
                "                                    <umf:set>\n" +
                "                                        <rdf:Bag>\n" +
                "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                                <rdf:value>" + scene.viewport.x + "</rdf:value>\n" +
                "                                                <umf:name>x</umf:name>\n" +
                "                                            </rdf:li>\n" +
                "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                                <rdf:value>" + scene.viewport.y + "</rdf:value>\n" +
                "                                                <umf:name>y</umf:name>\n" +
                "                                            </rdf:li>\n" +
                "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                                <rdf:value>" + scene.viewport.xFov + "</rdf:value>\n" +
                "                                                <umf:name>xFOV</umf:name>\n" +
                "                                            </rdf:li>\n" +
                "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                                <rdf:value>" + scene.viewport.yFov + "</rdf:value>\n" +
                "                                                <umf:name>yFOV</umf:name>\n" +
                "                                            </rdf:li>\n" +
                "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                                <rdf:value>" + scene.viewport.id + "</rdf:value>\n" +
                "                                                <umf:name>id</umf:name>\n" +
                "                                            </rdf:li>\n" +
                "                                        </rdf:Bag>\n" +
                "                                    </umf:set>\n" +
                "                                </rdf:li>\n" +
                "                                <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                    <umf:schema>image</umf:schema>\n" +
                "                                    <umf:set>\n" +
                "                                        <rdf:Bag>\n" +
                "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                                <rdf:value>" + scene.image.format + "</rdf:value>\n" +
                "                                                <umf:name>format</umf:name>\n" +
                "                                            </rdf:li>\n" +
                "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                                <rdf:value>" + scene.image.href + "</rdf:value>\n" +
                "                                                <umf:name>href</umf:name>\n" +
                "                                            </rdf:li>\n" +
                "                                        </rdf:Bag>\n" +
                "                                    </umf:set>\n" +
                "                                </rdf:li>\n" +
                "                            </rdf:Bag>\n" +
                "                        </umf:set>\n" +
                "                    </rdf:li>\n" +
                "                    <rdf:li rdf:parseType=\"Resource\">\n" +
                "                        <umf:schema>link</umf:schema>\n" +
                "                        <umf:set>\n" +
                "                            <rdf:Bag>\n" +
                "                                <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                    <umf:schema>region</umf:schema>\n" +
                "                                    <umf:set>\n" +
                "                                        <rdf:Bag>\n" +
                "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                                <rdf:value>" + scene.link.region.shape
                + "</rdf:value>\n" +
                "                                                <umf:name>shape</umf:name>\n" +
                "                                            </rdf:li>\n" +
                "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                                <rdf:value>" + scene.link.region.x + "</rdf:value>\n" +
                "                                                <umf:name>x</umf:name>\n" +
                "                                            </rdf:li>\n" +
                "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                                <rdf:value>" + scene.link.region.y + "</rdf:value>\n" +
                "                                                <umf:name>y</umf:name>\n" +
                "                                            </rdf:li>\n" +
                "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                                <rdf:value>" + scene.link.region.w + "</rdf:value>\n" +
                "                                                <umf:name>width</umf:name>\n" +
                "                                            </rdf:li>\n" +
                "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                                <rdf:value>" + scene.link.region.h + "</rdf:value>\n" +
                "                                                <umf:name>height</umf:name>\n" +
                "                                            </rdf:li>\n" +
                "                                            <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                                <rdf:value>" + scene.link.region.rotation
                + "</rdf:value>\n" +
                "                                                <umf:name>rotation</umf:name>\n" +
                "                                            </rdf:li>\n" +
                "                                        </rdf:Bag>\n" +
                "                                    </umf:set>\n" +
                "                                </rdf:li>\n" +
                "                                <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                    <rdf:value>" + scene.link.duration + "</rdf:value>\n" +
                "                                    <umf:name>duration</umf:name>\n" +
                "                                </rdf:li>\n" +
                "                                <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                    <rdf:value>" + scene.link.vpid + "</rdf:value>\n" +
                "                                    <umf:name>vpid</umf:name>\n" +
                "                                </rdf:li>\n" +
                "                                <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                    <rdf:value>" + scene.link.sprite + "</rdf:value>\n" +
                "                                    <umf:name>sprite</umf:name>\n" +
                "                                </rdf:li>\n" +
                "                                <rdf:li rdf:parseType=\"Resource\">\n" +
                "                                    <rdf:value>" + scene.link.to + "</rdf:value>\n" +
                "                                    <umf:name>to</umf:name>\n" +
                "                                </rdf:li>\n" +
                "                            </rdf:Bag>\n" +
                "                        </umf:set>\n" +
                "                    </rdf:li>\n" +
                "                </rdf:Bag>\n" +
                "            </umf:metadata>"
                + "\t\t</rdf:Description>\n" +
                "\t</rdf:RDF>\n" +
                "</x:xmpmeta>\n" +
                "<?xpacket end=\"w\"?>");
        return sb.toString();
    }

    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Set Scene Data":
                new Scene(this);
                break;

            case "Done":
                System.out.println(this.setData(this.scene));
                fileName = this.fileNameField.getText();
                for (byte b : this.setData(scene).getBytes()) {
                    baos.write(b);
                }
                System.out.println(this.fileName);
                try {
                    Files.write(Paths.get(folderName + fileName + ".xsd"), baos.toByteArray());
                } catch (IOException ex) {
                    Logger.getLogger(XMLBuilder.class.getName()).log(Level.SEVERE, null, ex);
                }

                frame.dispose();

        }
    }
}