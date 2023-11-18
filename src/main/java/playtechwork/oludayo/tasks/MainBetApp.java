package playtechwork.oludayo.tasks;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import org.apache.commons.lang3.tuple.ImmutablePair;
import playtechwork.oludayo.tasks.Operations;
import playtechwork.oludayo.tasks.OperationsType;
import playtechwork.oludayo.tasks.Player;
import playtechwork.oludayo.tasks.Match;

public class MainBetApp {
	
	 private static Map<String, Player> players = new HashMap()<>();


 public static void main(String[] args) {
	 
	 
	
     Scanner scanner = new Scanner(System.in);
     System.out.println("Enter your id:");
     String id = scanner.nextLine();
     System.out.println("Choose operation (DEPOSIT, BET, WITHDRAW):");
     String operation = scanner.nextLine();

     // Get Player object or create a new one if it doesn't exist
     Player player = players.getOrDefault(id, new Player(UUID.randomUUID(), 0, null));
     players.put(id, player);  // Add Player object to map

     // Create an Operations object
     Operations operations = new Operations();

     // Perform the operation
     double operationResult = operations.performOperation(operation, player);


     // Reading the Files
     Flowable<Match> matches = Flowable.fromIterable(Files.readAllLines(Paths.get("match_data.txt")))
         .map(line -> line.split(","))
         .map(parts -> new Match(UUID.fromString(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), parts[3]))
         .onErrorResumeNext(throwable -> {
             System.out.println("Error reading match.txt: " + throwable.getMessage());
             return Flowable.empty();
         });

     Flowable<Player> players = Flowable.fromIterable(Files.readAllLines(Paths.get("player_data.txt")))
         .map(line -> line.split(","))
         .map(parts -> new Player(UUID.fromString(parts[0]), Double.parseDouble(parts[1]), null))
         .onErrorResumeNext(throwable -> {
             System.out.println("Error reading playerdata.txt: " + throwable.getMessage());
             return Flowable.empty();
         });

     // Processing the Data
     matches.zipWith(players, (match, otherPlayer) -> new ImmutablePair<>(match, player))
         .onErrorResumeNext(throwable -> {
             System.out.println("Error zipping matches and players: " + throwable.getMessage());
             return Flowable.empty();
         })
         .subscribe(pair -> System.out.println(pair));

     // Writing the Results
     Maybe<String> resultMaybe = matches.zipWith(players, (match, otherPlayer) -> new ImmutablePair<>(match, player))
         .map(pair -> pair.toString())
         .reduce((result1, result2) -> result1 + "\n" + result2)
         .onErrorResumeNext(throwable -> {
             System.out.println("Error writing results: " + throwable.getMessage());
             return Flowable.empty();
         });

     String result = resultMaybe.blockingGet();

     try {
         Files.write(Paths.get("result.txt"), result.getBytes());
     } catch (Exception e) {
         System.out.println("Error writing to result.txt: " + e.getMessage());
     }
     
     System.out.println("Your current balance is: " + player.getBalance());
     System.out.println("The result of your operation is: " + operationResult);
 }
}