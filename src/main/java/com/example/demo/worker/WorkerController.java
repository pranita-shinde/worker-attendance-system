package com.example.demo.worker;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/workers")
@AllArgsConstructor
public class WorkerController {

    private final WorkerService workerService;

    @GetMapping
    public List<Worker> getWorkers() {
        return workerService.getAllWorkers();
    }

    @PostMapping
    public Worker addWorker(@Valid @RequestBody Worker worker) {
        return workerService.addWorker(worker);     
    }

    @GetMapping("{workerId}")
    public Worker getWorker(@PathVariable Long workerId) {
        return workerService.getWorker(workerId);
    }

    @DeleteMapping(path = "{workerId}")
    public void deleteWorker(@PathVariable Long workerId) {
        workerService.deleteWorker(workerId);
    }
}