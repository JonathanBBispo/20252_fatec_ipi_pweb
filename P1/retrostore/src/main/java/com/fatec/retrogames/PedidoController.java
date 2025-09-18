package com.fatec.retrogames;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
public class PedidoController {
    @Autowired
    PedidoRepository bd;

    @Autowired
    RetroStoreService util;

    @PostMapping("/api/pedido")
    public void gravar(@RequestBody Pedido pedido) {
        double total = pedido.getItens().stream().mapToDouble(Produto::getValor).sum();
        pedido.setTotal(total);
        bd.save(pedido);

        String email = "<b>Confirmação de Pedido</b><br>Pedido realizado com sucesso!<br>Total: R$ " + total;
        util.enviaEmailHTML(pedido.getCliente().getEmail(), "Pedido confirmado", email);
        System.out.println("Pedido gravado com sucesso!");
    }

    @PutMapping("/api/pedido")
    public void alterar(@RequestBody Pedido obj) {
        Optional<Pedido> opt = bd.findById(obj.getCodigo());
        if(opt.isPresent()) bd.save(obj);
        System.out.println("Pedido alterado com sucesso!");
    }

    @DeleteMapping("/api/pedido/{codigo}")
    public void remover(@PathVariable int codigo) {
        bd.deleteById(codigo);
        System.out.println("Pedido removido com sucesso!");
    }

    @GetMapping("/api/pedido/{codigo}")
    public Pedido carregar(@PathVariable int codigo) {
        if(bd.existsById(codigo)){
            System.out.println("Cliente encontrado !");
            return bd.findById(codigo).get();
        } else {
            return new Pedido();
        }
    }

    @GetMapping("/api/pedido/cliente/{codigo}")
    public List<Pedido> listarPorCliente(@PathVariable int codigo) {
        return bd.findByClienteCodigo(codigo);
    }

    @GetMapping("/api/pedidos")
    public List<Pedido> listarTodos() {
        return bd.findAll();
    }
}
