package jlink.JLINKBuilder;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 *
 * @author carlos
 */
public class fileGetter {
    JFrame frame = new JFrame();

    public File getFile(String option) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(option);
        frame.setSize(fileChooser.getSize());
        fileChooser.showOpenDialog(frame);
        frame.getContentPane().add(fileChooser);
        return (File) fileChooser.getSelectedFile().getAbsoluteFile();
    }

    public void dispose() {
        frame.dispose();
    }
}
