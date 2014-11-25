package hu.dobrei.diploma.model.algebra;

import hu.dobrei.diploma.model.Route;

public class LeastHopAlgebra extends AbstractAlgebra<Integer> {

	@Override
	public Integer W(Route route) {
		return 1;
	}

	@Override
	public Integer bigOPlus(Integer w1, Integer w2) {
		return w1 + w2;
	}

	@Override
	public Integer phi() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Integer best() {
		return 0;
	}

	@Override
	public int order(Integer w1, Integer w2) {
		return Integer.compare(w1, w2);
	}
}
