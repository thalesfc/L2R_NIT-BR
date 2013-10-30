package methodology;

import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

public class RandomListGenerator {
	
	public static Set<Integer> randomSample4(Random rnd, int n, int m){
		LinkedHashSet<Integer> res = new LinkedHashSet<Integer>(m); 
		for(int i = n-m; i < n; i++){
			int pos = rnd.nextInt(i+1);
			if (res.contains(pos))
				res.add(i);
			else
				res.add(pos);      
		}
		return res;
	}

}
