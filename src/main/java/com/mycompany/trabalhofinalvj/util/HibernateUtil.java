/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.trabalhofinalvj.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;


/**
 *
 * @author franc
 */
//Classe que tem como objetivo fazer o link entre BD e a aplicação JAVA

public class HibernateUtil {
    
    private static final SessionFactory sessionFactory = buildSessionFactory();
    private static SessionFactory buildSessionFactory(){
        //tenta
        try{
            //criar uma configuração a partir do hibernate.cfg.xml
            Configuration configurador = new Configuration().configure("hibernate.cfg.xml");
            
            //Ela registra manualmente a classe Aluno como uma entidade do Hibernate
            configurador.addAnnotatedClass(com.mycompany.trabalhofinalvj.Aluno.class);
            
   
            //cria o ServiceRegistry e aplica as configuracoes(propriedades do configurador como URL,usuario, senha)
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configurador.getProperties()).build();
            
            //constroi e retorna a sessão criada.
            return configurador.buildSessionFactory(serviceRegistry);
            
        //nao consiga executar o bloco a cima, retorna o erro.
        }catch(Exception ex){
            System.err.println("SessionFactory falhou ao ser criada" + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public static void shutdown() {
        if (sessionFactory != null) {
            getSessionFactory().close();
        }
    }

}
