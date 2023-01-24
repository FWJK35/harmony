/**
* Notepad class creates the frontend user interface
* It serves as a simple text editor for Harmony code
* With open/create, save, and run file functionalities
* Using a private compiler  
*
* Basic framework for implementation of editable notepad from :
* Sandeep Sharma @ c-sharpcorner.com/UploadFile/fd0172/how-to-create-editable-notepad-in-java/
*/ 

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;  
import javax.swing.*;  

public class Notepad implements ActionListener
{  
    private Compiler compiler;
    private File file;
    private JFrame frame;
    private JMenuBar menuBar;  
    private JMenu fileMenu, editMenu;  
    private JMenuItem openItem, saveItem, runItem;
    private JMenuItem cutItem, copyItem, pasteItem, selectAll;
    private JTextArea textArea;  

    public static void main(String[] args) {
        Main.main(null);
    }

    // constructors
    public Notepad() {
        this(promptFile());
    }
    public Notepad(String fileName) {
        file = new File(fileName);
        compiler = new Compiler(file);
        frame = new JFrame(fileName);
        menuBar = new JMenuBar();
        menuBar.setBounds(0, 0, 500, 20);
        
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
        
        menuBar.add(fileMenu);  
        menuBar.add(editMenu);
        textArea = new JTextArea(400, 400);  
        textArea.setBounds(5,30,460,460);
        textArea.setFont(new Font("Courier", Font.PLAIN, 12));

        JScrollPane scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(0, 20, 500, 500);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(15, 0));
        scroll.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 15));

        frame.add(menuBar);
        frame.add(scroll);
        frame.setLayout(null);  
        frame.setSize(500, 500);
        frame.setResizable(true);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                scroll.setBounds(0, menuBar.getHeight(), frame.getWidth()-scroll.getVerticalScrollBar().getWidth(), frame.getHeight()-menuBar.getHeight()-38);
                menuBar.setBounds(0, 0, frame.getWidth(), 20);
            }
        }); 

        try {
            file = new File(fileName);
            Scanner scan = new Scanner(file);
            while(scan.hasNextLine()) {
                textArea.append(scan.nextLine() + "\n");
            }
            scan.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("the file tastes bad :(");
        }
    }  
    
    // menuBar actions implementation
    public void actionPerformed(ActionEvent ae) {   
        if (ae.getSource() == cutItem) {
            textArea.cut();  
        }
        else if (ae.getSource() == pasteItem) {    
            textArea.paste();
        }
        else if (ae.getSource() == copyItem) {
            textArea.copy();
        }
        else if (ae.getSource() == selectAll) {
            textArea.selectAll();  
        }
        else if (ae.getSource() == openItem) {
            new Notepad();
        }

        // saves text to file
        else if (ae.getSource() == saveItem || ae.getSource() == runItem) {
            try {
                Scanner in = new Scanner(textArea.getText());
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

            // compiles and runs saved file for action runItem
            if (ae.getSource() == runItem) {
                try {
                    compiler.compile();
                    compiler.runCode();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("lol code bad");
                }
            }
        }
    }

    // uses a JOptionPane to prompt user for a file name
    // if file not found, creates a new file upon confirmation
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