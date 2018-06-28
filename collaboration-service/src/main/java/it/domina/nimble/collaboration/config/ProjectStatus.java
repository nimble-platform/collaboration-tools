package it.domina.nimble.collaboration.config;

public enum ProjectStatus {

	ACTIVE("ACTIVE"),
	ARCHIVED("ARCHIVED");	

	private String val;

	public static ProjectStatus valueOfSigla(String s) {
		for (ProjectStatus st: ProjectStatus.values()){
			if (s.equals(st.val)){
				return st;
			}
		}
		return null;
	}

	
	private ProjectStatus(String s){
		this.val = s;
	}
	
	public String toString() {
		return this.val;
	};
		
	
}
