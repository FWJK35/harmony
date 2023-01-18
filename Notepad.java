import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;  
import javax.swing.*;  

public class Notepad implements ActionListener  
{  
    public static int windows = 0;
    private File file;
    private Compiler compiler;
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
        try {
                String fileName = JOptionPane.showInputDialog("Input File Name: ");
                fileName = fileName.trim();
                if (fileName.isEmpty()) {
                    int i = 1;
                    while(new File(fileName).exists()) {
                        fileName = "Untitled " + i; 
                        i++;
                    }
                }
                File file = new File(fileName);
                if (file.createNewFile()) {
                    if (JOptionPane.showConfirmDialog(null, "Create new file named \"" + fileName + "\"?", "Error: File not found", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        new Notepad(fileName);
                    }
                    else {
                        file.delete();
                    }
                }
                else {
                    try {
                        new Notepad(fileName);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Bad file name");
                    }
                }
        } catch (IOException e) {
            e.getStackTrace();
            System.out.println("Error: Invalid File");
        }
    }

    public Notepad(String fileName) {
        windows++;

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

        JScrollPane scroll = new JScrollPane(txtarea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setBounds(0, 20, 500, 500);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        scroll.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 10));

        frm.add(mnubr);
        frm.add(scroll);
        frm.setLayout(null);  
        frm.setSize(500, 500);
        frm.setResizable(true);
        frm.setVisible(true);
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frm.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                scroll.setBounds(0, 20, frm.getWidth()-20, frm.getHeight()-40);
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
            try {
                String fileName = JOptionPane.showInputDialog("Input File Name: ");
                fileName = fileName.trim();
                if (fileName.isEmpty()) {
                    int i = 1;
                    while(new File(fileName).exists()) {
                        fileName = "Untitled " + i; 
                        i++;
                    }
                }
                File file = new File(fileName);
                if (file.createNewFile()) {
                    if (JOptionPane.showConfirmDialog(null, "Create new file named \"" + fileName + "\"?", "Error: File not found", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        new Notepad(fileName);
                    }
                    else {
                        file.delete();
                    }
                }
                else {
                    try {
                        new Notepad(fileName);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Bad file name");
                    }
                }
            } catch (IOException e) {
                e.getStackTrace();
                System.out.println("Error: Invalid File");
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
}  