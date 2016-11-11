/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winhash;

import java.util.ArrayList;
import java.util.Map;
import SimPLE.Module;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author marci
 */
public class WinHash extends Module{  

    public static void main(String[] args) throws Throwable {
        WinHash winhash = new WinHash("winhash");
        winhash.run(args);
    }
    private final DefaultTableModel data;
    private final JTextField pattern;
    
    public WinHash(String ID) throws InterruptedException {
        super(ID);
        JFrame win = new JFrame("Windows to Hash visualization");
        
        JTable table = new JTable();
        data = new DefaultTableModel( null, new String[]{"ID", "Value"} ){
            @Override
            public Class getColumnClass(int columnIndex) {
                return String.class;
            }
        };
        table.setModel(data);
        table.setEnabled(false) ;
        
        JScrollPane scrollPane = new JScrollPane(table);
        
        //table.setRowHeight(23);
        scrollPane.setPreferredSize(new Dimension(750, 500));
        
        JPanel panel = new JPanel();
        
        JLabel lb = new JLabel("Pattern: ");
        panel.add(lb);
        
        pattern = new JTextField("(.)*", 30);
        
        panel.add(pattern);
        
        panel.add(scrollPane);
        
        win.add(panel);
        
        win.setSize(800, 600);
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setVisible(true);
    }

    @Override
    public void start() throws Throwable {
        //sleep_ms(1000);
    }

    @Override
    public void loop() throws Throwable {
        sleep_ms(100);
        ArrayList<Map.Entry<String, String>> list = hashtable(pattern.getText());
        
        
        //remove all extra rows
        while(data.getRowCount()>list.size()){
            data.removeRow(data.getRowCount()-1);
        }
        //simple update the table
        int n=0;
        for(Map.Entry<String, String> e : list){
            if(n<data.getRowCount()){
                //row exist, only update the values
                if(!data.getValueAt(n, 0).equals(e.getKey())){
                    data.setValueAt(e.getKey(), n, 0);
                }
                data.setValueAt(e.getValue(), n, 1);
            }else{
                //row dont exist, add a new row
                data.addRow(new Object[]{e.getKey(), e.getValue()});
            }
            n++;
        }
    } 
}
