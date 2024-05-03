package br.com.applip1.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.applip1.cloneprof.model.Produto;

public class ProdutoController {

	private static List<Produto> listaProdutos;
	
	private static int proxId = 1;
	
	@GetMapping("/produtos")
	public ResponseEntity<List<Produto>>  produtos() {
		return ResponseEntity
				.status(HttpStatus.OK) 
				.body(listaProdutos); 
	
	}
	
	public ResponseEntity<Produto> novoProduto(@RequestBody Produto produto) {
		
		produto.setId(proxId++);
		
		listaProdutos.add(produto);
		
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(produto);
	}
	
	@GetMapping("/produtos/{id}") 
	public ResponseEntity<Object> buscarProdutoPorId(@PathVariable(value="id") Integer id){ 
		
		ResponseEntity<Object> produto = findById(id);
		
		if (produto == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
		}
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(produto);
	}
	
	private ResponseEntity<Object> findById(Integer id) {
		for(Produto p : listaProdutos) {
			if (p.getId() == id) {
				return ResponseEntity.ok(p);
			}
		}
		
		  return null;
	}
	
	@PutMapping("/produtos/{id}")
	public ResponseEntity<Object> atualizarProduto(
			@PathVariable(value = "id") Integer id,
			@RequestBody Produto produto) {
		
		produto.setId(id);
		
		ResponseEntity<Object> produtoEncontrado = findById(id);
		
		if (produtoEncontrado.getStatusCode() == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
		}
		
		BeanUtils.copyProperties(produto, produtoEncontrado);
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(produtoEncontrado);
	}

	@DeleteMapping("/produtos/{id}")
	public ResponseEntity<Object> apagarProduto(
			@PathVariable(value = "id") Integer id) {
		
		Produto produtoApagado = procurarEApagar(id);
		
		if (produtoApagado == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
		}
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body("Produto apagado com sucesso.");
	}
	
	private Produto procurarEApagar(Integer id) {
		
		Iterator<Produto> i = listaProdutos.iterator();
		while(i.hasNext()) {
			Produto p = i.next();
			if (p.getId() == id) {
				i.remove();
				return p;
			}
		}
		
		return null;
	}
}
