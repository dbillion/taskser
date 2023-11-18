package playtechwork.oludayo.tasks;

import java.util.UUID;

import lombok.Data;

@Data
public class Operations {
	   private OperationsType type;
	   private UUID matchId;
	   private int coins;
	   private String side;
	   
	public Operations(OperationsType type, UUID matchId, int coins, String side) {
		super();
		this.type = type;
		this.matchId = matchId;
		this.coins = coins;
		this.side = side;
	}

	public Operations() {
		super();
	}

	 public double performOperation(String operation, Player player) {
	       switch (operation) {
	           case "DEPOSIT":
	               player.deposit(100);
	               break;
	           case "BET":
	               player.bet(50);
	               break;
	           case "WITHDRAW":
	               player.withdraw(20);
	               break;
	           default:
	               System.out.println("Invalid operation");
	               return 0;
	       }
	       return player.getBalance();
	   }
	}
