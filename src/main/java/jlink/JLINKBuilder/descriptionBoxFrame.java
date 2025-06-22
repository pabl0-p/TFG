package jlink.JLINKBuilder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.mipams.jumbf.entities.ContiguousCodestreamBox;
import org.mipams.jumbf.entities.JumbfBoxBuilder;
import org.mipams.jumbf.services.content_types.ContiguousCodestreamContentType;
import org.mipams.jumbf.util.MipamsException;

/**
 *
 * @author carlos
 */
public class descriptionBoxFrame implements ActionListener {
    JFrame frame;
    JFrame frame2;
    JFrame frame3;

    JTextField tfLabel = new JTextField();
    JTextField tfID = new JTextField();
    JTextField tfUUID = new JTextField();
    JTextField tfMediaType = new JTextField();
    JTextField tfFileName = new JTextField();
    JLabel labelLabel = new JLabel("Set JUMBF box Label");
    JLabel labelId = new JLabel("Set JUMBF box Id");
    JLabel labelType = new JLabel("Set JUMBF box Type");
    JLabel labelUUID = new JLabel("Set UUID format XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX");
    JLabel labelMediaType = new JLabel("Set Media Type");
    JLabel labelFileName = new JLabel("Set File name");
    JCheckBox Bit1 = new JCheckBox("Is Requestable");
    JCheckBox Bit2 = new JCheckBox("Has Label");
    JCheckBox Bit3 = new JCheckBox("Has ID");
    JCheckBox Bit4 = new JCheckBox("Has Signature");

    JumbfBoxBuilder parentBoxBuilder;

    public descriptionBoxFrame(JumbfBoxBuilder jlinkBoxBuilder) throws Exception {
        this.parentBoxBuilder = jlinkBoxBuilder;
        frame = new JFrame();
        setDescriptionFrame();
    }

    protected void setDescriptionFrame() throws Exception {

        JButton b1 = new JButton("Continue");
        JButton baux = new JButton();

        baux.setVisible(false);

        tfLabel.setBounds(50, 60, 300, 30);
        tfID.setBounds(50, 140, 50, 30);
        b1.setBounds(250, 300, 100, 40);
        labelLabel.setBounds(50, 20, 300, 30);
        labelId.setBounds(50, 100, 300, 30);
        labelType.setBounds(50, 180, 300, 30);
        Bit1.setBounds(300, 120, 200, 20);
        Bit2.setBounds(300, 140, 200, 20);
        Bit3.setBounds(300, 160, 200, 20);
        Bit4.setBounds(300, 180, 200, 20);

        tfLabel.addActionListener(this);
        tfID.addActionListener(this);
        b1.addActionListener(this);
        Bit1.addActionListener(this);
        Bit2.addActionListener(this);
        Bit3.addActionListener(this);
        Bit4.addActionListener(this);

        frame.setSize(500, 400);
        frame.getContentPane().add(tfLabel);
        frame.getContentPane().add(tfID);
        frame.getContentPane().add(b1);
        frame.getContentPane().add(labelLabel);
        frame.getContentPane().add(labelId);
        frame.getContentPane().add(labelType);
        frame.getContentPane().add(Bit1);
        frame.getContentPane().add(Bit2);
        frame.getContentPane().add(Bit3);
        frame.getContentPane().add(Bit4);

        frame.getContentPane().add(baux);
        frame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        fileGetter fg2 = new fileGetter();
        File f2 = fg2.getFile("Select a CodeStream file");

        ContiguousCodestreamBox jp2c = new ContiguousCodestreamBox();
        jp2c.setFileUrl(f2.getAbsolutePath());

        ContiguousCodestreamContentType service = new ContiguousCodestreamContentType();

        JumbfBoxBuilder jumbfBoxBuilder = new JumbfBoxBuilder(service);
        jumbfBoxBuilder.setJumbfBoxAsRequestable();
        jumbfBoxBuilder.setLabel(tfLabel.getText());
        jumbfBoxBuilder.setId(Integer.parseInt(tfID.getText()));
        jumbfBoxBuilder.appendContentBox(jp2c);

        try {
            parentBoxBuilder.appendContentBox(jumbfBoxBuilder.getResult());
        } catch (MipamsException ex) {
            Logger.getLogger(descriptionBoxFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        fg2.dispose();

        frame.dispose();

    }

}
