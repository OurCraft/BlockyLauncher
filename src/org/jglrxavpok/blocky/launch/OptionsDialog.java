package org.jglrxavpok.blocky.launch;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class OptionsDialog extends JDialog
{

    private static final long serialVersionUID = 3594605807931261054L;
    private JTextField path;

    public OptionsDialog(JFrame parent)
    {
        super(parent, "Blocky Options", true);
        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
            }
        });
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        JPanel folderPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        folderPane.add(new JLabel("Working folder for the game: "));
        path = new JTextField(40);
        path.setText(BlockyLauncher.getFolder().getPath());
        JButton find = new JButton("Browse...");
        find.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.showOpenDialog(OptionsDialog.this);
                File file = chooser.getSelectedFile();
                if(file != null)
                {
                    path.setText(file.getPath());
                }
            }
        });
        folderPane.add(path);
        folderPane.add(find);
        content.add(folderPane);
        JButton confirm = new JButton("Confirm");
        confirm.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                save();
                OptionsDialog.this.dispose();
            }
        });
        JButton close = new JButton("Cancel");
        close.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                OptionsDialog.this.dispose();
            }
        });
        
        JPanel pane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pane.add(confirm);
        pane.add(close);
        content.add(pane);
        this.setContentPane(content);
        pack();
        setLocationRelativeTo(parent);
    }

    protected void save()
    {
        BlockyLauncher.folder = new File(path.getText());
        BlockyLauncher.setProperty("workFolder", path.getText());
    }
}
