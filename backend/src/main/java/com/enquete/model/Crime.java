package com.enquete.model;
import java.util.List;

public class Crime {
	private String victime;
	private List<Suspect> suspects;
	private List<Indice> indices;
	private Suspect coupable;
	
	public Crime(String victime, List<Suspect> suspects, List<Indice> indices, Suspect coupable) {
		this.victime = victime;
		this.suspects = suspects;
		this.indices = indices;
		this.coupable = coupable;
	}
	
	public String getVictime() { return victime; }
	public List<Suspect> getSuspects() { return suspects; }
	public List<Indice> getIndices() { return indices; }
	public Suspect getCoupable() { return coupable; }
	
	@Override
	public String toString() {
		return "Victime: " + victime + "\nNombre de suspects: " + suspects.size();
	}
	

}
