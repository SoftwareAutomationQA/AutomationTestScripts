package webservice.rest.exam;

public class Landlord {
	
	public String firstName;
	public String lastName;
	public boolean trusted ;
	
	
	public Landlord(String firstName, String lastName, boolean trusted) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.trusted = trusted;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public boolean getTrusted() {
		return trusted;
	}


	public void setTrusted(boolean trusted) {
		this.trusted = trusted;
	}
	
	
	

}
