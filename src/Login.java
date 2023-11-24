import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Login extends JPanel implements ActionListener {

    protected JLabel lb1;
    protected JLabel lb2;
    protected JButton btn1;
    protected JTextField textField;

    protected JTextField textField2;
    protected JTextArea textArea;
    private final static String newline = "\n";

    public Login() {
        super(new GridBagLayout());
        lb1 = new JLabel("Nome");
        lb2 = new JLabel("Senha");
        btn1 = new JButton("Enter");
        btn1.setBounds(100,200,250,70);
        btn1.setFont(new Font("Arial",Font.BOLD,40));
        btn1.setBackground(new Color(222, 239, 227));
        textField = new JTextField(65);
        textField2 = new JTextField( 65);

        textArea = new JTextArea(25, 20);
        textArea.setEditable(false);


        //Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;

        c.fill = GridBagConstraints.HORIZONTAL;
        add(lb1, c);
        add(textField, c);
        add(lb2, c);
        add(textField2, c);
        add(btn1, c);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 100.0;
        c.weighty = 150.0;

        //Adcionando para ele ler a função this/actionPerformed
        btn1.addActionListener(this);


    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add contents to the window.
        frame.add(new Login());

        //Display the window.

        frame.pack();
        frame.setSize(275,200);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);


    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    @Override
    public void actionPerformed(ActionEvent evt) {
        String nome = textField.getText();
        /*ConectaBd x = new ConectaBd;
        try{
            x.conect(nome,senha);
        }
        catch (Error){*/
            JOptionPane.showMessageDialog(null,nome+" sua senha está incorreta", "Erro ao Logar", JOptionPane.ERROR_MESSAGE);
        }
    }
//}