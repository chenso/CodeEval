package alignmentDNA;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
public class Main {
	private static String top;
	private static String bot;
	private static Map<Inputs, Integer> cache = new HashMap<Inputs, Integer>();
	
	public static int score(int topindex, int botindex, boolean indelTopStarted, boolean indelBotStarted) {
		if (topindex == top.length() && botindex == bot.length()) {
			return 0;
		}
		if (topindex == top.length() || botindex == bot.length() )  {
			return -Integer.MAX_VALUE / 8;
		}
		int scoreIndelTop;
		Inputs inputForIndelTop = new Inputs(topindex, botindex + 1, true, false);
		if (cache.containsKey(inputForIndelTop)) {
			scoreIndelTop = cache.get(inputForIndelTop);
		} else {
			scoreIndelTop = score(topindex, botindex + 1, true, false);
		}
		if (indelTopStarted) {
			scoreIndelTop -= 1;
		} else {
			scoreIndelTop -= 8;
		}
		
		int scoreIndelBot;
		Inputs inputForIndelBot = new Inputs(topindex + 1, botindex, false, true);
		if (cache.containsKey(inputForIndelBot)) {
			scoreIndelBot = cache.get(inputForIndelBot);
		} else {
			scoreIndelBot = score(topindex + 1, botindex, false, true);
		}
		if (indelBotStarted) {
			 scoreIndelBot -= 1;
		} else {
			scoreIndelBot -= 8;
		}
		int scoreComp;
		Inputs inputNoIndel = new Inputs(topindex + 1, botindex + 1, false, false);
		if (cache.containsKey(inputNoIndel)) {
			scoreComp = cache.get(inputNoIndel);
		} else {
			scoreComp =  score(topindex + 1, botindex + 1, false, false);
		}
		if (top.charAt(topindex) == bot.charAt(botindex)) {
			scoreComp += 3;
		} else {
			scoreComp -= 3;
		}
		
		int finalScore = Math.max(scoreIndelBot, Math.max(scoreIndelTop, scoreComp));
		Inputs inputForCache  = new Inputs(topindex, botindex, indelTopStarted, indelBotStarted);
		cache.put(inputForCache, finalScore);
		return finalScore;
	}
	
	public static int score() {
		return score(0, 0, false, false);
	}
	
    public static void main (String[] args) throws IOException {
        File file = new File(args[0]);
        long a =file.length();
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line = "GCATGCT | GATTACA";
        while ((line = buffer.readLine()) != null) {
        	
        	cache.clear();
            line = line.trim();
           
            if (line.length() == 0) return;
            // Process line of input Here
            String[] split = line.split("\\s\\|\\s");
            top = split[0];
            bot = split[1];
            int score = score();
            System.out.println(score);
        }
       
       buffer.close();
    }
}

class Inputs {
	int topindex;
	int botindex;
	boolean indelTopStarted;
	boolean indelBotStarted;
	public Inputs(int topindex, int botindex, boolean indelTopStarted, boolean indelBotStarted) {
		this.topindex = topindex;
		this.botindex = botindex;
		this.indelTopStarted = indelTopStarted;
		this.indelBotStarted = indelBotStarted;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + botindex;
		result = prime * result + (indelBotStarted ? 1231 : 1237);
		result = prime * result + (indelTopStarted ? 1231 : 1237);
		result = prime * result + topindex;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Inputs))
			return false;
		Inputs other = (Inputs) obj;
		if (botindex != other.botindex)
			return false;
		if (indelBotStarted != other.indelBotStarted)
			return false;
		if (indelTopStarted != other.indelTopStarted)
			return false;
		if (topindex != other.topindex)
			return false;
		return true;
	}
}



/*
import java.io.*;
import java.util.HashMap;
import java.util.Map;
public class Main {
	
	private static Map<Inputs, Integer> cache = new HashMap<Inputs, Integer>();
	static int count = 0;
	public static int score(String longer, String shorter, boolean indelTopStarted, boolean indelBotStarted) {
		if (longer.length() == 0 && shorter.length() == 0) {
			return 0;
		}
		if (longer.length() == 0 || shorter.length() == 0) return -Integer.MAX_VALUE / 8;
		int scoreIndelTop;
		Inputs inputForIndelTop = new Inputs(longer, shorter.substring(1), true, false);
		if (cache.containsKey(inputForIndelTop)) {
			scoreIndelTop = cache.get(inputForIndelTop);
		} else {
			scoreIndelTop = score(longer, shorter.substring(1), true, false);
		}
		if (indelTopStarted) {
			 scoreIndelTop -= 1;
		} else {
			scoreIndelTop -= 8;
		}
		
		int scoreIndelBot;
		Inputs inputForIndelBot = new Inputs(longer.substring(1), shorter, false, true);
		if (cache.containsKey(inputForIndelBot)) {
			scoreIndelBot = cache.get(inputForIndelBot);
		} else {
			scoreIndelBot = score(longer.substring(1), shorter, false, true);
		}
		if (indelBotStarted) {
			 scoreIndelBot -= 1;
		} else {
			scoreIndelBot -= 8;
		}
		
		count++;
		int scoreComp;
		Inputs inputNoIndel = new Inputs(longer.substring(1), shorter.substring(1), false, false);
		if (cache.containsKey(inputNoIndel)) {
			scoreComp = cache.get(inputNoIndel);
		} else {
			scoreComp =  score(longer.substring(1), shorter.substring(1), false, false);
		}
		if (longer.charAt(0) == shorter.charAt(0)) {
			scoreComp += 3;
		} else {
			scoreComp -= 3;
		}
		int finalScore = Math.max(scoreIndelBot, Math.max(scoreIndelTop, scoreComp));
		Inputs inputForCache  = new Inputs(longer, shorter, indelTopStarted, indelBotStarted);
		cache.put(inputForCache, finalScore);
		return finalScore;
	}
	
	public static int score(String longer, String shorter) {
		return score(longer, shorter, false, false);
	}
	
    public static void main (String[] args) throws IOException {
        //File file = new File(args[0]);
        //BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line = "GCATGCT | GATTACA";
       // while ((line = buffer.readLine()) != null) {
        	
        	cache.clear();
            line = line.trim();
            if (line.length() == 0) return;
            // Process line of input Here
            String[] split = line.split("\\s\\|\\s");
            int score = score(split[0], split[1]);
            System.out.println(score);
            System.out.println(count);
       // }
     //  buffer.close();
    }
}

class Inputs {
	String longer;
	String shorter;
	boolean indelTopStarted;
	boolean indelBotStarted;
	public Inputs(String longer, String shorter, boolean indelTopStarted, boolean indelBotStarted) {
		this.longer = longer;
		this.shorter = shorter;
		this.indelTopStarted = indelTopStarted;
		this.indelBotStarted = indelBotStarted;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (indelBotStarted ? 1231 : 1237);
		result = prime * result + (indelTopStarted ? 1231 : 1237);
		result = prime * result + ((longer == null) ? 0 : longer.hashCode());
		result = prime * result + ((shorter == null) ? 0 : shorter.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Inputs))
			return false;
		Inputs other = (Inputs) obj;
		if (indelBotStarted != other.indelBotStarted)
			return false;
		if (indelTopStarted != other.indelTopStarted)
			return false;
		if (longer == null) {
			if (other.longer != null)
				return false;
		} else if (!longer.equals(other.longer))
			return false;
		if (shorter == null) {
			if (other.shorter != null)
				return false;
		} else if (!shorter.equals(other.shorter))
			return false;
		return true;
	}
}

*/
