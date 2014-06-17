package net.gabordobrei.diploma.simulator.model;

import java.util.Set;

public abstract class RoutingAlgebra<W> {
	Set<Weight<W>> weightSet;
	Weight<W> infinityWeight;
	CompositionOperator co;
	WeightComparisonOperator wco;
	
}
