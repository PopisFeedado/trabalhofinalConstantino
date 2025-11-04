/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.trabalhofinalvj;
import java.util.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.Period;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import javax.persistence.*;
/**
 *
 * @author franc
 */
@Entity
@Table(name = "aluno")
//para identificar a tabela aluno.
public class Aluno {
    //Crie um Java Bean Aluno com os seguintes atributos: matrícula, nome, idade, dataNascimento onde
//deve ser do tipo date e armazenar no nosso formato dd/mm/yyyy, telefone, CPF. Deve possuir os
//métodos getters e setters de cada atributo.
    //atributos
    @Id
    private int matricula;
    private String nome;
    private int idade;
    @Temporal(TemporalType.DATE)
    private Date dataNasc;
    
    private String cpf;
    private String telefone;
    
    //calcula a idade a ser utilizada no getIdade
    public static int alunoIdade(Date dataNasc){
        // Se a data de nascimento for nula, retorna 0;
        if (dataNasc == null) {
            return 0; 
        }
        //pega a data de nascimento do aluno e converte para LocalDate
        LocalDate dataNascimento = dataNasc.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        //pega a data atual do computador
        LocalDate dataAtual = LocalDate.now();
        //faz a comparação entre a data atual e a data de nascimento
        Period periodo = Period.between(dataNascimento,dataAtual);
        //retorna o campo de anos do periodo comparado anteriormente.
        return periodo.getYears();
    }
    //getters e setters
    public int getMatricula() {
        return matricula;
    }
    public String getNome() {
        return nome;
    }
    public int getIdade() {
        return idade;
    }
    public Date getDataNasc() {
        return dataNasc;
    }
    public String getCpf() {
        return cpf;
    }
    public String getTelefone() {
        return telefone;
    }
    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setIdade(Date dataNasc) {
        this.idade = alunoIdade(dataNasc);
    }
    public void setDataNasc(String dataNascimentoS){
        
        //Recebe a data como String e coloca no formato requisitado(dd/MM/yyyy
        SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
        //converte a data formatada pro tipo data e coloca dentro do campo dataNasc do objeto.
        try{
            Date dataConvertida = formatador.parse(dataNascimentoS); //converte para o padrao dd/MM/yyyy
            this.dataNasc = dataConvertida;
        }catch(ParseException e){
            //seta a data como NULL caso dê erro durante a formatação
            this.dataNasc = null;
        }
    }
    
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    
//Métodos que mostram para a classe que quando duas matriculas/ou CPF forem iguais, os dois objetos também são iguais.
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.matricula;
        return hash;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Aluno other = (Aluno) obj;
        return this.matricula == other.matricula;
    }
 
            
    
  
    
}
