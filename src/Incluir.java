import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class Incluir extends JPanel implements ActionListener{

    
    protected JButton btnIncluir;

    protected JLabel CpfCliente;
    protected JTextField cpf;

    protected JLabel idProd;
    protected JTextField prodId;

    protected JLabel qtdComprada;
    protected JTextField qtdCompra;

    protected JLabel data;
    protected JTextField dataVenda;

    protected JLabel Cep;
    protected JTextField cepCliente;

    protected JLabel numCasa;
    protected JTextField numeroDaCasa;

    protected JLabel idFunc;
    protected JTextField idFuncionario;

    protected JTextArea textArea;


    public Incluir (){
        super(new GridBagLayout());
        //Definição do idFunc
            idFunc = new JLabel("Id do funcionário");
            idFuncionario = new JTextField(45);
        //Definição da Casa
            numCasa = new JLabel("Número da casa do Cliente");
            numeroDaCasa = new JTextField(45);
        //Definição do CEP
            Cep = new JLabel("CEP da entrega");
            cepCliente = new JTextField(45);
        //Definição do CPF
            CpfCliente = new JLabel("CPF do Cliente");
            cpf = new JTextField(45);
        //Definição do idProd
            idProd = new JLabel("Id Do Produto");
            prodId = new JTextField(45); 
        //Definição do QTD
            qtdComprada = new JLabel("Quantas unidades compradas");
            qtdCompra = new JTextField(45);
        //Definição da data
            data = new JLabel("Data da compra DD-MM-AAAA");
            dataVenda = new JTextField(45);  
        //Definição do botão Incluir
        btnIncluir = new JButton("Incluir");
        btnIncluir.setBounds(100,200,250,100);
        btnIncluir.setFont(new Font("Arial",Font.BOLD,40));
        btnIncluir.setBackground(new Color(222, 239, 227));
        btnIncluir.addActionListener(this::actionPerformed);




        //Definição da área do formulário
        textArea = new JTextArea(55, 55);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);



        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;


        //ADD os itens no formulário
        add(CpfCliente,c);
        add(cpf,c);
        add(idProd,c);
        add(prodId,c);
        add(qtdComprada,c);
        add(qtdCompra,c);
        add(data,c);
        add(dataVenda,c);
        add(Cep,c);
        add(cepCliente,c);
        add(numCasa,c);
        add(numeroDaCasa,c);
        add(idFunc,c);
        add(idFuncionario,c);
        add(btnIncluir,c);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 100.0;
        c.weighty = 100.0;




    }

    public void actionPerformed(ActionEvent e) {
    // Fazer o Lote de comando para incluir no BD 
        JOptionPane.showMessageDialog(null,"Você incluiu a seguinde venda: \n\n\n "+ "CPF do cliente: "+cpf.getText()+"\n ID do Produto: "+prodId.getText()+
        "\n Quantidade Compra: "+qtdCompra.getText() + "\n Data: "+ dataVenda.getText() + "\n CEP: "+ cepCliente.getText() + "\n N°: "+numeroDaCasa.getText() +"\n Quem Vendeu: "+idFuncionario.getText(), "Incluir", JOptionPane.OK_CANCEL_OPTION);
    }
    private static void mostrarGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Mesuring/Incluir Venda");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Adiciona o conteudo do Incluir no formulário
        frame.add(new Incluir());

        //Mostra a janela
        frame.pack();
        frame.setSize(800,500);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mostrarGUI();
            }
        });
    }
}