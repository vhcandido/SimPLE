/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

/**
 *
 * @author marcio
 */
public abstract class sTabbedAbstract extends sPanel{
    public static final FlowLayout layout = new FlowLayout(FlowLayout.LEADING, WGAP, HGAP);
    
    public abstract void ActionPerformed(ActionEvent e, String item, int index) throws Exception;
    public void keyPressed2(KeyEvent e, String item, int index){
        
    }
    public sTabbedAbstract(Color default_color) {
        super(layout, default_color);
    }
    
    public sPanel AddPanel(){
        sPanel panel = new sPanel(cDEFAULT){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawRect(0, 0, getWidth()-1, getHeight()-1);
            }
        };
        this.add(panel);
        return panel;
    }
    public void addMenuItem(Object ...objs){
        int n = 0;
        JButton btm = null;
        sPanel panel = null;
        for(final Object o : objs){
            if(o instanceof KeyStroke){
                //menuItem.setAccelerator((KeyStroke) o);
                btm.setMnemonic(((KeyStroke) o).getKeyCode());
            }else if(o instanceof Boolean){
                btm.setEnabled((Boolean) o);
            }else if(o instanceof String){
                final String name = (String) o;
                if(name.matches("[\\-]*")){
                    if(n%2==1){
                        panel.add(new JLabel(""));
                    }
                    panel = AddPanel();
                }else{
                    final int index = n;
                    btm = new JButton(name);
                    btm.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyPressed(KeyEvent e) {
                            try{
                                keyPressed2(e, name, index);
                            }catch(Exception ex){
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });
                    btm.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            try{
                                ActionPerformed(e, name, index);
                            }catch(Exception ex){
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });
                    if(panel==null){
                        panel = AddPanel();
                    }
                    panel.add(btm);
                    n++;
                }
            }else{
                JOptionPane.showMessageDialog(null, "In FMenu.addMenuItem type not defined", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
        if(n%2==1){
            panel.add(new JLabel(""));
        }
    }
    public void addRadioButtonMenuItem(int selected_index, UIManager.LookAndFeelInfo[] LookAndFeelds) {
        String S[] = new String[LookAndFeelds.length];
        for(int i=0; i<S.length; i++){
            S[i] = LookAndFeelds[i].getName();
        }
        addRadioButtonMenuItem(selected_index, S);
    }
    public void addRadioButtonMenuItem(int selected_index, String ...names) {
        ButtonGroup buttonGroup = new ButtonGroup();
        int n = 0;
        sPanel panel = null;
        for(final String name : names){
            if(name.matches("[\\-]*")){
                if(n%2==1){
                    panel.add(new JLabel(""));
                }
                panel = AddPanel();
            }else{
                final int index = n;
                JRadioButton rb = new JRadioButton(name, n==selected_index);
                buttonGroup.add(rb);
                rb.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try{
                            ActionPerformed(e, name, index);
                        }catch(Exception ex){
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
                if(panel==null){
                    panel = AddPanel();
                }
                panel.add(rb);
            }
            n++;
        }
        if(n%2==1){
            panel.add(new JLabel(""));
        }
    }


    public void Enabled(String ...names) {
        for(String s : names){
            setEnabled(s, true);
        }
    }
    public void Disabled(String ...names) {
        for(String s : names){
            setEnabled(s, false);
        }
    }
    public void setEnabled(String name, boolean b) {
        for(Component comp : this.getComponents()){
            if(comp instanceof JPanel){
                JPanel panel = (JPanel) comp;
                for(Component c : panel.getComponents()){
                    //System.out.println(c);
                    if(c instanceof JButton){
                        JButton m = (JButton) c;
                        if(m.getText().equals(name)){
                            m.setEnabled(b);
                            //System.out.println(name + " : "+b);
                        }
                    }
                }
            }
        }

    }

    @Override
    public void Config(int w, int h) {
        setPreferredSize(new Dimension(w, h));
        for(Component comp : this.getComponents()){
            if(comp instanceof sPanel){
                sPanel panel = (sPanel) comp;
                int n = 0;
                for(Component c : panel.getComponents()){
                    //System.out.println(c);
                    if(c instanceof JButton){
                        JButton m = (JButton) c;
                        m.setPreferredSize(new Dimension(120, 23));
                    }else if(c instanceof JRadioButton){
                        JRadioButton m = (JRadioButton) c;
                        m.setPreferredSize(new Dimension(120, 23));
                    }else if(c instanceof JLabel){
                        JLabel m = (JLabel) c;
                        m.setPreferredSize(new Dimension(120, 23));
                    }
                    n++;
                }
                panel.Config(6+(120+6)*(n/2 + n%2), 6+23+6+23+6);
            }
        }
    }
}
