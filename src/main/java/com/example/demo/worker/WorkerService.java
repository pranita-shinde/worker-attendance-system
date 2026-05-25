package com.example.demo.worker;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.demo.exception.DuplicateWorkerException;
import com.example.demo.exception.WorkerNotFoundException;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
@Service
public class WorkerService {
    private static final Logger log = LoggerFactory.getLogger(WorkerService.class);
    
    private final WorkerRepository workerRepository;

    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    //add worker
    public Worker addWorker(Worker worker) {
        log.info("Attempting to add worker with phone {}", worker.getPhone());
        Boolean exists = workerRepository.existsByPhone(worker.getPhone());

        if (exists) {
            log.error("Duplicate worker found with phone {}", worker.getPhone());
            throw new DuplicateWorkerException(
                "Worker with phone " + worker.getPhone() + " already exists"
            );
        }
        log.info("Worker added successfully with phone {}", worker.getPhone());
        return workerRepository.save(worker);
    }

    //get worker by id
    public Worker getWorker(Long workerId) {
        return workerRepository.findById(workerId)
            .orElseThrow(() -> new WorkerNotFoundException(
                    "Worker with id " + workerId + " not found"
            ));
    }

    //delete worker
    public Worker deleteWorker(Long workerId) {
        log.info("Attempting to delete worker with id {}", workerId);


        Worker worker = workerRepository.findById(workerId)
            .orElseThrow(() -> new WorkerNotFoundException(
                    "Worker with id " + workerId + " not found"
            ));

        workerRepository.delete(worker);
        log.info("Worker deleted successfully with id {}", workerId);
        return worker;
       
    }

}