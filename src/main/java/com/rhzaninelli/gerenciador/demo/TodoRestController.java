package com.rhzaninelli.gerenciador.demo;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TodoRestController {

    private final TodoRepository todoRepository;

    public TodoRestController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @GetMapping("/todos")
    public Flux<Todo> lerTodos(){
        return todoRepository.findAll();
    }

    @GetMapping("/todos/{feito}")
    public Flux<Todo> lerFeito(@PathVariable boolean feito){
        return todoRepository.findByFeito(feito);
    }

    @DeleteMapping("/todos/{id}")
    public Mono<Void> deletar(@PathVariable String id){
        return todoRepository.deleteById(id);
    }

    @PostMapping("/todos")
    public Mono<Todo> criar(@RequestBody Todo todo){
        return todoRepository.save(todo);
    }

    @PutMapping("/todos/{id}")
    public Mono<Todo> atualizar(@PathVariable String id){
        return todoRepository
                .findById(id)
                .map(todoAtual -> new Todo(id, todoAtual.titulo(), todoAtual.descricao(), !todoAtual.feito()))
                .flatMap(todoRepository::save)
                .onTerminateDetach();
    }
}
