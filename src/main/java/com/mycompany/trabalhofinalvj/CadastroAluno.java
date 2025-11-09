/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

//Área reservada para importação de classes e pacotes Java.
package com.mycompany.trabalhofinalvj;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.mycompany.trabalhofinalvj.util.HibernateUtil;
import newpackagedao.AlunoDAO;
import newpackagedao.RemocaoAlunoDAO;
import java.awt.Color;

/**
 *
 * @author franc
 */
public class CadastroAluno extends javax.swing.JFrame {
    int cont = 0;
    private List<Aluno> listaAlunos;
    private final SimpleDateFormat dataAjustada = new SimpleDateFormat("dd/MM/yyyy");

    private RemocaoAlunoDAO AlunoDao;
    /**
     * Creates new form oiii
     */
    //Construtor do JFrame
    public CadastroAluno() {
        //metodo do netBeans que inicializa os componentes da tela
        initComponents();
        //Muda a cor da tela
        this.getContentPane().setBackground(Color.getHSBColor(0.5f, 0.1f, 0.9f));
        //Cria a lista de alunos
        this.listaAlunos = new ArrayList<>();
        //le o arquivo csv e adiciona os alunos na lista
        lercsv("ListagemAlunos.txt",listaAlunos);        
        //gera a tabela
        configTabela();
        //intera na lista e preenche a tabela
        preencheTabela();
        //cria o AlunoDao
        this.AlunoDao = new RemocaoAlunoDAO();
    }
    
    //f.verificacaoAlunoNaLista
    public Aluno verificarAluno(int matBusca){
        Aluno alunoEncontrado = null;
        for(Aluno aluno : this.listaAlunos){
            if(aluno.getMatricula() == matBusca){
                alunoEncontrado = aluno;
                break;
            }
        }
        return alunoEncontrado;
    }
    
    public int indiceAluno(int matBusca){
        int a = 0;
        Aluno alunoEncontrado = null;
        for(Aluno aluno : this.listaAlunos){    
            if(aluno.getMatricula() == matBusca){  
                return a;
            }
            a++;
        }
        return -999;
    }
    
    
    
    
    
    
    //funcao que configura a tabela
    private void configTabela(){
        //pega o modelo tabelaAlunos
        DefaultTableModel modelo = (DefaultTableModel) tabelaAlunos.getModel();
        
        //cria array de nomes e coloca elas como o nome das colunas
        String[] nomeColunas = {"Matrícula","Nome","Idade","DataNasc","CPF"};
        modelo.setColumnIdentifiers(nomeColunas);
        modelo.setRowCount(0);//inicia tabela vazia
    }
    
    //f.preenche a tabela
    private void preencheTabela(){
        //novamente pega o modelo tabelaAlunos
        DefaultTableModel modelo = (DefaultTableModel) tabelaAlunos.getModel();
        modelo.setRowCount(0); //zera a quantidade de linhas para evitar que o mesmo dado seja mostrado várias vezes
        //Define o padrão desejado
        //itera dentro da lista de Alunos
        for(Aluno aluno : this.listaAlunos){
            //ajusta a data nascimento para o padrão anteriormente "setado"
            String dataNascFormatada;
            if(aluno.getDataNasc() != null){
                dataNascFormatada = dataAjustada.format(aluno.getDataNasc());
            }else{
                dataNascFormatada = "";
            }
            
            //cria uma array de objetos para adicionar na tabela
            Object[] dadosLinha = new Object[]{
                aluno.getMatricula(),
                aluno.getNome(),
                aluno.getIdade(),
                dataNascFormatada,
                aluno.getCpf()
            };
            //adiciona a linha com base no array dadosLinha
            modelo.addRow(dadosLinha);
        }
        
        
        
    }
    //função que vai ser usada quando iniciar as funções a fim de ler os dados 
    private void lercsv(String nome,List<Aluno> lista){
        
        String linha="";
        String separador= ",";
        //inicia um bloco de texto que contém oq esta escrito no txt
        try (BufferedReader br = new BufferedReader(new FileReader(nome))){
         int posicaolinha=1;
         //passa para cada linha até ela ser nula 
         while((linha=br.readLine())!=null){
            //copia cada linha para um vetor e inicaliza os alunos de acordo coma  sua posição
            String[] dados = linha.split(separador);
            Aluno alunoAdd= new Aluno();
              //[nome,cpf,data,fone,mat] - parametros da funlão adicionarAluno
              //[matricula - 0,nome - 1,idade - 2,data - 3,cpf - 4,telefone - 5] - arquivo CSV salva exatamente assim
              //A cada , ele identificará um dado a ser armazenado, então escolheremos a ordem 1,4,3,5,0;
            alunoAdd = adicionarAluno(dados[1],dados[4],dados[3],dados[5],dados[0]);
             
            lista.add(alunoAdd);
             
            posicaolinha++; 
             
         }
            
            
        
        } catch (IOException e) {
            // Imprime o erro caso o arquivo não seja encontrado ou haja outro problema de I/O
            e.printStackTrace();
    }
                    
    }      
            
