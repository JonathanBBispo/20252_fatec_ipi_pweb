package com.fatec.loja;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.fatec.loja.Produto;

@RestController
@CrossOrigin(origins = "*")
public class ProdutoController {
    /*
     * POST - INSERIR
     * GET - LER
     * PUT - ALTERAR
     * DELETE - APAGAR
     */
    @GetMapping("/api/produto/{codigo}")
    public Produto carregar(@PathVariable("codigo")int codigo) {
        //TODO BUSCAR DO BD
        Produto obj = new Produto();
        obj.setCodigo(codigo);
        obj.setNome("Maquita");
        obj.setDescritivo("Maquita, Serra Mármore A Seco Bosch");
        obj.setDestaque(1);
        obj.setValor(50);
        obj.setPromo(40);
        obj.setQuantidade(10);
        return obj;
    }
    

}
