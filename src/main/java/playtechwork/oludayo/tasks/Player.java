package playtechwork.oludayo.tasks;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import io.reactivex.rxjava3.core.Flowable;
import lombok.Data;

@Data
public class Player {
	   private UUID id;
	   private String name;
	   private double balance;
	   private Map<UUID, Operations> operations;
	public Player(UUID id, double balance, Map<UUID, Operations> operations) {
		super();
		this.id = id;
		this.balance = balance;
		this.operations = operations;
	}
	public Player() {
		super();
	}
	
	public void writeToFile(String fileName) {
		   Path path = Paths.get(fileName);
		   String data = "Player ID: " + id + "\n" + "Balance: " + balance + "\n";
		   Flowable.fromIterable(Arrays.asList(data.split("\n")))
		           .flatMap(line -> Flowable.fromCallable(() -> {
		               Files.write(path, line.getBytes());
		               return line;
		           }))
		           .subscribe(line -> System.out.println("Written: " + line),
		                  error -> System.err.println("Error: " + error.getMessage()));
		}
	public Player(String name) {
	       this.name = name;
	       this.balance = 0;
	   }

	   public void deposit(double amount) {
	       this.balance += amount;
	   }

	   public void bet(double amount) {
	       if (this.balance >= amount) {
	           this.balance -= amount;
	       } else {
	           System.out.println("Insufficient balance");
	       }
	   }

	   public void withdraw(double amount) {
	       if (this.balance >= amount) {
	           this.balance -= amount;
	       } else {
	           System.out.println("Insufficient balance");
	       }
	   }

	   public double getBalance() {
	       return this.balance;
	   }

	
	   
	  
	}