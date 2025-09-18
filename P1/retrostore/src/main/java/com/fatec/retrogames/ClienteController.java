package com.fatec.retrogames;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class ClienteController {
    @Autowired
    private ClienteRepository bd;
    @Autowired
    private RetroStoreService util;

    @PostMapping("/api/cliente") 
    public void gravar(@RequestBody Cliente obj){
        bd.save(obj);
        String email = "<b>Email de confirmação de cadastro</b><br><br>" +
                    "seja bem vindo ao Retro Games Store, "+ obj.getNome() + ", clique no link abaixo para "+
                    "confirmar o seu cadastro.<br><br>"+
                    "<a href='http://localhost:8080/api/cliente/efetivar/"+ obj.getCodigo() +"'>Clique aqui</a>";
        String retorno = util.enviaEmailHTML(obj.getEmail(), "Confirmação de novo cadastro", email);
        System.out.println("Cliente gravado com sucesso! "+ retorno);
    }

    @PutMapping("/api/cliente")
    public void alterar(@RequestBody Cliente obj){
        if(bd.existsById(obj.getCodigo())) bd.save(obj);
        System.out.println("Cliente alterado com sucesso!");
    }

    @DeleteMapping("/api/cliente/{codigo}")
    public void remover(@PathVariable("codigo") int id){
        bd.deleteById(id);
        System.out.println("cliente removido com sucesso!");
    }

    @GetMapping("/api/cliente/{codigo}")
    public Cliente carregar(@PathVariable("codigo") int id){
        if(bd.existsById(id)){
            System.out.println("Cliente encontrado !");
            return bd.findById(id).get();
        } else {
            return new Cliente();
        }
    }

    @GetMapping("/api/clientes")
    public List<Cliente> listar(){
        return bd.findAll();
    }

    @PostMapping("/api/cliente/login")
    public Cliente fazerLogin(@RequestBody Cliente obj){
      Optional<Cliente> retorno  =  bd.fazerLogin(obj.getEmail(), obj.getSenha());
      if(retorno.isPresent()){
            System.out.println("Login efetuado com sucesso!");
          return retorno.get();
      } else {
          return new Cliente();
      }
    }

    @PatchMapping("/api/cliente/efetivar/{codigo}")
    public void efetivar(@PathVariable("codigo") int codigo){
         Cliente obj = bd.findById(codigo).get();
         obj.setAtivo(1);
         bd.save(obj);
         System.out.println("cliente liberado"); 
    }

    @PostMapping("/api/cliente/esqueceuSenha")
    public void esqueceuSenha(@RequestBody Cliente obj){
      Optional<Cliente> clienteOpt = bd.findByEmail(obj.getEmail());
      if (clienteOpt.isPresent()) {
        Cliente cliente = clienteOpt.get();
        String email = "<b>Email de Redefinição de senha</b><br><br>" +
                  "Olá, "+ cliente.getNome() + ", clique no link abaixo para redefinir sua senha.<br><br>"+
                  "<a href='http://localhost:8080/api/cliente/redefinirSenha/"+ cliente.getCodigo() +"'>Clique aqui</a>";
        String retorno = util.enviaEmailHTML(cliente.getEmail(), "Redefinição de senha", email);
        System.out.println("Sua senha será redefinida!"+ retorno);
      } else {
          System.out.println("email não encontrado");
      }
    }


    @PutMapping("/api/cliente/redefinirSenha/{codigo}")
    public void redifinirSenha(@RequestBody Cliente objsenha, @PathVariable("codigo") int codigo){
      Cliente obj = bd.findById(codigo).get();
      obj.setSenha(objsenha.getSenha());
      bd.save(obj);
      System.out.println("Senha alterada com sucesso!");
    }
}