    private void salvarCSV(List<Aluno> lista){
        //tenta abrir o arquivo listagemAluno.txt
        try(FileWriter fw = new FileWriter("ListagemAlunos.txt", false);
            BufferedWriter bw = new BufferedWriter(fw))
        {
            for (Aluno aluno: listaAlunos){
                String dataFormatada = aluno.getDataNasc() != null ? dataAjustada.format(aluno.getDataNasc()) : "";
                String linha = aluno.getMatricula() + "," + aluno.getNome() + "," +aluno.getIdade() + "," +dataFormatada + "," + aluno.getCpf()+"," + aluno.getTelefone();
               
                bw.write(linha);
                bw.newLine();
            }
            JOptionPane.showMessageDialog(this,"Dados salvo no arquivo com sucesso!");
        }catch(IOException e){
            JOptionPane.showMessageDialog(this,"Erro!");
        }
    }
    //Usa o setters da classe aluno para modificar um objeto
    public static Aluno adicionarAluno(String nomeAluno, String cpfAluno, String dataNascAluno, String foneAluno, String matAluno){
        Aluno novoAluno = new Aluno();
        //Pega os valores passados em cada textField, que e são atribuidos ao novo objeto criado
        novoAluno.setNome(nomeAluno);
        novoAluno.setCpf(cpfAluno);
        novoAluno.setDataNasc(dataNascAluno);
        novoAluno.setTelefone(foneAluno);
        novoAluno.setMatricula(Integer.parseInt(matAluno.trim()));
        novoAluno.setIdade(novoAluno.getDataNasc());
        return novoAluno;
    }
    
