import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;  
import javax.swing.*;  

public class Notepad implements ActionListener  
{  
    public static int windows = 0;
    private Compiler compiler;
    private File file;
    private JFrame frm;
    private JMenuBar mnubr;  
    private JMenu fileMenu, editMenu;  
    private JMenuItem openItem, saveItem, runItem;
    private JMenuItem cutItem, copyItem, pasteItem, selectAll;
    private JTextArea txtarea;  

    public static void main(String[] args) {
        Main.main(null);
    }

    public Notepad() {
        this(promptFile());
    }

    public Notepad(String fileName) {
        windows++;

        file = new File(fileName);
        compiler = new Compiler(file);
        frm = new JFrame(fileName);
        mnubr = new JMenuBar();
        mnubr.setBounds(0, 0, 500, 20);
        
        fileMenu = new JMenu("File");
        openItem = new JMenuItem("openItem");
        openItem.addActionListener(this);
        fileMenu.add(openItem);
        saveItem = new JMenuItem("saveItem");
        saveItem.addActionListener(this);
        fileMenu.add(saveItem);
        runItem = new JMenuItem("runItem");
        runItem.addActionListener(this);
        fileMenu.add(runItem);

        editMenu = new JMenu("Edit");
        cutItem = new JMenuItem("cutItem");
        editMenu.add(cutItem);
        cutItem.addActionListener(this); 
        copyItem = new JMenuItem("copyItem");
        copyItem.addActionListener(this);
        editMenu.add(copyItem); 
        pasteItem = new JMenuItem("pasteItem");
        editMenu.add(pasteItem);
        pasteItem.addActionListener(this);
        selectAll = new JMenuItem("selectAllItem");
        selectAll.addActionListener(this);  
        editMenu.add(selectAll);  
        
        mnubr.add(fileMenu);  
        mnubr.add(editMenu);
        txtarea = new JTextArea(400, 400);  
        txtarea.setBounds(5,30,460,460);

        JScrollPane scroll = new JScrollPane(txtarea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(0, 20, 500, 500);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(15, 0));
        scroll.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 15));

        frm.add(mnubr);
        frm.add(scroll);
        frm.setLayout(null);  
        frm.setSize(500, 500);
        frm.setResizable(true);
        frm.setVisible(true);
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frm.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                scroll.setBounds(0, mnubr.getHeight(), frm.getWidth()-scroll.getVerticalScrollBar().getWidth(), frm.getHeight()-mnubr.getHeight()-38);
                mnubr.setBounds(0, 0, frm.getWidth(), 20);
            }
        }); 

        try {
            file = new File(fileName);
            Scanner scan = new Scanner(file);
            while(scan.hasNextLine()) {
                txtarea.append(scan.nextLine() + "\n");
            }
            scan.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("the file tastes bad :(");
        }
    }  
    
    public void actionPerformed(ActionEvent ae) {   
        if (ae.getSource() == cutItem) {
            txtarea.cut();  
        }
        else if (ae.getSource() == pasteItem) {    
            txtarea.paste();
        }
        else if (ae.getSource() == copyItem) {
            txtarea.copy();
        }
        else if (ae.getSource() == selectAll) {
            txtarea.selectAll();  
        }

        else if (ae.getSource() == openItem) {
            String fileName = promptFile();
            if (!fileName.isBlank()) { 
                new Notepad(promptFile());
            }
        }

        else if (ae.getSource() == saveItem || ae.getSource() == runItem) {
            try {
                Scanner in = new Scanner(txtarea.getText());
                PrintStream out = new PrintStream(file);
                
                while (in.hasNextLine()) {
                    out.println(in.nextLine());
                }

                in.close();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("idk what happened this should've been fine");
            }
        }
        if (ae.getSource() == runItem) {
            try {
                compiler.runCode(new Scanner(file));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("lol code bad");
            }
        }
    }

    public static String promptFile() {
        String fileName = JOptionPane.showInputDialog("Input File Name: ");
        fileName = fileName.trim();
        if (fileName.isBlank()) {
            int i = 2;
            fileName = "Untitled 1.hrm";
            while(new File(fileName).exists()) {
                fileName = "Untitled" + i + ".hrm"; 
                i++;
            }
        }

        try {
            File file = new File(fileName);
            if (file.createNewFile()) {
                if (JOptionPane.showConfirmDialog(null, "Create new file named \"" + fileName + "\"?", "Error: File not found", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                    file.delete();
                    return null;
                }
            }
            
            if (!file.exists()) {
                System.out.println("Error: Bad file name");
                return null;
            }
        } catch (IOException e) {
            e.getStackTrace();
            System.out.println("Error: Invalid File");
        }
        return fileName;
    }
}  