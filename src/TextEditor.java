import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;

public class TextEditor extends JFrame implements ActionListener {

  JTextArea textArea;
  JScrollPane scrollPane;
  JSpinner fontSizeSpinner;
  JLabel fontLabel;
  JButton fontColorButton;
  JComboBox fontBox;

  JMenuBar menuBar;
  JMenu fileMenu;
  JMenuItem openItem;
  JMenuItem saveItem;
  JMenuItem exitItem;

  TextEditor() {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setTitle("Text Editor");
    this.setSize(500, 500);
    this.setLayout(new FlowLayout());
    this.setLocationRelativeTo(null);

    textArea = new JTextArea();
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    textArea.setFont(new Font("Arial", Font.PLAIN, 20));

    scrollPane = new JScrollPane(textArea);
    scrollPane.setPreferredSize(new Dimension(450, 450));
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    fontLabel = new JLabel("Font: ");

    fontSizeSpinner = new JSpinner();
    fontSizeSpinner.setPreferredSize(new Dimension(50, 25));
    fontSizeSpinner.setValue(20);
    fontSizeSpinner.addChangeListener(e -> {
      textArea.setFont(new Font(textArea.getFont().getFamily(), Font.PLAIN, (int) fontSizeSpinner.getValue()));
    });

    fontColorButton = new JButton("Color");
    fontColorButton.addActionListener(this);

    String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

    fontBox = new JComboBox(fonts);
    fontBox.addActionListener(this);
    fontBox.setSelectedItem("Arial");

    // --- Menu Bar ---

    menuBar = new JMenuBar();
    fileMenu = new JMenu("File");
    openItem = new JMenuItem("Open");
    saveItem = new JMenuItem("Save");
    exitItem = new JMenuItem("Exit");

    openItem.addActionListener(this);
    saveItem.addActionListener(this);
    exitItem.addActionListener(this);

    fileMenu.add(openItem);
    fileMenu.add(saveItem);
    fileMenu.add(exitItem);

    menuBar.add(fileMenu);

    // --- Menu Bar ---

    this.setJMenuBar(menuBar);
    this.add(fontLabel);
    this.add(fontSizeSpinner);
    this.add(fontColorButton);
    this.add(fontBox);
    this.add(scrollPane);
    this.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == fontColorButton) {
      JColorChooser colorChooser = new JColorChooser();
      Color color = colorChooser.showDialog(null, "Choose a color", Color.BLACK);
      textArea.setForeground(color);
    }

    if (e.getSource() == fontBox) {
      textArea.setFont(new Font((String) fontBox.getSelectedItem(), Font.PLAIN, textArea.getFont().getSize()));
    }

    if (e.getSource() == openItem) {
      textArea.setText("");
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setCurrentDirectory(new File("."));
      FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
      fileChooser.setFileFilter(filter);

      int res = fileChooser.showOpenDialog(null);

      if (res == JFileChooser.APPROVE_OPTION) {
        File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
        Scanner fileIn = null;

        try {
          fileIn = new Scanner(file);
          if (file.isFile()) {
            while (fileIn.hasNextLine()) {
              String line = fileIn.nextLine() + "\n";
              textArea.append(line);
            }
          }
        } catch (Exception ex) {
          System.out.println(ex);
        } finally {
          fileIn.close();
        }
      }

    }

    if (e.getSource() == saveItem) {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setCurrentDirectory(new File("."));

      int res = fileChooser.showSaveDialog(null);

      if (res == JFileChooser.APPROVE_OPTION) {
        File file;
        PrintWriter fileOut = null;

        file = new File(fileChooser.getSelectedFile().getAbsolutePath());
        try {
          fileOut = new PrintWriter(file);
          fileOut.println(textArea.getText());
        } catch (Exception ex) {
          System.out.println(ex);
        } finally {
          fileOut.close();
        }

      }
    }

    if (e.getSource() == exitItem) {
      System.exit(0);
    }
  }

}
