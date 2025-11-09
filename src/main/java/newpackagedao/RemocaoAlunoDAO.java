/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package newpackagedao;
import com.mycompany.trabalhofinalvj.Aluno; // Importe seu Java Bean
import com.mycompany.trabalhofinalvj.CadastroAluno;
import java.util.List;
import javax.swing.JOptionPane;
/**
/**
 *
 * @author Vitor Manoel Barbosa
 */
public class RemocaoAlunoDAO implements AlunoDAO {
    public List removerAluno(List alunos, Aluno a){
        if(alunos.contains(a)){
            alunos.remove(a);         
        }
      return alunos; 
    }  
}