    /**S
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cadastroCPF = new javax.swing.JFormattedTextField();
        cadastroTelefone = new javax.swing.JFormattedTextField();
        cadastroDataNasc = new javax.swing.JFormattedTextField();
        botaoConfirmar = new javax.swing.JButton();
        cadastroNome1 = new javax.swing.JTextField();
        botaoBuscar = new javax.swing.JButton();
        botaoIdade = new javax.swing.JButton();
        botaoInserir = new javax.swing.JButton();
        sPane = new javax.swing.JScrollPane();
        tabelaAlunos = new javax.swing.JTable();
        botaoLista = new javax.swing.JButton();
        cadastroMatricula = new javax.swing.JFormattedTextField();
        jLabel7 = new javax.swing.JLabel();
        botaoRemove = new javax.swing.JButton();
        botaoAtualizar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Trabalho Final");
        setBackground(new java.awt.Color(255, 255, 51));

        jLabel1.setText("Cadastro");

        jLabel2.setText("Nome:");

        jLabel4.setText("Data de nascimento:");

        jLabel5.setText("Telefone");

        jLabel6.setText("CPF");

        try {
            cadastroCPF.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        cadastroCPF.setText("    .    .    -    ");
        cadastroCPF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cadastroCPFActionPerformed(evt);
            }
        });

        try {
            cadastroTelefone.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##) #####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        cadastroTelefone.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        cadastroTelefone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cadastroTelefoneActionPerformed(evt);
            }
        });

        try {
            cadastroDataNasc.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        cadastroDataNasc.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cadastroDataNasc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cadastroDataNascActionPerformed(evt);
            }
        });

        botaoConfirmar.setBackground(new java.awt.Color(153, 102, 255));
        botaoConfirmar.setText("Confirmar");
        botaoConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoConfirmarActionPerformed(evt);
            }
        });

        cadastroNome1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cadastroNome1ActionPerformed(evt);
            }
        });

        botaoBuscar.setBackground(new java.awt.Color(153, 102, 255));
        botaoBuscar.setText("Buscar");
        botaoBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoBuscarActionPerformed(evt);
            }
        });

        botaoIdade.setBackground(new java.awt.Color(153, 102, 255));
        botaoIdade.setText("Listar por Idade");
        botaoIdade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoIdadeActionPerformed(evt);
            }
        });

        botaoInserir.setBackground(new java.awt.Color(153, 102, 255));
        botaoInserir.setText("Inserir Em...");
        botaoInserir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoInserirActionPerformed(evt);
            }
        });

        tabelaAlunos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        sPane.setViewportView(tabelaAlunos);

        botaoLista.setBackground(new java.awt.Color(153, 102, 255));
        botaoLista.setText("Listagem");
        botaoLista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoListaActionPerformed(evt);
            }
        });

        try {
            cadastroMatricula.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("######")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        cadastroMatricula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cadastroMatriculaActionPerformed(evt);
            }
        });

        jLabel7.setText("Matricula");

        botaoRemove.setBackground(new java.awt.Color(153, 102, 255));
        botaoRemove.setText("Remover Aluno");
        botaoRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoRemoveActionPerformed(evt);
            }
        });

        botaoAtualizar.setBackground(new java.awt.Color(153, 102, 255));
        botaoAtualizar.setText("Atualizar");
        botaoAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoAtualizarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cadastroDataNasc, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cadastroTelefone, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cadastroCPF, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cadastroNome1)
                                .addGap(22, 22, 22))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(167, 167, 167)
                                .addComponent(jLabel1))
                            .addComponent(cadastroMatricula, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sPane))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(botaoRemove, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(botaoConfirmar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(botaoInserir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(botaoLista, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(botaoIdade, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(botaoAtualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(295, 295, 295)
                        .addComponent(botaoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {botaoAtualizar, botaoConfirmar, botaoInserir, botaoLista, botaoRemove});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sPane, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cadastroNome1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cadastroDataNasc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cadastroTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cadastroCPF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cadastroMatricula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(botaoInserir)
                        .addComponent(botaoConfirmar))
                    .addComponent(botaoIdade))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botaoLista)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(botaoAtualizar)
                        .addComponent(botaoBuscar)
                        .addComponent(botaoRemove)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cadastroCPFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cadastroCPFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cadastroCPFActionPerformed

    private void cadastroTelefoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cadastroTelefoneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cadastroTelefoneActionPerformed

    private void cadastroDataNascActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cadastroDataNascActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cadastroDataNascActionPerformed

    private void botaoConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoConfirmarActionPerformed
        //Cria o novo objeto, do tipo aluno
        Aluno novoAluno = new Aluno();
        novoAluno = adicionarAluno(cadastroNome1.getText(),cadastroCPF.getText(),cadastroDataNasc.getText(),cadastroTelefone.getText(),cadastroMatricula.getText());
        //cria as variaveis p/hibernate
        Session session = null;
        Transaction transaction = null;

        //verifica se a data está no formato correto e informa ao usuário
        if (novoAluno.getDataNasc() == null){
            JOptionPane.showMessageDialog(this,"Dados inválidos, recadastre");
        }
        if(this.listaAlunos.contains(novoAluno)){
            JOptionPane.showMessageDialog(this,"Aluno já cadastrado");
        }
        try {
        //Obtem a Session e iniciar Transação
        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.beginTransaction();

        //Salva o objeto criado no Banco de Dados
        session.save(novoAluno);

        //Confirmar a Transação
        transaction.commit();

        //Lógica de UI e Memória
        listaAlunos.add(novoAluno); // Adiciona na lista em memória
        JOptionPane.showMessageDialog(this, "Aluno inserido e salvo no BD corretamente!");

    } catch (Exception ex) {
        //Se o BD falhar, desfaz a transação
        if (transaction != null) {
            transaction.rollback();
        }
        JOptionPane.showMessageDialog(this, "Erro ao salvar no BD: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        //Imprime o erro completo no consoleS
        ex.printStackTrace();

    } finally {
        //Fechar a Sessão
        if (session != null) {
            session.close();
        }
    }
        preencheTabela();
        //responsável por limpar os campos após a coleta de dados do aluno.
        cadastroNome1.setText("");
        cadastroCPF.setText("");
        cadastroDataNasc.setText("");
        cadastroTelefone.setText("");
        cadastroMatricula.setText("");
        salvarCSV(this.listaAlunos);
        
    }//GEN-LAST:event_botaoConfirmarActionPerformed

    private void cadastroNome1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cadastroNome1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cadastroNome1ActionPerformed

    private void botaoBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoBuscarActionPerformed
        //cria a caixa responsável por capturar a matrícula que será pesquisada.
        
        //Será utlizado JOptionPane.showInputDialog com 4 parâmetros,this(p centralizar a caixa no centro do programa ao inves no centro da tela(null)),texto p/Usuário, nome da caixa e o desenho que aparecerá será nenhum, se quisermos por interrogacao, possamos usar QUESTION_MESSAGE;
        String matriculaDigitada = JOptionPane.showInputDialog(this, "Digite a matrícula do aluno: ","Buscar aluno P/Matrícula",JOptionPane.PLAIN_MESSAGE);
        Aluno alunoEncontrado = verificarAluno(Integer.parseInt(matriculaDigitada.trim()));
        if(alunoEncontrado != null){
            JOptionPane.showMessageDialog(null,"Aluno encontrado: \n Nome: " + alunoEncontrado.getNome() + "\n CPF: " + alunoEncontrado.getCpf()+ "\n Telefone: " + alunoEncontrado.getTelefone());
        }else{
            JOptionPane.showMessageDialog(null, "Aluno nao encontrado");
        }
        
        JOptionPane.showMessageDialog(null,"Quantidade de alunos: " + this.listaAlunos.size());
    }//GEN-LAST:event_botaoBuscarActionPerformed

    private void botaoIdadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoIdadeActionPerformed
        if (!this.listaAlunos.isEmpty()){
            Aluno maisNovo = Collections.min(this.listaAlunos, Comparator.comparingInt(Aluno::getIdade));
            Aluno maisVelho = Collections.max(this.listaAlunos, Comparator.comparingInt(Aluno::getIdade));
            JOptionPane.showMessageDialog(this,"Aluno mais novo tem" + maisNovo.getIdade() + " Anos. \n Nome: " + maisNovo.getNome() + "\n CPF: " + maisNovo.getCpf()+ "\n Telefone: " + maisNovo.getTelefone());
            JOptionPane.showMessageDialog(this,"Aluno mais velho tem" + maisVelho.getIdade() + " Anos. \n Nome: " + maisVelho.getNome() + "\n CPF: " + maisVelho.getCpf()+ "\n Telefone: " + maisVelho.getTelefone());
        }
        
         
    }//GEN-LAST:event_botaoIdadeActionPerformed

    private void botaoInserirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoInserirActionPerformed

        salvarCSV(this.listaAlunos);
        String matriculaBusca = JOptionPane.showInputDialog(this,"Qual posição você deseja inserir o aluno?","Inserir aluno",JOptionPane.PLAIN_MESSAGE);
        int posicao = Integer.parseInt(matriculaBusca.trim());
        
        Session session = null;
        Transaction transaction = null;
        
        if (posicao >= 0 && posicao <= this.listaAlunos.size()){
            Aluno novoAluno = new Aluno();
            novoAluno = adicionarAluno(cadastroNome1.getText(),cadastroCPF.getText(),cadastroDataNasc.getText(),cadastroTelefone.getText(),cadastroMatricula.getText());
            
            //verifica se a data está no formato correto e informa ao usuário
            if (novoAluno.getDataNasc() == null){
                JOptionPane.showMessageDialog(this,"Dados inválidos, recadastre");
            }
            if(this.listaAlunos.contains(novoAluno)){
                JOptionPane.showMessageDialog(this,"Aluno já cadastrado");
            }
            try{
                //Obtem a Session(Principal interface p/interagir c/Hibernate/Banco e inicia Transação(serve pra garantir a integridade dos dados )
                session = HibernateUtil.getSessionFactory().openSession();
                transaction = session.beginTransaction();

                //Salva o objeto criado no Banco de Dados
                session.save(novoAluno);

                //Confirmar a Transação
                transaction.commit();

                //Lógica de UI e Memória
                listaAlunos.add(posicao,novoAluno); // Adiciona na lista em memória
                JOptionPane.showMessageDialog(this, "Aluno inserido e salvo no BD corretamente!");

            }catch(Exception ex){
                //Se durante a execução do commit falhar, ele desfaz a transação - Rollback é contrário de commit
                if(transaction != null){
                    transaction.rollback();
                }
                JOptionPane.showMessageDialog(this, "Erro ao salvar no BD: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);

                }finally{
                //Caso a sessão foi iniciada (contrario de nao ser iniciada ou ocorrer um erro antes dela) ele fecha ela.
                if(session != null){
                    session.close();
                }
            }
            
            preencheTabela();
            //responsável por limpar os campos após a coleta de dados do aluno.
            cadastroNome1.setText("");
            cadastroCPF.setText("");
            cadastroDataNasc.setText("");
            cadastroTelefone.setText("");
            cadastroMatricula.setText("");
            salvarCSV(this.listaAlunos);
        }else{
            JOptionPane.showMessageDialog(this,"Posição Inválida!");
        }
        
    }//GEN-LAST:event_botaoInserirActionPerformed

    private void botaoListaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoListaActionPerformed
        //chama  a tela 
        ListagemAluno telaLista = new ListagemAluno(this.listaAlunos);
        telaLista.setVisible(true);
        telaLista.setLocationRelativeTo(this);
    }//GEN-LAST:event_botaoListaActionPerformed

    private void cadastroMatriculaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cadastroMatriculaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cadastroMatriculaActionPerformed

    private void botaoRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoRemoveActionPerformed
        String matriculaDigitada = JOptionPane.showInputDialog(this, "Digite a matrícula do aluno: ","Remover aluno P/Matrícula",JOptionPane.PLAIN_MESSAGE);
        Aluno alunoEncontrado = verificarAluno(Integer.parseInt(matriculaDigitada.trim()));
        String resultado = "";
        //cria as variaveis p/hibernate
        Session session = null;
        Transaction transaction = null;
        if (alunoEncontrado!=null){
              try {
        //Obtem a Session e iniciar Transação
        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.beginTransaction();

        //Salva o objeto criado no Banco de Dados
        session.remove(alunoEncontrado);

        //Confirmar a Transação
        transaction.commit();
        JOptionPane.showMessageDialog(this, "Aluno removido no BD corretamente!");
        listaAlunos = AlunoDao.removerAluno(listaAlunos, alunoEncontrado);
        salvarCSV(this.listaAlunos);
        

    } catch (Exception ex) {
        //Se o BD falhar, desfaz a transação
        if (transaction != null) {
            transaction.rollback();
        }
        JOptionPane.showMessageDialog(this, "Erro ao remover no BD: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        //Imprime o erro completo no consoleS
        ex.printStackTrace();

    } finally {
        //Fechar a Sessão
        if (session != null) {
            session.close();
        }
    }
        }
        preencheTabela();
    }//GEN-LAST:event_botaoRemoveActionPerformed

    private void botaoAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoAtualizarActionPerformed
    
    Session session = null;
    Transaction transaction = null;
     
    String matricula = JOptionPane.showInputDialog(this, "Digite a matricula do aluno:");
    String nome = JOptionPane.showInputDialog(this, "Digite o novo nome:"); 
    String data = JOptionPane.showInputDialog(this, "Digite a nova data(dd/MM/yyyy):  ");
    String telefone = JOptionPane.showInputDialog(this, "Digite o novo telefone:");
    String cpf = JOptionPane.showInputDialog(this, "Digite o novo cpf:");
    
    int alunoIndice = indiceAluno(Integer.parseInt(matricula.trim()));
    if (alunoIndice != -999){
        try {
        //Obtem a Session e iniciar Transação
        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.beginTransaction();
        
        //acessar no bd o objeto aluno a ser modificado
        Aluno alunoatt = (Aluno)session.get(Aluno.class,Integer.parseInt(matricula.trim()));
        alunoatt.setCpf(cpf);
        alunoatt.setDataNasc(data);
        alunoatt.setIdade(alunoatt.getDataNasc());
        alunoatt.setNome(nome);
        alunoatt.setTelefone(telefone);

        session.update(alunoatt);
        listaAlunos.set(alunoIndice ,alunoatt);
        //Confirmar a Transação
        transaction.commit();
        JOptionPane.showMessageDialog(this, "Aluno atualizado no BD!");
   
        salvarCSV(this.listaAlunos);
        
    } catch (Exception ex) {
        //Se o BD falhar, desfaz a transação
        if (transaction != null) {
            transaction.rollback();
        }
        JOptionPane.showMessageDialog(this, "Erro ao atualizar no BD: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        //Imprime o erro completo no consoleS
        ex.printStackTrace();

    } finally {
        //Fechar a Sessão
        if (session != null) {
            session.close();
        }
    }
        }
        preencheTabela();
 
     
    }//GEN-LAST:event_botaoAtualizarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CadastroAluno.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CadastroAluno.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CadastroAluno.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CadastroAluno.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable(){
            public void run() {
                new CadastroAluno().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botaoAtualizar;
    private javax.swing.JButton botaoBuscar;
    private javax.swing.JButton botaoConfirmar;
    private javax.swing.JButton botaoIdade;
    private javax.swing.JButton botaoInserir;
    private javax.swing.JButton botaoLista;
    private javax.swing.JButton botaoRemove;
    private javax.swing.JFormattedTextField cadastroCPF;
    private javax.swing.JFormattedTextField cadastroDataNasc;
    private javax.swing.JFormattedTextField cadastroMatricula;
    private javax.swing.JTextField cadastroNome1;
    private javax.swing.JFormattedTextField cadastroTelefone;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane sPane;
    private javax.swing.JTable tabelaAlunos;
    // End of variables declaration//GEN-END:variables
}
