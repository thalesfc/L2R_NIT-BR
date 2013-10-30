package methodology;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import common.Config;
import common.Data;

public class RandomListGenerator {
	public static List<String> randomList(String ISBN, String goodRatingUser, int M){
		// pegando todos usuarios que ja avaliaram o livro
		Set<String> ratedUsers = Data.newItemsRatedUsers().get(ISBN);

		//pegando todos os usuarios
		List<String> noRatedUsers = new ArrayList<String>();

		List<String> users = Data.getUSERS();
		for (String User : users) {
			//esse usuario nao deu rating pro item, possivel candidato a ser selecionado
			if( ratedUsers.contains(User) == false){
				noRatedUsers.add(User);
			}
		}

		int n = noRatedUsers.size();

		// fazendo sampling na lista dos usuarios que nao avaliaram o livro
		Random rnd = new Random(Config.SEED + ISBN.hashCode() + goodRatingUser.hashCode());
		Set<Integer> selectedPositions = randomSample4(rnd, n, 1000);
		Iterator<Integer> it = selectedPositions.iterator();

		// armazena o isbn dos selecionados
		List<String> returned = new ArrayList<String>(1000);
		while(it.hasNext()){

			int pos = it.next();
			String selectedUser = noRatedUsers.get(pos); 
			returned.add(selectedUser);
		}
		// adiciona o usuario que deu rating bom
		returned.add(goodRatingUser);
		return returned;
	}

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
