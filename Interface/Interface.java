import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Interface {

    private static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String DB_URL = "jdbc:sqlserver://regulus.cotuca.unicamp.br:1433;databaseName=BD23334;trustServerCertificate=true";
    private static final String USER = "BD23334";
    private static final String PASS = "BD23334";

    private Connection conn;
    private JFrame frame;
    private JTextField userField;
    private JPasswordField passField;
    private JButton btnLogin;
    private JTable table;
    private DefaultTableModel tableModel;

    JTextField idProdField = new JTextField();
    JTextField qtdCompradaField = new JTextField();
    JTextField dataField = new JTextField();
    JTextField CEPField = new JTextField();
    JTextField numCasaField = new JTextField();
    JTextField idFuncField = new JTextField();
    JTextField idVendaField = new JTextField();

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Interface window = new Interface();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Interface() {
        initialize();
        connectToDatabase();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        JPanel loginPanel = createLoginPanel();
        frame.getContentPane().add(loginPanel, BorderLayout.NORTH);

        JPanel consultaPanel = createConsultaPanel();
        frame.getContentPane().add(consultaPanel, BorderLayout.CENTER);
        consultaPanel.setVisible(false);
    }

    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new FlowLayout());

        JLabel lblUser = new JLabel("User:");
        loginPanel.add(lblUser);

        userField = new JTextField(15);
        loginPanel.add(userField);

        JLabel lblPass = new JLabel("Password:");
        loginPanel.add(lblPass);

        passField = new JPasswordField(15);
        loginPanel.add(passField);

        btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fazerLogin();
            }
        });
        loginPanel.add(btnLogin);

        return loginPanel;
    }

    private JPanel createConsultaPanel() {
        JPanel pedidoPanel = new JPanel(new BorderLayout());

        // Tabela para mostrar as consultas
        tableModel = new DefaultTableModel();
        tableModel.addColumn("idProd");
        tableModel.addColumn("qtd_Comprada");
        tableModel.addColumn("data");
        tableModel.addColumn("CEP");
        tableModel.addColumn("numCasa");
        tableModel.addColumn("idFunc");
        tableModel.addColumn("idVenda");

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        pedidoPanel.add(scrollPane, BorderLayout.CENTER);

        // Botões para adicionar, alterar e cancelar consultas
        JPanel buttonPanel = new JPanel();
        JButton btnAdicionarPedido = new JButton("Adicionar Pedido");
        btnAdicionarPedido.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirInserirPedido();
            }
        });

        JButton btnAlterarPedido = new JButton("Alterar Pedido");
        btnAlterarPedido.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirAlterarPedido();
            }
        });

        JButton btnCancelarPedido = new JButton("Cancelar Pedido");
        btnCancelarPedido.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelarPedido();
            }
        });

        buttonPanel.add(btnAdicionarPedido);
        buttonPanel.add(btnAlterarPedido);
        buttonPanel.add(btnCancelarPedido);
        pedidoPanel.add(buttonPanel, BorderLayout.SOUTH);

        return pedidoPanel;
    }

    private void connectToDatabase() {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Conexão com o banco de dados estabelecida com sucesso.");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Erro ao conectar ao banco de dados. Verifique o console para mais detalhes: " + e.getMessage());
        }
    }

    private void fazerLogin() {
        // Utilizaremos as suas credenciais diretamente
        String user = USER;
        String pass = PASS;

        if (verificarLogin(user, pass)) {
            mostrarPedidos();
        } else {
            JOptionPane.showMessageDialog(frame, "Login falhou. Verifique suas credenciais.");
        }
    }

    private boolean verificarLogin(String user, String pass) {
        String query =  "SELECT * FROM pra.Funcionario WHERE fFirNome = ? and senha = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, user);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Erro ao verificar login. Verifique suas credenciais.");
        }

        return false;
    }

    private void mostrarPedidos() {
        frame.getContentPane().getComponent(0).setVisible(false); // Oculta o painel de login
        frame.getContentPane().getComponent(1).setVisible(true); // Exibe o painel de consultas

        // Carregar consultas do banco de dados
        List<Pedido> pedidos = obterPedidosDoBanco();

        // Limpar tabela
        tableModel.setRowCount(0);

        // Adicionar consultas à tabela
        for (Pedido pedido : pedidos) {
            tableModel.addRow(pedido.toArray());
        }
    }

    private List<Pedido> obterPedidosDoBanco() {
        List<Pedido> pedidos = new ArrayList<>();

        String sql = "SELECT IdProd, qtd_Comprada, data, CEP, numCasa, idFunc, idVenda FROM pra.pedido";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int idProd = resultSet.getInt("IdProd");
                int qtd_Comprada = resultSet.getInt("qtd_Comprada");
                String CEP = resultSet.getString("CEP");
                int numCasa = resultSet.getInt("numCasa");
                int idFunc = resultSet.getInt("idFunc");

                // Formatando a data usando SimpleDateFormat
                Timestamp dataTimestamp = resultSet.getTimestamp("data");
                //String dataPedido = formatarData(dataTimestamp);

                int idVenda = resultSet.getInt("idVenda");

                Pedido pedido = new Pedido (idProd, qtd_Comprada, dataTimestamp, CEP, numCasa, idFunc , idVenda);
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pedidos;
    }

    private String formatarData(Timestamp dataTimestamp) {
        // Formatando a data usando SimpleDateFormat
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(dataTimestamp);
    }

    private void abrirInserirPedido() {
        // Interface para adicionar consulta
        JFrame inserirFrame = new JFrame("Adicionar Pedido");
        inserirFrame.setBounds(100, 100, 400, 300);
        inserirFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inserirFrame.getContentPane().setLayout(new BorderLayout());

        JTextField idProdField = new JTextField();
        JTextField qtdCompradaField = new JTextField();
        JTextField dataField = new JTextField();
        JTextField CEPField = new JTextField();
        JTextField numCasaField = new JTextField();
        JTextField idFuncField = new JTextField();
        JTextField idVendaField = new JTextField();

        JButton btnSalvarPedido = new JButton("Salvar Pedido");
        btnSalvarPedido.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                adicionarPedido(
                        idProdField.getText(),
                        qtdCompradaField.getText(),
                        dataField.getText(),
                        CEPField.getText(),
                        numCasaField.getText(),
                        idFuncField.getText(),
                        idVendaField.getText()
                );
                inserirFrame.dispose();
            }
        });

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inserirFrame.dispose();
            }
        });

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        formPanel.add(new JLabel("Produto ID:"));
        formPanel.add(idProdField);
        formPanel.add(new JLabel("Qtd Comprada:"));
        formPanel.add(qtdCompradaField);
        formPanel.add(new JLabel("Data:"));
        formPanel.add(dataField);
        formPanel.add(new JLabel("CEP:"));
        formPanel.add(CEPField);
        formPanel.add(new JLabel("Num Casa:"));
        formPanel.add(numCasaField);
        formPanel.add(new JLabel("Funcionário ID:"));
        formPanel.add(idFuncField);
        formPanel.add(new JLabel("Venda ID:"));
        formPanel.add(idVendaField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnSalvarPedido);
        buttonPanel.add(btnVoltar);

        inserirFrame.add(formPanel, BorderLayout.CENTER);
        inserirFrame.add(buttonPanel, BorderLayout.SOUTH);

        inserirFrame.setVisible(true);
    }

    private void abrirAlterarPedido() {
        // Interface para alterar consulta
        JFrame alterarFrame = new JFrame("Alterar Pedido");
        alterarFrame.setBounds(100, 100, 400, 300);
        alterarFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        alterarFrame.getContentPane().setLayout(new BorderLayout());

        JTextField idProdField = new JTextField();
        JTextField qtdCompradaField = new JTextField();
        JTextField dataField = new JTextField();
        JTextField CEPField = new JTextField();
        JTextField numCasaField = new JTextField();
        JTextField idFuncField = new JTextField();
        JTextField idVendaField = new JTextField();

        JButton btnBuscarPedido = new JButton("Buscar Pedido");
        btnBuscarPedido.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buscarPedido(idProdField.getText());
            }
        });

        JButton btnAlterarPedido = new JButton("Alterar Pedido");
        btnAlterarPedido.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                alterarPedido(
                        idProdField.getText(),
                        qtdCompradaField.getText(),
                        dataField.getText(),
                        CEPField.getText(),
                        numCasaField.getText(),
                        idFuncField.getText(),
                        idVendaField.getText()
                );
                alterarFrame.dispose();
            }
        });

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                alterarFrame.dispose();
            }
        });

        JPanel formPanel = new JPanel(new GridLayout(8, 2, 5, 5));
        formPanel.add(new JLabel("Pedido ID::"));
        formPanel.add(idProdField);
        formPanel.add(new JLabel("Qtd Comprada:"));
        formPanel.add(qtdCompradaField);
        formPanel.add(new JLabel("Data:"));
        formPanel.add(dataField);
        formPanel.add(new JLabel("CEP:"));
        formPanel.add(CEPField);
        formPanel.add(new JLabel("Num Casa:"));
        formPanel.add(numCasaField);
        formPanel.add(new JLabel("Func ID:"));
        formPanel.add(idFuncField);
        formPanel.add(new JLabel("Venda ID:"));
        formPanel.add(idVendaField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnBuscarPedido);
        buttonPanel.add(btnAlterarPedido);
        buttonPanel.add(btnVoltar);

        alterarFrame.add(formPanel, BorderLayout.CENTER);
        alterarFrame.add(buttonPanel, BorderLayout.SOUTH);

        alterarFrame.setVisible(true);
    }


    private void buscarPedido(String idProd) {
        if (pedidoExiste(idProd)) {
            String sql = "SELECT * FROM pra.pedido WHERE IdProd = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(idProd));
                ResultSet resultSet = pstmt.executeQuery();

                if (resultSet.next()) {
                    idProd = String.valueOf(resultSet.getInt("IdProd"));;
                    int qtdComprada = resultSet.getInt("qtd_Comprada");
                    int numCasa = resultSet.getInt("numCasa");
                    String CEP = resultSet.getString("CEP");

                    // Formatando a data usando SimpleDateFormat
                    Timestamp dataPedidoTimestamp = resultSet.getTimestamp("DataPedido");
                    String dataPedido = formatarData(dataPedidoTimestamp);

                    int idFunc = resultSet.getInt("idFunc");
                    int idVenda = resultSet.getInt("idVenda");

                    // Preencher os campos da interface com os dados da consulta
                    // Aqui, você deve ter campos correspondentes na interface gráfica (JTextField, etc.)
                    idProdField.setText(String.valueOf(idProd));
                    qtdCompradaField.setText(String.valueOf(qtdComprada));
                    dataField.setText(String.valueOf(dataPedido));
                    CEPField.setText(String.valueOf(CEP));
                    numCasaField.setText(String.valueOf(numCasa));
                    idFuncField.setText(String.valueOf(idFunc));
                    idVendaField.setText(String.valueOf(idVenda));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Erro ao buscar pedido. Verifique o console para mais detalhes.");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Pedido não encontrado.");
        }
    }

    private void alterarPedido(String idProd, String qtdComprada, String data, String CEP, String numCasa, String idFunc, String idVenda) {
        if (pedidoExiste(idProd)) {
            String sql = "UPDATE pra.pedido qtd_Comprada = ?, data = ?, CEP = ?, numCasa = ?, idFunc = ?, idVenda = ? WHERE IdProd = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(idProd));
                pstmt.setInt(2, Integer.parseInt(qtdComprada));
                pstmt.setString(3, qtdComprada);
                pstmt.setString(4, CEP);

                // Convertendo a string de dados para um objeto Timestamp usando SimpleDateFormat
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.util.Date parsedDate = dateFormat.parse(data);
                Timestamp dataPedidoTimestamp = new Timestamp(parsedDate.getTime());

                pstmt.setTimestamp(5, dataPedidoTimestamp);
                pstmt.setInt(6, Integer.parseInt(idFunc));
                pstmt.setInt(7, Integer.parseInt(idVenda));

                pstmt.executeUpdate();

                // Atualizar a tabela com as consultas após a alteração
                mostrarPedidos();

                JOptionPane.showMessageDialog(frame, "Pedido alterado com sucesso.");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Erro ao alterar o pedido. Verifique o console para mais detalhes.");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Pedido não encontrado.");
        }
    }

    private void adicionarPedido(String idProd, String qtdComprada, String data , String CEP, String numCasa, String idFunc, String idVenda) {
        // Implemente a lógica para adicionar uma nova consulta no banco de dados
        // Exemplo: Inserir os dados no banco usando uma instrução SQL INSERT

        String sql = "INSERT INTO pra.pedido (IdProd, qtd_Comprada, data, CEP, numCasa, idFunc, idVenda) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(idProd));
            pstmt.setInt(2, Integer.parseInt(qtdComprada));
            pstmt.setInt(3, Integer.parseInt(numCasa));
            pstmt.setString(4, CEP);

            // Convertendo a string de dados para um objeto Timestamp usando SimpleDateFormat
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date parsedDate = dateFormat.parse(data);
            Timestamp dataPedidoTimestamp = new Timestamp(parsedDate.getTime());

            pstmt.setTimestamp(5, dataPedidoTimestamp);
            pstmt.setInt(6, Integer.parseInt(idFunc));
            pstmt.setInt(7, Integer.parseInt(idFunc));

            pstmt.executeUpdate();

            // Atualizar a tabela com as consultas após a adição
            mostrarPedidos();

            JOptionPane.showMessageDialog(frame, "Pedido adicionado com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Erro ao adicionar o pedido. Verifique o console para mais detalhes.");
        }
    }

    private void cancelarPedido() {
        // Implemente a lógica para cancelar uma consulta, se necessário
        // Exemplo: Remover a consulta do banco de dados
    }

    private boolean pedidoExiste(String idProd) {
        try {
            String sql = "SELECT 1 FROM pra.pedido WHERE IdProd = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(idProd));

            ResultSet rs = pstmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // O restante do seu código...

    public static class Pedido {
        private int idProd;
        private int qtdComprada;
        private Timestamp data;
        private String CEP;
        private int numCasa;
        private int idFunc;
        private int idVenda;

        public Pedido(int idProd, int qtdComprada, Timestamp data, String CEP, int numCasa, int idFunc, int idVenda) {
            this.idProd = idProd;
            this.qtdComprada = qtdComprada;
            this.data = data;
            this.CEP = CEP;
            this.numCasa = numCasa;
            this.idFunc = idFunc;
            this.idVenda = idVenda;

        }

        public Object[] toArray() {
            return new Object[]{idProd, qtdComprada, data, CEP, numCasa, idFunc,idVenda};
        }
    }
}
