// Source Code : https://www.c-sharpcorner.com/UploadFile/fd0172/how-to-create-editable-notepad-in-java/
import java.io.*;
import java.util.*;
import java.awt.event.*;  
import javax.swing.*;  

public class Notepad implements ActionListener  
{  
    JFrame frm;  
    JMenuBar mnubr;  
    JMenu fileMenu, editMenu;  
    JMenuItem openItem, saveItem, runItem;
    JMenuItem cutItem, copyItem, pasteItem, selectAll;
    JTextArea txtarea;  
    Notepad(String fileName) {
        frm = new JFrame();  
        mnubr = new JMenuBar();  
        mnubr.setBounds(5, 5, 400, 20);  
        
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
        editMenu.add(cutItem);  
        editMenu.add(copyItem);  
        editMenu.add(pasteItem);  
        editMenu.add(selectAll);  
        cutItem = new JMenuItem("cutItem");  
        copyItem = new JMenuItem("copyItem");  
        pasteItem = new JMenuItem("pasteItem");  
        selectAll = new JMenuItem("selectAllItem");  
        copyItem.addActionListener(this);  
        cutItem.addActionListener(this);  
        selectAll.addActionListener(this);  
        pasteItem.addActionListener(this);

        mnubr.add(fileMenu);  
        mnubr.add(editMenu);
        txtarea = new JTextArea();  
        txtarea.setBounds(5, 30, 460, 460);  
        frm.add(mnubr);  
        frm.add(txtarea);  
        frm.setLayout(null);  
        frm.setSize(500, 500);  
        frm.setVisible(true);  

        Scanner scan = new Scanner(System.in);
        try {
            scan = new Scanner(new File(fileName));
            while(scan.hasNextLine()) {
                txtarea.append(scan.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Invalid File");
        }
        scan.close();
    }  
    
    public void actionPerformed(ActionEvent ae)  
    {  
        if (ae.getSource() == cutItem)  
            txtarea.cut();  
        else if (ae.getSource() == pasteItem)  
            txtarea.paste();  
        else if (ae.getSource() == copyItem)  
            txtarea.copy();  
        else if (ae.getSource() == selectAll)  
            txtarea.selectAll();  
    }
}  