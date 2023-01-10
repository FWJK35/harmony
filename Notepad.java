import java.io.*;
import java.util.*;
import java.awt.event.*;  
import javax.swing.*;  

public class Notepad implements ActionListener  
{  
    public static int windows = 0;
    private File file;
    private JFrame frm;
    private JMenuBar mnubr;  
    private JMenu fileMenu, editMenu;  
    private JMenuItem openItem, saveItem, runItem;
    private JMenuItem cutItem, copyItem, pasteItem, selectAll;
    private JTextArea txtarea;  
    Notepad(String fileName) {
        windows++;

        frm = new JFrame();
        mnubr = new JMenuBar();  
        mnubr.setBounds(5, 5, 500, 20);  
        
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
        copyItem = new JMenuItem("copyItem");  
        pasteItem = new JMenuItem("pasteItem");  
        selectAll = new JMenuItem("selectAllItem");  

        editMenu.add(cutItem);  
        editMenu.add(copyItem);  
        editMenu.add(pasteItem);  
        editMenu.add(selectAll);  
        
        copyItem.addActionListener(this);  
        cutItem.addActionListener(this);  
        selectAll.addActionListener(this);  
        pasteItem.addActionListener(this);

        mnubr.add(fileMenu);  
        mnubr.add(editMenu);
        txtarea = new JTextArea();  
        txtarea.setBounds(5, 30, 460, 460);  
        JScrollPane scroll = new JScrollPane (txtarea);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        frm.add(mnubr);  
        frm.add(txtarea);
        frm.setLayout(null);  
        frm.setSize(500, 500);
        frm.setResizable(true);
        frm.setVisible(true);

        Scanner scan = new Scanner(System.in);
        try {
            file = new File(fileName);
            scan = new Scanner(file);
            while(scan.hasNextLine()) {
                txtarea.append(scan.nextLine() + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Invalid File");
        }
        scan.close();

        
    }  
    
    public void actionPerformed(ActionEvent ae)  
    {   
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
                Compiler.runCode(new Scanner(file));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("idk what happened this should've been fine x2");
            }
        }
    }
}  