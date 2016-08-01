/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userInterface;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Griffin
 */
public class NewPuzzleWindow extends javax.swing.JFrame
{
    
    /**
     * main is the SlideUI that instantiated this. It is called back when this
     * window closes.
     */
    private SlideUI main;

    /**
     * Creates new form NewPuzzleWindow
     * 
     * @param main - SlideUI that is called back once this window closes.
     */
    public NewPuzzleWindow(SlideUI main)
    {
        this.main = main;
        initComponents();
        setTextFieldListeners();
    }

    /**
     * Adds document listeners to the underlying documents of the text fields.
     * This way, any change to the text field's text is an event, so the enter
     * key doesn't need to be pressed.
     */
    private void setTextFieldListeners()
    {
        DocumentListener doclstn = new DocumentListener()
        {
            @Override
            public void changedUpdate(DocumentEvent e)
            {
                createPuzzleButton.setEnabled(checkValidText());
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                createPuzzleButton.setEnabled(checkValidText());
            }

            @Override
            public void insertUpdate(DocumentEvent e)
            {
                createPuzzleButton.setEnabled(checkValidText());
            }
        };
        widthTextField.getDocument().addDocumentListener(doclstn);
        heightTextField.getDocument().addDocumentListener(doclstn);
    }

    /**
     * This checks to see if both text boxes are holding integers that are
     * larger than 1.
     *
     * @return
     */
    private boolean checkValidText()
    {
        return (widthTextField.getText().matches("\\d+")
                && heightTextField.getText().matches("\\d+")
                && !widthTextField.getText().matches("0*(0|1)")
                && !heightTextField.getText().matches("0*(0|1)"));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        promptLabel = new javax.swing.JLabel();
        textInputPanel = new javax.swing.JPanel();
        widthLabel = new javax.swing.JLabel();
        heightLabel = new javax.swing.JLabel();
        heightTextField = new javax.swing.JTextField();
        widthTextField = new javax.swing.JTextField();
        createPuzzleButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setType(java.awt.Window.Type.POPUP);

        promptLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        promptLabel.setText("<html>Enter the dimensions of your puzzle<br/>WARNING:  A Width of 2 may<br/>cause a stack overflow error</html>");
        getContentPane().add(promptLabel, java.awt.BorderLayout.PAGE_START);

        textInputPanel.setMinimumSize(new java.awt.Dimension(240, 100));
        textInputPanel.setPreferredSize(new java.awt.Dimension(240, 100));

        widthLabel.setText("Width:");

        heightLabel.setText("Height:");

        javax.swing.GroupLayout textInputPanelLayout = new javax.swing.GroupLayout(textInputPanel);
        textInputPanel.setLayout(textInputPanelLayout);
        textInputPanelLayout.setHorizontalGroup(
            textInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(textInputPanelLayout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addGroup(textInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(widthLabel)
                    .addComponent(heightLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(textInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(heightTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                    .addComponent(widthTextField))
                .addGap(0, 95, Short.MAX_VALUE))
        );
        textInputPanelLayout.setVerticalGroup(
            textInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(textInputPanelLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(textInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(widthLabel)
                    .addComponent(widthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(textInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(heightLabel)
                    .addComponent(heightTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        getContentPane().add(textInputPanel, java.awt.BorderLayout.CENTER);

        createPuzzleButton.setText("Create Puzzle");
        createPuzzleButton.setEnabled(false);
        createPuzzleButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                createPuzzleButtonActionPerformed(evt);
            }
        });
        getContentPane().add(createPuzzleButton, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void createPuzzleButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_createPuzzleButtonActionPerformed
    {//GEN-HEADEREND:event_createPuzzleButtonActionPerformed
        this.setVisible(false);
        this.dispose();
        main.createPuzzle(Integer.parseInt(widthTextField.getText()),
                Integer.parseInt(heightTextField.getText()));
    }//GEN-LAST:event_createPuzzleButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton createPuzzleButton;
    private javax.swing.JLabel heightLabel;
    private javax.swing.JTextField heightTextField;
    private javax.swing.JLabel promptLabel;
    private javax.swing.JPanel textInputPanel;
    private javax.swing.JLabel widthLabel;
    private javax.swing.JTextField widthTextField;
    // End of variables declaration//GEN-END:variables
}