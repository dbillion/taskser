package playtechwork.oludayo.tasks;

import java.util.UUID;

import lombok.Data;

@Data
public class Match {

	private UUID id;
	   private double rateA;
	   private double rateB;
	   private String result;
	public Match(UUID id, double rateA, double rateB, String result) {
		super();
		this.id = id;
		this.rateA = rateA;
		this.rateB = rateB;
		this.result = result;
	}
	public Match() {
		super();
	}
	   
	   
}
